package fl.app.pool;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class JPoolCLient 
{

	private static String	_host = "";
	private static String	_port = "";
	private	static int		_delay = 1000;
	
	private static void DoParseOptions(String[] args)
	{
		// create Options object
		Options options = new Options();


		Option  host = Option.builder("h")
						.hasArg()
						.longOpt("host")
						.required()
						.argName("host")
						.build();
		
		options.addOption(host);

		Option  port = Option.builder("p")
				.hasArg()
				.longOpt("port")
				.required()
				.argName("port")
				.build();

		options.addOption(port);
		
		Option  delay = Option.builder("d")
				.hasArg()
				.longOpt("delay")
				.required()
				.argName("delay")
				.build();

		options.addOption(delay);
		
		
		CommandLineParser parser = new DefaultParser();
		
		try 
		{
			CommandLine cmd = parser.parse( options, args);
						
			_host = cmd.getOptionValue("host");
			_port = cmd.getOptionValue("port");
			_delay = Integer.parseInt(cmd.getOptionValue("delay"));
			
		} catch (ParseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}				
		
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

		DoParseOptions(args);
		
		PoolCommand p = new PoolCommand(_host, Integer.parseInt(_port));
		if(p.PingServer())
		{
			try 
			{
				while(true)
				{
					String status = p.GetStatus();
					System.out.println(status);
					Thread.sleep(_delay);
				}
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
