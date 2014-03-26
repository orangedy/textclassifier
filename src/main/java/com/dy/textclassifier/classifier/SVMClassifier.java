package com.dy.textclassifier.classifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	
	private String outputPath = "results/svm_model.txt";
	
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
		initModel(outputPath);
		for(Document document : documents){
			double[] value = document.getVector();
			svm_node[] nodes = new svm_node[value.length];
			for(int i = 0; i < value.length; i++){
				svm_node node = new svm_node();
				node.index = i + 1;
				node.value = value[i];
				nodes[i] = node;
			}
			double result = svm.svm_predict(this.model, nodes);
			document.setEvalCategory(result);
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
		int docNum = documents.size();
		double[] y = new double[docNum];
		svm_node[][] x = new svm_node[docNum][];
		for (int i = 0; i < docNum; i++) {
			Document document = documents.get(i);
			int featureNum = document.getVector().length;
			y[i] = document.getCategory();
			List<svm_node> nodes = new ArrayList<svm_node>();
			for(int j = 0; j < featureNum; j++){
				if(document.getVector()[j] != 0){
					svm_node node = new svm_node();
					node.index = j + 1;
					node.value = document.getVector()[j];
					nodes.add(node);
				}
			}
			x[i] = nodes.toArray(new svm_node[0]);
			
		}
		svm_problem problem = new svm_problem();
		problem.l = docNum;
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
