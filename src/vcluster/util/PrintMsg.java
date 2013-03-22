package vcluster.util;

import vcluster.global.Config;

public class PrintMsg {
	
	public static void print(DMsgType type, String msg) {
		if (type == DMsgType.ERROR)
			System.out.println(msgtype(type) + msg);
		else if (Config.DEBUG_MODE == true)
			System.out.println(msgtype(type) + msg);
	}
	
	private static String msgtype(DMsgType type) {
		switch (type) {
		case ERROR: return "\t[ERROR]\t: ";
		case INFO: return "\t[INFO]\t: ";
		case MSG: return "\t[MSG]\t: ";
		}
		/* default */
		return "[MSG]\t: ";
	}
	
	public enum DMsgType {ERROR, INFO, MSG};

}
