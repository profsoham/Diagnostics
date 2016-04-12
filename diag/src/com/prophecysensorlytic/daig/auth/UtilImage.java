package com.prophecysensorlytic.daig.auth;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class UtilImage {

	final static String _OUT_DIR = "C:\\ProgramData\\Microsoft\\User Account Pictures";

	public UtilImage() {
		// TODO Auto-generated constructor stub
	}

	static void copyFile(File srcFile, File destDir) throws IOException {
		FileUtils.copyDirectoryToDirectory(srcFile, destDir);
	}

	static void changeUserAccountPics(File inputimgFile) throws IOException {
		BufferedImage inputImage = ImageIO.read(inputimgFile);
		ImageIO.write(inputImage, "bmp", new File(_OUT_DIR, "user.bmp"));
		ImageIO.write(inputImage, "png", new File(_OUT_DIR, "user.png"));
		BufferedImage image40 = getScaledBufferedImage(40, 40, inputImage);
		ImageIO.write(image40, "png", new File(_OUT_DIR, "user-40.png"));
		BufferedImage image200 = getScaledBufferedImage(200, 200, inputImage);
		ImageIO.write(image200, "png", new File(_OUT_DIR, "user-200.png"));
	}

	static void changeUserAccountPics(String password) throws Exception {
		BufferedImage inputImage = UtilQR.createQRCode(password, 200, 200);
		ImageIO.write(inputImage, "bmp", new File(_OUT_DIR, "user.bmp"));
		ImageIO.write(inputImage, "png", new File(_OUT_DIR, "user.png"));
		BufferedImage image40 = UtilQR.createQRCode(password, 40, 40);
		ImageIO.write(image40, "png", new File(_OUT_DIR, "user-40.png"));
		BufferedImage image200 = inputImage;
		ImageIO.write(image200, "png", new File(_OUT_DIR, "user-200.png"));
	}

	public static BufferedImage createRandomQR(String password) throws Exception {
		BufferedImage inputImage = UtilQR.createQRCode(password, 200, 200);

		return inputImage;
	}

	public static void randomQRPassoword() throws Exception {
		long data = System.currentTimeMillis();
		String x = String.valueOf(data);
		long y = UtilCryptography.encryptPassword(data, new byte[] { 10, 20, 30, 40, 50, 60, 70, 80 });

		changeUserAccountPics(x);
		// Runtime.getRuntime().exec("cmd /c net user SohamSengupta " + y);
	}

	public static Object[] randomQR_ChallengeForGlobal() throws Exception {
		long data = System.currentTimeMillis();
		String x = String.valueOf(data);
		long y = UtilCryptography.encryptPassword(data, new byte[] { 10, 20, 30, 40, 50, 60, 70, 80 });
		BufferedImage imgChallenge = createRandomQR(x);
		return new Object[] { y, imgChallenge };

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		/*
		 * BufferedImage inputImage=ImageIO.read(new
		 * File("D:\\images","i1.jpg")); BufferedImage
		 * b40=getScaledBufferedImage(200, 200, inputImage); ImageIO.write(b40,
		 * "jpeg", new File("D:\\","out.jpg"));
		 */
		/*
		 * String inputFilePath=new DataInputStream(System.in).readLine();
		 * changeUserAccountPics(new File("D:\\images",inputFilePath));
		 * System.out.println("Enter New password"); String newPwd=new
		 * DataInputStream(System.in).readLine();; Runtime.getRuntime().exec(
		 * "cmd /c net user SohamSengupta "+newPwd);
		 * System.out.println("Done!!!!");
		 */
		randomQRPassoword();

	}

	public static BufferedImage getScaledBufferedImage(int scaledWidth, int scaledHeight, BufferedImage inputImage) {
		BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

		// scales the input image to the output image
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();
		return outputImage;

	}

}
