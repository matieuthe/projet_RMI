

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class GameEngineImpl extends UnicastRemoteObject implements GameEngine {
	private Player _waitingPlayer;
	
	private static final long serialVersionUID = 1L;

	protected GameEngineImpl() throws RemoteException {
		super();
		_waitingPlayer = null;
	}
	
	@Override
	public void connectPlayer(Player p) throws RemoteException {
        System.out.println("Connexion de " + p.getName());
		int choix = p.choicePlayer();
		switch (choix){
			case 1: this.playMulti(p);
					break;
			case 2: this.playIANoob(p);
					break;
			case 3: this.playIAStrong(p);
					break;
			case 4 : p.quitGame();
		}
	}

	@Override
	public synchronized void playMulti(Player p) throws RemoteException {
        if(_waitingPlayer == null){
            p.sendMessage("Attente d'un adversaire");
            _waitingPlayer = p;
        }else{
            new Morpion(_waitingPlayer, p, this).start();
            _waitingPlayer = null;
        }
	}
	
	@Override
	public void playIANoob(Player p) throws RemoteException {
		new MorpionIA(p, this, 1).start();
	}
	
	@Override
	public void playIAStrong(Player p) throws RemoteException {
		new MorpionIA(p, this, 2).start();
	}
}