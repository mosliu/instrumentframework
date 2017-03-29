package net.labthink.instrument.device.intelligent.industrialpc.message;

/**
 * industrialpc's PC-51 Message
 *
 * @version        1.0.0.0, Nov 17, 2010
 * @author         Moses
 */
 
public interface industrialpcParams {
    public static final int zigbeehead = 5;

    public static final short PACKET_HEADER = (short) 0xAAAA;

    public static final short PACKET_TAIL = (short) 0xBBBB;

    public static final int PACKET_FULL_LEN = 13;

    public static final int PACKET_HEADER_LEN = 2;

    public static final int PACKET_TAIL_LEN = 2;

    public static final int PACKET_BODY_LEN = 9;
}
