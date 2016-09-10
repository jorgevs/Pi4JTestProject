package com.jvs.pi4j.extension;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.LCDBase;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class NewI2CLcdDisplay extends LCDBase implements LCD {

	public static final int PCAF8574_0x20 = 0x20; // 000
	public static final int PCAF8574_0x21 = 0x21; // 001
	public static final int PCAF8574_0x22 = 0x22; // 010
	
	// commands
	private static final int LCD_CLEARDISPLAY = 0x01;
	private static final int LCD_RETURNHOME = 0x02;
	private static final int LCD_ENTRYMODESET = 0x04;
	private static final int LCD_DISPLAYCONTROL = 0x08;
	private static final int LCD_CURSORSHIFT = 0x10;
	private static final int LCD_FUNCTIONSET = 0x20;
	private static final int LCD_SETCGRAMADDR = 0x40;
	private static final int LCD_SETDDRAMADDR = 0x80;

	// flags for display entry mode
	private static final int LCD_ENTRYRIGHT = 0x00;
	private static final int LCD_ENTRYLEFT = 0x02;
	private static final int LCD_ENTRYSHIFTINCREMENT = 0x01;
	private static final int LCD_ENTRYSHIFTDECREMENT = 0x00;

	// flags for display on/off control
	private static final int LCD_DISPLAYON = 0x04;
	private static final int LCD_DISPLAYOFF = 0x00;
	private static final int LCD_CURSORON = 0x02;
	private static final int LCD_CURSOROFF = 0x00;
	private static final int LCD_BLINKON = 0x01;
	private static final int LCD_BLINKOFF = 0x00;

	// flags for display/cursor shift
	private static final int LCD_DISPLAYMOVE = 0x08;
	private static final int LCD_CURSORMOVE = 0x00;
	private static final int LCD_MOVERIGHT = 0x04;
	private static final int LCD_MOVELEFT = 0x00;

	// flags for function set
	private static final int LCD_8BITMODE = 0x10;
	private static final int LCD_4BITMODE = 0x00;
	private static final int LCD_2LINE = 0x08;
	private static final int LCD_1LINE = 0x00;
	private static final int LCD_5x10DOTS = 0x04;
	private static final int LCD_5x8DOTS = 0x00;
	
	// flags for backlight control
	private static final int LCD_BACKLIGHT = 0x08;
	private static final int LCD_NOBACKLIGHT = 0x00;

	private static final int En = 0b00000100; // Enable bit
	private static final int Rw = 0b00000010; // Read/Write bit
	private static final int Rs = 0b00000001; // Register select bit
	
	private I2CBus bus;
	private I2CDevice device;

	protected int rows;
    protected int columns;
    private final int[] LCD_LINE_ADDRESS = { 0x80, 0xC0, 0x94, 0xD4 };
    

	public NewI2CLcdDisplay(int rows, int columns, int busNumber, int address) throws UnsupportedBusNumberException, IOException {		
		this(I2CFactory.getInstance(busNumber), address);
		this.rows = rows;
		this.columns = columns;
	}
	
    public NewI2CLcdDisplay(int busNumber, int address) throws UnsupportedBusNumberException, IOException {
		this(I2CFactory.getInstance(busNumber), address);
		this.rows = 2;
		this.columns = 16;
	}

	public NewI2CLcdDisplay(I2CBus bus, int address) throws IOException {
		// create I2C communications bus instance
		this.bus = bus; // 1
		// create I2C device instance
		device = bus.getDevice(address);
		this.init();
	}

	/**
	 * Initialize the LCD. 
	 * @throws IOException
	 */
	private void init() throws IOException {
		device.write((byte) 0x03);
		device.write((byte) 0x03);
		device.write((byte) 0x03);
		device.write((byte) 0x02);

		device.write((byte) (LCD_FUNCTIONSET | LCD_2LINE | LCD_5x8DOTS | LCD_4BITMODE));
		device.write((byte) (LCD_DISPLAYCONTROL | LCD_DISPLAYON));
		device.write((byte) (LCD_CLEARDISPLAY));
		device.write((byte) (LCD_ENTRYMODESET | LCD_ENTRYLEFT));

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}    
    
	@Override
	public int getRowCount() {
		return this.rows;
	}

	@Override
	public int getColumnCount() {
		return this.columns;
	}

	@Override
	public void setCursorPosition(int row, int column) {
		validateCoordinates(row, column);
		writeCommand((byte)(LCD_LINE_ADDRESS[row] + column));		
	}

	/**
	 * Write a char to the LCD.
	 */
	@Override
	public void write(byte data) {
		try{
			write(data, (byte) 1);
		} catch (Exception ex) {
			Logger.getLogger(NewI2CLcdDisplay.class.getName()).log(Level.SEVERE, null, ex);
		}	
	}
	
	/**
	 * Write a command to the LCD.
	 * @param cmd
	 */
	public void writeCommand(byte cmd) {
		try {
			write(cmd, (byte) 0);
		} catch (Exception ex) {
			Logger.getLogger(NewI2CLcdDisplay.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Write the received value as a Command or Data in the LCD,
	 * depending of the modeRS received as parameter. 
	 * @param value
	 * @param modeRS 0: Command, 1: Data
	 * @throws IOException
	 */
	private void write(byte value, byte modeRS) throws IOException {
		byte higherFourBits = (byte)(modeRS | (value & 0xF0) | LCD_BACKLIGHT);
		device.write(higherFourBits);
		strobe(higherFourBits);
		
		byte lowerFourBits = (byte) (modeRS | ((value << 4) & 0xF0) | LCD_BACKLIGHT);
		device.write(lowerFourBits);
		strobe(lowerFourBits);
	}
	
	/**
	 * Clocks EN to latch command
	 * @param data
	 * @throws IOException
	 */
	public void strobe(byte data) throws IOException {
		device.write((byte) (data | En | LCD_BACKLIGHT));
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		device.write((byte) ((data & ~En) | LCD_BACKLIGHT));
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Turn on/off the Backlight.
	 * @param state
	 * @throws IOException
	 */
	public void backlight(boolean state) throws IOException {
		// for state, true = on, false = off
		if (state) {
			device.write((byte) LCD_BACKLIGHT);
		} else {
			device.write((byte) LCD_NOBACKLIGHT);
		}
	}	

}
