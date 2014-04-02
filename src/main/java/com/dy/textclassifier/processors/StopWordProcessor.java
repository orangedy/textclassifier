package com.dy.textclassifier.processors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dy.textclassifier.common.bean.Document;
import com.dy.textclassifier.common.bean.TermCateTuple;
import com.dy.textclassifier.common.utils.FileUtil;

public class StopWordProcessor implements IProcessor {

	private static Logger log = LogManager.getLogger(StopWordProcessor.class);

	private HashSet<String> stopWords;

	private String stopWordsPath = "stopwords.txt";

	private List<String> stopWordTypes;

	private String stopWordTypesPath = "stopwordtypes.txt";

	private HashSet<String> filterWords;

	private String filterWordsSavePath = "results/filterWords.txt";

	/**
	 * 初始化停用词和停用词类型，从文件中读出，每行一个 文件目录可以不设置，即不进行停用词过滤
	 */
	public void init() {
		if (stopWordsPath != null) {
			try {
				List<String> lStopWords = FileUtil.readFileByLine(new File(stopWordsPath), "UTF-8");
				stopWords = new HashSet<String>(Math.max((int) (lStopWords.size() / .75f) + 1, 16));
				for (String word : lStopWords) {
					stopWords.add(word);
				}
			} catch (IOException e) {
				log.error("init stopword failed");
				e.printStackTrace();
			}
		}
		if (stopWordTypesPath != null) {
			try {
				stopWordTypes = FileUtil.readFileByLine(new File(stopWordTypesPath), "UTF-8");
			} catch (IOException e) {
				log.error("init stopword types failed");
				e.printStackTrace();
			}
		}
		filterWords = new HashSet<String>();
	}

	/*
	 * 去停用词过程，包括停用词表和停用词类型过滤 (non-Javadoc)
	 * 
	 * @see com.dy.textclassifier.processors.IProcessor#process(java.util.List)
	 */
	public void process(List<Document> documents) {
		log.info("start filter stop word");
		for (Document document : documents) {
			HashMap<String, Integer> termFrequency = new HashMap<String, Integer>();
			int sum = 0;
			for (String term : document.getTerms()) {
				if (!isStopWord(term)) {
					if (termFrequency.containsKey(term)) {
						termFrequency.put(term, termFrequency.get(term) + 1);
					} else {
						termFrequency.put(term, 1);
					}
					sum++;
				}
			}
			document.setTerms(null);
			document.setTermFrequency(termFrequency);
			document.setTermNum(sum);
			// for debug
			saveFilterWords();
		}
	}

	/**
	 * 输入参数为 词/词性形式，对词和词性分别进行过滤
	 * 
	 * @param word
	 * @return
	 */
	public boolean isStopWord(String word) {
		String[] terms = word.split("/");
		if (stopWords != null && stopWords.contains(terms[0])) {
			filterWords.add(word);
			return true;
		}
		if (terms.length == 2 && stopWordTypes != null && isFilterType(terms[1])) {
			filterWords.add(word);
			return true;
		} else {
			return false;
		}
	}

	public boolean isFilterType(String type) {
		for (String filterType : stopWordTypes) {
			if (filterType.length() <= type.length() && type.startsWith(filterType)) {
				return true;
			}
		}
		return false;
	}

	private void saveFilterWords() {
		File file = new File(filterWordsSavePath);
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			for (String word : filterWords) {
				fw.write(word + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getFilterWordsSavePath() {
		return filterWordsSavePath;
	}

	public void setFilterWordsSavePath(String filterWordsSavePath) {
		this.filterWordsSavePath = filterWordsSavePath;
	}

	public String getStopWordsPath() {
		return stopWordsPath;
	}

	public void setStopWordsPath(String stopWordsPath) {
		this.stopWordsPath = stopWordsPath;
	}

	public String getStopWordTypesPath() {
		return stopWordTypesPath;
	}

	public void setStopWordTypesPath(String stopWordTypesPath) {
		this.stopWordTypesPath = stopWordTypesPath;
	}
}
