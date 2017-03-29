package net.labthink.instrument.device.TSYW3.simulator;

import java.util.Random;

public class TSYW3DataProducer {
	static Random rand = new Random(System.currentTimeMillis());
	static int kind = 0;
	public static void main(String[] args) {
		String str = allCurve();
		
		// String a = runtimedata();
		System.out.println(str);
	}

	public static String allCurve() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int count = 10;
//		for (int i = 0; i < count; i++) {
			sb.append(oneCurve());
//			if (i < count - 1) {
////				sb.append(",\r\n");
//				sb.append(",");
//			}
			
//		}
		sb.append("]");
		return sb.toString();
	}

	private static String oneCurve() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(runtimedata());
		
		return sb.toString();
	}

	static String runtimedata() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rand.nextInt(200) + 1; i++) {
			sb.append("[");
			sb.append(rand.nextInt(100) + 1);
			sb.append(",");
			if (i == 0) {
				sb.append(rand.nextInt(7));
			} else {
				sb.append(rand.nextInt(9)-2);
			}
			sb.append(",");
			sb.append((rand.nextInt(20) + 1) * 80);
			
			sb.append(",");
			if (i == 0) {
				sb.append(0-rand.nextInt(7));
			}else if (i == 1) {
				sb.append(20+rand.nextInt(7));
			} else {
				sb.append(rand.nextInt(7)-4);
			}
			sb.append(",");
			sb.append((rand.nextInt(20) + 1) * 80);
			
			sb.append(",");
			if (i == 0) {
				sb.append(0-rand.nextInt(7));
			}else if (i == 1) {
				sb.append(20+rand.nextInt(7));
			} else {
				sb.append(rand.nextInt(7)-4);
			}
			sb.append(",");
			sb.append((rand.nextInt(20) + 1) * 80);
			sb.append("],");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();

	}
}
