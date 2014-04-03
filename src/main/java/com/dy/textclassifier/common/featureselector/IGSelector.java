package com.dy.textclassifier.common.featureselector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dy.textclassifier.common.bean.StatisticBean;
import com.dy.textclassifier.common.bean.TermCateTuple;

public class IGSelector implements ITermSelector {

	private static Logger log = LogManager.getLogger(IGSelector.class);
	
	private Map<String, Double> IGValues = new HashMap<String, Double>();
	
	public List<String> selectTerms(StatisticBean statistic, int featureNum) {
		double a, b, c, d, IGValue;
		double N = statistic.getDocNum();
		double posi = statistic.getPosiNum();
		double nega = statistic.getNegaNum();
		double log2 = Math.log(2);
		double hc = posi/N*Math.log(N/posi)/log2 + nega/N*Math.log(N/nega)/log2;
		for (Map.Entry<String, TermCateTuple> entry : statistic.getTermCateInfo().entrySet()) {
			a = entry.getValue().getPosi();
			b = entry.getValue().getNega();
			c = posi - a;
			d = nega - b;
			double hct = calHCT(a, b, N) + calHCT(c, d, N);
			IGValue = hc - hct;
			IGValues.put(entry.getKey(), IGValue);
		}
		return selectTopN(IGValues, featureNum);
	}
	
	private double calHCT(double a, double b, double N){
		double hct = (a + b)/N * (a/(a + b)*Math.log((a + b)/a)/Math.log(2) + b/(a + b)*Math.log((a + b)/b)/Math.log(2));
		return hct;
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
			results.add(tempList.get(i).getKey());
		}
		return results;
	}
}
