package bdt.kafka;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bdt.config.KafkaConfig;

public class KReducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(KReducer.class);
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		try {
			List<String> lines = Files.lines(Paths.get("/user_volumes/data/test.csv")).collect(Collectors.toList());
			publish(lines);
		} catch (IOException e) {
			System.out.println(e);
			LOGGER.error("Cannot read file. " + e);
		}
	}
	
	public static Producer<Long, String> createProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.KAFKA_BROKERS);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, KafkaConfig.MESSAGE_SIZE);
		
		return new KafkaProducer<>(props);
	}
	
	static void runProducer() {
		Producer<Long, String> producer = createProducer();
		for (int index = 0; index < KafkaConfig.MESSAGE_COUNT; index++) {
			ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(KafkaConfig.TOPIC_NAME,
					"This is record " + index);
			try {
				RecordMetadata metadata = producer.send(record).get();
				LOGGER.info("Record sent with key " + index + " to partition " + metadata.partition()
						+ " with offset " + metadata.offset());
			} catch (ExecutionException e) {
				LOGGER.error("Error in sending record " + e);
			} catch (InterruptedException e) {
				LOGGER.error("Error in sending record " + e);
			}
		}

	}
	
	public static void publish(List<String> data) throws IOException {
		try (Producer<Long, String> producer = createProducer()) {
			
			data.stream()
			.map(d -> new ProducerRecord<Long, String>(KafkaConfig.TOPIC_NAME, d))
			.forEach(record -> {
				try {
					producer.send(record).get();
				} catch (InterruptedException | ExecutionException e) {
					LOGGER.error("Cannot send record. " + e);
				}
			});
		}
	}
}
