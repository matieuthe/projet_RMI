

import java.net.MalformedURLException;
import java.rmi.*;
import java.util.Scanner;

public class Client {
	public static void main(String[] args){
		
		//Message d'accueil
		String accueil = "\n"
+"	  ___  ___  ___  ___  ___  ___  ___  ___  ___  ___  ___  ___  ___  ___  ___ \n"
+"	 (___)(___)(___)(___)(___)(___)(___)(___)(___)(___)(___)(___)(___)(___)(___)\n"
+"	                                                                            \n"
+"	          ____  ____  ___    ____   __    ___    ____  _____  ____          \n"
+"	         (_  _)(_  _)/ __)  (_  _) /__\\  / __)  (_  _)(  _  )( ___)         \n"
+"	           )(   _)(_( (__     )(  /(__)\\( (__     )(   )(_)(  )__)          \n"
+"	          (__) (____)\\___)   (__)(__)(__)\\___)   (__) (_____)(____)         \n"
+"	                                                                            \n"
+"	  ___  ___  ___  ___  ___  ___  ___  ___  ___  ___  ___  ___  ___  ___  ___ \n"
+"	 (___)(___)(___)(___)(___)(___)(___)(___)(___)(___)(___)(___)(___)(___)(___)\n\n\n";
		
		System.out.println(accueil);
        System.out.print("\t\tWelcome, please enter your name : ");
        
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        Player p = null;
        
		try {
			if(name == null) name = "temp";
			p = new PlayerImpl(name);
		} catch (RemoteException e1) {
			System.out.println("An error occured while creating the Player");
			System.exit(0);
		}
		
		GameEngine ge = null;
		try {
			ge = (GameEngine) Naming.lookup("rmi://localhost:5000/gameEngine");
			p.connectPlayer(ge);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("Unable to reach the Remote Object");
			System.exit(0);
		}
	}
}