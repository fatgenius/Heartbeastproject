package pi4led.controller;

import com.pi4j.io.gpio.*;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Gebruiker on 22/03/2017.
 */
public class SensorController {

    private  static GpioPinDigitalOutput pin;
    @RequestMapping("/")
    public String greeting(){
        return "hello,world";
    }

    @RequestMapping("/run")
    public String light() {
        if (pin == null) {
            GpioController gpio = GpioFactory.getInstance();
            GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MySensor", PinState.LOW);
        }
        pin.toggle();
        return "ok";
    }

}
