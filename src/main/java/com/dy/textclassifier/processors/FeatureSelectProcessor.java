package com.dy.textclassifier.processors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dy.textclassifier.common.bean.Document;
import com.dy.textclassifier.common.bean.StatisticBean;
import com.dy.textclassifier.common.bean.TermCateTuple;
import com.dy.textclassifier.common.bean.TermTuple;
import com.dy.textclassifier.common.featureselector.ITermSelector;
import com.dy.textclassifier.common.utils.FileUtil;

public class FeatureSelectProcessor implements IProcessor {
	private static Logger log = LogManager.getLogger(FeatureSelectProcessor.class);

	private ITermSelector termSelector;

	private int featureNum = 2000;

	private StatisticBean statistic;

	private String featureSavePath = "results/featureInfo.txt";

	public void init() {

	}

	public void process(List<Document> documents) {
		log.info("start to select feature");
		List<String> featureWords = termSelector.selectTerms(statistic, featureNum);
		shuffle(featureWords);
		shuffle(featureWords);
		shuffle(featureWords);
		Map<String, TermTuple> featureInfo = new HashMap<String, TermTuple>(Math.max((int) (featureNum / .75f) + 1, 16));
		for (int i = 0; i < featureNum; i++) {
			TermTuple tuple = new TermTuple();
			tuple.setIndex(i);
			tuple.setIDFWeight(calIDF(featureWords.get(i)));
			featureInfo.put(featureWords.get(i), tuple);
		}
		statistic.setFeatureInfo(featureInfo);
		statistic.setFeatureWords(featureWords);
		saveFeatureInfo();
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

	public double calIDF(String term) {
		double sum = statistic.getDocNum();
		double ID = statistic.getTermCateInfo().get(term).getPosi() + statistic.getTermCateInfo().get(term).getNega();
		return Math.log(sum / ID) / Math.log(10);
	}

	private void saveFeatureInfo() {
		File file = new File(featureSavePath);
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			int size = statistic.getFeatureWords().size();
			for (int i = 0; i < size; i++) {
				String term = statistic.getFeatureWords().get(i);
				String content = i + "	" + term + "	" + statistic.getFeatureInfo().get(term).getIDFWeight() + "	";
				content = content + "	" + statistic.getTermCateInfo().get(term).getPosi() + "	" + statistic.getTermCateInfo().get(term).getNega() + "\n";
				fw.write(content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void saveResult() {
		File featureInfo = new File("statistic.txt");
		try {
			FileUtil.writeStringToFile(featureInfo, "word    posiNum    negaNum\n", true, "UTF-8");
			for (Map.Entry<String, TermCateTuple> entry : statistic.getTermCateInfo().entrySet()) {
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

	public int getFeatureNum() {
		return featureNum;
	}

	public void setFeatureNum(int featureNum) {
		this.featureNum = featureNum;
	}

	public String getFeatureSavePath() {
		return featureSavePath;
	}

	public void setFeatureSavePath(String featureSavePath) {
		this.featureSavePath = featureSavePath;
	}
}
