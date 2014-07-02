package com.dy.textclassifier.common.bean;

import java.io.Serializable;

public class NaiveBayesModel implements Serializable{
	
	private static final long serialVersionUID = -9028042931038592991L;

	/**
	 * 各个类别的概率
	 */
	private double[] pCate;
	
	/**
	 * 各个特征词在正类中的概率
	 */
	private double[] pInPosi;
	
	/**
	 * 各个特征词在负类中的概率
	 */
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
