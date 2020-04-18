package bdt;

import org.apache.log4j.BasicConfigurator;
import bdt.kafka.KConsumer;

public class Application {

	public static void main(String[] args) throws InterruptedException {
		BasicConfigurator.configure();
		KConsumer.startConsumer();
	}
}
