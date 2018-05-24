import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameEngine extends Remote{
	public void connectPlayer(Player p) throws java.rmi.RemoteException;
	public void playMulti(Player p) throws java.rmi.RemoteException;
	void playIANoob(Player p) throws RemoteException;
	void playIAStrong(Player p) throws RemoteException;
}