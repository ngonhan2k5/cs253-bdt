package bdt.config;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HBaseConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(HBaseConfig.class);

	public static final String TABLE_NAME = "corona_cases";
	public static final String COLUMN_FAMILY = "cc";

	public static final String COL_STATE = "state";
	public static final String COL_COUNTRY = "country";
	public static final String COL_COUNTY = "county";
	public static final String COL_DATE = "date";
	public static final String COL_CONFIRMED_CASES = "confirmedCases";
	public static final String COL_DEATH_CASES = "deathCases";
	public static final String COL_RECOVERED_CASES = "recoveredCases";
	
	
	/**
	 * Analysis Tables
	 */
	public static final String ANALYSIS_COL_FAMILY = "ana";
	public static final String COL_COUNT = "total_cases";
	
	
	private static Connection connection;
	
	public static Connection getHBaseConnection() {
		if (connection == null) {
			try {
				Configuration config = HBaseConfiguration.create();
				connection = ConnectionFactory.createConnection(config);
			} catch (IOException e) {
				LOGGER.error("Cannot create HBase connection. " + e);
				System.exit(0);
			}
		}
		
		return connection;
	}
}
