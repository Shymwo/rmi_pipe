import static java.lang.Thread.sleep;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class BuforEngine extends UnicastRemoteObject implements Bufor {
	
	private static final long serialVersionUID = 6794722676713904510L;
	
	private final Map<String, Queue<String>> buforMap;
	private static final int MAXSIZE = 10;
	
	public BuforEngine() throws RemoteException {
		super();
		buforMap = new HashMap<String, Queue<String>>();
	}
	
	@Override
	public void write(String bufName, String msg) throws Exception {
		Queue<String> bufor = getBufor(bufName);
		while (!bufor.offer((String) msg)) {
			sleep(100);
		}
		System.out.println("zapisalem");
	}
	
	@Override
	public String read(String bufName) throws Exception {
		Queue<String> bufor = getBufor(bufName);
		String result = null;
		while ((result=bufor.poll()) == null) {
			sleep(100);
		}
		System.out.println("odczytalem");
		return result;
	}
	
	private Queue<String> getBufor(String name) {
		Queue<String> bufor = buforMap.get(name);
		if (bufor == null) {
			bufor = new ArrayBlockingQueue<String>(MAXSIZE);
			buforMap.put(name, bufor);
		}
		return bufor;
	}

}
