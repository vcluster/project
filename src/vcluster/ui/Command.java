package vcluster.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
/**
 * @author S.Y. Noh
 * This is the enum of commands. all the vcluster command is listed here
 * 
 */
public enum Command {

	/** Commands of vclman Category*/
	
	VHELP(CMD_GROUP.VCLMAN,"-h,--help,help"),
	QUIT (CMD_GROUP.VCLMAN, "quit, exit"),
	CHECK_P (CMD_GROUP.VCLMAN, "chkp,check_p"),
	CHECK_Q (CMD_GROUP.VCLMAN, "chkq,check_q"),
	SERVER_MODE(CMD_GROUP.VCLMAN,"servermode"),
	LOADBALANCERSTART(CMD_GROUP.VCLMAN,"loadbalancer"),
	
	
	
	/*Commands of cloudman Category*/
	REGISTER(CMD_GROUP.CLOUDMAN,"register,rgst"),
	LISTCLOUD(CMD_GROUP.CLOUDMAN,"list,ls"),
	LOADCLOUD(CMD_GROUP.CLOUDMAN,"load,ld"),
	UNLOADCLOUD(CMD_GROUP.CLOUDMAN,"unload,unld"),	
	HOSTON(CMD_GROUP.CLOUDMAN,"hoston"),
	HOSTOFF(CMD_GROUP.CLOUDMAN,"hostoff"),
	
	/*Commands of vmman Category*/
	SHOW(CMD_GROUP.VMMAN,"show,sh"),
	CREATE (CMD_GROUP.VMMAN, "create,crt"),
	START (CMD_GROUP.VMMAN, "start,st"),
	SUSPEND (CMD_GROUP.VMMAN, "suspend,ssp"),
	LISTVM (CMD_GROUP.VMMAN, "list,ls"),
	DESTROY (CMD_GROUP.VMMAN, "destroy,dt"),
	MIGRATE(CMD_GROUP.VMMAN,"migrate,mig"),
	
	
	/*Commands of plugman Category*/
	LOAD (CMD_GROUP.PLUGMAN, "load,ld"),
	UNLOAD (CMD_GROUP.PLUGMAN, "unload,uld"),
	LIST (CMD_GROUP.PLUGMAN, "list,ls"),
	INFO (CMD_GROUP.PLUGMAN, "info,ifo"),
	
		

	NOT_DEFINED (CMD_GROUP.NOT_DEFINED, "not_defined");


	private String command;
	private CMD_GROUP cmdGroup;
	private List<String> cmdList;

	/**
	 *Constructor of command, specify the command name and group
	 *@param group specify the command group
	 *@param cmdString is the command line
	 *
	 */
	Command(CMD_GROUP group, String cmdString) {

		cmdGroup = group;
		this.cmdList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(cmdString, ", ");
		
		boolean firstToken = true;
		String token = null;
		
		while(st.hasMoreTokens()) {
			token = st.nextToken();
			if (firstToken) {
				command = token;
				firstToken = false;
			}
			cmdList.add(token);
		}
	}
	
	public CMD_GROUP getCmdGroup() 
	{
		return cmdGroup;
	}
	
	public void toPrint()
	{
		System.out.println("Key = " + command);
		for(int i = 0; i < cmdList.size(); i++)
			System.out.println("\t cmd: " + cmdList.get(i));
	}
	
	public boolean contains(String aCmd)
	{
		return cmdList.contains(aCmd); 
	}
	
	
	public String getCommand()
	{
		return command;
	}
	
	public static Command getCommand(CMD_GROUP cmdGroup, String aCmdLine) 
	{
		StringTokenizer st = new StringTokenizer(aCmdLine);
		String aCmd = st.nextToken().trim();
    	if (cmdGroup==null){
            for (Command cmd : Command.values()){
            	if (cmd.contains(aCmd)) return cmd;
            }
    	}
        for (Command cmd : Command.values()){
        	if (cmd.getCmdGroup().equals(cmdGroup)&cmd.contains(aCmd)) return cmd;
        }
        return Command.NOT_DEFINED;
 	}
	
	/**
	 *This is enum of CMD_GROUP, all the command groups are defined here.  
	 *
	 */
	public enum CMD_GROUP {
		VCLMAN,
		VMMAN,
		CLOUDMAN,
		PLUGMAN,		
		NOT_DEFINED;
		
		
	}
	
}
