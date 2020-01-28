package com.takim.siem.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.takim.siem.constant.Constant;

public class IPTraceUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(IPTraceUtil.class);
	
	public static void getClientIP() {
		try {
			InetAddress address = InetAddress.getLocalHost();
			Constant.COLLECTOR_IP = address.getHostAddress();
		} catch (UnknownHostException e) {
			logger.error("Get Client IP 에러");
		}
	}

}
