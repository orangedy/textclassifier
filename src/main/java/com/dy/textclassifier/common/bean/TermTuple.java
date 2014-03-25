package com.dy.textclassifier.common.bean;

public class TermTuple {

	private int index;
	
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
