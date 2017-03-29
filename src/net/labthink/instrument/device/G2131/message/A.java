/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.labthink.instrument.device.G2131.message;

import java.io.Serializable;

/**
 *
 * @author Moses
 */
public class A  implements Serializable {
    public int a = 1;
    public byte[] a2 = {0x12,0x34,0x47};
    public A(){
        defaultPacket(null);
    }

//    protected abstract void defaultPacket(byte[] _content);
    protected void defaultPacket(byte[] _content) {
        System.out.println(this.a);
    }

}
