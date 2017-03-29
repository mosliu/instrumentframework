/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author Moses
 */
public class Package {
    private int length;//包体
    private int length_header;
    private int length_body;
    private int length_tail;
    private byte[] pkg;
    private byte[] head;
    private byte[] tail;
    private byte[] body;

    /*
     * 计算校验
     */
    void checksum(){

    }

    /*
     * 设置一部分代码 数据
     */
    public void setByte(byte[] byte_in,int pos_in,int pos_body,int count) {
        System.arraycopy(byte_in, pos_in, body, pos_body, count);
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the length_header
     */
    public int getLength_header() {
        return length_header;
    }

    /**
     * @param length_header the length_header to set
     */
    public void setLength_header(int length_header) {
        this.length_header = length_header;
    }

    /**
     * @return the length_body
     */
    public int getLength_body() {
        return length_body;
    }

    /**
     * @param length_body the length_body to set
     */
    public void setLength_body(int length_body) {
        this.length_body = length_body;
    }

    /**
     * @return the length_tail
     */
    public int getLength_tail() {
        return length_tail;
    }

    /**
     * @param length_tail the length_tail to set
     */
    public void setLength_tail(int length_tail) {
        this.length_tail = length_tail;
    }

    /**
     * @return the pkg
     */
    public byte[] getPkg() {
        return pkg;
    }

    /**
     * @param pkg the pkg to set
     */
    public void setPkg(byte[] pkg) {
        this.pkg = pkg;
    }

    /**
     * @return the head
     */
    public byte[] getHead() {
        return head;
    }

    /**
     * @param head the head to set
     */
    public void setHead(byte[] head) {
        this.head = head;
    }

    /**
     * @return the tail
     */
    public byte[] getTail() {
        return tail;
    }

    /**
     * @param tail the tail to set
     */
    public void setTail(byte[] tail) {
        this.tail = tail;
    }

    /**
     * @return the body
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(byte[] body) {
        this.body = body;
    }

    /*
     * 
     */
    
    
}
