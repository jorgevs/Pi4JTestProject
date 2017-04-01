package com.jvs.pi4j.test;

import java.io.IOException;

import com.jvs.pi4j.extension.NewI2CLcdDisplay;
import com.pi4j.component.lcd.LCDTextAlignment;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class I2CLcdExample {
	
    public final static int LCD_ROWS = 2;
    public final static int LCD_ROW_1 = 0;
    public final static int LCD_ROW_2 = 1;
    public final static int LCD_COLUMNS = 16;
	
	public static void main(String args[]) throws UnsupportedBusNumberException, IOException, InterruptedException {
		System.out.println("<--Pi4J--> I2C LCD Display example program");
		NewI2CLcdDisplay lcd = new NewI2CLcdDisplay(LCD_ROWS, LCD_COLUMNS, I2CBus.BUS_1, NewI2CLcdDisplay.PCA8574_0x20);
		
		lcd.backlight(true);
		lcd.clear();
		
		lcd.setCursorHome();
		
		lcd.write("c");
		lcd.write("a");
		lcd.write("s");
		lcd.write("a");
		lcd.write("s");
		
		Thread.sleep(1000);
		lcd.clear();
		lcd.write(LCD_ROW_1, 2, "Hello world mundo mundial");
		Thread.sleep(1000);
		lcd.backlight(false);
		Thread.sleep(1000);
		lcd.backlight(true);
		Thread.sleep(1000);
		lcd.write(0, "Hello world mundo mundial");
		Thread.sleep(1000);
		lcd.write("Hello world mundo mundial y que lo sepa todo el mundo!!!");
		Thread.sleep(1000);
		
    	// clear LCD
        lcd.clear();
        Thread.sleep(1000);

        // write line 1 to LCD
        lcd.write(LCD_ROW_1, "The Pi4J Project");

        // write line 2 to LCD
        lcd.write(LCD_ROW_2, "----------------");

        // line data replacement
        for(int index = 0; index < 5; index++)
        {
            lcd.write(LCD_ROW_2, "----------------");
            Thread.sleep(500);
            lcd.write(LCD_ROW_2, "****************");
            Thread.sleep(500);
        }
        lcd.write(LCD_ROW_2, "----------------");

        // single character data replacement
        for(int index = 0; index < lcd.getColumnCount(); index++) {
            lcd.write(LCD_ROW_2, index, ">");
            if(index > 0)
                lcd.write(LCD_ROW_2, index - 1, "-");
            Thread.sleep(300);
        }
        for(int index = lcd.getColumnCount()-1; index >= 0 ; index--) {
            lcd.write(LCD_ROW_2, index, "<");
            if(index < lcd.getColumnCount()-1)
                lcd.write(LCD_ROW_2, index + 1, "-");
            Thread.sleep(300);
        }

        // left alignment, full line data
        lcd.write(LCD_ROW_2, "----------------");
        Thread.sleep(500);
        lcd.write(LCD_ROW_2, "<< LEFT");
        Thread.sleep(1000);

        // right alignment, full line data
        lcd.write(LCD_ROW_2, "----------------");
        Thread.sleep(500);
        lcd.writeln(LCD_ROW_2, "RIGHT >>", LCDTextAlignment.ALIGN_RIGHT);
        Thread.sleep(1000);

        // center alignment, full line data
        lcd.write(LCD_ROW_2, "----------------");
        Thread.sleep(500);
        lcd.writeln(LCD_ROW_2, "<< CENTER >>", LCDTextAlignment.ALIGN_CENTER);
        Thread.sleep(1000);

        // mixed alignments, partial line data
        lcd.write(LCD_ROW_2, "----------------");
        Thread.sleep(500);
        lcd.write(LCD_ROW_2, "<L>", LCDTextAlignment.ALIGN_LEFT);
        lcd.write(LCD_ROW_2, "<R>", LCDTextAlignment.ALIGN_RIGHT);
        lcd.write(LCD_ROW_2, "CC", LCDTextAlignment.ALIGN_CENTER);
        Thread.sleep(3000);
		
	}
	
	
}
