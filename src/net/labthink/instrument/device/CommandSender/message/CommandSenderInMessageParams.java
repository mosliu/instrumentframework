package net.labthink.instrument.device.CommandSender.message;

/**
 * CommandSender's PC-51 Message
 *
 * @version        1.0.0.0, Oct 26, 2010
 * @author         Moses
 */
 
public interface CommandSenderInMessageParams {
    public static final short PACKET_HEADER = (short) 0xAAAA;

    public static final short PACKET_TAIL = (short) 0xBBBB;

    public static final int PACKET_FULL_LEN = 9;

    public static final int PACKET_HEADER_LEN = 2;

    public static final int PACKET_TAIL_LEN = 2;

    public static final int PACKET_BODY_LEN = 5;
}
