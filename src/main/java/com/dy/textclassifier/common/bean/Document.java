package com.dy.textclassifier.common.bean;

import java.util.Map;

public class Document {

	/** 文档内容	 */
	private String content;

	/** 原始类别，1为正类，-1为负类	 */
	private double category;
	
	/** 预测类别	 */
	private double evalCategory;

	/**	分词后的词按顺序组成的数组  */
	private String[] terms;

	/** 词频统计，已去停用词	 */
	private Map<String, Integer> termFrequency;
	
	/** 有效词的总数目	 */
	private int termNum;

	/** 文档的向量表达形式	 */
	private double[] vector;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public double getCategory() {
		return category;
	}

	public void setCategory(double category) {
		this.category = category;
	}

	public double getEvalCategory() {
		return evalCategory;
	}

	public void setEvalCategory(double evalCategory) {
		this.evalCategory = evalCategory;
	}

	public String[] getTerms() {
		return terms;
	}

	public void setTerms(String[] terms) {
		this.terms = terms;
	}

	public Map<String, Integer> getTermFrequency() {
		return termFrequency;
	}

	public void setTermFrequency(Map<String, Integer> termFrequency) {
		this.termFrequency = termFrequency;
	}

	public double[] getVector() {
		return vector;
	}

	public void setVector(double[] vector) {
		this.vector = vector;
	}
	
	public Document(String content) {
		this.content = content;
	}

	public int getTermNum() {
		return termNum;
	}

	public void setTermNum(int termNum) {
		this.termNum = termNum;
	}

}
