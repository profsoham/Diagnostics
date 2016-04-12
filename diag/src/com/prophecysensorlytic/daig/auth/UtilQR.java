package com.prophecysensorlytic.daig.auth;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class UtilQR {

	public UtilQR() {
		// TODO Auto-generated constructor stub
	}

	public static BufferedImage createQRCode(String qrCodeData, int qrCodeheight,
			int qrCodewidth) throws WriterException, IOException {
		BitMatrix matrix = new MultiFormatWriter().encode(qrCodeData,
				BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight);
		/*
		 * MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath
		 * .lastIndexOf('.') + 1), new File(filePath));
		 */
		return MatrixToImageWriter.toBufferedImage(matrix);
	}
}
