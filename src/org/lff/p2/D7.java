package org.lff.p2; /**
 * Created by LFF on 2015/11/1.
 */
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import java.util.HashMap;
import java.util.Map;

/**
 * This example code demonstrates how to perform simple state
 * control of a GPIO pin on the Raspberry Pi.
 *
 * @author Robert Savage
 */
public class D7 {

    private static Map<String, GpioPinDigitalOutput> map = new HashMap();

    private static String[] leds = new String[]{
        "abcdef", // 0
        "bc", // 1
        "abged", // 2
        "abgcd", // 3
        "fgbc", // 4
        "afgcd", // 5
        "afgced", // 6
        "abc", // 7
        "abcdefg", // 8
        "abcdfg", // 9
    };

    public static void main(String[] args) throws InterruptedException {

        System.out.println("<--Pi4J--> GPIO Control Example ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #01 as an output pin and turn on
        final GpioPinDigitalOutput a = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "a", PinState.HIGH);
        final GpioPinDigitalOutput b = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "b", PinState.HIGH);
        final GpioPinDigitalOutput c = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "c", PinState.HIGH);
        final GpioPinDigitalOutput d = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "d", PinState.HIGH);
        final GpioPinDigitalOutput e = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, "e", PinState.HIGH);
        final GpioPinDigitalOutput f = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "f", PinState.HIGH);
        final GpioPinDigitalOutput g = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, "g", PinState.HIGH);

        init(a,b,c,d,e,f,g);

        for (int i=0; ; i++) {
            display(i % 10);
            Thread.sleep(500);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        //gpio.shutdown();
    }

    private static void display(int digit) {
        String led = leds[digit];
        AllLow();
        for (int i=0; i<led.length(); i++) {
            String key = String.valueOf(led.charAt(i));
            GpioPinDigitalOutput pin = map.get(key);
            pin.high();
        }

    }

    private static void AllLow() {
        for (GpioPinDigitalOutput pin : map.values()) {
            pin.low();
        }
    }

    private static void init(GpioPinDigitalOutput ... p) {
        for (GpioPinDigitalOutput pin : p) {
            map.put(pin.getName(), pin);
            pin.low();
        }
    }
}