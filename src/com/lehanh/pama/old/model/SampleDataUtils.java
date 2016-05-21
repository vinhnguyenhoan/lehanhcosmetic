package com.lehanh.pama.old.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class SampleDataUtils {

	public static void main(String[] args) throws Exception {
		String loaded = loadFileAsText("olddata\\temp.txt", "|");
		System.out.println(loaded);
	}
	
	public static final String loadFileAsText(String dir, String separate) throws IOException {
		String result = "";
		FileInputStream fis = new FileInputStream(dir);
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line = null;
		while ((line = br.readLine()) != null) {
			result += line + separate;
		}
		br.close();
		return result;
	}
}
