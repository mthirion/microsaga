package org.jboss.integration.fis2demo.account;

public class Util {

	public static String getBankAccountNumberBE() {
		// ex: BE90 0634 2187 2132
		
		String a = Integer.toString(random2());
		String b = Integer.toString(random4());
		String c = Integer.toString(random4());
		String d = Integer.toString(random4());
		
		return "BE".concat(a).concat(b).concat(c).concat(d);
		
	}
	
	public static String getEventID() {
		// 6 numbers
		String a = Integer.toString(random4());
		String b = Integer.toString(random2());
		
		return a.concat(b);
	}

	private static int random4() {
		Double d = Math.random()*10000;
		return d.intValue();
	}
	private static int random2() {
		Double d = Math.random()*100;
		return d.intValue();		
	}
}
