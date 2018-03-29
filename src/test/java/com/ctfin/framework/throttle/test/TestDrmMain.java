/**
 * TestDrmMain.java
 * author: yujiakui
 * 2018年1月13日
 * 下午1:52:02
 */
package com.ctfin.framework.throttle.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author yujiakui
 *
 *         下午1:52:02
 *
 */
public class TestDrmMain {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		System.setProperty("applicationName", "yjk");
		System.setProperty("zk.server.addr", "192.168.1.202:2181");

		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				"com.ctfin.framework.throttle", "com.ctfin.framework.drm.client");
		System.out.println(annotationConfigApplicationContext);

		while (true) {
			Thread.sleep(10);
		}

	}

}
