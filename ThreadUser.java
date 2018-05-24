import java.rmi.RemoteException;

public class ThreadUser extends Thread{
	private GameEngine _ge;
	private Player _p;
	
	public ThreadUser(GameEngine ge, Player p){
		this._ge = ge;
		this._p = p;
	}
	
	public void run(){
		try {
			this._ge.connectPlayer(this._p);
		} catch (RemoteException e) {
			System.out.println("Unable to reach the Remote Object");
		}
	}
}