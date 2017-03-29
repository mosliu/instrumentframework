package net.labthink.instrument.device.OxygenTransducer.simulator;

//~--- non-JDK imports --------------------------------------------------------

import net.labthink.utils.BytePlus;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.BitSet;
import java.util.Random;

/**
 * 模拟仪器
 * @author Moses
 */
public class OxygenTransducer implements Serializable {

	//TODO 需要修改
    private static final long serialVersionUID = -685345324534534510L;

    private int currentState = 0;

    private String[] stateString =  {"",""};

    private transient  long   startTimeSpot        = 0;



    private Random rand = new Random(System.currentTimeMillis());

    /**
     * 构造函数
     *
     */
    public OxygenTransducer() {}

	//TODO 是否需要预设一些值
    public void PreSetSimulatorParameters(int balancetime,int muliplier) {
	/*
        cell1.setBalancetime(balancetime);
        cell1.setMultiplier(muliplier);
        cell1.setComputeFlag(1);
//        cell1.setComputeFlag(0);
        cell1.setPressureInt(rand.nextInt(13000));
        cell2.setBalancetime(balancetime);
        cell2.setMultiplier(muliplier);
        cell2.setComputeFlag(2);
//        cell2.setComputeFlag(0);
        cell2.setPressureInt(rand.nextInt(13000));
        cell3.setBalancetime(balancetime);
        cell3.setMultiplier(muliplier);
        cell3.setComputeFlag(3);
//        cell3.setComputeFlag(0);
        cell3.setPressureInt(rand.nextInt(13000));
	*/
    }

    public static void main(String[] args) {

    }

}
