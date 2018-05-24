import java.net.MalformedURLException;
import java.rmi.*;
import java.util.Scanner;

public class Serveur {
	public static void main (String[] args){
		try {
			GameEngine ge = new GameEngineImpl();
			String url = "rmi://localhost:5000/gameEngine";
			Naming.rebind(url, ge);
			System.out.println("Server is ready");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}