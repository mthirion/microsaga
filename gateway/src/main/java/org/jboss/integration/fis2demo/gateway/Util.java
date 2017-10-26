package org.jboss.integration.fis2demo.gateway;

public class Util {

	public static String getTransactionID() {
		// 8 digits
		String a = Integer.toString(random4());
		String b = Integer.toString(random4());
		
		return a.concat(b);
	}

	private static int random4() {
		Double d = Math.random()*10000;
		return d.intValue();
	}
}
