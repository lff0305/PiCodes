package org.lff.p2;

import com.pi4j.component.light.LED;
import com.pi4j.io.gpio.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LFF on 2015/11/4.
 */
public class H755 {

    private static String LED_0 = "10010000";
    private static String LED_1 = "11111001";
    private static String LED_2 = "10100100";
    private static String LED_3 = "10110000";
    private static String LED_4 = "10011001";
    private static String LED_5 = "10010010";
    private static String LED_6 = "10000010";
    private static String LED_7 = "11111000";
    private static String LED_8 = "10000000";
    private static String LED_9 = "10010000";

    private static Map<Integer, String> map = new HashMap<Integer, String>();

    static {
        map.put(0, LED_0);
        map.put(1, LED_1);
        map.put(2, LED_2);
        map.put(3, LED_3);
        map.put(4, LED_4);
        map.put(5, LED_5);
        map.put(6, LED_6);
        map.put(7, LED_7);
        map.put(8, LED_8);
        map.put(9, LED_9);
    }



    private static GpioPinDigitalOutput DIO;
    private static GpioPinDigitalOutput RCLK;
    private static GpioPinDigitalOutput SCLK;

    public static void main(String[] argu) {
        init();
        long l = System.currentTimeMillis();
        byte d3 = fromString(map.get(1));
        byte d2 = fromString(map.get(2));
        byte d1 = fromString(map.get(3));
        byte d0 = fromString(map.get(4));

        int count = 0;
        while(true) {
            //;
            display(d3, d2, d1, d0);
            count++;
            if (count % 100 == 0) {
                System.out.println(count + " in " + (System.currentTimeMillis() - l) + " ms.");
                l = System.currentTimeMillis();
            }
        }
    }

    private static void display(byte d3, byte d2, byte d1, byte d0) {
        LED_OUT(d3);
        LED_OUT((byte) 1);
        RCLK.low();
        RCLK.high();
        sleep(2);
        LED_OUT(d2);
        LED_OUT((byte)2);
        RCLK.low();
        RCLK.high();
        sleep(2);
        LED_OUT(d1);
        LED_OUT((byte)4);
        RCLK.low();
        RCLK.high();
        sleep(2);
        LED_OUT(d0);
        LED_OUT((byte)8);
        RCLK.low();
        RCLK.high();
        sleep(2);
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    private static void LED_OUT(byte X)
    {
        byte i;
        for(i=8;i>=1;i--)
        {
            if ((X & 0x80) != 0)
            {
                DIO.high();
            }
            else
            {
                DIO.low();
            }
            X<<=1;
            SCLK.low();
            SCLK.high();
        }
    }

    private static byte fromString(String bin) {
        int x = (Integer.parseInt(bin, 2));
        byte X = (byte)(x & 0xFF);
        return X;
    }

    private static void init() {

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        DIO = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "dio", PinState.HIGH);
        RCLK = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "rclk", PinState.HIGH);
        SCLK = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "sclk", PinState.HIGH);
    }
}
