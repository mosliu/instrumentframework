package net.labthink.instrument.device.SHG01.message;

/**
 * Interface description
 *
 *
 * @version        Enter version here..., 10/05/27
 * @author         Enter your name here...
 */
public interface SHG01OutMessageParams {

    /** Field description */
    public static final int PACKET_BODY_LEN = 5;

    /** Field description */
    public static final int PACKET_FULL_LEN = 9;

    /** Field description */
    public static final short PACKET_HEADER = (short) 0xAAAA;

    /** Field description */
    public static final int PACKET_HEADER_LEN = 2;

    /** Field description */
    public static final short PACKET_TAIL = (short) 0xBBBB;

    /** Field description */
    public static final int PACKET_TAIL_LEN = 2;
}
