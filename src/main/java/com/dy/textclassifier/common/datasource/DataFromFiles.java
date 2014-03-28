package com.dy.textclassifier.common.datasource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dy.textclassifier.common.bean.Document;
import com.dy.textclassifier.common.utils.FileUtil;

public class DataFromFiles implements DataSource {

	private String encoding = "GBK";

	private String trainPath;

	private String evalPath;

	private String testPath;

	public boolean init() {
		return true;
	}

	public Document getDocument(File file) {
		Document document = null;
		try {
			String content = FileUtil.readFile(file, encoding);
			document = new Document(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	public List<Document> getDocumentsForTrain() {
		String positivePath = this.trainPath + File.separator + "positive";
		String negativePath = this.trainPath + File.separator + "negative";
		List<Document> documents = new ArrayList<Document>();
		File directory = new File(positivePath);
		if (directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				Document document = getDocument(file);
				document.setCategory(1);
				documents.add(document);
			}
		}
		directory = new File(negativePath);
		if (directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				Document document = getDocument(file);
				document.setCategory(-1);
				documents.add(document);
			}
		}
		return documents;
	}

	public List<Document> getDocumentsForEval() {
		List<Document> documents = new ArrayList<Document>();
		File directory = new File(evalPath);
		if (directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				Document document = getDocument(file);
				documents.add(document);
			}
		}
		return documents;
	}

	public List<Document> getDocumentsForTest() {
		String positivePath = this.testPath + File.separator + "positive";
		String negativePath = this.testPath + File.separator + "negative";
		List<Document> documents = new ArrayList<Document>();
		File directory = new File(positivePath);
		if (directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				Document document = getDocument(file);
				document.setCategory(1);
				documents.add(document);
			}
		}
		directory = new File(negativePath);
		if (directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				Document document = getDocument(file);
				document.setCategory(0);
				documents.add(document);
			}
		}
		return documents;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getTrainPath() {
		return trainPath;
	}

	public void setTrainPath(String trainPath) {
		this.trainPath = trainPath;
	}

	public String getEvalPath() {
		return evalPath;
	}

	public void setEvalPath(String evalPath) {
		this.evalPath = evalPath;
	}

	public String getTestPath() {
		return testPath;
	}

	public void setTestPath(String testPath) {
		this.testPath = testPath;
	}

	// public static void main(String[] args) {
	// DataFromFiles test = new DataFromFiles();
	// String path = "E:\\worktemp\\train";
	// test.setPositivePath(path + "\\neagtive");
	// test.setNegativePath(path + "\\positive");
	// if (test.init()) {
	// List<Document> documents = test.getDocuments();
	// for (Document document : documents) {
	// System.out.println(document.getCategory() + " " + document.getContent());
	// }
	// }
	// }
}
