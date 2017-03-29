package net.labthink.instrument.device.Coder.codec;

import net.labthink.utils.BytePlus;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * LLTEST's ErrorMessageDecoder 当有错误帧时，抛弃错误帧。
 *
 * @version 1.0.0.0, Sep 16, 2010
 * @author Moses
 */
public class CoderErrorMessageDecoder implements MessageDecoder {

    boolean readHeader = false;
    int length;

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        
        
        
        if(in.remaining()>40){
            
            length = 20;
            return MessageDecoderResult.OK;
        }
        
        //判断缓冲字段是否大于6个，如果大于则肯定出现问题
        //未考虑校验和错
        if(in.remaining()>6){
            length = 0;
            byte C = 0;
            C = in.get();
            if((C!=(byte)0xfb)){
                length++;
            }else{
                return MessageDecoderResult.NOT_OK;
            }
            while(in.remaining()>0&&in.get()!=(byte)0xfb){
                length++;
            }
            
            return MessageDecoderResult.OK;
        }else{
            return MessageDecoderResult.NEED_DATA;
        }
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
        
        if (length>0){
            byte[] a = new byte[length];
            in.get(a);
            System.out.println("错误数据："+BytePlus.byteArray2String(a));
        }
        return MessageDecoderResult.OK;
        
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception {
    }
}
