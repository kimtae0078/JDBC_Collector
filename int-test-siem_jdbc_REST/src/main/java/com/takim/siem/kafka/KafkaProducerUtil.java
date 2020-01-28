package com.takim.siem.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.takim.siem.constant.Constant;

import kafka.common.FailedToSendMessageException;

public class KafkaProducerUtil {

	// kafka node
	private static Properties properties = new Properties();

	public static void createProducer() {

		// Kafka Producer Config
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.KAFKA_SERVER);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");

		Constant.KAFAK_PRODUCER = new KafkaProducer<String, String>(properties);
	}

	public static void sendMessage(ProducerRecord<String, String> message) {
		Logger logger = LoggerFactory.getLogger(KafkaProducerUtil.class);
		// send Message
		try {
			Constant.KAFAK_PRODUCER.send(message);
		} catch (FailedToSendMessageException e) {
			logger.error("Kafka 전송 에러");
		}
	}

}
