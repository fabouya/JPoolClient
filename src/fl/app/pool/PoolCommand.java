package fl.app.pool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

public class PoolCommand 
{
	protected Socket 	_sock = null;
	protected String 	_serverName = "";
	protected int		_serverPort = 0;
	
	protected final static Logger _logger = Logger.getLogger(PoolCommand.class);

	public PoolCommand(String serverName, int serverPort)
	{
		_logger.info("Create command interface object");
		_serverName = serverName;
		_serverPort = serverPort;
	}
	
	@SuppressWarnings("unchecked")
	protected String JsonError(String errormsg)
	{
		JSONObject obj = new JSONObject();
        obj.put("tag", "status");
        obj.put("status", "nok");
        obj.put("error_msg", errormsg);

        String answer = obj.toJSONString() + "\n";
        _logger.info("Error : " + answer);
        
        return answer;
	}

	@SuppressWarnings("unchecked")
	protected String JsonSuccess()
	{
		JSONObject obj = new JSONObject();
        obj.put("tag", "status");
        obj.put("status", "ok");

        String answer = obj.toJSONString() + "\n";
        
        return answer;
	}

	protected void Send(String s) throws Exception
	{
	      _logger.debug("Sending :<"+s+">");

	      PrintStream out = new PrintStream(_sock.getOutputStream());

	      out.println(s);
	}
	
	protected String Read() throws Exception
	{
	      BufferedReader in = new BufferedReader(new InputStreamReader(_sock.getInputStream()));

	      String reponse = in.readLine(); 
	    		  
	      _logger.debug("Reponse :>"+reponse+"<");
	      
	      return reponse;
	}	
	
	protected Boolean Connect()
	{		
		_logger.debug("Connecting to pool server");
		
	    try {
	    		InetAddress serveur = InetAddress.getByName(_serverName);
	    		_sock = new Socket(serveur, _serverPort);
	    		_logger.debug("Connecting ok");
	    		
	    		return true;	    

	    } catch (Exception e) 
	    {
	      _logger.debug("Error", e);
	      _logger.error("Connect Erreur");
	    }
		
		return false;
	}
	
	protected void Disconnect()
	{
	      _logger.debug("Command.Disconnect");
	      
		if(_sock != null)
		{
		    try 
		    {
	    			_sock.close();
	    			_sock = null;
		    } 
		    catch (IOException e) 
		    {
			      _logger.error("Connect Erreur", e);
		    }
		}
	}
	
	public String GetStatus() throws Exception
	{
	      _logger.debug("Command.GetStatus");
		
		if(Connect())
		{
		      Send("GET STATUS\n");		      
		      String s = Read();
		      Disconnect();
		      return s;
		}
		
		return JsonError("Echec get status");
	}

	public String GetMode() throws Exception
	{
		if(Connect())
		{
				Send("GET ALLMODE\n");
				String s = Read();
				Disconnect();
				return s;
		}
		
		return JsonError("Echec get mode");
	}
	
	@SuppressWarnings("unused")
	public String Reload() throws Exception
	{
		if(Connect())
		{
				Send("RELOAD\n");
				String s = Read();
				Disconnect();
				return JsonSuccess();
		}		
		
		return JsonError("Echec reload");
		
	}
	
	public Boolean PingServer()
	{
		if(Connect())
		{
				Disconnect();
				return true;
		}
		
		return false;
	}
	
	public String Set(String device, String value) throws Exception
	{
	      _logger.debug("Command.Set " + device + " " + value);
		
		if(Connect())
		{
		      Send("SET " + device + " " + value +"\n");		      
		      String s = Read();
		      Disconnect();
		      return JsonSuccess();
		}
		
		return JsonError("Echec connexion");
	}
	

}
