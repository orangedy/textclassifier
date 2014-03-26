package com.dy.textclassifier.processors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.dy.textclassifier.common.bean.Document;
import com.dy.textclassifier.common.bean.StatisticBean;
import com.dy.textclassifier.common.bean.TermCateTuple;

public class StatisticProcessor implements IProcessor {

	/**
	 * 统计信息
	 */
	private StatisticBean statistic;

	private String statisticSavePath = "results/statistic.txt";

	public void init() {
	}

	public void process(List<Document> documents) {
		statistic.setDocNum(documents.size());
		for (Document document : documents) {
			if (document.getCategory() == 1) {
				statistic.setPosiNum(statistic.getPosiNum() + 1);
			} else {
				statistic.setNegaNum(statistic.getNegaNum() + 1);
			}
			for (String term : document.getTermFrequency().keySet()) {
				TermCateTuple bean;
				if (!statistic.getTermCateInfo().containsKey(term)) {
					bean = new TermCateTuple();
					statistic.getTermCateInfo().put(term, bean);
				} else {
					bean = statistic.getTermCateInfo().get(term);
				}
				if (document.getCategory() == 1) {
					bean.setPosi(bean.getPosi() + 1);
				} else {
					bean.setNega(bean.getNega() + 1);
				}
			}
		}
		saveTermCateInfo();
	}

	private void saveTermCateInfo() {
		File file = new File(statisticSavePath);
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write("word	posiNum	negaNum\n");
			for (Map.Entry<String, TermCateTuple> entry : statistic.getTermCateInfo().entrySet()) {
				String content = entry.getKey() + "	" + entry.getValue().getPosi() + "	" + entry.getValue().getNega()
						+ "\n";
				fw.write(content);
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

	public StatisticBean getStatistic() {
		return statistic;
	}

	public void setStatistic(StatisticBean statistic) {
		this.statistic = statistic;
	}

	public String getStatisticSavePath() {
		return statisticSavePath;
	}

	public void setStatisticSavePath(String statisticSavePath) {
		this.statisticSavePath = statisticSavePath;
	}

}
