package com.dy.textclassifier.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.dy.textclassifier.common.bean.Document;
import com.dy.textclassifier.common.bean.StatisticBean;

public class VectorProcessor implements IProcessor {

	private StatisticBean statistic;

	public StatisticBean getStatistic() {
		return statistic;
	}

	public void setStatistic(StatisticBean statistic) {
		this.statistic = statistic;
	}

	public void process(List<Document> documents) {
		int featureNum = statistic.getFeatureWords().size();
		HashMap<String, Integer> featureIndex = new HashMap<String, Integer>(
				Math.max((int) (featureNum / .75f) + 1, 16));
		double[] IDFs = new double[featureNum];
		List<String> featureWords = statistic.getFeatureWords();
		shuffle(featureWords);
		shuffle(featureWords);
		shuffle(featureWords);

		for (int i = statistic.getFeatureWords().size() - 1; i > 0; i--) {
			featureIndex.put(featureWords.get(i), i);
			IDFs[i] = calIDF(featureWords.get(i));
		}
		for (Document document : documents) {
			double documentLen = document.getFeatures().size();
			double[] vector = new double[featureNum];
			for (Map.Entry<String, Integer> entry : document.getFeatures().entrySet()) {
				if (featureIndex.containsKey(entry.getKey())) {
					int index = featureIndex.get(entry.getKey());
					double DFIDF = entry.getValue() / documentLen * IDFs[index];
					vector[index] = DFIDF;
				}
			}
			document.setVector(vector);
		}
	}

	public double calIDF(String word) {
		double sum = statistic.getDocNum();
		double ID = statistic.getTermInfo().get(word).getPosi() + statistic.getTermInfo().get(word).getNega();
		return ID / sum;
	}

	public void shuffle(List<String> list) {
		Random random = new Random();
		for (int i = list.size() - 1; i >= 0; i--) {
			int tempPos = random.nextInt(i + 1);
			String temp = list.get(tempPos);
			list.set(tempPos, list.get(i));
			list.set(i, temp);
		}
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

}
