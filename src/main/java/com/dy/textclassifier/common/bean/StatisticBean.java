package com.dy.textclassifier.common.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StatisticBean {
	/** 总训练文档数	 */
	private int docNum;
	
	/** 正类训练文档数	 */
	private int posiNum = 0;
	
	/** 父类训练文档数	 */
	private int negaNum = 0;
	
	/** 词的类别分别统计，形式为“词  正类出现文档数 负类出现文档数”	 */
	private Map<String, Tuple> termInfo = new HashMap<String, Tuple>();
	
	private List<String> featureWords;
	
	/** 选择出的特征词信息，形式为“词 特征序号 权重”	 */
	private Map<String, TermTuple> featureInfo;

	public int getDocNum() {
		return docNum;
	}

	public void setDocNum(int docNum) {
		this.docNum = docNum;
	}

	public int getPosiNum() {
		return posiNum;
	}

	public void setPosiNum(int posiNum) {
		this.posiNum = posiNum;
	}

	public int getNegaNum() {
		return negaNum;
	}

	public void setNegaNum(int negaNum) {
		this.negaNum = negaNum;
	}

	public Map<String, Tuple> getTermInfo() {
		return termInfo;
	}

	public void setTermInfo(Map<String, Tuple> termInfo) {
		this.termInfo = termInfo;
	}

	public List<String> getFeatureWords() {
		return featureWords;
	}

	public void setFeatureWords(List<String> featureWords) {
		this.featureWords = featureWords;
	}

}