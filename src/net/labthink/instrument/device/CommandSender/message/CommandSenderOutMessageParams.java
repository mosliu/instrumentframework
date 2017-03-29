package net.labthink.instrument.device.CommandSender.message;

/**
 * CommandSender's 51-PC Message
 *
 * @version        1.0.0.0, Oct 26, 2010
 * @author         Moses
 */
 
public interface CommandSenderOutMessageParams {

	public static final short PACKET_HEADER = (short) 0x0;

    public static final short PACKET_TAIL = (short) 0x0;

    public static final int PACKET_FULL_LEN = 9;

    public static final int PACKET_HEADER_LEN = 0;

    public static final int PACKET_TAIL_LEN = 0;

    public static final int PACKET_BODY_LEN = 5;

    
}
