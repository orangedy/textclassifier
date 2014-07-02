package com.dy.textclassifier.processors;

import java.util.List;

import com.dy.textclassifier.common.bean.Document;

public class ProcessorChain {

	private List<IProcessor> processors;
	
	public void process(List<Document> documents){
		for(IProcessor processor : processors) {
			processor.process(documents);
		}
	}
}
