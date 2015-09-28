import java.rmi.Remote;

public interface Bufor extends Remote {
	
	void write(String bufName, String msg) throws Exception;
	String read(String bufName) throws Exception;

}
