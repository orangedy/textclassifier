package com.dy.textclassifier.classifier;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import com.dy.textclassifier.common.bean.Document;

public class SVMClassifier extends AbstractClassifier{
	private static Logger log = LogManager.getLogger(SVMClassifier.class);
	
	private String outputPath = "";
	
	private svm_model model;
	
	public void init() {
		
	}

	public void train(List<Document> documents) {
		svm_parameter param = getParam();
		svm_problem problem = getSvmProblem(documents);
		this.outputPath = System.getProperty("user.dir") + File.separator + this.outputPath;
		doTrain(problem, param, this.outputPath, true);
	}
	
	public void eval(List<Document> documents) {
		for(Document document : documents){
			double[] value = document.getVector();
			svm_node[] node = new svm_node[value.length];
			for(int i = 0; i < value.length; i++){
				node[i].index = i;
				node[i].value = value[i];
			}
			double result = svm.svm_predict(this.model, node);
			document.setCategory((int)result);
		}
	}


	public void initModel(String filePath){
		String path = filePath;
//		path = System.getProperty("user.dir") + File.separator + path;
		try {
			this.model = svm.svm_load_model(path);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("初始化model信息失败，请检查result/model.txt文件是否正确");
		}
	}
	
	protected svm_parameter getParam() {
		svm_parameter param = new svm_parameter();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 0.001;	// 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 0;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		return param;
	}
	
	protected svm_problem getSvmProblem(List<Document> documents) {
		int l = documents.size();
		double[] y = new double[l];

		svm_node[][] x = new svm_node[l][];
		for (int i = 0; i < l; i++) {
			y[i] = documents.get(i).getCategory();
			for(int j = 0; j < l; j++){
				svm_node node = new svm_node();
				node.index = j;
				node.value = documents.get(i).getVector()[j];
				x[i][j] = node;
			}
		}
		svm_problem problem = new svm_problem();
		problem.l = l;
		problem.y = y;
		problem.x = x;
		return problem;
	}
	
	protected void doTrain(svm_problem problem, svm_parameter param,String modelFilePath, boolean one) {
		log.debug("start check parameter");
		String result = svm.svm_check_parameter(problem, param);

		if (result == null) {
			log.debug("start param Optimization");
//			paramOptimization(problem, param, one);
			log.debug("start training and gain model");
			svm_model model = svm.svm_train(problem, param);
			try {
				log.info("start save model#" + modelFilePath);
				svm.svm_save_model(modelFilePath, model);
				log.debug("finish");
			} catch (IOException e) {
				throw new RuntimeException("SVM存储训练结果错误", e);
			}
		} else {
			log.debug("check parameter error : " + result);
		}
	}
}
