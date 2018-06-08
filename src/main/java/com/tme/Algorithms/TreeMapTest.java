package com.tme.Algorithms;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeMapTest {

	public static void main(String[] args) {
	
		/*
		List<Integer> finalResult = new ArrayList<>();
		List<Integer> list1 = new ArrayList<>();
		List<Integer> list2 = new ArrayList<>();
		
		for(int i = 1 ; i  < 10; i++) {
			list1.add(i);
			list2.add(i);
		}
		
		finalResult.addAll(list1);
		finalResult.addAll(list2);
		System.out.println(finalResult);
		*/
		
		
		
		
		
		if (false) {
			Pattern pattern = Pattern.compile("[.]+(:)$");
			String str = "dfasjfasdjk:";

			Matcher isNum = pattern.matcher(str);
			if (str.endsWith(":")) {
				System.out.println("pipei");
			}
		}

		if (false) {
			Map<Integer, String> map = new TreeMap<Integer, String>(new Comparator<Integer>() {
				public int compare(Integer arg0, Integer arg1) {
					// TODO Auto-generated method stub
					return arg1.compareTo(arg0);
				}
			});
			map.put(3, "ccccc");
			map.put(2, "aaaaa");
			map.put(1, "bbbbb");
			map.put(4, "ddddd");

			Set<Integer> keySet = map.keySet();
			Iterator<Integer> iter = keySet.iterator();
			while (iter.hasNext()) {
				Integer key = iter.next();
				System.out.println(key + ":" + map.get(key));
			}
		}
	}
}