package org.lff.p2.p3d;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.lff.p2.ZtLib;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by LFF on 2015/11/12.
 */
public class P3DHeading {

    private static int head = 100;

    private static AtomicBoolean first = new AtomicBoolean(true);
    private static final Lock lock = new ReentrantLock();

    private static long last = 0;

    public static void main(String [] argu) {
        init();
    }

    private static void init() {
        ZtLib.init();
        ZtLib.prepare();
        update();
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput pinClk = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, PinPullResistance.PULL_DOWN);
        final GpioPinDigitalInput pinDt = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        pinClk.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                PinState clk = event.getState();
                PinState dt = pinDt.getState();
                if (first.compareAndSet(true, false)) {
                    System.out.println("---> first, return to ingore.");
                    return;
                }
                System.out.println(clk.toString() + " " + dt.toString() + " " + first);
                if (!clk.equals(dt)) {
                    increase();
                } else {
                    decrease();
                }
            }
        });

        // keep program running until user aborts (CTRL-C)
        for (;;) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static void update() {
        ZtLib.display(head % 360);
    }

    private static void decrease() {
        process(-1);
    }

    private static void process(int increase) {
        long time = System.currentTimeMillis();
        long delta = 0;
        if (last != 0) {
            delta = time - last;
            head += increase* getDelta(delta);
        } else {
            head += increase;
        }
        last = time;
        update();
    }

    private static int getDelta(long delta) {
        if (delta > 100) {
            return 1;
        }
        if (delta > 10) {
            return 5;
        }
        if (delta <= 0) {
            return 10;
        }
        return 1;
    }


    private static void increase() {
        process(1);
    }
}
