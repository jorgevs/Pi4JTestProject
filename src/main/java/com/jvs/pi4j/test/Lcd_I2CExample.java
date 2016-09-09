package com.jvs.pi4j.test;

import java.io.IOException;

import com.jvs.pi4j.extension.GpioLcdI2CDisplay;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class Lcd_I2CExample {

	public static void main(String args[]) throws UnsupportedBusNumberException, IOException, InterruptedException {
		
		GpioLcdI2CDisplay lcd = new GpioLcdI2CDisplay(I2CBus.BUS_1, GpioLcdI2CDisplay.PCAF8574_0x20);
		System.out.println("<--Pi4J--> GPIO 4 bit LCD example program");
		
		lcd.backlight(true);
		
		lcd.clear();
		
		lcd.writeChar((byte) 'c');
		lcd.writeChar((byte) 'a');
		lcd.writeChar((byte) 's');
		lcd.writeChar((byte) 'a');
		lcd.writeChar((byte) 's');
		
		Thread.sleep(1000);
		
		lcd.clear();
		lcd.printString("Hello world mundo mundial", 1, 2);
		
		Thread.sleep(1000);
		
		lcd.printString("Hello world mundo mundial", 0);
		
		Thread.sleep(1000);
		
		lcd.printString("Hello world mundo mundial y que lo sepa todo el mundo!!!");
		
		Thread.sleep(1000);
		
		
		// create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
		
	}
	
	
}
