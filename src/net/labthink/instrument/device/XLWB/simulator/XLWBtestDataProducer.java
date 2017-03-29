package net.labthink.instrument.device.XLWB.simulator;

import java.util.Random;

public class XLWBtestDataProducer {
	static Random rand = new Random(System.currentTimeMillis());
	static int kind = -1;
	static int num = rand.nextInt(10)+1;
	public static void main(String[] args) {
		String str = allCurve();
		
		// String a = runtimedata();
		System.out.println(str);
	}

	public static String allCurve() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int count = 10;
		for (int i = 0; i < count; i++) {
			sb.append("{");
			sb.append(oneCurve(i));
			if (i < count - 1) {
				sb.append(",\r\n");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	private static String oneCurve(int i) {
		StringBuilder sb = new StringBuilder();
		sb.append("\"kind\":");
		if(kind==-1){
			sb.append(rand.nextInt(2));
		}else{
			sb.append(kind);
		}
		sb.append(",");
		sb.append("\"maxstr\":");
		sb.append((rand.nextInt(20) + 1) * 10);
		sb.append(",");
		sb.append("\"num\":");
		sb.append(num);
		sb.append(",");
		sb.append("\"speed\":");
		sb.append((rand.nextInt(5) + 1)*100);
		sb.append(",");
		sb.append("\"DataLists\":[");
		sb.append(runtimedata());
		sb.append("]}");
		return sb.toString();
	}

	static String runtimedata() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rand.nextInt(30) + 1; i++) {
			sb.append("[");
			sb.append(rand.nextInt(100) + 1);
			sb.append(",");
			if (i == 0) {
				sb.append(rand.nextInt(3)+1);
			} else {
				sb.append(rand.nextInt(7)-3);
			}
			sb.append(",");
			sb.append((rand.nextInt(20) + 1));
			sb.append("],");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();

	}

	public static int getKind() {
		return kind;
	}

	public static void setKind(int kind) {
		XLWBtestDataProducer.kind = kind;
	}
}
