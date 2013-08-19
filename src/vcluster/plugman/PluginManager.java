package vcluster.plugman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vcluster.ui.Command;

/**
 *  This class is responsible for managing plugins,including loading plugins into the JVM,unloading plugins,maintaining a plugin list.
 * @author kjwon15 modified by Dada Huang
 * 
 */
public class PluginManager {

	public static BatchInterface current_proxyExecutor;
	public static CloudInterface current_cloudExecutor;
	public static final String CLOUD_PLUGIN_DIR = "plugins"+File.separator+"cloud";
	public static final String BATCH_PLUGIN_DIR = "plugins"+File.separator+"batch";	
	
	
	public static Map<String, BatchInterface> loadedBatchPlugins = new HashMap<String, BatchInterface>();
	public static Map<String, CloudInterface> loadedCloudPlugins = new HashMap<String, CloudInterface>();
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
		//System.out.println(path);
		if (this.isLoaded(name)) {
				System.out.println(name + " : has already been loaded!");
				return;
			}
			Class<?> plugin = cl.loadClass(path);
	
			if(Command.TYPE_CLOUD.contains(type)){
				try {							
					//System.out.println("plug-in type : " + );
					CloudInterface ce = (CloudInterface) plugin.newInstance();
					loadedCloudPlugins.put(name, ce);
					current_cloudExecutor = ce;
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				
			}else if(Command.TYPE_BATCH.contains(type)){
						try {							
							//System.out.println("plug-in type : " + );
							loadedBatchPlugins.clear();
							BatchInterface pe = (BatchInterface) plugin.newInstance();
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
			//System.out.println("Plugin \""+name+"\" has been loaded successfully!");
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
	public static List<String> getCloudPluginList(){
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
	
	public static List<String> getBatchPluginList(){
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

	
	public static String getInfo(String pluginName) {
		// TODO Auto-generated method stub
		StringBuffer info =new StringBuffer("");
		String path = "";
		if(getCloudPluginList().contains(pluginName))path=CLOUD_PLUGIN_DIR;
		if(getBatchPluginList().contains(pluginName))path=BATCH_PLUGIN_DIR;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path+File.separator+pluginName+"Readme.txt")));
			String aLine = "";
			while ((aLine = br.readLine()) != null) {				
				System.out.println(aLine);
			}
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Readme file doesn't exist ,please check it on the plugin directory!");
			//e.printStackTrace();
		}
		
		return info.toString();
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
	
	public static boolean isLoaded(String name){
		
		if (loadedBatchPlugins.containsKey(name)||loadedCloudPlugins.containsKey(name)) {
			return true;
		}else{
			return false;
		}
		
	}
	

	

}