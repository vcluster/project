package vcluster.ui.remote;
import javax.jws.WebService;   
import javax.jws.WebMethod;   
import javax.xml.ws.Endpoint; 

import vcluster.executors.CmdCataloger;
import vcluster.ui.CmdComb;
@WebService
public class WebInterface {
	@WebMethod
	public String remoteMethod(String cmd){
		return CmdCataloger.execute(new CmdComb(cmd)).toString();
	}
	
	public static void startWebServer(){
		WebInterface wi = new WebInterface();
		Endpoint.publish("http://localhost:2166/wi", wi);  		
	}

}
