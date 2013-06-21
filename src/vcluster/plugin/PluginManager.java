package vcluster.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import vcluster.engine.groupexecutor.CloudExecutor;
import vcluster.engine.groupexecutor.ProxyExecutor;

/**
 *  This class is responsible for managing plugins,including loading plugins into the JVM,unloading plugins,maintaining a plugin list.
 * @author kjwon15 modified by Dada Huang
 * 
 */
public class PluginManager {
	/**
	 * A collection to store all the loaded plugin classes,indexed by the class name.
	 * */
	public static Map<String, ProxyExecutor> bcPlugins;
	public static Map<String, CloudExecutor> cloudPlugins;
	/**
	 * An instance of CustomClassLoader,it is responsible for loading classes form jar files.
	 */
	private CustomClassLoader cl;
	
	/**
	 * Directory where the plugin jar files are located.
	 * */
	private String pluginsdir;

	/**
	 * Plugin management class constructor.Initialize the collection of plugins,the plugins directory and the CustomClassLoader.
	 * @param dir A directory where plugins are located.
	 */
	public PluginManager(String dir) {
		bcPlugins = new HashMap<String, ProxyExecutor>();
		cloudPlugins = new HashMap<String,CloudExecutor>();
		File f = new File(System.getProperty("user.dir"), dir);
		cl = new CustomClassLoader(f);
		this.pluginsdir = dir;
	}
/**
 * Constructor with no parameter,initialize the pluginsdir as "plugins",which is the default plugins directory.
 * @see #PluginManager(String). 
 * */
	public PluginManager() {
		// TODO Auto-generated constructor stub
		this("plugins");
	}

	/**
	 * Loads a specified plugin(JAR file) into the JVM from the plugin directory. 
	 * @param name JAR file name without the extension ".jar",case sensitive.
	 * @throws ClassNotFoundException
	 */
	public void LoadPlugin(String name) throws ClassNotFoundException {
		
		
		if (bcPlugins.containsKey(name)||cloudPlugins.containsKey(name)) {
				return;
			}
			Class<?> plugin = cl.loadClass(name);
	
			if(name.startsWith("cloud")){
				try {							
					//System.out.println("plug-in type : " + );
					cloudPlugins.put(name, (CloudExecutor) plugin.newInstance());
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				
			}else if(name.startsWith("batch")){
						try {							
							//System.out.println("plug-in type : " + );
							bcPlugins.put(name, (ProxyExecutor) plugin.newInstance());
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

			}
	}

	public void LoadPlugin(String [] name){
		
		for(String pluginName:name){
			try {
				LoadPlugin(pluginName);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println(pluginName
						+ " does not contain valid class data");
			}
			
		}				
		
		
	}
	
	/**
	 * Loads all plugins from the plugin directory.
	 * @see #LoadPlugin(String)
	 */
	public void LoadAllPlugins() {
		File dir = new File(System.getProperty("user.dir") + File.separator
				+ pluginsdir);
		List<String> list = walk(dir.getPath());
		for (String filename : list) {
			try {
				String name = filename.substring(dir.toString().length() + 1,
						filename.indexOf(".")).replace(File.separatorChar, '.');
				LoadPlugin(name);
			}catch (ClassNotFoundException e) {
				System.err.println(filename
						+ " does not contain valid class data");
			}
		}
	}
	/**
	 * Get a plugins list of  all plugins in the directory,but does not load them.
	 * @return List<String>
	 */
	public List<String> getList(){
		File dir = new File(System.getProperty("user.dir") + File.separator	+ pluginsdir);
		File f = new File(dir.getPath());
		File[] list = f.listFiles();
		List<String> jars = new ArrayList<String>();
		
			try {
				for (File file : list) {			
						if (file.getPath().endsWith(".jar")) {
							jars.add(file.getName().replace(".jar", ""));
						
					}
				}
				return jars;
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				System.out.println("There is no plugins!");
				return null;
			}
		
	}
	
	
	/**
	 * Scan the plugins directiory get all the paths of JAR file
	 * @see #LoadAllPlugins() 
	 * @param path a path to be explore.
	 * @return a list of paths.
	 */
	public List<String> walk(String path) {
		File f = new File(path);
		File[] list = f.listFiles();
		List<String> classes = new ArrayList<String>();
		for (File file : list) {
			if (file.isDirectory() == true) {
				classes.addAll(walk(file.toString()));
			} else {
				if (file.getPath().endsWith(".jar")) {
					classes.add(file.toString());
				}
			}
		}
		return classes;
	}

	/**
	 * Unload a specified plugin.remove the plugin form the plugins collection 
	 * @param name a plugin name to be unloaded.
	 * @throws ClassNotFoundException
	 */
	public void UnloadPlugin(String name) throws ClassNotFoundException {
		if (!bcPlugins.containsKey(name)&&!cloudPlugins.containsKey(name)) {
			throw new ClassNotFoundException("Plugin didn't loaded: " + name);
		}
		if(bcPlugins.containsKey(name)){
			bcPlugins.remove(name);
		}else if(cloudPlugins.containsKey(name)){
			cloudPlugins.remove(name);
		}
		// and wait for GC runs..
	}

	/**
	 * Unload all loaded plugins, and invoke the function clear() to empty the plugins collection,
	 * all the plugin objects will be collected by the System.gc()
	 * @see #UnloadPlugin(String)
	 * @throws ClassNotFoundException
	 */
	public void UnloadAllPlugins() throws ClassNotFoundException {
		for (String name : GetLoadedPlugins()) {
			UnloadPlugin(name);
		}
		bcPlugins.clear();
		cloudPlugins.clear();
	}

	
	public void UnloadAllBatchPlugins()throws ClassNotFoundException{
		for (String name : GetLoadedBatchPlugins()) {
			UnloadPlugin(name);
		}
		bcPlugins.clear();
			
	}
	
	public void UnloadAllCloudPlugins() throws ClassNotFoundException {
		for (String name : GetLoadedCloudPlugins()) {
			UnloadPlugin(name);
		}
		//bcPlugins.clear();
		cloudPlugins.clear();
	}
	/**
	 * Get loaded plugins.
	 * @return a list of plugin names.
	 */
	public List<String> GetLoadedPlugins() {
		ArrayList<String> al = new ArrayList<String> ();
		al.addAll(bcPlugins.keySet());
		al.addAll(cloudPlugins.keySet());
		return al;
	}


	public List<String> GetLoadedCloudPlugins() {
		ArrayList<String> al = new ArrayList<String> ();
		//al.addAll(bcPlugins.keySet());
		al.addAll(cloudPlugins.keySet());
		return al;
	}
	
	public List<String> GetLoadedBatchPlugins() {
		ArrayList<String> al = new ArrayList<String> ();
		al.addAll(bcPlugins.keySet());
		//al.addAll(cloudPlugins.keySet());
		return al;
	}
	

}