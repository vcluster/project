package vcluster.ui.remote;

import java.rmi.server.*; 
import java.io.*; 
import java.net.*; 

import vcluster.Vcluster;


public class MyRMISocket extends RMISocketFactory 
{ 
	public Socket createSocket(String host, int port) throws IOException
	{ 
		return new Socket(host,port); 
	} 
	
	public ServerSocket createServerSocket(int port) throws IOException 
	{ 
		 if (port == 0) 
		    port = Vcluster.SERVER_PORT2;  
		return new ServerSocket(port); 
	}
	
} 