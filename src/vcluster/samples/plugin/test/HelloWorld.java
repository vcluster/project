package plugin.test;

import vcluster.engine.groupexecutor.ProxyExecutor;


public class HelloWorld implements ProxyExecutor {

	

	public boolean check_pool() {
		// TODO Auto-generated method stub
		TestPrint.testPrint();
		System.out.println("****** CHECK POOL ******\n");
		return false;
	}

	public boolean check_q() {
		// TODO Auto-generated method stub
		System.out.println("****** CHECK Q    ******\n");
		return false;
	}

	@Override
	public String toString() {
		return "haha : HelloWorld []";
	}

	public boolean condor(String cmdLine) {
		// TODO Auto-generated method stub
		if(cmdLine.equalsIgnoreCase("condor_q")){
			System.out.println("****** CONDOR_Q ******\n");
		}else if(cmdLine.equalsIgnoreCase("condor_status")){
			System.out.println("****** CONDOR_STATUS ******\n");
		}
		return false;
	}

	public boolean onevm(String cmdLine) {
		// TODO Auto-generated method stub
		System.out.println("helloworld : onevmlist\n");
		return false;
	}

	
	@Override
	public int getTotalJob() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIdleJob() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRunningJob() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeldJob() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRatio() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return "This is a test plugin";
	}

}
