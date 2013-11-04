package vcluster.engine.groupexecutor;

import java.util.StringTokenizer;

import vcluster.control.cloudman.CloudManager;

public class CloudmanExecutor {


	public static boolean register(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		String token = null;
		if (!st.hasMoreTokens()) {
			System.out.println("[USAGE] : cloudman register <cloudelement conf file>");
			return false;
		}
		token = st.nextToken().trim();
		//System.out.println(token);
		return CloudManager.loadCloudElments(token);

	}

	public static boolean load(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		String cmd = st.nextToken();
		if (!st.hasMoreTokens()) {
			System.out.println("[ERROR : ] Expect a cloud name!");
			return false;
		}
		else{			
			String [] arg = cmdLine.replace(cmd, "").trim().split(" ");	
			
			return CloudManager.deploy(arg);
		}
	}

	public static boolean list(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		String token = null;
		st.nextToken();
		if (st.hasMoreTokens()) {
			token = st.nextToken().trim();
			
			if(token.equalsIgnoreCase("private") || token.equalsIgnoreCase("public"))
				CloudManager.dump(token);
			else {
				CloudManager.dump();
				return true;
			}
			
		} else {
			CloudManager.dump();
		}
		return true;
	}

	public static boolean undefined(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		/* skip the command */
		st.nextToken();

		if (!st.hasMoreTokens()) {
			System.out.println("[USAGE] : cloudman dump [<private | public>>]");
			System.out.println("        : cloudman register <cloudelement conf file>");
			System.out.println("        : cloudman set <private | public> <cloud num>");			
			return false;
		}		
		return true;
	}

}
