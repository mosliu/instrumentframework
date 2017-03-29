package net.labthink.instrument.device.httl1.simulator;

import java.util.Random;

import net.labthink.utils.BytePlus;

public class Httl1 {
	Random rand = new Random(System.currentTimeMillis());
	// 01H 抗拉试验
	// 02H 剥离试验
	// 03H 热粘试验
	int TestType = 0;

	// 量程，高位在前，两位小数
	int TestParams_Range = 0;
	// 试验编号；范围：1~10
	int TestParams_Num = 0;
	// 试验速度
	/*
	 * X6 =01H 100mm/min X6 =02H 150mm/min X6 =03H 200mm/min X6 =04H 300mm/min
	 * X6 =05H 500mm/min
	 */
	int TestParams_Speed = 0;
	// 试样宽度，高位在前，一位小数
	int TestParams_SampleWidth = 0;

	// 实时力值 X3X4 高位在前，两位小数，有符号
	int Current_Strength = 0;
	// 最大力值（当次试验）
	int Max_Strength = 0;
	// 剥离试验 当次统计结果（单次）最大值，高位在前，两位小数，有符号
	int BL_Max = 0;
	// 剥离试验 当次统计结果（单次）最小值，高位在前，两位小数，有符号
	int BL_Min = Integer.MAX_VALUE;
	// 剥离试验 当次统计结果（单次）平均值，高位在前，两位小数，有符号
	int BL_Avg = 0;
	// 试验次数
	int expTimes = 0;
	// 当次试验强度结果（多次试验） 热粘试验
	int Result_hotattach = 0;

	// 动封头温度，高位在前，一位小数
	int HA_Move_Temp = 0;
	// 静风头温度，高位在前，一位小数
	int HA_Still_Temp = 0;
	// 热封压力，高位在前，整型；单位：kPa
	int HA_Preasure = 0;
	// 热封时间
	int HA_Time = 0;
	// 停滞时间
	int HA_Stoptime = 0;

	private byte[] content = new byte[9];

	/*
	 * 传入类型参数 // 01H 抗拉试验 // 02H 剥离试验 // 03H 热粘试验
	 */
	public Httl1(int TestType) {
		switch (TestType) {
			case 01:
				content[0] = 1;
				break;
			case 02:
				content[0] = 2;
				break;
			case 03:
				content[0] = 3;
				break;

			default:
				// donothing
				break;
		}
	}

	public void preSettestParams(int range, int num, int speed, int width) {
		TestParams_Range = range;
		TestParams_Num = num > 10 || num < 1 ? 10 : num;
		TestParams_Speed = speed > 5 || speed < 1 ? 1 : speed;
		TestParams_SampleWidth = width % 65536;
	}

	public void preSetHAParams(int Move_Temp, int Still_Temp, int Preasure,
			int Time, int Stoptime) {
		HA_Move_Temp = Move_Temp % 65536;
		HA_Still_Temp = Still_Temp % 65536;
		HA_Preasure = Preasure % 65536;
		HA_Time = Time % 65536;
		HA_Stoptime = Stoptime % 65536;
	}

	public byte[] setCurrent_Strength(int kind, int range) {
		int tempnum = rand.nextInt(range);
		Current_Strength =  Current_Strength + tempnum*kind;
				
		if (Max_Strength < Current_Strength) {
			Max_Strength = Current_Strength;
			BL_Max = Current_Strength;
//			System.out.println("最大值更新为："+Max_Strength);
		}
		if (Current_Strength < BL_Min) {
			BL_Min = Current_Strength>0?Current_Strength:0;
//			System.out.println("最小值更新为："+BL_Min);
		}
		tempnum = BL_Avg * expTimes + Current_Strength;
		expTimes++;
		BL_Avg = tempnum / expTimes;

		content[1] = 2;
		byte[] tempbytes = BytePlus.int2bytes(Current_Strength);
		System.arraycopy(tempbytes, 2, content, 2, 2);
		for (int i = 4; i < content.length; i++) {
			content[i] = 0;
		}

		return content;
	}

	// 获得热封时间
	public byte[] getHATime() {
		content[1] = 8;
		byte[] tempbytes = BytePlus.int2bytes(HA_Time);
		System.arraycopy(tempbytes, 2, content, 2, 2);
		tempbytes = BytePlus.int2bytes(HA_Stoptime);
		System.arraycopy(tempbytes, 2, content, 4, 2);
		for (int i = 6; i < content.length; i++) {
			content[i] = 0;
		}		
		return content;
	}

	// 获得热封参数
	public byte[] getHAParams() {
		content[1] = 7;
		byte[] tempbytes = BytePlus.int2bytes(HA_Move_Temp);
		System.arraycopy(tempbytes, 2, content, 2, 2);
		tempbytes = BytePlus.int2bytes(HA_Still_Temp);
		System.arraycopy(tempbytes, 2, content, 4, 2);
		tempbytes = BytePlus.int2bytes(HA_Preasure);
		System.arraycopy(tempbytes, 2, content, 6, 2);
		
		
		return content;
	}

