package it.contocorrente2.utils;

public class MyUtils {
	public static String formateStringWithPadding(String str, int padd) {
		String paddStr = "%-" + padd + "s";
		return String.format(paddStr, str);
	}
}
