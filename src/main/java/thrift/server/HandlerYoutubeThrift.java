package thrift.server;

import java.util.List;
import java.util.Map;

import database.io.DataIO;

public class HandlerYoutubeThrift implements InterfaceYoutubeThrift.Iface {
	
	DataIO dataIO = new DataIO();
	
	public boolean clientInsertIP(String ip, String authentication) {
		return dataIO.clientCheckStop(ip, authentication);
	}

    public boolean controllerUpdateIP(String ips, String emails, String passwords, String authentication) {
    	return dataIO.controllerUpdateIP(ips, emails, passwords, authentication);    			
    }

    public boolean clientCheckStop(String ip, String authentication) {
    	return dataIO.clientCheckStop(ip, authentication);
    }

    public boolean clientSetLog(String ip, String log, String authentication) {
    	return dataIO.clientSetLog(ip, log, authentication);
    }

    public Map<String,String> gettingParameterSearch(String authentication) {
    	return dataIO.gettingParameterSearch(authentication);
    }

    public boolean settingParameterSearch(Map<String,String> mapParameters, String authentication) {
    	return dataIO.settingParameterSearch(mapParameters, authentication);
    }

    public Map<String,String> gettingParameterViewSuggest(String authentication) {
    	return dataIO.gettingParameterViewSuggest(authentication);
    }

    public boolean settingParameterViewSuggest(Map<String,String> mapParameters, String authentication) {
    	return dataIO.settingParameterViewSuggest(mapParameters, authentication);
    }

    public Map<String,String> gettingParameterClickSuggest(String authentication) {
    	return dataIO.gettingParameterClickSuggest(authentication);
    }

    public boolean settingParameterClickSuggest(Map<String,String> mapParameters, String authentication) {
    	return dataIO.settingParameterClickSuggest(mapParameters, authentication);
    }

    public boolean setStatus(String ips, String status, String authentication) {
    	return dataIO.setStatus(ips, status, authentication);
    }

    public boolean deleteIP(String ips, String authentication) {
    	return dataIO.deleteIP(ips, authentication);
    }

    public List<String> gettingAllIP(String authentication) {
    	return dataIO.gettingAllIP(authentication);
    }

    public List<String> gettingWarningIP(String authentication) {
    	return dataIO.gettingWarningIP(authentication);
    }

    public String gettingAuthentication(String username, String password) {
    	return dataIO.gettingAuthentication(username, password);
    }
}