import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PipeClient {
    
	public static void main(String[] args) throws Exception {
		
		if (args.length < 4) {
			System.out.println("You must specify: hostname port buforName method");
			return;
		}
		
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);
		String buforName = args[2];
		String method = args[3];
		
		String name = "//"+hostname+":"+port+"/PipeServer";
		
		Registry r = LocateRegistry.getRegistry(port);
		Bufor buf = (Bufor) r.lookup(name);
		if ("write".equals(method)) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				System.out.print("> ");
				String input = br.readLine();
				buf.write(buforName, input);
			}
		} else if ("read".equals(method)) {
			while (true) {
				System.out.println(buf.read(buforName));
			}
		}
		
	}
}
