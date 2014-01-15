package vcluster.plugins.plugman;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import vcluster.plugins.BatchInterface;
import vcluster.plugins.CloudInterface;

/**
 * This class extends java.lang.ClassLoader,which is responsible for loading java classes into the JVM dynamically.In this class,we override the method "loadClass" 
 * to make it can load class files into the JVM from the plugins forder.
 * @version 1.0,2013-05-31
 * @since JDK1.7
 */


public class CustomClassLoader extends ClassLoader {
	/**
	 * Specifys the directory where the plugin files are located. 
	 */
	
	/**
	 * Invoke the superclass's constructor and initialize the plugins directory.
	 * @param dir The plugins directory
	 * */
	// @Override
	public CustomClassLoader(){
		super(CustomClassLoader.class.getClassLoader());
	}
	
	/**
	 * Loads a specified Class to the JVM from a jar file.if the JAR file doesn't include a eligible class(implements the interface of ProxyExecutor),it will be dropped.
	 * @param jarName The name of the target jar file,without extension .jar.
	 * @return Class<?>
	 * @exception ClassNotFoundException
	 * 
	 */
	public Class<?> loadClass(String jarName){
		
		try {  
			  
			  File f = new File(jarName);
			  JarFile jarFile = new JarFile(f);  
			  URL url = f.toURI().toURL();
			  //@test
			  
			  URLClassLoader loader = new URLClassLoader(new URL[]{url});
			  Enumeration<JarEntry> es = jarFile.entries();  
			  while (es.hasMoreElements()) {  
			   JarEntry jarEntry = (JarEntry) es.nextElement();  
			   String name = jarEntry.getName();  
			   if(name != null && name.endsWith(".class")){
				   Class<?> c = loader.loadClass(name.replace("/", ".").substring(0,name.length() - 6)); 
					Class<?>[] intfs = c.getInterfaces();
					for (Class<?> intf : intfs) {
						if (intf.getName().equals(BatchInterface.class.getName())||intf.getName().equals(CloudInterface.class.getName())) {
							return c;
						}
					}
				  // return c;
			   }
			  }
			
			  jarFile.close();
			  loader.close();
			  
			}catch(IOException e){e.printStackTrace();return null;}
		     catch(ClassNotFoundException e){e.printStackTrace();return null;}
		return null;
	}
}
