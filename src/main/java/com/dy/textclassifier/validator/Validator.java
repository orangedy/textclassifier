package com.dy.textclassifier.validator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dy.textclassifier.classifier.AbstractClassifier;
import com.dy.textclassifier.common.bean.Document;
import com.dy.textclassifier.common.datasource.DataSource;
import com.dy.textclassifier.common.utils.FileUtil;
import com.dy.textclassifier.processors.IProcessor;
import com.dy.textclassifier.trainer.Trainer;

public class Validator {

	private static Logger log = LogManager.getLogger(Trainer.class);

	private DataSource dataInput;

	private String outputFile = "results/result.txt";

	private List<Document> documents;

	private List<IProcessor> processors;

	private AbstractClassifier classifier;

	public void init() {
		if (dataInput == null || dataInput.init() == false) {
			log.error("input data can't be emply");
		}
		for (IProcessor processor : processors) {
			processor.init();
		}
	}

	public void eval() {
		init();
		documents = dataInput.getDocumentsForTest();
		if (documents.size() > 0) {
			for (IProcessor processor : processors) {
				processor.process(documents);
			}
		}
		classifier.eval(documents);
		checkResult();
	}

	public void checkResult() {
		int positiveNum = 0;
		int negativeNum = 0;
		int positiveRight = 0;
		int positiveWrong = 0;
		int negativeRight = 0;
		int negativeWrong = 0;
		for (Document document : documents) {
			if (document.getCategory() == 1) {
				positiveNum++;
				if (document.getEvalCategory() == 1) {
					positiveRight++;
				} else {
					positiveWrong++;
				}
			} else {
				negativeNum++;
				if (document.getEvalCategory() == 1) {
					negativeWrong++;
				} else {
					negativeRight++;
				}
			}
		}

		File file = new File(outputFile);
		String content = "positiveNum:" + positiveNum + "\n";
		content += "negativeNum:" + negativeNum + "\n";
		content += positiveRight + "	" + positiveWrong + "\n";
		content += negativeWrong + "	" + negativeRight;
		log.info(content);
		try {
			FileUtil.writeStringToFile(file, content, false, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("ApplicationContext.xml");
		Validator validator = (Validator) ctx.getBean("validator");
		validator.eval();
	}

	public DataSource getDataInput() {
		return dataInput;
	}

	public void setDataInput(DataSource dataInput) {
		this.dataInput = dataInput;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public List<IProcessor> getProcessors() {
		return processors;
	}

	public void setProcessors(List<IProcessor> processors) {
		this.processors = processors;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public AbstractClassifier getClassifier() {
		return classifier;
	}

	public void setClassifier(AbstractClassifier classifier) {
		this.classifier = classifier;
	}
}
