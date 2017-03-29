package net.labthink.instrument.device.XLWB.simulator;

import java.util.Random;

import net.labthink.utils.BytePlus;

public class XLWB {
	Random rand = new Random(System.currentTimeMillis());

	// FF 01 00 00 01抗拉试验 00剥离
	int TestType = -1;

	// FF 0E C8 00 200N
	int Strength_maxValue = 0;
	int Current_Strength = 0;
	// FF 03 96 00 速度
	int Speed = 0;

	// FF 04 02 00 试样件数
	int TestCount = 0;

	// FF 0F 01 00 //暂时还没有确定。暂时怀疑为开始试验。
	// FF 06 00 00 结束？
	// FF 08 DE 06 最大值
	// FF 0B DE 06 统计最大
	// FF 0C 7F 06 统计最小
	// FF 0D AE 06 统计平均

	// FF 05 FA 00

	// 最大力值（当次试验）
	int Max_Strength = 0;
	// 当次统计结果（单次）最大值，高位在前，两位小数，有符号
	int TJ_Max = 0;
	// 当次统计结果（单次）最小值，高位在前，两位小数，有符号
	int TJ_Min = Integer.MAX_VALUE;
	// 当次统计结果（单次）平均值，高位在前，两位小数，有符号
	int TJ_Avg = 0;
	// 试验次数
	int expTimes = 0;

	private byte[] content = new byte[3];

	/*
	 * 传入类型参数 // 01H 抗拉试验 // 00H 剥离试验
	 */
	public XLWB(int TestType) {
		switch (TestType) {
			case 01:
				content[0] = 1;
				break;
			case 00:
				content[0] = 0;
				break;

			default:
				// donothing
				break;
		}
	}

	public void preSettestParams(int _type, int _str, int _speed, int _count) {
		TestType = _type % 2;
		Strength_maxValue = _str & 0xff;
		Speed = _speed & 0xff;
		TestCount = _count % 10 + 1;
		expTimes = 0;
	}

	public byte[] setCurrent_Strength(int kind, int range) {
		int tempnum = rand.nextInt(range);
		if (Current_Strength + tempnum * kind >= 20000) {
			Current_Strength = Current_Strength - tempnum;
		} else if (Current_Strength + tempnum * kind < 0) {
			Current_Strength = rand.nextInt(range);
		} else {
			Current_Strength = Current_Strength + tempnum * kind;
		}
		if (Max_Strength < Current_Strength) {
			Max_Strength = Current_Strength;
			TJ_Max = Current_Strength;
			// System.out.println("最大值更新为："+Max_Strength);
		}
		if (Current_Strength < TJ_Min) {
			TJ_Min = Current_Strength > 0 ? Current_Strength : 0;
			// System.out.println("最小值更新为："+BL_Min);
		}
		tempnum = TJ_Avg * expTimes + Current_Strength;
		expTimes++;
		TJ_Avg = tempnum / expTimes;

		content[0] = 5;
		Current_Strength=Current_Strength&0x7f;
		byte[] tempbytes = BytePlus.int2bytes(Current_Strength);
		content[1] = tempbytes[3];
		content[2] = tempbytes[2];
		// System.arraycopy(tempbytes, 2, content, 1, 2);

		return content;
	}

	// 获得类型
	public byte[] getTestType() {
		content[1] = 8;
		byte[] tempbytes = BytePlus.int2bytes(TestType);
		System.arraycopy(tempbytes, 3, content, 0, 1);
		for (int i = 1; i < content.length; i++) {
			content[i] = 0;
		}
		return content;
	}

	// 获得传感器量程。
	public byte[] getTestStrength_MaxValue() {
		content[0] = 0x0e;
		byte[] tempbytes = BytePlus.int2bytes(Strength_maxValue);
		System.arraycopy(tempbytes, 3, content, 1, 1);
		content[2] = 0;
		return content;
	}

	// 获得速度
	public byte[] getTestSpeed() {
		content[0] = 3;
		byte[] tempbytes = BytePlus.int2bytes(Speed);
		System.arraycopy(tempbytes, 3, content, 1, 1);
		content[2] = 0;
		return content;
	}

	// 获得试样件数
	public byte[] getTestCountValue() {
		content[0] = 4;
		byte[] tempbytes = BytePlus.int2bytes(TestCount);
		System.arraycopy(tempbytes, 3, content, 1, 1);
		content[2] = 0;
		return content;
	}

	// 实验开始信号
	public byte[] getTestBegin() {
		content[0] = 0x0f;
		content[1] = 0x01;
		content[2] = 0x00;
		return content;
	}

	// 实验结束信号
	public byte[] getTestEnd() {
		content[0] = 0x06;
		content[1] = 0x00;
		content[2] = 0x00;
		return content;
	}

	public byte[] getMax_StrengthBs() {
		content[0] = 8;
		byte[] tempbytes = BytePlus.int2bytes(Max_Strength);
		System.arraycopy(tempbytes, 2, content, 1, 2);
		return content;
	}

	public byte[] getTJMax() {
		content[0] = 0x0b;
		byte[] tempbytes = BytePlus.int2bytes(TJ_Max);
		System.arraycopy(tempbytes, 2, content, 1, 2);
		return content;
	}

	public byte[] getTJmin() {
		content[0] = 0x0c;
		byte[] tempbytes = BytePlus.int2bytes(TJ_Min);
		System.arraycopy(tempbytes, 2, content, 1, 2);
		return content;
	}

	public byte[] getTJAvg() {
		content[0] = 0x0d;
		byte[] tempbytes = BytePlus.int2bytes(TJ_Avg);
		System.arraycopy(tempbytes, 2, content, 1, 2);
		return content;
	}

	public boolean isEnd() {
		if (expTimes < TestCount) {
			return false;
		}
		return true;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getStrength_maxValue() {
		return Strength_maxValue;
	}

	public void setStrength_maxValue(int strengthMaxValue) {
		Strength_maxValue = strengthMaxValue;
	}

	public int getCurrent_Strength() {
		return Current_Strength;
	}

	public void setCurrent_Strength(int currentStrength) {
		Current_Strength = currentStrength;
	}

	public int getSpeed() {
		return Speed;
	}

	public void setSpeed(int speed) {
		Speed = speed;
	}

	public int getTestCount() {
		return TestCount;
	}

	public void setTestCount(int testCount) {
		TestCount = testCount;
	}

	public int getMax_Strength() {
		return Max_Strength;
	}

	public void setMax_Strength(int maxStrength) {
		Max_Strength = maxStrength;
	}

	public int getTJ_Max() {
		return TJ_Max;
	}

	public void setTJ_Max(int tJMax) {
		TJ_Max = tJMax;
	}

	public int getTJ_Min() {
		return TJ_Min;
	}

	public void setTJ_Min(int tJMin) {
		TJ_Min = tJMin;
	}

	public int getTJ_Avg() {
		return TJ_Avg;
	}

	public void setTJ_Avg(int tJAvg) {
		TJ_Avg = tJAvg;
	}

	public int getExpTimes() {
		return expTimes;
	}

	public void setExpTimes(int expTimes) {
		this.expTimes = expTimes;
	}

	public void setTestType(int testType) {
		TestType = testType;
	}

}
