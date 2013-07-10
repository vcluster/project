package vcluster.engine.groupexecutor;

import vcluster.control.cloudman.CloudElement;

public interface CloudExecutor {

	public boolean run_instance(final CloudElement cloud);
	public boolean describe_instance(CloudElement cloud, String cmdLine);
	public boolean terminate_instance(CloudElement cloud, String cmdLine);
	public boolean start_instance(CloudElement cloud, String cmdLine);
	public boolean stop_instance(CloudElement cloud, String cmdLine);
	public boolean describe_image(CloudElement cloud, String cmdLine);
	public boolean rest_launch(CloudElement cloud);
	public String getInfo();
	
}
