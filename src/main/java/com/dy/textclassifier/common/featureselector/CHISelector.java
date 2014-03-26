package com.dy.textclassifier.common.featureselector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dy.textclassifier.common.bean.StatisticBean;
import com.dy.textclassifier.common.bean.TermCateTuple;

public class CHISelector implements ITermSelector {

	private Logger log = LogManager.getLogger(CHISelector.class);

	/**
	 * 最小的开方检验的值，当开方检验的值为0.455时，相关性的置信度是50%
	 */
	private double minCHIValue = 0.0;

	public double getMinCHIValue() {
		return minCHIValue;
	}

	public void setMinCHIValue(double minCHIValue) {
		this.minCHIValue = minCHIValue;
	}

	public List<String> selectTopN(Map<String, Double> termMap, int N) {
		List<String> results = new ArrayList<String>();
		ArrayList<Map.Entry<String, Double>> tempList = new ArrayList<Map.Entry<String, Double>>(termMap.entrySet());
		Collections.sort(tempList, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				if (o1.getValue() < o2.getValue()) {
					return 1;
				} else if (o1.getValue().equals(o2.getValue())) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		for (int i = 0; i < N && i < tempList.size(); i++) {
			if (tempList.get(i).getValue() > this.minCHIValue) {
				results.add(tempList.get(i).getKey());
			} else {
				break;
			}
		}
		return results;
	}

	public double calculateCHI(int a, int b, int c, int d) {
		double CHIValue = 0;
		double numerator = (double) (a + b + c + d) * (a * d - b * c) * (a * d - b * c);
		double denominator = (double) (a + c) * (a + b) * (b + d) * (c + d);
		CHIValue = numerator / denominator;
		return CHIValue;
	}

	private Map<String, Double> CHIValues = new HashMap<String, Double>();

	public List<String> selectTerms(StatisticBean statistic, int featureNum) {
		double a, b, c, d, CHIValue;
		double N = statistic.getDocNum();
		double posi = statistic.getPosiNum();
		double nega = statistic.getNegaNum();
		for (Map.Entry<String, TermCateTuple> entry : statistic.getTermCateInfo().entrySet()) {
			a = entry.getValue().getPosi();
			b = entry.getValue().getNega();
			c = posi - a;
			d = nega - b;
			double numerator = (double) N * (a * d - b * c) * (a * d - b * c);
			double denominator = (double) (a + c) * (a + b) * (b + d) * (c + d);
			CHIValue = numerator / denominator;
			CHIValues.put(entry.getKey(), CHIValue);
		}
		return selectTopN(CHIValues, featureNum);
	}

}
