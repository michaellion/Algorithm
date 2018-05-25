package com.tme.Algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 试题
 */
public class Problem {
	private static final long serialVersionUID = 1L;

	public static final String 单选题 = "单选题";
	public static final String 多选题 = "多选题";
	public static final String 判断题 = "判断题";
	public static final String 填空题 = "填空题";
	public static final String 问答题 = "问答题";
	public static final String 技能题 = "技能题";
	public static final String 名词解释 = "名词解释";

	public static final String[] TYPES = { 单选题, 多选题, 判断题, 填空题, 问答题, 名词解释, 技能题 };
	public static final Set<String> objectives = new HashSet<String>();
	public static final Set<String> subjectives = new HashSet<String>();

	static {
		objectives.add(单选题);
		objectives.add(多选题);
		objectives.add(判断题);

		subjectives.add(填空题);
		subjectives.add(问答题);
		subjectives.add(名词解释);
	}

	public static final char ERROR_OPTION = (char) -1; // 错误的选项编号

	private long id;
	private long departmentId;
	private Long categoryId; // 分类
	private Long createrId; // 上传者
	private String text; // 试题题面
	private String type; // 试题类型

	private String difficult; // 试题难度
	private long packageId; // 试题包ID
	private Double score; // 试题分数 只作为考试显示使用

	private boolean open;// 是否公开

	private String chapter;// 章节

	private List<ProblemItem> items = new ArrayList<ProblemItem>(); // 试题选项
	private String examState; // 当前考试状态
	private Integer solveCount;// 考生解答总数，暂时用于统计正确率时使用
	private String packageName;// 试题包名称
	private int count;
	private Boolean correct;// 是否为考生答对试题，仅用于统计分析用

	public long getId() {
		return id;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDifficult() {
		return difficult;
	}

	public void setDifficult(String difficult) {
		this.difficult = difficult;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	public List<ProblemItem> getItems() {
		return items;
	}

	public void setItems(List<ProblemItem> items) {
		this.items = items;
	}

	public long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	public boolean equals(Object obj) {
		String str = obj.toString();

		return !str.equals(type);
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getExamState() {
		return examState;
	}

	public void setExamState(String examState) {
		this.examState = examState;
	}

	public Long getCreaterId() {
		return createrId;
	}

	public void setCreaterId(Long createrId) {
		this.createrId = createrId;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public Integer getSolveCount() {
		return solveCount;
	}

	public void setSolveCount(Integer solveCount) {
		this.solveCount = solveCount;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Boolean getCorrect() {
		return correct;
	}

	public void setCorrect(Boolean correct) {
		this.correct = correct;
	}
}