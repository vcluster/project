package vcluster.ui.remote;

import java.rmi.server.*; 
import java.io.*; 
import java.net.*; 
public class MyRMISocket extends RMISocketFactory 
{ 
	public Socket createSocket(String host, int port) throws IOException
	{ 
		return new Socket(host,port); 
	} 
	
	public ServerSocket createServerSocket(int port) throws IOException 
	{ 
		 if (port == 0) 
		    port = 5099;  
		return new ServerSocket(port); 
	}
	
} 