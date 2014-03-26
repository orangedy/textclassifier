package com.dy.textclassifier.processors;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dy.textclassifier.common.bean.Document;
import com.dy.textclassifier.common.tokenizer.ITokenizer;

public class TokenizerProcessor implements IProcessor {

	private Logger log = LogManager.getLogger(TokenizerProcessor.class);

	private ITokenizer tokenizer;

	public void process(List<Document> documents) {
		for (Document document : documents) {
			String[] terms = tokenizer.tokenize(document.getContent());
			document.setContent(null);
			document.setTerms(terms);
			// for debug
//			log.debug(document.getContent());
			StringBuilder message = new StringBuilder();
			for(String term : terms){
				message.append(term + " ");
			}
			log.debug(message.toString());
		}
	}

	public ITokenizer getTokenizer() {
		return tokenizer;
	}

	public void setTokenizer(ITokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	public void init() {
		
	}

}
