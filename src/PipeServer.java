import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PipeServer {

	public static void main(String[] args) throws Exception {
		
		if (args.length < 1) {
			System.out.println("Server port is not defined");
			return;
		}
		
		int port = Integer.parseInt(args[0]);
		String name = "//localhost:"+port+"/PipeServer";
		
		Bufor engine = new BuforEngine();
		Registry r = LocateRegistry.createRegistry(port);
		r.rebind(name, engine);

	}

}
