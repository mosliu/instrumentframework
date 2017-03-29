package net.labthink.instrument.device.XLWB.message;

public interface XLWBMessageParams {
    public static final byte PACKET_HEADER = (byte) 0xFF;
    
    public static final short PACKET_TAIL = (short) 0;

    public static final int PACKET_FULL_LEN = 4;
    
    public static final int PACKET_HEADER_LEN = 1;
    
    public static final int PACKET_TAIL_LEN = 0;

    public static final int PACKET_BODY_LEN = 3;
}
