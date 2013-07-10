package vcluster.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vcluster.engine.groupexecutor.CloudExecutor;
import vcluster.engine.groupexecutor.ProxyExecutor;

/**
 *  This class is responsible for managing plugins,including loading plugins into the JVM,unloading plugins,maintaining a plugin list.
 * @author kjwon15 modified by Dada Huang
 * 
 */
public class PluginManager {

	public static ProxyExecutor current_proxyExecutor;
	public static CloudExecutor current_cloudExecutor;
	public static final String CLOUD_PLUGIN_DIR = "plugins/cloud";
	public static final String BATCH_PLUGIN_DIR = "plugins/batch";	
	public static Map<String, ProxyExecutor> loadedBatchPlugins = new HashMap<String, ProxyExecutor>();
	public static Map<String, CloudExecutor> loadedCloudPlugins = new HashMap<String, CloudExecutor>();
	private CustomClassLoader cl;//An instance of CustomClassLoader,it is responsible for loading classes form jar files.


	public PluginManager() {
		// TODO Auto-generated constructor stub
		cl = new CustomClassLoader();
	}

	/**
	 * Loads a specified plugin(JAR file) into the JVM from the plugin directory. 
	 * @param name JAR file name without the extension ".jar",case sensitive.
	 * @throws ClassNotFoundException
	 */
	public void LoadPlugin(String path,String type) throws ClassNotFoundException {
		 
		File f = new File(path);
		String name = f.getName().replace(".jar", "");		
		
		if (this.isLoaded(name)) {
				System.out.println(name + " : has already been loaded!");
				return;
			}
			Class<?> plugin = cl.loadClass(path);
	
			if(type.equalsIgnoreCase("-c")){
				try {							
					//System.out.println("plug-in type : " + );
					loadedCloudPlugins.put(name, (CloudExecutor) plugin.newInstance());
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				
			}else if(type.equalsIgnoreCase("-b")){
						try {							
							//System.out.println("plug-in type : " + );
							loadedBatchPlugins.clear();
							ProxyExecutor pe = (ProxyExecutor) plugin.newInstance();
							loadedBatchPlugins.put(name, pe);
							current_proxyExecutor = pe;
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

			}
	}

		
	/**
	 * Loads all plugins from the plugin directory.
	 * @see #LoadPlugin(String)
	 */
	public void LoadAllCloudPlugins() {
		File dir = new File(System.getProperty("user.dir") + File.separator
				+ CLOUD_PLUGIN_DIR);
		List<String> list = getCloudPluginList();
		for (String filename : list) {
			try {
				String name = filename.substring(dir.toString().length() + 1,
						filename.indexOf(".")).replace(File.separatorChar, '.');
				LoadPlugin(name,"-c");
			}catch (ClassNotFoundException e) {
				System.err.println(filename
						+ " does not contain valid class data");
			}
		}
	}

	
	/**
	 * Scan the plugins directiory get all the paths of JAR file
	 * @see #LoadAllPlugins() 
	 * @return a list of paths.
	 */
	public List<String> getCloudPluginList(){
		File dir = new File(System.getProperty("user.dir") + File.separator
				+ CLOUD_PLUGIN_DIR);
		File f = new File(dir.getPath());
		//System.out.println(dir.getPath());
		File[] list = f.listFiles();
		List<String> jars = new ArrayList<String>();
			for (File file : list) {				
					if (file.getPath().endsWith(".jar")) {
						jars.add(file.getName().replace(".jar", ""));
					
				}
			}

		return jars;
				
	}
	
	public List<String> getBatchPluginList(){
		File dir = new File(System.getProperty("user.dir") + File.separator
				+ BATCH_PLUGIN_DIR);
		File f = new File(dir.getPath());
		File[] list = f.listFiles();
		List<String> jars = new ArrayList<String>();
		if(list.length>0){
			for (File file : list) {
				
					if (file.getPath().endsWith(".jar")) {
						jars.add(file.getName().replace(".jar", ""));
					
				}
			}
		}
		return jars;
				
	}	
	
	
	

	/**
	 * Unload a specified plugin.remove the plugin form the plugins collection 
	 * @param name a plugin name to be unloaded.
	 * @throws ClassNotFoundException
	 */
	public void UnloadPlugin(String name) throws ClassNotFoundException {
		if (!this.isLoaded(name)) {
			throw new ClassNotFoundException("Plugin didn't loaded: " + name);
		}
		if(loadedBatchPlugins.containsKey(name)){
			loadedBatchPlugins.remove(name);
			current_proxyExecutor = null;
		}else if(loadedCloudPlugins.containsKey(name)){
			if(current_proxyExecutor == loadedCloudPlugins.get(name)){
				current_proxyExecutor = null;
			}
			loadedCloudPlugins.remove(name);
		}
		// and wait for GC runs..
	}

	


	public List<String> GetLoadedCloudPlugins() {
		ArrayList<String> al = new ArrayList<String> ();
		//al.addAll(bcPlugins.keySet());
		al.addAll(loadedCloudPlugins.keySet());
		return al;
	}
	
	public List<String> GetLoadedBatchPlugins() {
		ArrayList<String> al = new ArrayList<String> ();
		al.addAll(loadedBatchPlugins.keySet());
		//al.addAll(cloudPlugins.keySet());
		return al;
	}
	
	public boolean isLoaded(String name){
		
		if (loadedBatchPlugins.containsKey(name)||loadedCloudPlugins.containsKey(name)) {
			return true;
		}else{
			return false;
		}
		
	}
	
	public String getUsage(){
		StringBuffer usage = new StringBuffer();
		usage.append("[USAGE] :plugman"+System.getProperty("line.separator"));
		usage.append("                    -h : list all options and usages"+System.getProperty("line.separator"));
		usage.append("                  list : list all the plugins"+System.getProperty("line.separator"));
		usage.append("                        -c : list all cloud plugins "+System.getProperty("line.separator"));		
		usage.append("                        -b : list all batch plugins "+System.getProperty("line.separator"));		
		usage.append("                        -l : list all loaded plugins "+System.getProperty("line.separator"));
		usage.append("                  load : "+System.getProperty("line.separator"));	
		usage.append("                        -c pluginname-1,pluginname-2......,pluginname-n : load n cloud plugins "+System.getProperty("line.separator"));
		usage.append("                        -b pluginname : load a batch plugin "+System.getProperty("line.separator"));	
		usage.append("                unload : unload plugins"+System.getProperty("line.separator"));	
		usage.append("                  info : prints the information about a plugin "+System.getProperty("line.separator"));	
		return usage.toString();
		
	}
	

}