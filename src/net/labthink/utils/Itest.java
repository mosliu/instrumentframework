package net.labthink.utils;

public class Itest {
    public static void main(String[] args) {

//	int max_log = 0;
//	int max_id = 0;
//	int temp = 0;
//	for (int i = 1; i < 1000000; i++) {
//	    temp = Test2(i);
//	    if (temp > max_log) {
//		max_id = i;
//		max_log = temp;
//	    }
//	    if(temp>1000){
//		System.out.println("over 5000!! : "+i);
//	    }
//	}
//	System.out.println("No." + max_id + ":" + max_log);
//	 System.out.println(Test(999167));
//	Test(999167);
    }

    public static long Test(int i) {
	long Res = 0;
	while (i != 1) {
	    ++Res;
	    // System.out.println("No." + Res + ":\ti is " + i);
	    if (i % 2 == 0) {
		i /= 2;
		// System.out.println("\t\ti /= 2;");
	    } else {
		i = i * 3 + 1;
		// System.out.println("\t\ti = i * 3 + 1;");
	    }
	    if(Res>10000000000L){
		return Res;
	    }
	}
	return Res;
    }
    public static int Test2(int i) {
	int Res = 0;
	while (i != 1) {
	    ++Res;
	    // System.out.println("No." + Res + ":\ti is " + i);
	    if (i % 2 == 0) {
		i /= 2;
		// System.out.println("\t\ti /= 2;");
	    } else {
		i = i * 3 + 1;
		// System.out.println("\t\ti = i * 3 + 1;");
	    }
	    if(Res>500000){
		return Res;
	    }
	}
	
	return Res;
    }

}
