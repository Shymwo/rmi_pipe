import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class PipeServer {

	public static void main(String[] args) throws Exception {
		
		if (args.length < 1) {
			System.out.println("Config file is required");
			return;
		}
		
		int port;
		List<Registry> remoteBuffers = new ArrayList<Registry>();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(args[0]));
			port = Integer.parseInt(br.readLine());
			while (true) {
				String s = br.readLine();
				if (s == null || "".equals(s)) {
					break;
				}
				String[] splited = s.split("\\s+");
				Registry r = LocateRegistry.getRegistry(splited[0], 
						Integer.parseInt(splited[1]));
				remoteBuffers.add(r);
			}
		} finally {
			br.close();
		}
		
		String name = "/PipeServer";
		
		Bufor engine = new BuforEngine(remoteBuffers);
		Registry registry = LocateRegistry.createRegistry(port);
		registry.rebind(name, engine);

	}

}
