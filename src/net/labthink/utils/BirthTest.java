package net.labthink.utils;

import java.util.Random;

public class BirthTest {
    public static void main(String[] args) {
	int a = 0;
	int b = 0;
	for(int i=0;i<1;i++){
	    if(turn()){
		++a;
	    }else{
		++b;
	    }
	}
	System.out.println("男多女少次数:"+ a);
	System.out.println("男少女多次数:"+ b);
    }
    public static boolean turn(){
	long male = 0;
	long female = 0;
	long population = 1000000000;
	for(long i=0;i<population;i++){
	    Family f = new Family();
	    f.makebaby();
	    male +=f.son;	  
	    female += f.daughter;	    
	}
	return male>female;
    }
    
}

class Family{
    int daughter = 0;
    int son = 0;
    public int getDaughter() {
        return daughter;
    }
    public void setDaughter(int daughter) {
        this.daughter = daughter;
    }
    public int getSon() {
        return son;
    }
    public void setSon(int son) {
        this.son = son;
    }
    public int getcount(){
	return son+daughter;
    }
    public void makebaby(){
	Random d = new Random(System.nanoTime());
	//0：女，1：男
	int k = -1;
	do{
	    k = d.nextInt(2);
	    
	    if(k==1){
		son++;
	    }else{
		daughter++;
	    }
	}while(k==0);
    }
}