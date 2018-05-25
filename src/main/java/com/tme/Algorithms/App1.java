package com.tme.Algorithms;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

//消除空白行 input.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1");
/**
 * 要求： 文章中必须声明五种题型， { 单选题, 多选题, 判断题, 填空题, 问答题}; 字体必须一致 否则无法识别 需要保证关键字正确 答案 难度
 * 
 * 
 * 
 **/

public class App1 {
	enum questionType {
		单选题, 多选题, 判断题, 填空题, 问答题
	}

	// 分開每一道大體
	public Map<Integer, String> getPaperQuestionType(String content) {
		Map<Integer, String> map = new TreeMap<Integer, String>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				return o2.compareTo(o1);
			}
		});
		for (questionType qType : questionType.values()) {
			if (content.contains(qType.toString())) {
				int index = content.indexOf(qType.toString());
				map.put(index, qType.toString());
			}
		}
		return map;
	}

	// 分開單選題和多選題
	public Map<Integer, String> getQuestionChoice(String value) {
		String line[] = value.split("\n");
		// 取出每一道題
		Map<Integer, String> eachQuestionMap = new TreeMap<Integer, String>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		// 取出所有符合要求的題目
		Map<Integer, String> allQuestionMap = new TreeMap<Integer, String>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);
			}
		});
		for (int i = 0; i < line.length; i++) {
			// System.out.println(line[i]);
			if (startWithNumber(line[i]) && startWithLetter(line[i + 1])) {
				allQuestionMap.put(value.indexOf(line[i]), line[i]);
			}
		}
		String restContent = value;

		for (Map.Entry<Integer, String> allQuestionEntry : allQuestionMap.entrySet()) {
			Integer indexKey = allQuestionEntry.getKey(); // 当前索引值
			eachQuestionMap.put(indexKey, restContent.substring(indexKey));
			restContent = restContent.substring(0, indexKey);
		}
		return eachQuestionMap;
	}

	// 单选题的处理
	public List<Problem> singleChoice(String key, String value) {
		List<Problem> result = new ArrayList<Problem>();
		Map<Integer, String> map = getQuestionChoice(value);
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			String line[] = entry.getValue().split("\n");
			Problem problem = new Problem();
			List<ProblemItem> problemItemList = new ArrayList<ProblemItem>();
			problem.setType(key);
			// 得到試題答案
			String choiceAnswer = entry.getValue().substring(entry.getValue().indexOf("答案") + 3,
					entry.getValue().indexOf("答案") + 4);
			for (int i = 0; i < line.length; i++) {
				ProblemItem problemItem = new ProblemItem();
				line[i] = line[i].replaceAll(" ", "");
				if (startWithNumber(line[i])) {
					problem.setText(line[i]);
					// System.out.println("題目： " + getQuestionContent(line[i]));
				} else if (startWithLetter(line[i])) {
					problemItem.setText(line[i].substring(2));
					if (line[i].contains(choiceAnswer)) {
						problemItem.setAnswer(true);
					} else {
						problemItem.setAnswer(false);
					}
					problemItemList.add(problemItem);
				} else if (startWithDegreeOfDifficulty(line[i])) {
					// line[i].replace(":", "：");
					// String[] DegreeOfDifficulty = line[i].split("：");
					problem.setDifficult(line[i].substring(3));
				} else {

				}
				
				problem.setItems(problemItemList);
			}
			result.add(problem);
		}
		// -----------------------test------------------------------
		/*
		 * for(Problem p : result) { System.out.println(p.getText()); List<ProblemItem>
		 * pList = p.getItems(); for(ProblemItem pItem : pList) {
		 * System.out.println(pItem.getText()); System.out.println(pItem.isAnswer()); }
		 * System.out.println(p.getDifficult()); System.out.println(p.getText()); }
		 */
		// -----------------------test end------------------------------
		return result;
	}

	// 分開獲取判斷題 填空題 問答題的內容
	public Map<Integer, String> getAllQuestion(String key, String value) {
		String line[] = value.split("\n");
		// 取出每一道題
		Map<Integer, String> eachQuestionMap = new TreeMap<Integer, String>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		// 取出所有符合要求的題目
		Map<Integer, String> allQuestionMap = new TreeMap<Integer, String>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);
			}
		});
		for (int i = 0; i < line.length; i++) {
			// System.out.println(line[i]);
			if (startWithNumber(line[i]) && startWithAnswer(line[i + 1])) {
				allQuestionMap.put(value.indexOf(line[i]), line[i]);
			}
		}
		String restContent = value;

		for (Map.Entry<Integer, String> allQuestionEntry : allQuestionMap.entrySet()) {
			Integer indexKey = allQuestionEntry.getKey(); // 当前索引值
			eachQuestionMap.put(indexKey, restContent.substring(indexKey));
			restContent = restContent.substring(0, indexKey);
		}
		return eachQuestionMap;

	}

	// 判断题的处理
	public List<Problem> TFQuesion(String key, String value) {

		List<Problem> result = new ArrayList<>();
		Map<Integer, String> map = getAllQuestion(key, value);

		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			String answer = entry.getValue().substring(entry.getValue().indexOf("答案") + 3,
					entry.getValue().indexOf("答案") + 5);
			// System.out.println(choiceAnswer);
			String[] line = entry.getValue().split("\n");
			Problem problem = new Problem();
			List<ProblemItem> problemItemList = new ArrayList<>();
			problem.setType(key);
			for (int i = 0; i < line.length; i++) {
				ProblemItem problemItem = new ProblemItem();

				if (startWithNumber(line[i])) {
					problem.setText(getQuestionContent(line[i]));

				} else if (startWithAnswer(line[i])) {
					if (answer.equals("正确")) {

						problemItem.setPlace(1);
					} else {

						problemItem.setPlace(0);
					}
					problemItemList.add(problemItem);
					problem.setItems(problemItemList);
				}
				if (startWithDegreeOfDifficulty(line[i])) {

					problem.setDifficult(line[i].substring(3));
				} else {
					System.out.println("rubbish");
				}

			}

			result.add(problem);
		}
		// ----------------test--------------------

		for (Problem p : result) {
			System.out.println(p.getText());
			List<ProblemItem> pList = p.getItems();
			// System.out.println(pList);
			for (ProblemItem pItem : pList) {
				System.out.println(pItem.getPlace());

			}

			System.out.println(p.getDifficult());

		}

		// ----------------test--------------------
		return result;
	}

	// 填空题的处理
	public List<Problem> fillBlank(String key, String value) {
		List<Problem> result = new ArrayList<>();

		Map<Integer, String> map = getAllQuestion(key, value);
		
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			String[] line = entry.getValue().split("\n");
			Problem problem = new Problem();
			problem.setType(key);
			List<ProblemItem> problemItemList = new ArrayList<>();
			for (int i = 0; i < line.length; i++) {
				ProblemItem problemItem = new ProblemItem();

				if (startWithNumber(line[i])) {
					problem.setText(getQuestionContent(line[i]));

				} else if (startWithAnswer(line[i])) {
					problemItem.setText(line[i].substring(3));
					problemItemList.add(problemItem);
					problem.setItems(problemItemList);
				}
				if (startWithDegreeOfDifficulty(line[i])) {

					problem.setDifficult(line[i].substring(3));
				} else {
					System.out.println("rubbish");
				}

			}

			result.add(problem);
		}
		// ----------------test--------------------

		for (Problem p : result) {
			System.out.println(p.getText());
			List<ProblemItem> pList = p.getItems();
			// System.out.println(pList);
			for (ProblemItem pItem : pList) {
				System.out.println(pItem.getText());

			}

			System.out.println(p.getDifficult());

		}

		// ----------------test--------------------
		return result;
	}

	// 多选题的处理
	public List<Problem> multiChoice(String key, String value) {
		List<Problem> result = new ArrayList<>();

		Map<Integer, String> map = getQuestionChoice(value);
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			String line[] = entry.getValue().split("\n");
			Problem problem = new Problem();
			List<ProblemItem> problemItemList = new ArrayList<ProblemItem>();
			problem.setType(key);
			// 得到試題答案
			String choiceAnswer = entry.getValue().substring(entry.getValue().indexOf("答案") + 3);
			String RightAnswer = getRightAnswer(choiceAnswer);

			for (int i = 0; i < line.length; i++) {
				ProblemItem problemItem = new ProblemItem();
				line[i] = line[i].replaceAll(" ", "");
				if (startWithNumber(line[i])) {
					problem.setText(line[i]);
					// System.out.println("題目： " + getQuestionContent(line[i]));
				} else if (startWithLetter(line[i])) {
					problemItem.setText(line[i].substring(2));
					if (RightAnswer.contains(line[i].substring(0, 1))) {
						problemItem.setAnswer(true);
					} else {
						problemItem.setAnswer(false);
					}
					problemItemList.add(problemItem);
				} else if (startWithDegreeOfDifficulty(line[i])) {
					// line[i].replace(":", "：");
					// String[] DegreeOfDifficulty = line[i].split("：");
					problem.setDifficult(line[i].substring(3));
				} else {

				}
				problem.setItems(problemItemList);
			}
			result.add(problem);
		}
		// -----------------------test------------------------------
		/*
		 * for(Problem p : result) { //System.out.println(p.getText());
		 * List<ProblemItem> pList = p.getItems(); for(ProblemItem pItem : pList) {
		 * System.out.println(pItem.getText()); System.out.println(pItem.isAnswer()); }
		 * System.out.println(p.getDifficult()); System.out.println(p.getText()); }
		 */
		// -----------------------test end------------------------------
		return result;
	}

	private String getRightAnswer(String choiceAnswer) {
		// TODO Auto-generated method stub
		String result = "";
		char[] array = choiceAnswer.toCharArray();
		for (int i = 0; i < array.length; i++) {
			if (Character.isLetter(array[i])) {
				result += array[i];
			}
		}
		return result;
	}

	public List<String> getAllQAQuestion(String value) {
		String line[] = value.split("\n");
		// 抓取所有的题目 每道题分开存
		Map<String, String> eachQuestion = new HashMap<>();
		// 取出所有符合要求的题
		Map<Integer, String> questionMap = new TreeMap<>(new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				return o2.compareTo(o1);
			}
		});
		// System.out.println(value);
		for (int i = 0; i < line.length; i++) {
			// System.out.println(line[i]);
			if (startWithNumber(line[i]) && endWithColon(line[i]) && startWithAnswer(line[i + 1])) {
				questionMap.put(value.indexOf(line[i]), line[i]);
			}
		}
		// 将没一道题存入map中 对每一道题解析 忽略中间的内容
		String restContent = value;
		List<String> list = new ArrayList<>();
		for (Map.Entry<Integer, String> questionEntry : questionMap.entrySet()) {
			Integer indexKey = questionEntry.getKey(); // 当前索引值
			list.add(restContent.substring(indexKey));
			restContent = restContent.substring(0, indexKey);

		}
		return list;
	}

	// 问答题的处理
	public List<Problem> QAQuestion(String key, String value) {
		List<Problem> result = new ArrayList<>();
		List<String> questionList = getAllQAQuestion(value);
		//System.out.println(questionList);
		
		for (String blockString : questionList) {
			
			Problem problem = new Problem();
			ProblemItem problemItem = new ProblemItem();
			List<ProblemItem> problemItemList = new ArrayList<>();
			// System.out.println(string);
			problem.setType(key);
			String restQA = blockString;
			String finalDifficulty = "";
			String finalAnswer = "";
			String finalQuestion = "";
			// System.out.println(blockString);
			if (restQA.contains("难度")) {
				int index = restQA.indexOf("难度");
				finalDifficulty = restQA.substring(index);
				restQA = restQA.substring(0, index);
			}
			if (restQA.contains("答案")) {
				int index = restQA.indexOf("答案");
				finalAnswer = restQA.substring(index);
				restQA = restQA.substring(0, index);
				// System.out.println(finalAnswer);
			}
			if (startWithNumber(restQA)) {
				finalQuestion = restQA.substring(0);
			}
			problem.setText(finalQuestion);
			problemItem.setText(finalAnswer);
			problemItemList.add(problemItem);
			problem.setItems(problemItemList);
			
			problem.setDifficult(finalDifficulty);
			result.add(problem);

		}
		// ----------------test--------------------

				for (Problem p : result) {
					System.out.println(p.getText());
					List<ProblemItem> pList = p.getItems();
					// System.out.println(pList);
					for (ProblemItem pItem : pList) {
						System.out.println(pItem.getText());

					}

					System.out.println(p.getDifficult());

				}

				// ----------------test--------------------
		return result;
	}

	private Map<String, String> seperatePaperByQuestionType(Map<Integer, String> map, String content) {
		Map<String, String> result = new HashMap<>();
		String restContent = content;
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			// System.out.println(entry.getKey());
			// System.out.println(entry.getValue());
			result.put(entry.getValue(), restContent.substring(entry.getKey()));
			restContent = restContent.substring(0, entry.getKey());
		}
		// System.out.println(result);
		return result;
	}

	public static void main(String[] args) throws Exception {
		String inputFile = "../Algorithms/src/main/java/com/tme/Algorithms/第二章 细胞的基本功能--导入1.docx";
		App1 app1 = new App1();
		app1.importPaper(inputFile);
	}

	public List<Problem> importPaper(String inputFile) throws IOException, OpenXML4JException {
		FileInputStream fis = new FileInputStream(inputFile);
		// 得到所有的word文檔內容
		String result = microsoftWordDocumentToString(fis);
		// 去除所有空行
		String content = removeAllBlankSpace(result);
		// 獲取試卷包含題的類型
		Map<Integer, String> getPaperQuestionType = getPaperQuestionType(content);
		// 問題和所有內容 例如 選擇題---> content
		Map<String, String> problemMap = seperatePaperByQuestionType(getPaperQuestionType, content);
		// 返回值
		List<Problem> problemList = new ArrayList<Problem>();

		// 對每種題型分別處理
		for (Map.Entry<String, String> entry : problemMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (key == "单选题") {
				singleChoice(key, value);
			} else if (key == "多选题") {
				multiChoice(key, value);

			} else if (key == "判断题") {
				TFQuesion(key, value);
			} else if (key == "问答题") {
				QAQuestion(key, value);
				
			} else if (key == "填空题") {
				fillBlank(key, value);
			} else {

			}
		}
		// 開關
		return problemList;
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

	// 去除題目所有的數字與字符 前面三位的
	public String removeNumberAndPunctuation(String str) {
		String result = "";

		return result;
	}

	// 處理所有的空格 换行符 空格 tab
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static boolean endWithColon(String str) {
		if (str.endsWith(":") || str.endsWith("：")) {
			return true;
		}
		return false;
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
		Pattern pattern = Pattern.compile("[0-9].*");
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

	// remove 单行中所有的空格 包括前面 后面 中间 （填空题不适用）
	public static String removeBlankSpaceWithinLine(String str) {
		return str.replaceAll(" ", "");
	}

	// remove文本中所有的空行
	public String removeAllBlankSpace(String str) {
		String result = "";
		String pattern = "(?m)^\\s*\\r?\\n|\\r?\\n\\s*(?!.*\\r?\\n)";
		result = str.replaceAll(pattern, "");
		return result;
	}

	// 抓取所有的文本
	public static String microsoftWordDocumentToString(InputStream inputStream) throws IOException, OpenXML4JException {
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