/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.Fluke45.codec;

import net.labthink.instrument.device.Fluke45.packet.Fluke45Pkt;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 *
 * @author Moses
 */
public class Fluke45MessageEncoder<T extends Fluke45Pkt> implements MessageEncoder<T> {

    @Override
    public void encode(IoSession session, T message, ProtocolEncoderOutput out) throws Exception {
        byte[] pktbyte = message.getAll();
        IoBuffer buf = IoBuffer.allocate(pktbyte.length);
        buf.setAutoExpand(true);
        // 测试用语句
        buf.put(pktbyte);
        buf.flip();
        out.write(buf);
    }

}
