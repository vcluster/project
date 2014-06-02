package vcluster.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import vcluster.Vcluster;
import vcluster.Vcluster.uiType;
import vcluster.executors.CmdCataloger;



/**
 * This is the main class of vcluster, the main function is involved in this class.
 * @author S.Y.Noh
 * @version 1.0
 *
 */
public class UIMain {


	public UIMain()
	{
		//VCluster.init();
		//if(!Config.configFile.isEmpty())
		//	Util.loadConfig(Config.configFile);
		Vcluster.currUI = Vcluster.uiType.CMDLINE;
	}
	

	/**
	 * Judge if the input command line is empty or not.Space character is regarded as empty. 
	 * @return boolean type.	 
	 */
	private boolean isEmpty(String aCmd)
	{
		if (aCmd == null) return true;
		
		String cmd = aCmd.replaceAll(" ", "");
		return cmd.isEmpty();
	}

	/**
	 * Generate "vcluster" prompt, get the input command line and send it to the executor.
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
	    	//userCmd+=" --cmdLine";
	    }
	    catch(Exception e){return true;}

	    /* check emptiness, then just no more action */
	    if (isEmpty(userCmd))
	    	return true;

	    /* extract the command from the command string */
	    CmdSet command = new CmdSet(userCmd);
	    command.setUi(uiType.CMDLINE);
	    /* if quit, then forcedly quit */
	    if (command.getCmd() == Command.QUIT) {
	    	
	    	/* forcedly exit */
	    	System.exit(0);
	    }
	    
	    /*
	     * if command is not defined, no more action
	     */
	    if (command.getCmd() == Command.NOT_DEFINED) {
	    	System.out.println("command is not supported!");
	    	return true;
	    }
	    
	    /* execution here */
	    CmdCataloger.execute(command);

	    return true;
	    
	}

	
	/**
	 *This is the main function, loops the promptGen function, keep staying at the "vcluster " prompt line, wait for inputing the command. 
	 * 
	 */
	public static void main(String[] args) throws Exception {
		UIMain uimain = new UIMain();

		boolean more = false;
		do {
			more = uimain.promptGen();
		}while (more == true);
	}

}
