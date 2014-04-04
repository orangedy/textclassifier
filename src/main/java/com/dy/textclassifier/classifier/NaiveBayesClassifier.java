package com.dy.textclassifier.classifier;

import java.util.HashMap;
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
	
	private StatisticBean statistic ;
	
	private NaiveBayesModel model;
	
	@Override
	public void train(List<Document> documents) {
		int featureNum = statistic.getFeatureWords().size();
		double posiNum = statistic.getPosiNum();
		double negaNum = statistic.getNegaNum();
		double N = statistic.getDocNum();
		Map<String, TermCateTuple> termCate = statistic.getTermCateInfo();
		model = new NaiveBayesModel();
		double[] pCate = new double[2];
		pCate[0] = posiNum/N;
		pCate[1] = negaNum/N;
		model.setpCate(pCate);
		model.setpInNega(new double[featureNum]);
		model.setpInPosi(new double[featureNum]);
		for(int i = 0; i < featureNum; i++) {
			String feature = statistic.getFeatureWords().get(i);
			double pxc0 = termCate.get(feature).getPosi()/posiNum;
			double pxc1 = termCate.get(feature).getNega()/negaNum;
			model.getpInPosi()[i] = pxc0;
			model.getpInNega()[i] = pxc1;
		}
		saveModel();
	}

	private void saveModel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eval(List<Document> documents) {
		NaiveBayesModel model = initModel();
		int featureNum = model.getpInNega().length;
		for(Document document : documents) {
			double p4posi = 0;
			double p4nega = 0;
			for(int i = 0; i < featureNum; i++){
				if(document.getVector()[i] == 0){
					p4posi *=(1 - model.getpInPosi()[i]);
					p4nega *=(1 - model.getpInNega()[i]);
				}else{
					p4posi *= model.getpInPosi()[i];
					p4nega *= model.getpInNega()[i];
				}
			}
			if(p4posi > p4nega){
				document.setEvalCategory(1);
			}else{
				document.setEvalCategory(-1);
			}
		}
	}

	private NaiveBayesModel initModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
