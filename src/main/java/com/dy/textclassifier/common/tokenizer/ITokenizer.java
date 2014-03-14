package com.dy.textclassifier.common.tokenizer;

/**
 * 分词类接口
 * @author orangedy
 *
 */
public interface ITokenizer {

	/**
	 * @param sinput
	 * @param encoding
	 * @return
	 */
	public String[] tokenize(byte[] binput, String encoding);
	
	/**
	 * @param sinput
	 * @return
	 */
	public String[] tokenize(String sinput);
	
}
