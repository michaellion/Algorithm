package com.tme.Algorithms;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class App {

	public static void main(String[] args) throws Exception {
		String inputFile = "../Algorithms/src/main/java/com/tme/Algorithms/第二章 细胞的基本功能--导入1.docx";
		FileInputStream fis = new FileInputStream(inputFile);
		String content = microsoftWordDocumentToString(fis);
		String[] lString = content.split("\n");
		// System.out.println(lString.length);
		Map<String, String> problemMap = new HashMap<>();
		// 順序不能变
		for (int i = 0; i < 5; i++) {
			if (content.contains("问答题")) {
				int index = content.indexOf("问答题");
				String con = content.substring(index);
				problemMap.put("问答题", con);
				content = content.substring(0, index);
			} else if (content.contains("填空题")) {
				int index = content.indexOf("填空题");
				String con = content.substring(index);
				problemMap.put("填空题", con);
				content = content.substring(0, index);
			} else if (content.contains("判断题")) {
				int index = content.indexOf("判断题");
				String con = content.substring(index);
				problemMap.put("判断题", con);
				content = content.substring(0, index);
			} else if (content.contains("多选题")) {
				int index = content.indexOf("多选题");
				String con = content.substring(index);
				problemMap.put("多选题", con);
				content = content.substring(0, index);
			} else if (content.contains("单选题")) {
				int index = content.indexOf("单选题");
				String con = content.substring(index);
				problemMap.put("单选题", con);
				content = content.substring(0, index);
			} else {

			}
		}

		// 對每種題型分別處理
		for (Map.Entry<String, String> entry : problemMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (key == "单选题") {
				String line[] = value.split("\n");
				for (int i = 0; i < line.length; i++) {
					line[i] = replaceBlank(line[i]);
					if (startWithNumber(line[i])) {
						String questoinNumber = getQuestionNumber(line[i].substring(0, 4));
						// System.out.println("單選題： "+questoinNumber);
						// System.out.println(getQuestionContent(line[i]));
					} else if (startWithLetter(line[i])) {
						// System.out.println(line[i].substring(0, 1) + "-------" +
						// line[i].substring(2));
					} else if (startWithAnswer(line[i])) {

						// System.out.println("答案： " + line[i].substring(3));
					}
					if (startWithDegreeOfDifficulty(line[i])) {
						// 得到難易程度
						// if (startWithDegreeOfDifficulty(line[i])) {
						line[i].replace(":", "：");
						String[] DegreeOfDifficulty = line[i].split("：");
						// System.out.println(
						// "DegreeOfDifficulty: " + DegreeOfDifficulty[0] + " 11111111 " +
						// DegreeOfDifficulty[1]);
						// }
					} else {

					}
				}

			} else if (key == "多选题") {
				String line[] = value.split("\n");
				for (int i = 0; i < line.length; i++) {
					line[i] = line[i].trim();

					if (startWithNumber(line[i])) {
						// 得到題目
						// String questoinNumber = getQuestionNumber(line[i].substring(0, 4));
						// System.out.println("多選題"+questoinNumber);
						// System.out.println(getQuestionContent(line[i]));
						// System.out.println("this is question" + ":" + line[i]);
					} else if (startWithLetter(line[i])) {
						// 得到選項
						// System.out.println(line[i].substring(0, 1) + "-------" +
						// line[i].substring(2));
					} else if (startWithAnswer(line[i])) {
						// System.out.println("答案： " + line[i].substring(3));
					} else if (startWithDegreeOfDifficulty(line[i])) {

					}
					if (startWithDegreeOfDifficulty(line[i])) {
						// 得到難易程度
						// if (startWithDegreeOfDifficulty(line[i])) {
						line[i].replace(":", "：");
						// String[] DegreeOfDifficulty = line[i].split("：");
						// System.out.println(
						// "DegreeOfDifficulty: " + DegreeOfDifficulty[0] + " 11111111 " +
						// DegreeOfDifficulty[1]);
						// }
					} else {

					}
				}
			} else if (key == "判断题") {
				String line[] = value.split("\n");
				for (int i = 0; i < line.length; i++) {
					line[i] = line[i].trim();
					if (startWithNumber(line[i])) {
						// 得到題目
						// String questoinNumber = getQuestionNumber(line[i].substring(0, 4));
						// System.out.println(questoinNumber);
						// System.out.println(getQuestionContent(line[i]));

					} else if (startWithAnswer(line[i])) {

						// System.out.println("判斷題答案： " + line[i].substring(3));
					} else if (startWithDegreeOfDifficulty(line[i])) {
						line[i].replace(":", "：");
						// String[] DegreeOfDifficulty = line[i].split("：");
						// System.out.println("判斷題 DegreeOfDifficulty: " + DegreeOfDifficulty[0] + "
						// 11111111 "
						// + DegreeOfDifficulty[1]);
					} else {

					}
				}
			} else if (key == "问答题") {
				// System.out.println(value);
				//value = value.replaceAll("\\s+", "");
				String line[] = value.split("\n");
				System.out.println(value);
				Map<String, String> qaMap = new HashMap<>();
				// -------------------------------------------------
				StringBuffer sb = null;
				//String questionKey = "";
				for (int i = 0; i < line.length; i++) {

					/*if (line[i].startsWith("问答题")) {
						continue;
					}*/
					System.out.println(line[i].length());
					

				}
				
		
			} else if (key == "填空题") {
				String line[] = value.split("\n");
				for (int i = 0; i < line.length; i++) {
					line[i] = line[i].trim();
					if (startWithNumber(line[i])) {
						// 得到題目 特殊處理
						// String questoinNumber = getQuestionNumber(line[i].substring(0, 4));
						// System.out.println(questoinNumber);
						// System.out.println(getQuestionContent(line[i]));

						// System.out.println("this is 填空题 question" + ":" + line[i]);
					} else if (startWithAnswer(line[i])) {
						// 得到答案
						// System.out.println("this is 填空题 answer" + ":" + line[i]);
					} else if (startWithDegreeOfDifficulty(line[i])) {
						line[i].replace(":", "：");
						// String[] DegreeOfDifficulty = line[i].split("：");
						// System.out.println("填空题 DegreeOfDifficulty: " + DegreeOfDifficulty[0] + "
						// 11111111 "
						// + DegreeOfDifficulty[1]);
					} else {

					}
				}
			} else {

			}
		}
		// 開關
	}

	// 返回判斷題的題號
	public static String getQuestionNumber(String str) {
		char[] charArray = str.toCharArray();
		String result = "";
		for (int i = 0; i < charArray.length; i++) {
			if (Character.isDigit(charArray[i])) {
				result += charArray[i];
			}
		}
		return result;
	}

	// 返回題目內容
	public static String getQuestionContent(String str) {

		int length = getQuestionNumber(str).length();

		return str.substring(length + 1);

	}
	// 返回題號
	/*
	 * public static int getQuestionNumber(char[] charArray) { String result = "";
	 * for (int i = 0; i < charArray.length; i++) { if
	 * (Character.isDigit(charArray[i])) { result += charArray[i]; } }
	 * 
	 * return Integer.parseInt(result);
	 * 
	 * }
	 */

	//截取答案内容
	
	
	
	
	//
	
	// 获取题目
	public static List<String> getQuestion(String str) {
		List<String> list = new ArrayList<String>();
		if (str.contains("、") || (str.contains(".")) || str.contains("、")) {
			str.replaceAll(".", "、");
			str.replaceAll("、", "、");
			String question[] = str.split("、");
			String questionNumber = question[0];
			String questionContent = question[1];
			list.add(questionNumber);
			list.add(questionContent);
			return list;
		}
		return null;
	}

	// 處理空格
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	// 難易程度
	public static boolean startWithDegreeOfDifficulty(String str) {
		str = str.trim();
		// System.out.println(str);
		if (str.startsWith("难度")) {
			return true;
		}
		return false;
	}

	// 是否是答案
	public static boolean startWithAnswer(String str) {
		str = str.trim();
		// System.out.println(str);
		if (str.startsWith("答案")) {
			return true;
		}
		return false;
	}

	// 判斷是否是數字開頭
	public static boolean startWithNumber(String str) {
		str = str.trim();
		replaceBlank(str);
		Pattern pattern = Pattern.compile("[0-9]*");
		// System.out.println(str);
		Matcher isNum = pattern.matcher(str.charAt(0) + "");
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	// 是否是以換行結尾
	public static boolean endWithEnter(String str) {
		str = str.trim();
		Pattern pattern = Pattern.compile("\n");
		Matcher isEndWithEnter = pattern.matcher(str.charAt(str.length() - 1) + "");
		if (!isEndWithEnter.matches()) {
			return false;
		}
		return true;
	}

	// 是否以字母開頭
	public static boolean startWithLetter(String str) {
		str = str.trim();
		Pattern pattern = Pattern.compile("[A-Za-z]*");
		Matcher isLetter = pattern.matcher(str.charAt(0) + "");
		if (!isLetter.matches()) {
			return false;
		}
		return true;
	}

	// 抓取所有的文本
	private static String microsoftWordDocumentToString(InputStream inputStream)
			throws IOException, OpenXML4JException {
		String strRet;
		try (InputStream wordStream = new BufferedInputStream(inputStream)) {
			// 处理97～03
			if (POIFSFileSystem.hasPOIFSHeader(wordStream)) {
				WordExtractor wordExtractor = new WordExtractor(wordStream);
				strRet = wordExtractor.getText();
				wordExtractor.close();
			} else {
				// 处理07.....
				long start = System.currentTimeMillis();
				XWPFWordExtractor wordXExtractor = new XWPFWordExtractor(new XWPFDocument(wordStream));
				strRet = wordXExtractor.getText();
				long end = System.currentTimeMillis();
				 System.out.println("total times: " + (end - start));
			}
		}
		return strRet;
	}

}