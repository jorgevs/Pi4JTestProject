package com.jvs.pi4j.extension;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jvs.pi4j.component.ArduinoBase;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class I2CArduinoUno extends ArduinoBase {

	// ArduinoUno I2C address
	public static final int ARDUINO_UNO_ADDR = 0x04;

	private I2CBus bus;
	private I2CDevice device;

	public I2CArduinoUno(int busNumber, int address) throws UnsupportedBusNumberException, IOException, InterruptedException {
		this(I2CFactory.getInstance(busNumber), address);
	}

	public I2CArduinoUno(I2CBus bus, int address) throws IOException, InterruptedException {
		// create I2C communications bus instance
		this.bus = bus; // 1
		// create I2C device instance
		device = bus.getDevice(address);
		this.init();
	}

	/**
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void init() throws IOException, InterruptedException {

	}

	@Override
	public void write(byte data) {
		try {
			device.write(data);
		} catch (Exception ex) {
			Logger.getLogger(I2CArduinoUno.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public int read() {
		int value = -1;
		try {
			value = device.read();
		} catch (IOException ex) {
			Logger.getLogger(I2CArduinoUno.class.getName()).log(Level.SEVERE, null, ex);
		}
		return value;
	}

	public AnalogJoystick readAnalogJoystick() throws IOException {
		byte[] buf = new byte[256];
		int res = device.read(0, buf, 0, 3);

		if (res != 3) {
			throw new RuntimeException("Read failure: Got only " + res + " bytes from I2CArduinoUno");
		}

		AnalogJoystick ret = new AnalogJoystick();
		ret.x = asInt(buf[0]);
		ret.y = asInt(buf[1]);
		ret.sw = asInt(buf[2]);
		return ret;
	}

	private int asInt(byte b) {
		int i = b;
		if (i < 0) {
			i = i + 256;
		}
		return i;
	}

	public static class AnalogJoystick {
		public int x;
		public int y;
		public int sw;
	}
}
