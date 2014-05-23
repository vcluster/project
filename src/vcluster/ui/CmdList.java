package vcluster.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
/**
 * @author S.Y. Noh
 * This is the enum of commands. all the vcluster command is listed here
 * 
 */
public enum CmdList {

	/* Commands of vclman Category*/
	
	VHELP(CMD_GROUP.VCLMAN,"-h,--help,help"),
	QUIT (CMD_GROUP.VCLMAN, "quit, exit, done, stop"),
	LOADCONF (CMD_GROUP.VCLMAN, "loadf"),
	CHECK_P (CMD_GROUP.VCLMAN, "chkp,check_p"),
	CHECK_Q (CMD_GROUP.VCLMAN, "chkq,check_q"),
	PRINTARC (CMD_GROUP.VCLMAN, "print,prac"),
	TESTALGO(CMD_GROUP.VCLMAN, "algo"),
	TESTCHKQ(CMD_GROUP.VCLMAN, "tchkq"),
	TESTDEMO(CMD_GROUP.VCLMAN,"start blancer"),
	
	
	/*Commands of cloudman Category*/
	CLOUDMAN(CMD_GROUP.CLOUDMAN,"cloudman,cm"),
	REGISTER(CMD_GROUP.CLOUDMAN,"register,rgst"),
	LISTCLOUD(CMD_GROUP.CLOUDMAN,"list,ls"),
	LOADCLOUD(CMD_GROUP.CLOUDMAN,"load,ld"),
	UNLOADCLOUD(CMD_GROUP.CLOUDMAN,"unload,unld"),	
	HOSTON(CMD_GROUP.CLOUDMAN,"hoston"),
	HOSTOFF(CMD_GROUP.CLOUDMAN,"hostoff"),
	
	/*Commands of vmman Category*/
	VMMAN (CMD_GROUP.VMMAN, "vmman, vm"),
	SHOW(CMD_GROUP.VMMAN,"show,sh"),
	CREATE (CMD_GROUP.VMMAN, "create,crt"),
	START (CMD_GROUP.VMMAN, "start,st"),
	SUSPEND (CMD_GROUP.VMMAN, "suspend,ssp"),
	LISTVM (CMD_GROUP.VMMAN, "list,ls"),
	DESTROY (CMD_GROUP.VMMAN, "destroy,dt"),
	MIGRATE(CMD_GROUP.VMMAN,"migrate,mig"),
	
	
	/*Commands of plugman Category*/
	PLUGMAN(CMD_GROUP.PLUGMAN,"plugman,pm"),
	LOAD (CMD_GROUP.PLUGMAN, "load,ld"),
	UNLOAD (CMD_GROUP.PLUGMAN, "unload,uld"),
	LIST (CMD_GROUP.PLUGMAN, "list,ls"),
	INFO (CMD_GROUP.PLUGMAN, "info,ifo"),
	
		

	NOT_DEFINED (CMD_GROUP.NOT_DEFINED, "not_defined");

	/**
	 * 
	 */
	private String command;
	private CMD_GROUP cmdGroup;
	private List<String> cmdList;

	/**
	 *Constructor of command, specify the command name and group
	 *@param group specify the command group
	 *@param cmdString is the command line
	 *
	 */
	CmdList(CMD_GROUP group, String cmdString) {

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
		vcluster.util.Util.print("Key = " + command);
		for(int i = 0; i < cmdList.size(); i++)
			vcluster.util.Util.print("\t cmd: " + cmdList.get(i));
	}
	
	public boolean contains(String aCmd)
	{
		return cmdList.contains(aCmd); 
	}
	
	
	public String getCommand()
	{
		return command;
	}
	
	public static CmdList getCommand(String cmdGroup, String aCmdLine) 
	{
		StringTokenizer st = new StringTokenizer(aCmdLine);
		String aCmd = st.nextToken().trim();
    	if (cmdGroup==null){
            for (CmdList cmd : CmdList.values()){
            	if (cmd.contains(aCmd)) return cmd;
            }
    	}
        for (CmdList cmd : CmdList.values()){
        	if (cmd.getCmdGroup().toString().equalsIgnoreCase(cmdGroup)&cmd.contains(aCmd)) return cmd;
        }
        return CmdList.NOT_DEFINED;
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