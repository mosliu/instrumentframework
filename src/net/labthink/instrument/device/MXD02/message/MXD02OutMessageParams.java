package net.labthink.instrument.device.MXD02.message;

/**
 * MXD02's 51-PC Message
 *
 * @version        1.0.0.0, Jun 13, 2011
 * @author         Moses
 */
 
public interface MXD02OutMessageParams {

	public static final short PACKET_HEADER = (short) 0xAAAA;

    public static final short PACKET_TAIL = (short) 0xBBBB;

    public static final int PACKET_FULL_LEN = 12;

    public static final int PACKET_HEADER_LEN = 2;

    public static final int PACKET_TAIL_LEN = 2;

    public static final int PACKET_BODY_LEN = 8;

    
}
