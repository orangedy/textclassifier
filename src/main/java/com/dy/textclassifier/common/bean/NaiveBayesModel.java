package com.dy.textclassifier.common.bean;

public class NaiveBayesModel {
	
	private double[] pCate;
	
	private double[] pInPosi;
	
	private double[] pInNega;

	public double[] getpCate() {
		return pCate;
	}

	public void setpCate(double[] pCate) {
		this.pCate = pCate;
	}

	public double[] getpInPosi() {
		return pInPosi;
	}

	public void setpInPosi(double[] pInPosi) {
		this.pInPosi = pInPosi;
	}

	public double[] getpInNega() {
		return pInNega;
	}

	public void setpInNega(double[] pInNega) {
		this.pInNega = pInNega;
	}
}
