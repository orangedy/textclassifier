package com.dy.textclassifier.common.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static String readFileNoException(File file) {
		StringBuilder line = new StringBuilder();
		try {
			// BufferedReader reader = new BufferedReader(new FileReader(file));
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gb2312"));
			String str = "";
			while ((str = reader.readLine()) != null) {
				line.append(str);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return line.toString();
	}

	public static String readFile(File file) throws IOException {
		String encoding = "gb2312";
		return readFile(file, encoding);
	}

	public static String readFile(File file, String encoding) throws IOException {
		InputStream in = null;
		StringBuilder content = new StringBuilder();
		try {
			in = openInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding));
			String line = reader.readLine();
			while (line != null) {
				content.append(line);
				line = reader.readLine();
			}
		} finally {
			close(in);
		}
		return content.toString();
	}

	public static List<String> readFileByLine(File file) throws IOException {
		String encoding = "gb2312";
		return readFileByLine(file, encoding);
	}

	public static List<String> readFileByLine(File file, String encoding) throws IOException {
		InputStream in = null;
		List<String> content = new ArrayList<String>();
		try {
			in = openInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding));
			String line = reader.readLine();
			while (line != null) {
				content.add(line);
				line = reader.readLine();
			}
		} finally {
			close(in);
		}
		return content;
	}
	
	public static void writeStringToFile(File file, String content, boolean append, String encoding) throws IOException {
		OutputStream out = null;
		try {
			out = openOutputStream(file, append);
			out.write(content.getBytes(encoding));
		} finally {
			close(out);
		}
	}

	public static FileInputStream openInputStream(File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if (file.canRead() == false) {
				throw new IOException("File '" + file + "' cannot be read");
			}
		} else {
			throw new FileNotFoundException("File '" + file + "' does not exist");
		}
		return new FileInputStream(file);
	}

	public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if (file.canWrite() == false) {
				throw new IOException("File '" + file + "' cannot be written to");
			}
		} else {
			File parent = file.getParentFile();
			if (parent != null) {
				if (!parent.mkdirs() && !parent.isDirectory()) {
					throw new IOException("Directory '" + parent + "' could not be created");
				}
			}
		}
		return new FileOutputStream(file, append);
	}
	
	public static void close(Closeable closeable){
		try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
	}
	
	public static void main(String[] args) {
		File file = new File("E:\\a\\b");
		try {
			readFileByLine(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
