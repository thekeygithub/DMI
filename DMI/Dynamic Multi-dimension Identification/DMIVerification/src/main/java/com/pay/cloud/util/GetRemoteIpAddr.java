package com.pay.cloud.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

/** 
 * 获取本机IP地址
 */
public class GetRemoteIpAddr {

	

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if(ValidateUtils.isEmpty(ip)){
			ip = "127.0.0.1";
		}
		return ip;
	}

	/**
	 * 获取windows系统IP地址
	 * 
	 * @return ip地址
	 */
	public static String getWindowsIPAddress() {
		try {
			InetAddress inet = InetAddress.getLocalHost();
			return inet.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取Linux系统IP地址
	 * 
	 * @return ip地址
	 */
	@SuppressWarnings("unchecked")
	public static String getLinuxIPAddress() {
		try {
			Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				System.out.println(ni.getName());
				ip = (InetAddress) ni.getInetAddresses().nextElement();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
					return ip.getHostAddress();
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
