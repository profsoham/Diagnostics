package com.prophecysensorlytic.daig.auth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class UtilCrypto_Web {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		long R=generateRandomSeed();
		System.out.println(R+"  "+BigInteger.valueOf(R));
		System.err.println(new BigInteger("123456789098765"));
		String y=generateDigest("123456789098765");
		System.err.println("Digest = "+y);
		BigInteger x = generateText_QR(y,R);
		System.out.println(x.toString());
		BigInteger m=getDigestForValidation(x, R);
		System.out.println(m);
		
		
		

	}
	
	public static BigInteger getDigestForValidation(BigInteger result,final long R ){
		BigInteger bInt=new BigInteger(String.valueOf(R));
		return result.xor(bInt);
	}
	

	public static BigInteger generateText_QR(final String C, long r) {
		
		BigInteger R = new BigInteger(String.valueOf(r));
		BigInteger bInt = new BigInteger(C);
		return bInt.xor(R);

	}
	
	
	public static String getUserResponseAsGiven(String x,String r){
		BigInteger bIntOne=new BigInteger(x);
		BigInteger bIntTwo=new BigInteger(r);
		return bIntOne.xor(bIntTwo).toString();
	}

	public static long generateRandomSeed() {
		Random rnd = new Random(System.currentTimeMillis());
		long r = rnd.nextLong();
		return r;
	}

	public static String generateDigest(String information) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(information.getBytes());
		byte[] digest = md.digest();
		return new BigInteger(digest).toString();
		
	}

}
