package com.dy.textclassifier.classifier;

import java.util.List;

import com.dy.textclassifier.common.bean.Document;

public abstract class AbstractClassifier {

	abstract public void train(List<Document> documents);
	
	abstract public void eval(List<Document> documents);
}
