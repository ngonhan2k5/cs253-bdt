package bdt.hbase;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bdt.config.AnalysisTable;
import bdt.config.HBaseConfig;
import bdt.model.CaseReport;
import bdt.model.CaseReportByCountry;
import bdt.model.CaseReportByCountryDate;
import bdt.model.CaseReportByDate;
import bdt.model.CoronaRecord;
import bdt.model.HBCoronaRecord;
import bdt.utils.RecordParser;
import scala.Tuple2;

public class HBaseRepository implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HBaseRepository.class);
	public static final DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	public static final DateTimeFormatter FORMATER_2 = DateTimeFormatter.ofPattern("yyyyMMdd");

	private static HBaseRepository INSTANCE;

	private HBaseRepository() {
		init();
	}

	public static HBaseRepository getInstance() {
		if (INSTANCE == null)
			INSTANCE = new HBaseRepository();
		return INSTANCE;
	}

	private void init() {
		try (Admin admin = HBaseConfig.getHBaseConnection().getAdmin()) {
			
			HTableDescriptor table = new HTableDescriptor(TableName.valueOf(HBaseConfig.TABLE_NAME));
			table.addFamily(new HColumnDescriptor(HBaseConfig.COLUMN_FAMILY).setCompressionType(Algorithm.NONE));
			
			if (!admin.tableExists(table.getTableName())) {
				LOGGER.info("================== Creating table 'corona_cases' ... ==================");
				admin.createTable(table);
				LOGGER.info("================== Table 'corona_cases' created! ==================");
			}
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
		}
	}

	public CoronaRecord get(Connection connection, String key) throws IOException {
		try (Table tb = connection.getTable(TableName.valueOf(HBaseConfig.TABLE_NAME))) {
			Get g = new Get(Bytes.toBytes(key));
			return parseResult(tb.get(g));
		}
	}
	
	public void save(Configuration config, JavaRDD<CoronaRecord> record) throws MasterNotRunningException, Exception {
		Job job = Job.getInstance(config);
		LOGGER.info("in SAVE ======== " + record.count());
		job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, HBaseConfig.TABLE_NAME);
		job.setOutputFormatClass(TableOutputFormat.class);
		JavaPairRDD<ImmutableBytesWritable, Put> hbasePuts = record.mapToPair(new MyPair());
		hbasePuts.saveAsNewAPIHadoopDataset(job.getConfiguration());
	}
	
	public void saveAnalysis(List<? extends CaseReport> records, AnalysisTable tableName) {
		createAnalysisTable(tableName.value());
		
		try (Table table = HBaseConfig.getHBaseConnection().getTable(TableName.valueOf(tableName.value()))) {
			List<Put> puts = null;
			
			switch (tableName) {
				case CASES_BY_COUNTRY:
					puts = records.stream().map(r -> generateReportPut((CaseReportByCountry)r)).collect(Collectors.toList());
					break;
				case PILOT:
					Map<String, String> countryMap = generateCountryMap();
					LOGGER.info("================== PILOT COUNTRIES: " + countryMap.values());
					puts = records.stream()
							.map(r -> RecordParser.transformPilot((CaseReportByCountryDate)r, countryMap))
							.filter(r -> countryMap.values().contains(r.getCountry()))
							.map(this::generatePilotReportPut)
							.collect(Collectors.toList());
					LOGGER.info("================== TOTAL CASES PILOT DATA: " + puts.size());
					break;
				case CASES_BY_DATE:
					puts = records.stream().map(r -> generateReportPut((CaseReportByDate)r)).collect(Collectors.toList());
					break;
				case CASES_BY_COUNTRY_DATE:
					puts = records.stream().map(r -> generateReportPut((CaseReportByCountryDate)r)).collect(Collectors.toList());
					break;
	
				default:
					break;
			}
			
			if (puts != null)
				table.put(puts);
		} catch (Exception e) {
			LOGGER.error("Unable to save ananlysis data. " + e);
		}
	}
	
	public List<HBCoronaRecord> scanRecords() throws IOException {
		List<HBCoronaRecord> records = new ArrayList<>();
		if (!isTableExist(HBaseConfig.TABLE_NAME)) return records;
		LOGGER.info("================== START SCANNING DATA from table corona_cases... =====================");
		Scan s = new Scan(); HBCoronaRecord record;
		try (Table table = HBaseConfig.getHBaseConnection().getTable(TableName.valueOf(HBaseConfig.TABLE_NAME)); 
				ResultScanner scanner = table.getScanner(s)) {
			Result result = scanner.next();
			while(result != null) {
				record = parseResultHB(result);
				if (record != null) {
					records.add(record);
				}
				result = scanner.next();
			}
		}
		LOGGER.info("================== SCANNING DATA: DONE !!! =====================");
		LOGGER.info("================== TOTAL DATA =====================: " + records.size());
		return records;
	}
	
	public void createAnalysisTable(String tableName) {
		try (Admin admin = HBaseConfig.getHBaseConnection().getAdmin()) {
			TableName tblName = TableName.valueOf(tableName);
			// Clean up table before persist new data
			if (!AnalysisTable.PILOT.value().equals(tableName) && admin.tableExists(tblName)) {
				admin.disableTable(tblName);
				admin.deleteTable(tblName);
			}
			HTableDescriptor table = new HTableDescriptor(tblName);
				
			if (AnalysisTable.PILOT.value().equals(tableName)) {
				table.addFamily(new HColumnDescriptor(HBaseConfig.COL_COUNTRY).setCompressionType(Algorithm.NONE));
			} else {
				table.addFamily(new HColumnDescriptor(HBaseConfig.ANALYSIS_COL_FAMILY).setCompressionType(Algorithm.NONE));
			}
				
			LOGGER.info("================== Creating table "+ tableName +" ... =====================: " + tableName);
			admin.createTable(table);
			LOGGER.info("================== Created table '"+ tableName +"' !!! ===================== ");
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
		}
	}
	
	private Map<String, String> generateCountryMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("United Kingdom", "UK");
		map.put("United State", "US");
		map.put("Japan", "JP");
		map.put("South Korea", "KR");
		map.put("China", "CN");
		map.put("Italy", "IT");
		map.put("Spain", "ES");
		map.put("France", "FR");
		map.put("Iran", "IR");
		map.put("Germany", "DE");
		map.put("Turkey", "TR");
		map.put("Belgium", "BE");
		return map;
	}
	
	private Put generatePilotReportPut(CaseReportByCountryDate record) {
		Put put = new Put(Bytes.toBytes(record.getDate().replaceAll("/", "")));
		put.addImmutable(HBaseConfig.COL_COUNTRY.getBytes(), parseValue(record.getCountry()), parseValue(String.valueOf(record.getConfirmedCases())));
		return put;
	}
	
	private Put generateReportPut(CaseReportByDate record) {
		Put put = new Put(Bytes.toBytes(record.getDate()));
		put.addImmutable(HBaseConfig.ANALYSIS_COL_FAMILY.getBytes(), HBaseConfig.COL_DATE.getBytes(), parseValue(record.getDate()));
		put.addImmutable(HBaseConfig.ANALYSIS_COL_FAMILY.getBytes(), HBaseConfig.COL_CONFIRMED_CASES.getBytes(), parseValue(String.valueOf(record.getConfirmedCases())));
		put.addImmutable(HBaseConfig.ANALYSIS_COL_FAMILY.getBytes(), HBaseConfig.COL_RECOVERED_CASES.getBytes(), parseValue(String.valueOf(record.getRecoveredCases())));
		put.addImmutable(HBaseConfig.ANALYSIS_COL_FAMILY.getBytes(), HBaseConfig.COL_DEATH_CASES.getBytes(), parseValue(String.valueOf(record.getDeathCases())));
		return put;
	}
	
	private Put generateReportPut(CaseReportByCountry record) {
		Put put = new Put(Bytes.toBytes(record.getCountry()));
		put.addImmutable(HBaseConfig.ANALYSIS_COL_FAMILY.getBytes(), HBaseConfig.COL_COUNTRY.getBytes(), parseValue(record.getCountry()));
		put.addImmutable(HBaseConfig.ANALYSIS_COL_FAMILY.getBytes(), HBaseConfig.COL_CONFIRMED_CASES.getBytes(), parseValue(String.valueOf(record.getConfirmedCases())));
		put.addImmutable(HBaseConfig.ANALYSIS_COL_FAMILY.getBytes(), HBaseConfig.COL_RECOVERED_CASES.getBytes(), parseValue(String.valueOf(record.getRecoveredCases())));
		put.addImmutable(HBaseConfig.ANALYSIS_COL_FAMILY.getBytes(), HBaseConfig.COL_DEATH_CASES.getBytes(), parseValue(String.valueOf(record.getDeathCases())));
		return put;
	}
	
	private Put generateReportPut(CaseReportByCountryDate record) {
		Put put = new Put(Bytes.toBytes(record.getCountry() + record.getDate()));
		put.addImmutable(HBaseConfig.ANALYSIS_COL_FAMILY.getBytes(), HBaseConfig.COL_COUNTRY.getBytes(), parseValue(record.getCountry()));
		put.addImmutable(HBaseConfig.ANALYSIS_COL_FAMILY.getBytes(), HBaseConfig.COL_DATE.getBytes(), parseValue(record.getDate()));
		put.addImmutable(HBaseConfig.ANALYSIS_COL_FAMILY.getBytes(), HBaseConfig.COL_CONFIRMED_CASES.getBytes(), parseValue(String.valueOf(record.getConfirmedCases())));
		put.addImmutable(HBaseConfig.ANALYSIS_COL_FAMILY.getBytes(), HBaseConfig.COL_RECOVERED_CASES.getBytes(), parseValue(String.valueOf(record.getRecoveredCases())));
		put.addImmutable(HBaseConfig.ANALYSIS_COL_FAMILY.getBytes(), HBaseConfig.COL_DEATH_CASES.getBytes(), parseValue(String.valueOf(record.getDeathCases())));
		return put;
	}

	private CoronaRecord parseResult(Result result) {
		if (result.isEmpty()) {
			return null;
		}

		byte[] country = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_COUNTRY);
		byte[] state = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_STATE);
		byte[] county = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_COUNTY);
		byte[] date = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_DATE);
		byte[] confirmedCases = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_CONFIRMED_CASES);
		byte[] recoveredCases = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_RECOVERED_CASES);
		byte[] deathCases = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_DEATH_CASES);

		return new CoronaRecord(Bytes.toString(country), Bytes.toString(state), Bytes.toString(county), LocalDate.parse(date.toString(), FORMATER),
						Bytes.toInt(confirmedCases), Bytes.toInt(deathCases), Bytes.toInt(recoveredCases));
	}
	
	private HBCoronaRecord parseResultHB(Result result) {
		if (result.isEmpty()) {
			return null;
		}

		byte[] country = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_COUNTRY);
		byte[] state = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_STATE);
		byte[] county = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_COUNTY);
		byte[] date = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_DATE);
		byte[] confirmedCases = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_CONFIRMED_CASES);
		byte[] recoveredCases = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_RECOVERED_CASES);
		byte[] deathCases = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_DEATH_CASES);

		return new HBCoronaRecord(Bytes.toString(country), Bytes.toString(state), Bytes.toString(county), Bytes.toString(date),
						Bytes.toInt(confirmedCases), Bytes.toInt(recoveredCases), Bytes.toInt(deathCases));
	}

	private Put generatePut(String rowKey, CoronaRecord record) {
		Put put = new Put(Bytes.toBytes(rowKey));
		put.addImmutable(HBaseConfig.COLUMN_FAMILY.getBytes(), HBaseConfig.COL_COUNTRY.getBytes(), parseValue(record.getCountry()));
		put.addImmutable(HBaseConfig.COLUMN_FAMILY.getBytes(), HBaseConfig.COL_STATE.getBytes(), parseValue(record.getState()));
		put.addImmutable(HBaseConfig.COLUMN_FAMILY.getBytes(), HBaseConfig.COL_COUNTY.getBytes(), parseValue(record.getCounty()));
		put.addImmutable(HBaseConfig.COLUMN_FAMILY.getBytes(), HBaseConfig.COL_DATE.getBytes(), parseValue(record.getDate()));
		put.addImmutable(HBaseConfig.COLUMN_FAMILY.getBytes(), HBaseConfig.COL_CONFIRMED_CASES.getBytes(), parseValue(record.getConfirmedCases()));
		put.addImmutable(HBaseConfig.COLUMN_FAMILY.getBytes(), HBaseConfig.COL_RECOVERED_CASES.getBytes(), parseValue(record.getRecoveredCases()));
		put.addImmutable(HBaseConfig.COLUMN_FAMILY.getBytes(), HBaseConfig.COL_DEATH_CASES.getBytes(), parseValue(record.getDeathCases()));
		return put;
	}
	
	private byte[] getValue(Result result, String columnFamily, String columnName) {
		return result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));
	}
	
	private byte[] parseValue(String value) {
		return Optional.ofNullable(value).orElse("").getBytes();
	}
	
	private byte[] parseValue(int value) {
		return Bytes.toBytes(value);
	}
	
	private byte[] parseValue(LocalDate value) {
		return Optional.ofNullable(value).map(v -> v.format(FORMATER)).orElse("").getBytes();
	}
	
	private boolean isTableExist(String tableName) throws IOException {
		try (Admin admin = HBaseConfig.getHBaseConnection().getAdmin()) {
			return admin.tableExists(TableName.valueOf(tableName));
		}
	}
	
	class MyPair implements PairFunction<CoronaRecord, ImmutableBytesWritable, Put> {
		private static final long serialVersionUID = 1L;

		@Override
		public Tuple2<ImmutableBytesWritable, Put> call(CoronaRecord record) throws Exception {
			String date = record.getDate() != null ? record.getDate().format(FORMATER_2) : "";
			String key = Stream.of(record.getCountry(), record.getState(), record.getCounty(), date)
					.filter(StringUtils::isNotBlank)
					.map(v -> v.replaceAll("\\s+", ""))
					.collect(Collectors.joining("|"));
			
			Put put = generatePut(key, record);
			return new Tuple2<ImmutableBytesWritable, Put>(new ImmutableBytesWritable(), put);
		}
		
	};
}
