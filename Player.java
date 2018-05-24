

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Player extends Remote{
	String getName() throws java.rmi.RemoteException;
	void sendMessage(String msg) throws java.rmi.RemoteException;
	int[] getCoordonnees() throws RemoteException;
	int choicePlayer() throws RemoteException;
	void quitGame() throws RemoteException;
	int getId() throws RemoteException;
	void printGrille(int[][] grille, int numPlayer, String advName, int scorePlayer, int scoreOpponent, int Round) throws RemoteException;
	void connectPlayer(GameEngine ge) throws java.rmi.RemoteException;
}
