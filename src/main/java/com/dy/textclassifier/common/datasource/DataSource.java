package com.dy.textclassifier.common.datasource;

import java.util.List;

import com.dy.textclassifier.common.bean.Document;

public interface DataSource {

	public boolean init();

	public List<Document> getDocumentsForTrain();
	
	public List<Document> getDocumentsForEval();
	
	public List<Document> getDocumentsForTest();
}
