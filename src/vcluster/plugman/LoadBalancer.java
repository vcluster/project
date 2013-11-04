package vcluster.plugman;

import java.util.ArrayList;

import org.w3c.dom.Document;

public interface LoadBalancer {

	public ArrayList<String> loadBala(String command,Document xmlDoc);	

}
