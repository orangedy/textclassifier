package com.dy.textclassifier.processors;

import java.util.List;

import com.dy.textclassifier.common.bean.Document;

public interface IProcessor {
	
	public void init();

	public void process(List<Document> documents);
}
