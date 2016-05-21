package com.lehanh.pama.ui.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.wb.swt.SWTResourceManager;

public class PamaResourceManager extends SWTResourceManager {

	public static Image getImage(String path, String key, int maxW, int maxH) throws FileNotFoundException, IOException {
		Image actualImg = getImage(new FileInputStream(path + key));
		
		Rectangle actualS = actualImg.getBounds();
		if (actualS.width == maxW && actualS.height == maxH) {
			return actualImg;
		}
		
		double ratio = 1;
		if (maxH <= 0) {
			ratio = (double) actualS.width / (double) maxW;
		} else if (maxW <= 0) {
			ratio = (double) actualS.height / (double) maxH;
		} else {
			double ratioByH = (double) actualS.height / (double) maxH;
			double ratioByW = (double) actualS.width / (double) maxW;
			ratio = Math.min(ratioByH, ratioByW);
		
			int newW = (int) ((double) actualS.width / ratio);
			int newH = (int) ((double) actualS.height / ratio);
			
			if (newW > maxW || newH > maxH) {
				ratio = Math.max(ratioByH, ratioByW);
			}
		}
		int newW = (int) ((double) actualS.width / ratio);
		int newH = (int) ((double) actualS.height / ratio);
		
		Image result = new Image(actualImg.getDevice(), actualImg.getImageData().scaledTo(newW, newH));
		actualImg.dispose();
		return result;
	}
	
}
