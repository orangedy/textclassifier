package com.dy.textclassifier.common.featureselector;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dy.textclassifier.common.bean.StatisticBean;

public interface ITermSelector {

	public List<String> selectTerms(StatisticBean statistic, int featureNum);
	
}
