package net.labthink.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

public class textparse {
	public static void main(String[] args){
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < 40; i++) {
			System.out.print("[");
			System.out.print(rand.nextInt(50)+1);			
			System.out.print(",");
			System.out.print(rand.nextInt(4)-2);			
			System.out.print(",");
			System.out.print((rand.nextInt(20)+1)*100);
			System.out.print("],");
		}
	}
	public static void main2(String[] args) throws Exception {
		Map map = new LinkedHashMap();
		map.put("kind", 1);
		map.put("PreParams", 2);
		map.put("HAParams", 3);
		List l1 = new LinkedList();
		int[] k = { 3, 1, 80 };
		l1.add(k);
		int[] t = { 8, -1, 20 };
		l1.add(t);
		map.put("DataList", l1);
		map.put("ends", "ends");

		JSONObject jo = new JSONObject(map);
		JSONArray ja = new JSONArray();
		
		ja.put(map);
		ja.put(map);
		System.out.println(ja.toString());
		
		String s = jo.toString();
		System.out.println(s);
		
		
		String a = FilePlus.ReadTextFileToString("bin/conf.json");
		 
		System.out.println(a);

		JSONObject jo2 = new JSONObject(a);
//		Map map2 = (Map) jo2.get("value");
		ja =  jo2.getJSONArray("DataLists");
		JSONArray ja2 = ja.getJSONArray(0);
		System.out.println(ja2.getInt(0));
		System.out.println(ja2.getInt(1));
		System.out.println(ja2.getInt(2));
		
		
	}

	public static void main1(String[] args) {
		String choices = "A．公有成员和保护成员    B．公有成员和私有成员  C．保护成员和私有成员    D．公有成员、保护成员和私有成员";
		String choices2 = "A．CDatabase";
		String[] singleones = choices.split("([． ]+)");
		StringBuilder sb = new StringBuilder();

		System.out.println(singleones.length);
		for (int i = 0; i < singleones.length; i++) {
			String temp = singleones[i];
			if (temp.length() == 1 && temp.matches("[a-zA-Z]")) {
				sb.append(temp.toLowerCase());
				sb.append(") ");
			} else {
				sb.append(temp);
				sb.append("     ");
			}
			// System.out.println(singleones[i]);
		}
		System.out.println(sb.toString().trim());
		// name();
		// System.out.println(Integer.parseInt("n", 36));
		// System.out.println(Integer.parseInt("o", 36));
		// System.out.println(Integer.parseInt("k", 36));
		// System.out.println(Integer.parseInt("i", 36));
		// System.out.println(Integer.parseInt("a", 36));
		//	
		//	
		// System.out.println(Integer.parseInt("D3", 16));
		// System.out.println(new Integer('N'));
		// System.out.println(new Integer('O'));
		// System.out.println(new Integer('K'));
		// System.out.println(new Integer('I'));
		// System.out.println(new Integer('A'));
		// int a =4;
		// a += (a++);
		// System.out.println(a);

	}

	public static String a = "D3B29B0C9297E7F5" + "7619D42ECFE7E173"
			+ "DA7D76A9DFE239E0" + "DC0EA7D9AEF1BB0D";
	public static String b = "142A20237A272A" + "343A23227A676F"
			+ "24363E2F3F2761" + "2D202C341A3222" + "26262269393A22";

	public static void name() {
		ByteBuffer bb = ByteBuffer.allocate(100);
		for (int i = 0; i < a.length(); i += 2) {
			bb.put(Byte.parseByte(a.substring(i, i + 2), 16));
		}
		byte[] ba = bb.array();
		File f = new File("c:/aa.a");
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(ba);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
