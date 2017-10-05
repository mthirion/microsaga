package org.jboss.integration.fis2demo.cards;

public class Util {

	
	public static String getBankCardNumberBE() {
		// ex: 5333 5333 7444 7444

		String a = Integer.toString(random4());
		String b = Integer.toString(random4());
		String c = Integer.toString(random4());
		String d = Integer.toString(random4());
		
		return a.concat(b).concat(c).concat(d);
	}
	
	private static int random4() {
		Double d = Math.random()*10000;
		return d.intValue();
	}
	
}
