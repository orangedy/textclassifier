package com.dy.textclassifier.common.tokenizer;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class ICTCLASTokenizer implements ITokenizer {

	private Logger log = LogManager.getLogger(ICTCLASTokenizer.class);
	
	private static String dllPath = "NLPIR";

	private static String dataPath = "";

	private int bPOSTagged = 1;

	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		CLibrary Instance = (CLibrary) Native.loadLibrary(dllPath,
				CLibrary.class);

		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);

		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);

		public int NLPIR_AddUserWord(String sWord);// add by qp 2008.11.10

		public int NLPIR_DelUsrWord(String sWord);// add by qp 2008.11.10
		
		public int NLPIR_ImportUserDict(String sFileName);

		public void NLPIR_Exit();
	}
	
	public ICTCLASTokenizer() {
		super();
		CLibrary.Instance.NLPIR_Init(dataPath, 1, "0");
	}

	public int addUserWord(String sWord) {
		return CLibrary.Instance.NLPIR_AddUserWord(sWord);
	}

	public int delUserWord(String sWord) {
		return CLibrary.Instance.NLPIR_DelUsrWord(sWord);
	}

	public String getKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut) {
		return CLibrary.Instance.NLPIR_GetKeyWords(sLine, nMaxKeyLimit,
				bWeightOut);
	}

	public String[] tokenize(String sinput) {
		if (sinput != null && sinput.length() != 0) {
			return ictclasTokenizer(sinput);
		} else {
			return new String[0];
		}
	}
	
	public String[] tokenize(byte[] binput, String encoding) {
		String sinput = null;
		try {
			sinput = new String(binput, encoding);
		} catch (UnsupportedEncodingException e) {
			log.error("encoding error, can't encode to string");
			e.printStackTrace();
		}
		return tokenize(sinput);
	}

	private String[] ictclasTokenizer(String sinput) {
		String sOutput = null;
		sOutput = CLibrary.Instance.NLPIR_ParagraphProcess(sinput, bPOSTagged);
		return sOutput.split("\\s+");
	}

	// for test
	public static void main(String[] args) {
		ICTCLASTokenizer test = new ICTCLASTokenizer();
		String sInput = "今天下雪了，江南styleaa abc";
		byte[] bInput = null;
		try {
			bInput = sInput.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] results = test.tokenize(sInput);
		for (String result : results) {
			System.out.println(result);
		}
		String[] results1 = test.tokenize(bInput, "UTF-8");
		for(String result : results1) {
			System.out.println(result);
		}
	}

}
