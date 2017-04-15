package com.jvs.pi4j.test.i2c.arduino;

import java.io.IOException;

import com.jvs.pi4j.extension.I2CArduinoUno;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.util.Console;

public class I2CArduinoUnoExample {

   public static void main(String[] args) throws InterruptedException, PlatformAlreadyAssignedException, IOException, UnsupportedBusNumberException {

       // create Pi4J console wrapper/helper
       // (This is a utility class to abstract some of the boilerplate code)
       final Console console = new Console();

       // print program title/header
       console.title("<-- The Pi4J Project -->", "I2CArduinoUnoExample");

       // allow for user to exit program using CTRL-C
       console.promptForExit();

       I2CArduinoUno i2cArduinoUno = new I2CArduinoUno(I2CBus.BUS_1, I2CArduinoUno.ARDUINO_UNO_ADDR);

       byte valuetoSend = (byte) 1;
       console.println("Sending value to ArduinoUno through I2C: " + valuetoSend);
       i2cArduinoUno.write(valuetoSend);

       // wait while collects data
       Thread.sleep(500);

       // now performs a I2C READ operation
       console.println("... reading from ArduinoUno through I2C.");
       int data = i2cArduinoUno.read();

       // print the results from I2CArduinoUno
       console.println("Value received = " + String.format("0x%02x", data));

       console.println("Exiting...");

   }
	
}
