package ru.rintd.controller.network;

public class NodeInfo {

	public double Temp = 20.0;
	public double density = 0.1;
	public boolean isFire = false;
	/**
	 * 0 -- nan
	 * 1 -- alive
	 * 2 -- not work sensors
	 * 3 -- not signal
	 */
	public int status = 3;
	public int battery = 100;
	
	
}
