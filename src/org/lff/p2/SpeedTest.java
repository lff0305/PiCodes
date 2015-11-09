package org.lff.p2;

import com.pi4j.io.gpio.*;

/**
 * Created by LFF on 2015/11/7.
 */
public class SpeedTest {

    private static GpioPinDigitalOutput DIO;

    static final GpioController gpio = GpioFactory.getInstance();

    public static void main(String[] argu) {
        init();
        test();
        gpio.shutdown();
    }

    private static void test() {
        long l0 = System.currentTimeMillis();
        int count = 10000;
        for (int i=0;i<count;i++) {
            DIO.high();
            DIO.low();
        }
        long time = (System.currentTimeMillis() - l0);
        System.out.println(time + " used.");

        float per = time / (float)count;

        System.out.println(per + " per toggle, " + (1000 / per) + " per second");
    }

    private static void init() {
        // create gpio controller
        DIO = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "dio", PinState.HIGH);
    }
}
