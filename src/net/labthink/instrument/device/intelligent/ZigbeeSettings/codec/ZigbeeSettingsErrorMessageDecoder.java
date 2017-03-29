package net.labthink.instrument.device.intelligent.ZigbeeSettings.codec;


import java.nio.ByteBuffer;
import net.labthink.utils.BytePlus;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * industrialpc's ErrorMessageDecoder 当有错误帧时，抛弃错误帧。
 *
 * @version 1.0.0.0, Nov 17, 2010
 * @author Moses
 */
public class ZigbeeSettingsErrorMessageDecoder implements MessageDecoder {

    boolean readHeader = false;
    byte START = (byte) 0xA0;
    byte END = (byte) 0xAF;
    int length = 0;
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        //System.out.println("position:"+in.position());
        byte C = in.get();
        //System.out.println("position:"+in.position());
        if(!(START <= C && C <= END)){
            //检查包头是否符合zigbee包格式，如果符合格式则放过，如果不符合格式则处理
            do{
                length++;
                if(length<in.remaining()-1){
                    C = in.get();
                }else{
                    return MessageDecoderResult.NEED_DATA;
                }
            }while(!(START <= C && C <= END));
            return MessageDecoderResult.OK;
        }

        if (in.remaining() > 300) {
            return MessageDecoderResult.OK;
        } else {
            return MessageDecoderResult.NEED_DATA;
        }
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
        // Try to skip header if not read.
        
        int k = 0;
        if(length == 0){
            k=in.remaining();
        }else{
            k=length;
            length =0;
        }
        
        byte[] a = new byte[k];
        in.get(a);
        System.out.println("错误数据："+BytePlus.byteArray2String(a));
        
        return MessageDecoderResult.OK;
//        if (!readHeader) {
//            in.getShort(); // 跳过 'header'.
//            readHeader = true;
//        }
//        if (in.get() == 0xBB) {
//            if (readHeader == false) {
//                readHeader = true;
//                return MessageDecoderResult.NEED_DATA;
//            } else {
//                return MessageDecoderResult.OK;
//            }
//        } else {
//            return MessageDecoderResult.NEED_DATA;
//        }
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception {
    }

    public static void main(String[] args) {
        for (int i = 0x9e; i < 0xb3; i++) {


            byte C = (byte) i;
            byte START = (byte) 0xA0;
            byte END = (byte) 0xAF;
            //C = (byte) (C & FMT);
            System.out.println(C);
            //System.out.println(FMT);
            System.out.println(START <= C && C <= END);
        }
    }
}
