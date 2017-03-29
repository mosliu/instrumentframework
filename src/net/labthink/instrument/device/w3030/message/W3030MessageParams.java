package net.labthink.instrument.device.w3030.message;

public interface W3030MessageParams {
    public static final short PACKET_HEADER = (short) 0xAAAA;
    
    public static final short PACKET_TAIL = (short) 0xBBBB;

    public static final int PACKET_FULL_LEN = 21;
    
    public static final int PACKET_HEADER_LEN = 2;
    
    public static final int PACKET_TAIL_LEN = 2;

    public static final int PACKET_BODY_LEN = 17;
}
