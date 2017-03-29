package net.labthink.instrument.device.VACVBS.message;

/**
 * VACVBS's PC-51 Message
 *
 * @version        1.0.0.0, 2010/08/20
 * @author         Moses
 */
 
public interface VACVBSInMessageParams {
    public static final short PACKET_HEADER = (short) 0xAAAA;

    public static final short PACKET_TAIL = (short) 0xBBBB;

    public static final int PACKET_FULL_LEN = 9;

    public static final int PACKET_HEADER_LEN = 2;

    public static final int PACKET_TAIL_LEN = 2;

    public static final int PACKET_BODY_LEN = 5;
}
