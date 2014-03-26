package com.dy.textclassifier.common.bean;

public class TermCateTuple {
	/**
	 * 正类中包含该词的文本数量
	 */
	private int posi = 0;
	/**
	 * 负类中包含该词的文本数量
	 */
	private int nega = 0;

	public int getPosi() {
		return posi;
	}

	public void setPosi(int posi) {
		this.posi = posi;
	}

	public int getNega() {
		return nega;
	}

	public void setNega(int nega) {
		this.nega = nega;
	}
}