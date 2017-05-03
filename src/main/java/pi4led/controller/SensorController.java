package pi4led.controller;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pi4j.wiringpi.Gpio.analogRead;

/**
 * Created by Gebruiker on 22/03/2017.
 */
@RestController
public class SensorController {

    private  static GpioPinDigitalOutput pin;
    @RequestMapping("/")
    public String greeting(){
        return "hello,world";
    }//check run on rasp

    @RequestMapping("/toggle")
    public String toggle() {
        getPin().toggle();
        value();
        //Gpio.digitalRead(4);
        return "ok";
    }

    private GpioPinDigitalOutput getPin(){
        if (pin == null) {
            GpioController gpio = GpioFactory.getInstance();
            pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MySensor", PinState.LOW);
        }
        return  pin;
    }
    @RequestMapping("/value")
    public double value(){

      //  int ledPin = 13; // This pin is connected on the Arduino board to an LED, if you want to blink this LED
        // to indicate a pulse is being detected, defining it here is a good idea, however
        // in the code that follows, this is never done, so this line could be deleted.
       // int sensorPin = 0; // This is the analog sensor pin the backwards S pin is connected to
        // you can use any of the analog pins, but would need to change this to match
        double alpha = 0.75; // This code uses a rather cool way of averaging the values, using 75% of the
        // average of the previous values and 25% of the current value.
        int period = 20; // This is how long the code delays in milliseconds between readings (20 mSec)

        double change = 0.0; // My guess is that this was going to be used to detect the peaks, but the
        // code never does... you can delete this line as well.

        double oldValue = 0; // used for averaging.
        double oldChange = 0; // not currently used
        int rawValue = analogRead (0); // This reads in the value from the analog pin.
        // this is a 10 bit number, and will be between 0 and 1023
        // If this value doesn't change, you've connected up
        // something wrong


        double value = alpha * oldValue + (1 - alpha) * rawValue; // Calculate an average using 75% of the
        // previous value and 25null% of the new


        System.out.println(value);  // Send out the average value and a new line
        oldValue = value;        // Save the average for next iteration
        Gpio.delay (20);            // Wait 20 mSec
        return value;
    }

}
