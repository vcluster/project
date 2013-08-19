package vcluster.engine.groupexecutor;

import java.io.File;

import vcluster.plugman.PluginManager;

public class BatchExecutor {

	public static boolean ConnectTo(File conf){
		return PluginManager.current_proxyExecutor.ConnectTo(conf);
	}
	public static boolean check_pool(){
		return PluginManager.current_proxyExecutor.check_pool();
	}
	public static boolean check_q(){
		return PluginManager.current_proxyExecutor.check_q();
	}
	public static int getTotalJob(){
		return PluginManager.current_proxyExecutor.getTotalJob();
	}
	public static int getIdleJob(){
		return PluginManager.current_proxyExecutor.getIdleJob();
	}
	public static int getRunningJob(){
		return PluginManager.current_proxyExecutor.getRunningJob();
	}
	public static int getHeldJob(){
		return PluginManager.current_proxyExecutor.getHeldJob();
	}

}
