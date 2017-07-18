package fl.app.pool;

public class JPoolCLient {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

		PoolCommand p = new PoolCommand("localhost", 5832);
		if(p.PingServer())
		{
			try 
			{
				while(true)
				{
					String status = p.GetStatus();
					System.out.println(status);
					Thread.sleep(10000);
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
