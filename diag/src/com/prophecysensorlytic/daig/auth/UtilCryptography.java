package com.prophecysensorlytic.daig.auth;
import java.nio.ByteBuffer;

public class UtilCryptography {

	public static int encryptPassword(long data, byte[] keys) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(8);
		byteBuffer.putLong(data);
		byteBuffer.flip();
		byte[] x = byteBuffer.array();
		int sum=0;
		
		for (int i = 0; i < x.length; i++) {
			byte b = x[i];
			sum += (i+1)*(b * keys[i]);
		}
		
		byteBuffer = null;
		return sum;
	}

	public static long decrypt(long p, byte[] keys) {
		ByteBuffer buffer = ByteBuffer.allocate(8).putLong(p);
		buffer.flip();
		byte[] r = buffer.array();
		byte[] result = new byte[r.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte) (r[i] - keys[i]);
		}
		buffer = null;
		ByteBuffer resultBuffer = ByteBuffer.wrap(result);

		return resultBuffer.getLong();

	}

	public static void main(String[] ar) {
		long x = System.currentTimeMillis();
		long y = encryptPassword(x,
				new byte[] { 10, 20, 30, 40, 50, 60, 70, 80 });
		System.out.println(x+"\n"+y);
		
	}
}
