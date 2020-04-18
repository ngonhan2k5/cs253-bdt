package bdt.utils;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.spark.api.java.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import bdt.model.CaseReportByCountryDate;
import bdt.model.CoronaRecord;
import scala.Tuple2;

public class RecordParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordParser.class);
	public static final DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final DateTimeFormatter FORMATER_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	public static final DateTimeFormatter FORMATER_3 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final DateTimeFormatter FORMATER_4 = DateTimeFormatter.ofPattern("MM/dd/yy");
	public static final DateTimeFormatter FORMATER_5 = DateTimeFormatter.ofPattern("M/dd/yy");
	public static final DateTimeFormatter FORMATER_6 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	public static final DateTimeFormatter FORMATER_7 = DateTimeFormatter.ofPattern("M/dd/yyyy");
	public static final DateTimeFormatter FORMATER_8 = DateTimeFormatter.ofPattern("M/d/yyyy");
	public static final DateTimeFormatter FORMATER_9 = DateTimeFormatter.ofPattern("M/d/yy");
	
	private static final String DELIMETER = "\t";
	private static final CSVParser PARSER = new CSVParserBuilder().withSeparator(',').withQuoteChar('"').build();

	public static CoronaRecord parse(String line) {
		try {
			String[] fields = PARSER.parseLine(line);
			
			if (fields.length < 7)
				return null;

			// Headers: Country/Region, Province/State, County, Last Update, Confirmed, Deaths, Recovered
			String country = fields[0];
			String state = fields[1];
			String county = fields[2];
			LocalDate date = parseDate(fields[3]);
			int confirmedCases = StringUtils.isNotBlank(fields[4]) ? Integer.parseInt(fields[4]) : 0;
			int deathCases = StringUtils.isNotBlank(fields[5]) ? Integer.parseInt(fields[5]) : 0;
			int recoveredCases = StringUtils.isNotBlank(fields[6]) ? Integer.parseInt(fields[6]) : 0;

			return new CoronaRecord(parseCountry(country), state, county, date, confirmedCases, deathCases, recoveredCases);
		} catch (Exception e) {
			LOGGER.warn("Cannot parse record. [" + line + "] " + e);
			return null;
		}
	}
	
	private static String parseCountry(String country) {
		if (country != null && country.toUpperCase().contains("CHINA"))
			return "China";
		return country;
	}

	public static LocalDate parseDate(String value) {
		if (StringUtils.isBlank(value))
			return null;
		String dateStr = value.split(" ")[0];
		
		List<DateTimeFormatter> formaters = Arrays.asList(FORMATER, FORMATER_2, FORMATER_3, FORMATER_4, FORMATER_5, FORMATER_6, FORMATER_7, FORMATER_8, FORMATER_9);
		
		for (DateTimeFormatter formater : formaters) {
			try {
				return LocalDate.parse(dateStr, formater);
			} catch(DateTimeParseException ex) {
			}
		}
		
		return null;
	}
	
	public static CoronaRecord parse(Tuple2<String, String> tuple2) {
		return parse(tuple2._2());
	}

	public static String joinFields(String[] fields) {
		return Stream.of(fields).collect(Collectors.joining(DELIMETER));
	}

	public static List<String> parseRecords(String data) throws IOException {
		try (CSVReader csvReader = new CSVReaderBuilder(new StringReader(data)).withSkipLines(1).withCSVParser(PARSER)
				.build()) {
			List<String[]> lines = csvReader.readAll();

			return lines.stream().map(RecordParser::joinFields).collect(Collectors.toList());
		}
	}
	
	public static CaseReportByCountryDate transformPilot(CaseReportByCountryDate record, Map<String, String> transformMap) {
		CaseReportByCountryDate result = new CaseReportByCountryDate();
		try {
			BeanUtils.copyProperties(result, record);
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.warn("Cannot transform record. " + e);
		}
		result.setCountry(Optional.ofNullable(transformMap.get(record.getCountry())).orElse(record.getCountry()));
		return result;
	}

	/**
	 * Test parsing record
	 * 
	 * @param args [input file] [skip records] [limit records]
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		try {
			String input = args != null && args.length > 0 ? args[0] : "test.csv";
			int skipRecords = args != null && args.length > 1 ? Integer.parseInt(args[1]) : 1;
			int limitRecords = args != null && args.length > 2 ? Integer.parseInt(args[2]) : 200;

			List<String> lines = Files.readAllLines(Paths.get(input));

			lines.stream().skip(skipRecords).map(RecordParser::parse).limit(limitRecords).map(CoronaRecord::toString)
					.forEach(LOGGER::info);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
