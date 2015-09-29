import java.rmi.Remote;

public interface Bufor extends Remote {
	
	void write(String bufName, String msg) throws Exception;
	String read(String bufName) throws Exception;
	
	void replicateWrite(String bufName, String msg) throws Exception;
	void replicateRead(String bufName) throws Exception;

}
