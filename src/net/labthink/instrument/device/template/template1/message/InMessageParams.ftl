package net.labthink.instrument.device.${devicename}.message;

/**
 * ${devicename}'s PC-51 Message
 *
 * @version        ${version}, ${pp.now?date}
 * @author         ${author?cap_first}
 */
 
public interface ${devicename}InMessageParams {
    public static final short PACKET_HEADER = (short) 0x${packet.in.head};

    public static final short PACKET_TAIL = (short) 0x${packet.in.tail};

    public static final int PACKET_FULL_LEN = ${packet.in.total};

    public static final int PACKET_HEADER_LEN = ${packet.in.headlen};

    public static final int PACKET_TAIL_LEN = ${packet.in.taillen};

    public static final int PACKET_BODY_LEN = ${packet.in.total-packet.in.headlen-packet.in.taillen};
}
