package vcluster.ui;

import java.io.*;

import vcluster.global.Config;
import vcluster.global.VCluster;
import vcluster.util.PrintMsg;
import vcluster.util.Util;
import vcluster.util.PrintMsg.DMsgType;

public class UIMain {


	public UIMain()
	{
		VCluster.init();
		if(!Config.configFile.isEmpty())
			Util.loadConfig(Config.configFile);
	}
	

	
	private boolean isEmpty(String aCmd)
	{
		if (aCmd == null) return true;
		
		String cmd = aCmd.replaceAll(" ", "");
		return cmd.isEmpty();
	}

	/**
	 * generate "vcluster" prompt
	 * 
	 * @return true if it has to be continue; otherwise false
	 */
	private  boolean promptGen() {
		
	    String userCmd = "";

	    /* prompt */
	    System.out.print("vcluter > ");
		
	    InputStreamReader input = new InputStreamReader(System.in);
	    BufferedReader reader = new BufferedReader(input);
	    
	    try {
		    /* get a command string */
	    	userCmd = reader.readLine(); 
	    }
	    catch(Exception e){return true;}

	    /* check emptiness, then just no more action */
	    if (isEmpty(userCmd))
	    	return true;

	    /* extract the command from the command string */
	    Command command = CmdExecutor.getCommand(userCmd);

	    /* if quit, then forcedly quit */
	    if (command == Command.QUIT) {
	    	CmdExecutor.quit();
	    	
	    	/* forcedly exit */
	    	System.exit(0);
	    }
	    
	    /*
	     * if command is not defined, no more action
	     */
	    if (command == Command.NOT_DEFINED) {
	    	PrintMsg.print(DMsgType.ERROR, "command is not supported!");
	    	return true;
	    }
	    
	    /* execution here */
	    CmdExecutor.execute(userCmd);

	    return true;
	    
	}

	public static void main(String[] args) throws Exception {

		UIMain uimain = new UIMain();

		boolean more = false;
		do {
			more = uimain.promptGen();
		}while (more == true);
	}

}
