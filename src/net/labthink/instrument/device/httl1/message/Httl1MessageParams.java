package net.labthink.instrument.device.httl1.message;

public interface Httl1MessageParams {
    public static final short PACKET_HEADER = (short) 0xAAAA;
    
    public static final short PACKET_TAIL = (short) 0xBBBB;

    public static final int PACKET_FULL_LEN = 13;
    
    public static final int PACKET_HEADER_LEN = 2;
    
    public static final int PACKET_TAIL_LEN = 2;

    public static final int PACKET_BODY_LEN = 9;
}
