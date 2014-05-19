package vcluster.configs;
public class Config {

	public static String configFile = "vcluster.conf";
	public static String CONDOR_IPADDR = "150.183.233.59";
	public static final int PORTNUM = 9734;
	public static enum uiType {CMDLINE, REMOTECLIENT, WEBUI};	
	public static uiType currUI;
	
}