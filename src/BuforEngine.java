import static java.lang.Thread.sleep;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class BuforEngine extends UnicastRemoteObject implements Bufor {
	
	private static final long serialVersionUID = 6794722676713904510L;
	
	private final Map<String, Queue<String>> buforMap;
	private static final int MAXSIZE = 10;
	private static final String SERVER_NAME = "/PipeServer";
	private static final boolean DEBUG = true;
	
	private List<Registry> remoteBuffers;
	
	public BuforEngine(List<Registry> remoteBuffers) throws RemoteException {
		super();
		buforMap = new HashMap<String, Queue<String>>();
		this.remoteBuffers = remoteBuffers;
	}
	
	@Override
	public void write(String bufName, String msg) throws Exception {
		internalWrite(bufName, msg);
		for (Registry r : remoteBuffers) {
			try {
				Bufor buf = (Bufor) r.lookup(SERVER_NAME);
				buf.replicateWrite(bufName, msg);
				if (DEBUG) System.out.println(r.toString()+" zapisalem");
			} catch (Exception e) {
				if (DEBUG) System.out.println(r.toString()+" niedostepny");
			}
		}
	}
	
	@Override
	public String read(String bufName) throws Exception {
		String result = internalRead(bufName);
		for (Registry r : remoteBuffers) {
			try {
				Bufor buf = (Bufor) r.lookup(SERVER_NAME);
				buf.replicateRead(bufName);
				if (DEBUG) System.out.println(r.toString()+" odczytalem");
			} catch (Exception e) {
				if (DEBUG) System.out.println(r.toString()+" niedostepny");
			}
		}
		return result;
	}
	
	@Override
	public void replicateWrite(String bufName, String msg) throws Exception {
		Queue<String> bufor = getBufor(bufName);
		bufor.offer((String) msg);
		if (DEBUG) System.out.println("replikacja - zapisalem");
	}

	@Override
	public void replicateRead(String bufName) throws Exception {
		Queue<String> bufor = getBufor(bufName);
		bufor.poll();
		if (DEBUG) System.out.println("replikacja - odczytalem");
	}
	
	private Queue<String> getBufor(String name) {
		Queue<String> bufor = buforMap.get(name);
		if (bufor == null) {
			bufor = new ArrayBlockingQueue<String>(MAXSIZE);
			buforMap.put(name, bufor);
		}
		return bufor;
	}
	
	private void internalWrite(String bufName, String msg) throws Exception {
		Queue<String> bufor = getBufor(bufName);
		while (!bufor.offer((String) msg)) {
			sleep(100);
		}
		if (DEBUG) System.out.println("zapisalem");
	}
	
	private String internalRead(String bufName) throws Exception {
		Queue<String> bufor = getBufor(bufName);
		String result = null;
		while ((result=bufor.poll()) == null) {
			sleep(100);
		}
		if (DEBUG) System.out.println("odczytalem");
		return result;
	}

}
