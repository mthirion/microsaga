package org.jboss.integration.fis2demo.monitor;

public class Util {

	
	public static String getEventID() {
		// 12 numbers

		String a = Integer.toString(random4());
		String b = Integer.toString(random4());
		String c = Integer.toString(random4());
		
		return a.concat(b).concat(c);
	}
	
	private static int random4() {
		Double d = Math.random()*10000;
		return d.intValue();
	}
	
}
