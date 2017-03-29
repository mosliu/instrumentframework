package net.labthink.instrument.device.TSYW3.simulator;

import java.util.Random;

import net.labthink.utils.BytePlus;

public class TSYW3 {
	Random rand = new Random(System.currentTimeMillis());

	// 湿度
	int wvtr = 0;
	// ppm
	int ppm = 0;
	// 流量
	int flux = 0;

	private byte[] content = new byte[10];

	public TSYW3() {

	}

	public void preSetParams(int _wvtr, int _ppm, int _flux) {
		wvtr = _wvtr;
		ppm = _ppm;
		flux = _flux;
		// TestParams_Range = range;
		// TestParams_Num = num > 10 || num < 1 ? 10 : num;
		// TestParams_Speed = speed > 5 || speed < 1 ? 1 : speed;
		// TestParams_SampleWidth = width % 65536;
	}

	public byte[] fetchCurrentValue(int wvtr_trend, int wvtr_range, int ppm_trend, int ppm_range, int flux_trend,
			int flux_range) {
		int tempnum = rand.nextInt(wvtr_range);
		wvtr = wvtr + tempnum * wvtr_trend;
		tempnum = rand.nextInt(ppm_range);
		ppm = ppm + tempnum * ppm_trend;
		tempnum = rand.nextInt(flux_range);
		flux = flux + tempnum * flux_trend;
		

		//X1:02   试验开始...数据(X2X3X4透湿量；X5X6--PPM值；X7X8气体流量）
		content[0] = 2;
		
		byte[] tempbytes = BytePlus.int2bytes(wvtr);
		System.arraycopy(tempbytes, 1, content, 1, 3);
		
		tempbytes = BytePlus.int2bytes(ppm);
		System.arraycopy(tempbytes, 2, content, 4, 2);
		
		tempbytes = BytePlus.int2bytes(flux);
		System.arraycopy(tempbytes, 2, content, 6, 2);
//		for (int i = 11; i < content.length; i++) {
//			content[i] = 0;
//		}
		return content;
	}

	// 04   系统复位  W3
		public byte[] getReset() {
		content[0] = 4;
		
		for (int i = 1; i < content.length; i++) {
			content[i] = 0;
		}
		return content;
	}

	
	
	//X1:05   停止    W3
	public byte[] getTestEnd() {
		content[0] = 5;
		for (int i = 1; i < content.length; i++) {
			content[i] = 0;
		}
		return content;
	}

	//X1:03	试验中止退出 
	public byte[] getTestStop() {
		content[0] = 3;
		for (int i = 1; i < content.length; i++) {
			content[i] = 0;
		}
		return content;
	}
	
	//X1:01   系统吹洗
	public byte[] getBlowClean() {
		content[0] = 1;
		for (int i = 1; i < content.length; i++) {
			content[i] = 0;
		}
		return content;
	}



	public int getPpm() {
		return ppm;
	}

	public void setPpm(int ppm) {
		this.ppm = ppm;
	}

	public int getFlux() {
		return flux;
	}

	public void setFlux(int flux) {
		this.flux = flux;
	}

	public int getWvtr() {
		return wvtr;
	}

	public void setWvtr(int wvtr) {
		this.wvtr = wvtr;
	}
	
	

}
