package com.dy.textclassifier.classifier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import com.dy.textclassifier.common.utils.FileUtil;

public class SVMClassifier extends AbstractClassifier {
	private static Logger log = LogManager.getLogger(SVMClassifier.class);

	private String outputPath = "results/svm_model.txt";

	private svm_model model;

	public void init() {

	}

	public void train(List<Document> documents) {
		svm_problem problem = getSvmProblem(documents);
		saveProblem(problem, "results/train_problem.txt");
		svm_parameter param = getParam(documents.get(0).getVector().length);
		this.outputPath = System.getProperty("user.dir") + File.separator + this.outputPath;
		doTrain(problem, param, this.outputPath, true);
	}

	public void eval(List<Document> documents) {
		log.info("start to eval");
		initModel(outputPath);
		svm_problem problem = getSvmProblem(documents);
		saveProblem(problem, "results/test_problem.txt");
		for (Document document : documents) {
			double[] value = document.getVector();
			List<svm_node> nodes = new ArrayList<svm_node>();
			for (int i = 0; i < value.length; i++) {
				if (document.getVector()[i] != 0) {
					svm_node node = new svm_node();
					node.index = i + 1;
					node.value = document.getVector()[i];
					nodes.add(node);
				}
			}
			double result = svm.svm_predict(this.model, nodes.toArray(new svm_node[0]));
			document.setEvalCategory(result);
		}
	}

	public void initModel(String filePath) {
		String path = filePath;
		try {
			this.model = svm.svm_load_model(path);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("初始化model信息失败，请检查result/model.txt文件是否正确");
		}
	}

	protected svm_parameter getParam(double num_features) {
		svm_parameter param = new svm_parameter();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 1.0 / num_features; // 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
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
			for (int j = 0; j < featureNum; j++) {
				if (document.getVector()[j] != 0) {
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

	protected void saveProblem(svm_problem problem, String filePath) {
		File file = new File(filePath);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			int size = problem.l;
			for (int i = 0; i < size; i++) {
				StringBuilder sb = new StringBuilder();
				sb.append(problem.y[i]);
				for (svm_node node : problem.x[i]) {
					sb.append(" " + node.index + ":" + node.value);
				}
				sb.append("\n");
				bw.write(sb.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doTrain(svm_problem problem, svm_parameter param, String modelFilePath, boolean one) {
		log.debug("start check parameter");
		String result = svm.svm_check_parameter(problem, param);

		if (result == null) {
			log.debug("start param Optimization");
			paramOptimization(problem, param);
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

	private double do_cross_validation(svm_problem prob, svm_parameter param, int nr_fold) {
		int i;
		int total_correct = 0;
		double total_error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
		double[] target = new double[prob.l];
		svm.svm_cross_validation(prob, param, nr_fold, target);
		if (param.svm_type == svm_parameter.EPSILON_SVR || param.svm_type == svm_parameter.NU_SVR) {
			for (i = 0; i < prob.l; i++) {
				double y = prob.y[i];
				double v = target[i];
				total_error += (v - y) * (v - y);
				sumv += v;
				sumy += y;
				sumvv += v * v;
				sumyy += y * y;
				sumvy += v * y;
			}
			System.out.print("Cross Validation Mean squared error = " + total_error / prob.l + "\n");
			System.out.print("Cross Validation Squared correlation coefficient = "
					+ ((prob.l * sumvy - sumv * sumy) * (prob.l * sumvy - sumv * sumy))
					/ ((prob.l * sumvv - sumv * sumv) * (prob.l * sumyy - sumy * sumy)) + "\n");
		} else {
			for (i = 0; i < prob.l; i++)
				if (target[i] == prob.y[i])
					++total_correct;
			System.out.print("Cross Validation Accuracy = " + 100.0 * total_correct / prob.l + "%\n");
		}
		return 100.0 * total_correct / prob.l;
	}

	/**
	 * 参数优选
	 * 
	 * @param problem
	 * @param param
	 */
	protected void paramOptimization(svm_problem problem, svm_parameter param) {
		// 最终值
		double c_last = 0;
		double g_last = 0;
		double maxAccuracy = Double.MIN_VALUE;

		// start param optimization
		for (int i = -5; i <= 15; i += 2) {
			double c = Math.pow(2, i);

			for (int gi = 3; gi >= -15; gi -= 2) {
				// param g
				double g = Math.pow(2, gi);
				log.debug("c : " + c + "  g : " + g);

				// set param.c and param.g
				param.C = c;
				param.gamma = g;
				// start cross validation
				double accuracy = do_cross_validation(problem, param, 10);
				log.debug("accuracy: " + accuracy + "%");
				// update param_c param_g and errorNum
				if (accuracy > maxAccuracy) {
					c_last = c;
					g_last = g;
					maxAccuracy = accuracy;
				} else if (accuracy == maxAccuracy && g_last == g & c_last > c) {
					c_last = c;
				}
			}
		}
		log.debug("c last : " + c_last + "g last : " + g_last + "error : " + maxAccuracy);
		param.C = c_last;
		param.gamma = g_last;
	}

	// for test
	public void testTrain() {
		svm_problem problem = getTestProblem();
		svm_parameter param = getParam(119);
		String error_msg = svm.svm_check_parameter(problem, param);

		if (error_msg != null) {
			System.err.print("ERROR: " + error_msg + "\n");
			System.exit(1);
		}
		doTrain(problem, param, "test/svm_model.txt", true);
	}

	private svm_problem getTestProblem() {
		svm_problem problem = new svm_problem();
		File file = new File("test//train.txt");
		try {
			List<String> content = FileUtil.readFileByLine(file, "UTF-8");
			int docNum = content.size();
			problem.l = docNum;
			problem.y = new double[docNum];
			problem.x = new svm_node[docNum][];
			for (int i = 0; i < docNum; i++) {
				String[] array = content.get(i).split(" ");
				problem.y[i] = Double.valueOf(array[0]);
				problem.x[i] = new svm_node[array.length - 1];
				for (int j = 1; j < array.length; j++) {
					problem.x[i][j - 1] = new svm_node();
					problem.x[i][j - 1].index = Integer.valueOf(array[j].split(":")[0]);
					problem.x[i][j - 1].value = Double.valueOf(array[j].split(":")[1]);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return problem;
	}

	public void testEval() {
		initModel("test//svm_model.txt");
		File file = new File("test//test.txt");
		int num11 = 0, num10 = 0, num01 = 0, num00 = 0;
		try {
			List<String> content = FileUtil.readFileByLine(file, "UTF-8");
			int docNum = content.size();
			for (int i = 0; i < docNum; i++) {
				String line = content.get(i);
				String[] array = line.split(" ");
				double label = Double.valueOf(array[0]);
				svm_node[] nodes = new svm_node[array.length - 1];
				for (int j = 1; j < array.length; j++) {
					nodes[j - 1] = new svm_node();
					nodes[j - 1].index = Integer.valueOf(array[j].split(":")[0]);
					nodes[j - 1].value = Double.valueOf(array[j].split(":")[1]);
				}
				double result = svm.svm_predict(this.model, nodes);
				if (label == 1 && result == 1) {
					num11++;
				} else if (label == 1 && result == -1) {
					num10++;
				} else if (label == -1 && result == 1) {
					num01++;
				} else {
					num00++;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(num11 + " " + num10 + "\n");
		System.out.println(num01 + " " + num00 + "\n");
	}

	public static void main(String[] args) {
		SVMClassifier classifier = new SVMClassifier();
		// classifier.testTrain();
		classifier.testEval();
	}
}
