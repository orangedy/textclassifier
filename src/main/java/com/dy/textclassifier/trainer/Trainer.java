package com.dy.textclassifier.trainer;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dy.textclassifier.classifier.AbstractClassifier;
import com.dy.textclassifier.common.bean.Document;
import com.dy.textclassifier.common.datasource.DataSource;
import com.dy.textclassifier.processors.IProcessor;

public class Trainer {

	private static Logger log = LogManager.getLogger(Trainer.class);

	private DataSource dataInput;

	private String outputFile;

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

	public void train() {
		init();
		documents = dataInput.getDocumentsForTrain();
		if (documents.size() > 0) {
			for (IProcessor processor : processors) {
				processor.process(documents);
			}
		}
		classifier.train(documents);
	}
	
	public void getVector(){
		for (IProcessor processor : processors) {
			processor.process(documents);
		}
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("ApplicationContext.xml");
		Trainer trainer = (Trainer) ctx.getBean("trainer");
		trainer.train();
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

	public AbstractClassifier getClassifier() {
		return classifier;
	}

	public void setClassifier(AbstractClassifier classifier) {
		this.classifier = classifier;
	}
}
