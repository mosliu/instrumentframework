/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.Fluke45.codec;

import java.nio.ByteBuffer;
import net.labthink.instrument.device.Fluke45.packet.Fluke45Pkt;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 *
 * @author Moses
 */
public class Fluke45MessageDecoder implements MessageDecoder {
    private int capacity =100; //最大100个字节一包
    
    @Override
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {

        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        }

        //起码有两个字符
        //待解码字段举例：
        //2B 39 2E 37 32 45 2D 33 0D 0A
        //+9.72E-3回车 换行
        byte od = in.get();
        byte oa = in.get();

        while (od != 0x0d && oa != 0x0a) {
            if (in.hasRemaining()) {
                od = oa;
                oa = in.get();
            } else {
                return MessageDecoderResult.NEED_DATA;
            }
        }

        return MessageDecoderResult.OK;
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        Fluke45Pkt pkt = decodeBody(session, in);
//        if (pkt.CheckState()) {
        if (true) {
            //包正确
            out.write(pkt);
            return MessageDecoderResult.OK;
        } else {
            return MessageDecoderResult.NOT_OK;
        }
    }

    protected Fluke45Pkt decodeBody(IoSession session, IoBuffer in) {

        if (in.remaining() < 2) {
            return null;
        }
        Fluke45Pkt pkt = new Fluke45Pkt();
        ByteBuffer bbuf = ByteBuffer.allocate(capacity);
        //起码有两个字符
        //待解码字段举例：
        //2B 39 2E 37 32 4S 2D 33 0D 0A
        //+9.72E-3回车 换行
        byte od = in.get();
        byte oa = in.get();

        while (od != 0x0d && oa != 0x0a) {
                bbuf.put(od);
            if (in.hasRemaining()) {
                od = oa;
                oa = in.get();
            }
        }
        bbuf.put(od);
        bbuf.put(oa);
        bbuf.flip();//翻转，确定position 和limit
        
        
        byte[] content = new byte[bbuf.limit()];
        bbuf.get(content);
        
        
        pkt.setPkt(content);

        return pkt;
    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
    }

}
