package com.dy.textclassifier.common.bean;

public class TermTuple {

	/**
	 * 特征序号
	 */
	private int index;
	
	/**
	 * 特征权值，即IDF值
	 */
	private double IDFWeight;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getIDFWeight() {
		return IDFWeight;
	}

	public void setIDFWeight(double iDFWeight) {
		IDFWeight = iDFWeight;
	}
}
