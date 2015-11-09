package org.lff.p2;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

/**
 * Created by LFF on 2015/11/9.
 */
public class ZtLib {

    private static byte REG_DAT       =      (0x02);
    private static byte REG_SLEEP      =     (0x04) ;
    private static byte REG_BRIGHTNESS    =  0x0A;

    private static I2CBus bus;
    private static I2CDevice device;

    private static final byte[] codes = new byte[] {
         0x3F,0x06,0x5B,0x4F,0x66,0x6D,0x7D,0x07,0x7F,0x6F
         //   0x77, 0x7C,0x39,0x5E,0x79,0x71,0x00
    };

    private static void write(byte[] cmd) {
        try {
            device.write(cmd, 0, cmd.length);
        } catch (IOException e) {
        }
    }

    public static void main(String[] argu) {
        init();
        byte[] wake = new byte[]{REG_SLEEP, (byte)(0xA1 & 0xFF), 0, 0, 0};
        write(wake);
        byte[] bright = new byte[]{REG_BRIGHTNESS, 0x1f, 0x1f, 0, 0};
        write(bright);


        for (int i=0; ; i++) {
            int d3 = i / 1000;
            int d2 = (i - d3 * 1000) / 100;
            int d1 = (i - d3 * 1000 - d2 * 100) / 10;
            int d0 = i % 10;
            display(d3, d2, d1, d0);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void display(int d3, int d2, int d1, int d0 ) {
        byte[] data = new byte[]{REG_DAT, codes[d0], codes[d1], codes[d2], codes[d3]};
        write(data);
    }

    private static void init() {
        try {
            bus = I2CFactory.getInstance(1);
            device = bus.getDevice(0x51); //Default, address is 0x51
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