	// 获得实验参数
	public byte[] getTestParams() {
		content[1] = 1;
		byte[] tempbytes = BytePlus.int2bytes(TestParams_Range);
		System.arraycopy(tempbytes, 2, content, 2, 2);
		content[4] = (byte) TestParams_Num;
		content[5] = (byte) TestParams_Speed;
		tempbytes = BytePlus.int2bytes(TestParams_SampleWidth);
		System.arraycopy(tempbytes, 2, content, 6, 2);
		return content;
	}

	// 实验结束信号
	public byte[] getTestEnd() {
		content[1] = 3;
		for (int i = 2; i < content.length; i++) {
			content[i] = 0;
		}
		// byte[] tempbytes = new byte[6];
		// System.arraycopy(tempbytes, 2, content, 2, 6);
		return content;
	}

	public byte[] getMax_StrengthBs() {
		content[1] = 4;
		byte[] tempbytes = BytePlus.int2bytes(Max_Strength);
		System.arraycopy(tempbytes, 2, content, 2, 2);
//		for (int i = 4; i < content.length; i++) {
//			content[i] = 0;
//		}
                tempbytes = BytePlus.int2bytes(BL_Min);
		System.arraycopy(tempbytes, 2, content, 4, 2);
		tempbytes = BytePlus.int2bytes(BL_Avg);
		System.arraycopy(tempbytes, 2, content, 6, 2);
		for (int i = 7; i < content.length; i++) {
			content[i] = 0;
		}
		return content;
	}

	public byte[] getBL_ResultBs() {
		content[1] = 4;
		byte[] tempbytes = BytePlus.int2bytes(BL_Max);
		System.arraycopy(tempbytes, 2, content, 2, 2);
		tempbytes = BytePlus.int2bytes(BL_Min);
		System.arraycopy(tempbytes, 2, content, 4, 2);
		tempbytes = BytePlus.int2bytes(BL_Avg);
		System.arraycopy(tempbytes, 2, content, 6, 2);
		for (int i = 7; i < content.length; i++) {
			content[i] = 0;
		}
		return content;
	}

	//

	public int getTestType() {
		return TestType;
	}

	public void setTestType(int testType) {
		TestType = testType;
	}

	public int getTestParams_Range() {
		return TestParams_Range;
	}

	public void setTestParams_Range(int testParamsRange) {
		TestParams_Range = testParamsRange;
	}

	public int getTestParams_Num() {
		return TestParams_Num;
	}

	public void setTestParams_Num(int testParamsNum) {
		TestParams_Num = testParamsNum;
	}

	public int getTestParams_Speed() {
		return TestParams_Speed;
	}

	public void setTestParams_Speed(int testParamsSpeed) {
		TestParams_Speed = testParamsSpeed;
	}

	public int getTestParams_SampleWidth() {
		return TestParams_SampleWidth;
	}

	public void setTestParams_SampleWidth(int testParamsSampleWidth) {
		TestParams_SampleWidth = testParamsSampleWidth;
	}

	public int getCurrent_Strength() {
		return Current_Strength;
	}

	public void setCurrent_Strength(int currentStrength) {
		Current_Strength = currentStrength;
	}

	public int getMax_Strength() {
		return Max_Strength;
	}

	public void setMax_Strength(int maxStrength) {
		Max_Strength = maxStrength;
	}

	public int getBL_Max() {
		return BL_Max;
	}

	public void setBL_Max(int bLMax) {
		BL_Max = bLMax;
	}

	public int getBL_Min() {
		return BL_Min;
	}

	public void setBL_Min(int bLMin) {
		BL_Min = bLMin;
	}

	public int getBL_Avg() {
		return BL_Avg;
	}

	public void setBL_Avg(int bLAvg) {
		BL_Avg = bLAvg;
	}

	public int getResult_hotattach() {
		return Result_hotattach;
	}

	public void setResult_hotattach(int resultHotattach) {
		Result_hotattach = resultHotattach;
	}

	public int getHA_Move_Temp() {
		return HA_Move_Temp;
	}

	public void setHA_Move_Temp(int hAMoveTemp) {
		HA_Move_Temp = hAMoveTemp;
	}

	public int getHA_Still_Temp() {
		return HA_Still_Temp;
	}

	public void setHA_Still_Temp(int hAStillTemp) {
		HA_Still_Temp = hAStillTemp;
	}

	public int getHA_Preasure() {
		return HA_Preasure;
	}

	public void setHA_Preasure(int hAPreasure) {
		HA_Preasure = hAPreasure;
	}

	public int getHA_Time() {
		return HA_Time;
	}

	public void setHA_Time(int hATime) {
		HA_Time = hATime;
	}

	public int getHA_stoptime() {
		return HA_Stoptime;
	}

	public void setHA_stoptime(int hAStoptime) {
		HA_Stoptime = hAStoptime;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}
