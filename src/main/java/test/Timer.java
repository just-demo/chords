package test;

import java.util.Date;

public class Timer {
	private static long startTime;
	public static void start(){
		startTime = timeStamp();
	}
	
	private static long timeStamp(){
		return (new Date()).getTime();
	}
	
	public static double time(){
		return (timeStamp() - startTime) / 1000d;
	}
}
