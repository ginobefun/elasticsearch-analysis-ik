package org.wltea.analyzer.db;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 本机IP获取工具类。
 * Created by ginozhang on 2017/4/5.
 */
public final class HostUtils {

	private static volatile String cachedIpAddress;

	/**
	 * 获取本机IP地址. 有限获取外网IP地址. 也有可能是链接着路由器的最终IP地址.
	 * 
	 * @return 本机IP地址
	 */
	public static String getIp() {
		if (null != cachedIpAddress) {
			return cachedIpAddress;
		}
		Enumeration<NetworkInterface> netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (final SocketException ex) {
			return "unknown ip";
		}

		String localIpAddress = null;
		while (netInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = netInterfaces.nextElement();
			Enumeration<InetAddress> ipAddresses = netInterface.getInetAddresses();
			while (ipAddresses.hasMoreElements()) {
				InetAddress ipAddress = ipAddresses.nextElement();
				if (isPublicIpAddress(ipAddress)) {
					String publicIpAddress = ipAddress.getHostAddress();
					cachedIpAddress = publicIpAddress;
					return publicIpAddress;
				}
				if (isLocalIpAddress(ipAddress)) {
					localIpAddress = ipAddress.getHostAddress();
				}
			}
		}
		cachedIpAddress = localIpAddress;
		return localIpAddress;
	}

	private static boolean isPublicIpAddress(final InetAddress ipAddress) {
		return !ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !isV6IpAddress(ipAddress);
	}

	private static boolean isLocalIpAddress(final InetAddress ipAddress) {
		return ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !isV6IpAddress(ipAddress);
	}

	private static boolean isV6IpAddress(final InetAddress ipAddress) {
		return ipAddress.getHostAddress().contains(":");
	}
}
