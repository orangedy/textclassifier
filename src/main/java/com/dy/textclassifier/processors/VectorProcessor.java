package com.dy.textclassifier.processors;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dy.textclassifier.common.bean.Document;
import com.dy.textclassifier.common.bean.StatisticBean;
import com.dy.textclassifier.common.bean.TermTuple;

public class VectorProcessor implements IProcessor {
	private static Logger log = LogManager.getLogger(VectorProcessor.class);

	private StatisticBean statistic;

	public StatisticBean getStatistic() {
		return statistic;
	}

	public void setStatistic(StatisticBean statistic) {
		this.statistic = statistic;
	}

	public void init() {

	}

	public void process(List<Document> documents) {
		log.info("start to build vector");
		int featureNum = statistic.getFeatureWords().size();
		Map<String, TermTuple> featureInfo = statistic.getFeatureInfo();
		for (Document document : documents) {
			double documentLen = document.getTermNum();
			double[] vector = new double[featureNum];
			for (Map.Entry<String, Integer> entry : document.getTermFrequency().entrySet()) {
				if (featureInfo.containsKey(entry.getKey())) {
					int index = featureInfo.get(entry.getKey()).getIndex();
					double DFIDF = entry.getValue() / documentLen * featureInfo.get(entry.getKey()).getIDFWeight();
					vector[index] = DFIDF;
				}
			}
			document.setVector(vector);
			document.setTermFrequency(null);
		}
	}

}
