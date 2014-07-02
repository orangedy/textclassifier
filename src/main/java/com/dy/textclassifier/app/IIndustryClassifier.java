package com.dy.textclassifier.app;

public interface IIndustryClassifier {
	/**
	 * @param document 输入待分类的文档
	 * @return 返回类别编号
	 */
	public int classify(String document);
}
