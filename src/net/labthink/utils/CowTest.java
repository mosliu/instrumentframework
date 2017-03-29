package net.labthink.utils;

import java.util.ArrayList;
import java.util.List;

public class CowTest {

    public static void main(String[] args) {

	Cowherd james= new  Cowherd(1);
	for (int yearth = 1; yearth <= 20; yearth++) {
	    james.PassYaer();
	}
	System.out.println(james.getCowCount());
	System.out.println("=============================");
	List cowList = new ArrayList<Cow>();
	Cow firstCow = new Cow(); // First cow
	cowList.add(firstCow);
	// 10 years
	for (int yearth = 1; yearth <= 20; yearth++) {
	    int cowChildSum = 0;
	    for (int listIdx = 0; listIdx < cowList.size(); listIdx++) {
		Cow tempCow = (Cow) cowList.get(listIdx);
		// add age
		tempCow.addAge();
//		cowList.set(listIdx, tempCow);
		// judge how many cows will born
		if (tempCow.isBreedable()) {
		    cowChildSum++;
		}
	    }
	    for (int i = 1; i <= cowChildSum; i++) {
		cowList.add(new Cow());// new born cow
	    }
	    // 输出每年牛的总数
	    System.out.println("Year" + yearth + "：" + cowList.size());
	}
    }
}

/**
 * @author Moses 牛，3年后可生，20年后绝生 平均年龄30
 */
class Cow {
    // the age of the cow
    private int age;

    public Cow() {
	this.age = 0;
    }

    // after 3 years ,the cow can breed
    public boolean isBreedable() {
	return age >= 3 && age <= 20 ? true : false;
    }

    // after 30 years ,the cow can breed
    public boolean isDead() {
	return age >= 30 ? true : false;
    }

    // the age growed
    public void addAge() {
	age++;
    }

}

class Cowherd {
    List<Cow> cows = new ArrayList<Cow>();
    int herdyear;

    public Cowherd(int newcows) {
	for (int i = 0; i < newcows; i++) {
	    cows.add(new Cow());
	}
	herdyear = 1;
    }
    
    public int getCowCount(){
	return cows.size();
    }
    
    // 每过一年年总结
    public void PassYaer() {
	List<Cow> thisYearBornCows = new ArrayList<Cow>();
	List<Cow> thisYearDeadCows = new ArrayList<Cow>();
	for (Cow checkingCow : cows) {
	 // add cow age
	    checkingCow.addAge();
	    // judge how many cows will born
	    if (checkingCow.isBreedable()) {
		thisYearBornCows.add(new Cow());
	    }
	    if(checkingCow.isDead()){
		thisYearDeadCows.add(checkingCow);
	    }
	}
	
	cows.addAll(thisYearBornCows);
	cows.removeAll(thisYearDeadCows);
    }
}