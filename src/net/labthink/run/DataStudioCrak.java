package net.labthink.run;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Hex;

public class DataStudioCrak {
	public static void main(String[] args) {
		String company = "Labthink";
		byte[] companyByteArray = company.getBytes();
		byte[] companyByteIntArray = intToByteArray(compute(companyByteArray,
				companyByteArray.length));
		// byte[] ff = "zhulixia".getBytes();
		byte[] snByte = new byte[32];
		byte[] byte1 = new byte[] { 7, 1 };
		byte[] byte2 = "zhaodapengpojiehahahahahaha".getBytes();
		byte[] byte3 = new byte[] { 127 };
		byte[] snMain = new byte[24];
		System.arraycopy(byte1, 0, snMain, 0, 2);
		System.arraycopy(byte2, 0, snMain, 2, 17);
		System.arraycopy(companyByteIntArray, 0, snMain, 19, 4);
		System.arraycopy(byte3, 0, snMain, 23, 1);
		// ���к����� 1 - single license,2 - site license ,3 educational license
		snMain[2] = (byte) 1;
		int intSn = compute(snMain, snMain.length);
		System.out.println("intSn=" + intSn);
		byte[] key1 = "dddd".getBytes();
		byte[] key2 = intToByteArray(intSn);
		byte[] key = new byte[8];
		System.arraycopy(key1, 0, key, 0, 4);
		System.arraycopy(key2, 0, key, 4, 4);

		byte encodedSnMain[] = new byte[snMain.length];
		try {
			DESKeySpec deskeyspec = new DESKeySpec(key);
			javax.crypto.SecretKey secretkey = SecretKeyFactory.getInstance(
					"DES").generateSecret(deskeyspec);
			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, secretkey);
			cipher.update(snMain, 0, snMain.length, encodedSnMain);
			cipher.doFinal();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.arraycopy(key1, 0, snByte, 0, 4);
		System.arraycopy(key2, 0, snByte, 28, 4);
		System.arraycopy(encodedSnMain, 0, snByte, 4, 24);
		char[] snCharArray = Hex.encodeHex(snByte);
		String sn = new String(snCharArray);
		System.out.println("sn=" + sn);
	}

	private static int compute(byte abyte0[], int i) {
		int j = 0;
		int k = 0;
		for (int l = 0; l < i; l++) {
			j = 31 * j + abyte0[k++];
		}

		return j;
	}

	public static byte[] intToByteArray(int value) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			int offset = (b.length - 1 - i) * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
		return b;
	}

	private static int byteArrayToInt(byte abyte0[]) {
		return (abyte0[3] & 0xff) + ((abyte0[2] & 0xff) << 8)
				+ ((abyte0[1] & 0xff) << 16) + ((abyte0[0] & 0xff) << 24);
	}
}
