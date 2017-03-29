package net.labthink.instrument.device.InfraredSensorSimulator.message;

/**
 * MXD02's PC-51 Message
 *
 * @version        1.0.0.0, Jun 13, 2011
 * @author         Moses
 */
 
public interface InfraredMessageParams {
    public static final short PACKET_HEADER = (short) 0xAAAA;

    public static final short PACKET_TAIL = (short) 0xBBBB;

    public static final int PACKET_FULL_LEN = 8;

    public static final int PACKET_HEADER_LEN = 2;

    public static final int PACKET_TAIL_LEN = 2;

    public static final int PACKET_BODY_LEN = 4;
}
