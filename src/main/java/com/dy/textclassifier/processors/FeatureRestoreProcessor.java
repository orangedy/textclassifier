package com.dy.textclassifier.processors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dy.textclassifier.common.bean.Document;
import com.dy.textclassifier.common.bean.StatisticBean;
import com.dy.textclassifier.common.bean.TermTuple;
import com.dy.textclassifier.common.utils.FileUtil;

public class FeatureRestoreProcessor implements IProcessor {
	
	private String featureInfoPath = "results/featureInfo.txt";
	
	private StatisticBean statistic;

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public void process(List<Document> documents) {
		File file = new File(featureInfoPath);
		try {
			List<String> lines = FileUtil.readFileByLine(file, "UTF-8");
			int lineNum = lines.size();
			List<String> featureWords = new ArrayList<String>(lineNum);
			Map<String, TermTuple> featureInfo = new HashMap<String, TermTuple>(Math.max((int) (lineNum / .75f) + 1, 16));
			for(String line : lines) {
				String[] infos = line.split("	");
				//for test
				for(String str : infos){
					System.out.print(str + " ");
				}
				int index = Integer.valueOf(infos[0]);
				featureWords.add(index, infos[1]);
				TermTuple tuple = new TermTuple();
				tuple.setIndex(index);
				tuple.setIDFWeight(Double.valueOf(infos[2]));
				featureInfo.put(infos[1], tuple);
			}
			statistic.setFeatureWords(featureWords);
			statistic.setFeatureInfo(featureInfo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public StatisticBean getStatistic() {
		return statistic;
	}

	public void setStatistic(StatisticBean statistic) {
		this.statistic = statistic;
	}

}
