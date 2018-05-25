package com.tme.Algorithms;



/**
 *作者 赵智舜
 *时间 2014-5-6
 */
public class ProblemItem{
	private static final long serialVersionUID = 1L;

	private long id;
	private long problemId; //试题ID
	private int place; //位置 判断题定义为0:1 0表示错误选项 1表示正确选项
	private String text; //选项内容 主观题为参考答案

	private boolean answer; // 是否是正确答案
	private Boolean isSolve;//是否为考生所选

	public long getId() {
		return id;
	}

	public long getProblemId() {
		return problemId;
	}

	public void setProblemId(long problemId) {
		this.problemId = problemId;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isAnswer() {
		return answer;
	}

	public void setAnswer(boolean answer) {
		this.answer = answer;
	}

	public Boolean getIsSolve() {
		return isSolve;
	}

	public void setIsSolve(Boolean isSolve) {
		this.isSolve = isSolve;
	}
}
