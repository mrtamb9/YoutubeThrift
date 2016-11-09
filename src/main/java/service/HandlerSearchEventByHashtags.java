package service;

import database.io.DataIO;

public class HandlerSearchEventByHashtags implements ServiceSearchEventByHashtags.Iface {

	public String searchEventByHashtags(String hashtags) 
	{
		DataIO dataIO = new DataIO();
		
		boolean result;
		result = dataIO.clientInsertIP("1.2.3.4", "mrtamb9 thanhtam");
		return result + "";
	}
}