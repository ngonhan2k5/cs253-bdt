package bdt.config;

import java.util.HashMap;
import java.util.Map;

public class KafkaConfig {
	public static final String KAFKA_BROKERS = "127.0.0.1:9092";
	public static final Integer MESSAGE_COUNT = 1000;
	public static final String CLIENT_ID = "client1";
	public static final String TOPIC_NAME = "corona_cases";
	public static final String GROUP_ID_CONFIG = "cGroup1";
	public static final Integer MAX_NO_MESSAGE_FOUND_COUNT = 100;
	public static final String OFFSET_RESET_LATEST = "latest";
	public static final String OFFSET_RESET_EARLIER = "earliest";
	public static final Integer MAX_POLL_RECORDS = 1;
	public static final Integer MESSAGE_SIZE = 20971520;
	
	public static Map<String, String> generateKafkaParams() {
		Map<String, String> params = new HashMap<>();
		params.put("bootstrap.servers", KAFKA_BROKERS);
		params.put("fetch.message.max.bytes", String.valueOf(MESSAGE_SIZE));
		return params;
	}
}
