package com.tme.Algorithms;

import java.util.HashSet;
import java.util.Set;

public class DemoTest {
	public static void main(String[] args) {
		Set<String> set = new HashSet<>();
		String s1 = "this is good";
		String s2 = "this is good and this is bad"; 
		if(s2.contains(s1)) {
			System.out.println("this is good");
		}else {
			System.out.println("this is bad");
		}
	}
}
