package com.jvs.pi4j.test.i2c.arduino;

import com.jvs.pi4j.extension.I2CArduinoUno;
import com.jvs.pi4j.extension.I2CArduinoUno.AnalogJoystick;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.util.Console;
import java.io.IOException;

public class I2CArduinoUnoJoystickExample {

	public static void main(String[] args) throws UnsupportedBusNumberException, IOException, InterruptedException {

		// create Pi4J console wrapper/helper
		// (This is a utility class to abstract some of the boilerplate code)
		final Console console = new Console();

		// print program title/header
		console.title("<-- The Pi4J Project -->", "I2CArduinoUnoExample");

		// allow for user to exit program using CTRL-C
		console.promptForExit();

		I2CArduinoUno i2cArduinoUno = new I2CArduinoUno(I2CBus.BUS_1, I2CArduinoUno.ARDUINO_UNO_ADDR);

		int i = 0;
		AnalogJoystick analogJoystick;

		do {
			analogJoystick = i2cArduinoUno.readAnalogJoystick();

			console.println("\tValue X received = " + analogJoystick.x);
			console.println("\tValue Y received = " + analogJoystick.y);
			console.println("\tValue SW received = " + analogJoystick.sw);
			i++;
			Thread.sleep(100);
		} while (i < 200);

		console.println("Exiting...");
	}

}
