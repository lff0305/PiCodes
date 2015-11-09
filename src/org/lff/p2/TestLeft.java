package org.lff.p2;

/**
 * Created by LFF on 2015/11/4.
 */
public class TestLeft {
    public static void main(String[] a) {
        byte b = (byte) 8;
        for(byte i=8;i>=1;i--) {
            if ((b & 0x80) != 0) {
                System.out.println("1");
            } else {
                System.out.println("0");
            }
            b <<= 1;
        }
    }
}
