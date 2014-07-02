package com.dy.textclassifier.app;

public interface IRelevanceFilter {

	/**
	 * @param document 待判定的文档
	 * @return 返回true指有效信息，false指无效信息
	 */
	public boolean isRelevant(String document);
}
