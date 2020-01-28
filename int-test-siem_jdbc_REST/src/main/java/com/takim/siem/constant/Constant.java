package com.takim.siem.constant;

import java.util.Arrays;
import java.util.List;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Constant {

//  Authentication
	public static final String HEADER_AUTH = "X-Auth-Token";
	public static final String TOKEN_ISSUER = "takimLab";
	public static final String COOKIE_NAME = "jdbccolletor-cookie";
	public static final List<String> ALLOW_IP_LIST = Arrays.asList("172.17.2.26");

//  Kafka
	public static final String KAFKA_SERVER = "172.16.100.177:9092";
	public static KafkaProducer<String, String> KAFAK_PRODUCER;
	public static ProducerRecord<String, String> MESSAGE;
	public static final String KAFKA_TOPIC = "jdbc_logsource";
	
// 	Client IP
	public static String COLLECTOR_IP;
}
