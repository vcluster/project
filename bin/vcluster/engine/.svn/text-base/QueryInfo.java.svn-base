package vcluster.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import vcluster.ui.Menu;

public class QueryInfo {

	public QueryInfo()
	{
		cmdType = Menu.TYPE_NOT_DEFINED;
	    attrMap = new HashMap<String, String>();
	}

	public void putCmdType(int aCmdType)
	{
		cmdType = aCmdType;
	}
	
	public int getCmdType()
	{
		return cmdType;
	}

	
	public String getAttrValue(String aKeyString)
	{
		return attrMap.get(aKeyString);
	}

	public Set<String> getKeySet()
	{
		return attrMap.keySet();
	}
	
	public void print()
	{
		System.out.println("--------------------------------------");
		System.out.println("Cmd Type: " + cmdType);
		
		for (Map.Entry<String, String> e : attrMap.entrySet())
		    System.out.println(e.getKey() + ": " + e.getValue());


		System.out.println("--------------------------------------");
	}
	
	public void putValue(String keyStr, String valStr)
	{
		attrMap.put(keyStr, valStr);
	}
	
	
    private Map <String, String> attrMap;
    private int cmdType = Menu.TYPE_NOT_DEFINED;
}
