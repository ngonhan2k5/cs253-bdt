package bdt.sparksql;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.BasicConfigurator;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bdt.config.AnalysisTable;
import bdt.config.HBaseConfig;
import bdt.config.SparkConfig;
import bdt.hbase.HBaseRepository;
import bdt.model.CaseReport;
import bdt.model.CaseReportByCountry;
import bdt.model.CaseReportByCountryDate;
import bdt.model.CaseReportByDate;
import bdt.model.HBCoronaRecord;

public class CoronaAnalysisApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoronaAnalysisApp.class);
	public static final DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH");
	private static SparkSession sparkSession;
	
	public static void init() throws IOException {
		LOGGER.info("================== INITIAL APPLICATION DATA ====================");
		HBaseRepository db = HBaseRepository.getInstance();
		sparkSession = SparkSession.builder()
				.appName("Spark SQL")
				.master(SparkConfig.MASTER_LOCAL)
				.getOrCreate();
		
		sparkSession.createDataFrame(db.scanRecords(), HBCoronaRecord.class)
		.createOrReplaceTempView(HBaseConfig.TABLE_NAME);
		LOGGER.info("================== SPARK SESSION CREATED !!! ====================");
	}

	public static void printTotalCasesByDate() {
		String query =  " SELECT date, SUM(confirmedCases) AS confirmedCases, SUM(recoveredCases) AS recoveredCases, SUM(deathCases) AS deathCases "
					  + " FROM " + HBaseConfig.TABLE_NAME 
					  + " GROUP BY date"
					  + " ORDER BY date DESC ";
		
		Dataset<Row> sqlDF = sparkSession.sql(query);
		
		LOGGER.info("================== PERSISTING ANALYSIS DATA ... =====================");
		List<CaseReportByDate> records = sqlDF.as(Encoders.bean(CaseReportByDate.class)).collectAsList();
		HBaseRepository.getInstance().saveAnalysis(records, AnalysisTable.CASES_BY_DATE);
		sqlDF.show();
		LOGGER.info("\n================== Query String: " + query);
		LOGGER.info("================== PERSISTED ANALYSIS DATA at table " + AnalysisTable.CASES_BY_DATE.value());
	}
	
	public static void printTotalCasesByCountry() {
		String query =  " SELECT country, SUM(confirmedCases) AS confirmedCases, SUM(recoveredCases) AS recoveredCases, SUM(deathCases) AS deathCases "
				  + " FROM " + HBaseConfig.TABLE_NAME 
				  + " GROUP BY country"
				  + " ORDER BY country DESC ";
		
		Dataset<Row> sqlDF = sparkSession.sql(query);
		
		LOGGER.info("================== PERSISTING ANALYSIS DATA ... =====================");
		List<CaseReportByCountry> records = sqlDF.as(Encoders.bean(CaseReportByCountry.class)).collectAsList();
		HBaseRepository.getInstance().saveAnalysis(records, AnalysisTable.CASES_BY_COUNTRY);

		sqlDF.show();
		LOGGER.info("\n================== Query String: " + query);
		LOGGER.info("================== PERSISTED ANALYSIS DATA at table " + AnalysisTable.CASES_BY_COUNTRY.value());
	}
	
	public static void generateTotalCasesPilot() {
		LOGGER.info("================== GENERATE TOTAL CASES PILOT DATA ... =====================");
		String query =  " SELECT country, date, SUM(confirmedCases) AS confirmedCases, 0 AS recoveredCases, 0 AS deathCases FROM " + HBaseConfig.TABLE_NAME 
				  + " GROUP BY country, date"
				  + " ORDER BY country DESC, date DESC ";
		
		Dataset<Row> sqlDF = sparkSession.sql(query);
		List<CaseReportByCountryDate> records = sqlDF.as(Encoders.bean(CaseReportByCountryDate.class)).collectAsList();
		HBaseRepository.getInstance().saveAnalysis(records, AnalysisTable.PILOT);
		LOGGER.info("================== PERSISTED ANALYSIS DATA at table " + AnalysisTable.PILOT.value());
	}
	
	public static void printCasesForCountryByDates() {
		String query =  " SELECT country, date, SUM(confirmedCases) AS confirmedCases, SUM(recoveredCases) AS recoveredCases, SUM(deathCases) AS deathCases "
				  + " FROM " + HBaseConfig.TABLE_NAME 
				  + " GROUP BY country, date"
				  + " ORDER BY country DESC, date DESC ";
		
		Dataset<Row> sqlDF = sparkSession.sql(query);
		
		LOGGER.info("================== PERSISTING ANALYSIS DATA ... =====================");
		List<CaseReportByCountryDate> records = sqlDF.as(Encoders.bean(CaseReportByCountryDate.class)).collectAsList();
		HBaseRepository.getInstance().saveAnalysis(records, AnalysisTable.CASES_BY_COUNTRY_DATE);
		
		sqlDF.show();
		LOGGER.info("\n================== Query String: " + query);
		LOGGER.info("================== PERSISTED ANALYSIS DATA at table " + AnalysisTable.CASES_BY_COUNTRY_DATE.value());
	}
	
	public static void saveRecordsToFile(String tableName, List<? extends CaseReport> records) {
		LOGGER.info("================== PERSISTING ANALYSIS DATA ... =====================");
		String fileName = new StringBuilder()
				.append("corona_output/")
				.append(tableName).append("_")
				.append(LocalDateTime.now().toString())
				.append(".csv")
				.toString();
		
		JavaSparkContext sparkContext = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
		sparkContext.parallelize(records).saveAsTextFile(fileName);
		LOGGER.info("================== PERSISTED ANALYSIS DATA at: " + fileName);
	}
	
	public static void printCustomQuery(String queryStr) {
		Dataset<Row> sqlDF = sparkSession.sql(queryStr);
		sqlDF.show();
		LOGGER.info("\n================== Query String: " + queryStr);
	}
	
	private static void printMenu() {
		System.out.println("================= Welcome to Corona Analysis Application  =====================");
		System.out.println("Please select program:");
		System.out.println("1. Show total Cases by Date");
		System.out.println("2. Show total Cases by Country");
		System.out.println("3. Show total Cases by Country and Date");
		System.out.println("4. Enter custom query");
		System.out.println("5. Generate Pilot data");
		System.out.println("Type 'exit' to stop program.");
		System.out.println("Your option: ");
	}
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		try (Scanner scanner = new Scanner(System.in)){
			init();
			
			while (true) {
				printMenu();
				String option = scanner.nextLine();

				switch (option) {
					case "1":
						printTotalCasesByDate();
						break;
					case "2":
						printTotalCasesByCountry();
						break;
					case "3":
						printCasesForCountryByDates();
						break;
					case "4":
						System.out.println("Enter your query: ");
						String queryStr = scanner.nextLine();
						printCustomQuery(queryStr);
						break;
					case "5":
						generateTotalCasesPilot();
						break;
					case "exit":
						System.exit(1);
					default:
				}
			}
			
		} catch (IOException e) {
			LOGGER.info("================== PROCESSING TO EXIT APPLICATION ... ====================");
			LOGGER.error("An error occur while running CoronaAnalysisApp. " + e);
			System.exit(0);
		}
	}
}
