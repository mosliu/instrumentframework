package net.labthink.instrument.device.${devicename}.message;

/**
 * ${devicename}'s 51-PC Message
 *
 * @version        ${version}, ${pp.now?date}
 * @author         ${author?cap_first}
 */
 
public interface ${devicename}OutMessageParams {

	public static final short PACKET_HEADER = (short) 0x${packet.out.head};

    public static final short PACKET_TAIL = (short) 0x${packet.out.tail};

    public static final int PACKET_FULL_LEN = ${packet.out.total};

    public static final int PACKET_HEADER_LEN = ${packet.out.headlen};

    public static final int PACKET_TAIL_LEN = ${packet.out.taillen};

    public static final int PACKET_BODY_LEN = ${packet.out.total-packet.out.headlen-packet.out.taillen};

    
}
