/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.G2131.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Moses
 */
public class B implements Serializable {
    private static final String STORE_FILE = "bytes.out";
    public  int a = 2;

    A aa = new A();
    A ab = new A();
    

    public static void main(String[] args) {
        ObjectOutputStream out = null;
        try {
            B b = new B();
            b.aa.a = 33;
            b.ab.a = 34;
            out = new ObjectOutputStream(new FileOutputStream(STORE_FILE));
            out.writeObject(b);

            B o = null;
            File f = new File(STORE_FILE);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fis);

            o = (B) in.readObject();
            System.out.println(b.aa.a);
            System.out.println(f.getAbsolutePath());

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(B.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(B.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(B.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
}
