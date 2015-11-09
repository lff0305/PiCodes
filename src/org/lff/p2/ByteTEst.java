package org.lff.p2;

/**
 * Created by LFF on 2015/11/7.
 */
public class ByteTEst {
    public static void main(String[] argu) {
        int x = (Integer.parseInt("11110000", 2));
        System.out.println(x & 0xFF);

        byte X = (byte)(x & 0xFF);
        System.out.println(X);

        for(int i=8;i>=1;i--) {
            if ((X & 0x80) != 0) {
                System.out.print("1");

            } else {
                System.out.print("0");

            }
            X <<= 1;
        }
    }
}
