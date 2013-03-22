package vcluster.ui;

import java.util.*;

public class KeyCmdMap {

	public KeyCmdMap(int aKey, String cmdString)
	{
		this.key = aKey;
		this.cmdList = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(cmdString, ", ");
		
		while(st.hasMoreTokens()) {
			cmdList.add(st.nextToken());
		}
	}

	public void toPrint()
	{
		System.out.println("Key = " + key);
		for(int i = 0; i < cmdList.size(); i++)
			System.out.println("\t cmd: " + cmdList.get(i));
	}
	
	public boolean contains(String aCmd)
	{
		return cmdList.contains(aCmd); 
	}
	
	private int key;
    private List<String> cmdList;

}
