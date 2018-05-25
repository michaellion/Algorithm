package com.tme.Algorithms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpression {
	public static void main(String[] args) {
		RegularExpression regularExpression = new RegularExpression();
		regularExpression.testRemoveNumber();

	}

	public void testRemoveNumber() {
		String string = "1、神经调节的基本方式是：";
		Pattern pattern = Pattern.compile("^[1-9].*[、| . | 。]");
		Matcher matcher = pattern.matcher(string);
		if (matcher.matches()) {
			//matcher.replaceAll("").trim();
			System.out.println(string);
		}
		//System.out.println(string);
	}
	
	
	public void testAnswer() {
		String string = "答案fasghkfgjh答案fasfask答案fasfask答案fasfask1";

		Pattern pattern = Pattern.compile("^[答案].*(\\d)$");

		Matcher matcher = pattern.matcher(string);

		if (matcher.matches()) {
			System.out.println(matcher.start(0));
			System.out.println("good");
		} else {
			System.out.println("bad");
		}
	}
}
