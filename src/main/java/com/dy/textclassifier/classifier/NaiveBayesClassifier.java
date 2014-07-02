package com.dy.textclassifier.classifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dy.textclassifier.common.bean.Document;
import com.dy.textclassifier.common.bean.NaiveBayesModel;
import com.dy.textclassifier.common.bean.StatisticBean;
import com.dy.textclassifier.common.bean.TermCateTuple;

public class NaiveBayesClassifier extends AbstractClassifier {

	private static Logger log = LogManager.getLogger(NaiveBayesClassifier.class);

	private StatisticBean statistic;

	private NaiveBayesModel model;

	private String outputPath = "results/naive_model.txt";

	@Override
	public void train(List<Document> documents) {
		int featureNum = statistic.getFeatureWords().size();
		double posiNum = statistic.getPosiNum();
		double negaNum = statistic.getNegaNum();
		double N = statistic.getDocNum();
		Map<String, TermCateTuple> termCate = statistic.getTermCateInfo();
		model = new NaiveBayesModel();
		double[] pCate = new double[2];
		pCate[0] = posiNum / N;
		pCate[1] = negaNum / N;
		model.setpCate(pCate);
		model.setpInNega(new double[featureNum]);
		model.setpInPosi(new double[featureNum]);
		for (int i = 0; i < featureNum; i++) {
			String feature = statistic.getFeatureWords().get(i);
			double pxc0 = (termCate.get(feature).getPosi() + 1) / (posiNum + 2);
			double pxc1 = (termCate.get(feature).getNega() + 1) / (negaNum + 2);
			model.getpInPosi()[i] = pxc0;
			model.getpInNega()[i] = pxc1;
		}
		saveModel(model);
	}

	private void saveModel(NaiveBayesModel model) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(outputPath)));
			oos.writeObject(model);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadModel() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(outputPath)));
			model = (NaiveBayesModel) ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException("can't load model");
		}
	}

	@Override
	public void eval(List<Document> documents) {
		loadModel();
		int featureNum = model.getpInNega().length;
		for (Document document : documents) {
			double p4posi = model.getpCate()[0];
			double p4nega = model.getpCate()[1];
			for (int i = 0; i < featureNum; i++) {
				if (document.getVector()[i] == 0) {
					p4posi *= (1 - model.getpInPosi()[i]);
					p4nega *= (1 - model.getpInNega()[i]);
				} else {
					p4posi *= model.getpInPosi()[i];
					p4nega *= model.getpInNega()[i];
				}
			}
			if (p4posi > p4nega) {
				document.setEvalCategory(1);
			} else {
				document.setEvalCategory(-1);
			}
		}
	}

	public StatisticBean getStatistic() {
		return statistic;
	}

	public void setStatistic(StatisticBean statistic) {
		this.statistic = statistic;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

}
