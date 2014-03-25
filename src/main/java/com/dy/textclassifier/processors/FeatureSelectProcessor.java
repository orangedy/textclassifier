package com.dy.textclassifier.processors;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.dy.textclassifier.common.bean.Document;
import com.dy.textclassifier.common.bean.StatisticBean;
import com.dy.textclassifier.common.bean.Tuple;
import com.dy.textclassifier.common.featureselector.ITermSelector;
import com.dy.textclassifier.common.utils.FileUtil;

public class FeatureSelectProcessor implements IProcessor {

	private ITermSelector termSelector;

	private int featureNum = 2000;

	private StatisticBean statistic;

	public void init() {

	}

	public void process(List<Document> documents) {
		statistic.setDocNum(documents.size());
		for (Document document : documents) {
			if (document.getCategory() == 1) {
				statistic.setPosiNum(statistic.getPosiNum() + 1);
			} else {
				statistic.setNegaNum(statistic.getNegaNum() + 1);
			}
			for (String word : document.getFeatures().keySet()) {
				Tuple bean;
				if (!statistic.getTermInfo().containsKey(word)) {
					bean = new Tuple();
					statistic.getTermInfo().put(word, bean);
				} else {
					bean = statistic.getTermInfo().get(word);
				}
				if (document.getCategory() == 1) {
					bean.setPosi(bean.getPosi() + 1);
				} else {
					bean.setNega(bean.getNega() + 1);
				}
			}
		}
		List<String> featureWords = termSelector.selectTerms(statistic, featureNum);
		statistic.setFeatureWords(featureWords);
		saveResult();
	}

	public void saveResult() {
		File featureInfo = new File("statistic.txt");
		try {
			FileUtil.writeStringToFile(featureInfo, "word    posiNum    negaNum\n", true, "UTF-8");
			for (Map.Entry<String, Tuple> entry : statistic.getTermInfo().entrySet()) {
				String content = entry.getKey() + "		" + entry.getValue().getPosi() + "		" + entry.getValue().getNega()
						+ "\n";
				FileUtil.writeStringToFile(featureInfo, content, true, "UTF-8");
			}
			for (String feature : statistic.getFeatureWords()) {
				FileUtil.writeStringToFile(featureInfo, feature + "\n", true, "UTF-8");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ITermSelector getTermSelector() {
		return termSelector;
	}

	public void setTermSelector(ITermSelector termSelector) {
		this.termSelector = termSelector;
	}

	public StatisticBean getStatistic() {
		return statistic;
	}

	public void setStatistic(StatisticBean statistic) {
		this.statistic = statistic;
	}
}
