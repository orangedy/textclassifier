package com.dy.textclassifier.app;

import java.util.List;

public interface IRiskMultiClassifier {
	/**
	 * @param document 待进行风险打标的文档
	 * @return 返回一个int型列表，每个int值代表一种风险类型
	 */
	public List<Integer> calRiskType(String document);
}
