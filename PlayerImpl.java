

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class PlayerImpl extends UnicastRemoteObject implements Player {
	private String _name;
	private int _id;
	private static int ID = 0;
	
	public PlayerImpl(String name) throws RemoteException {
		this._name = name;
		this._id = ID++;
	}
	
	@Override
	public int getId() throws RemoteException {
		return this._id;
	}
	
	@Override
	public String getName() throws RemoteException {
		return this._name;
	}

	@Override
	public void sendMessage(String msg) throws RemoteException {
		System.out.println("\n\t\t" + msg + "\n");
	}
	
	@Override
    public void printGrille(int[][] grille, int numPlayer, String advName, int scorePlayer, int scoreOther, int round) throws RemoteException{
		System.out.print("\033[H\33[2J");
		System.out.flush();
		System.out.println("\n\t\t--- Game map ---");
		System.out.println("\t\tRound : " + round + "/10");
		String symbYou = "O";
		String symbHim = "X";
		if(numPlayer == 0){
			symbYou = "X";
			symbHim = "O";
		}
		
		System.out.println("\t\t" + symbYou + " " + this._name +  " : "+ scorePlayer + "/3   \t" + symbHim + " " + advName + " : " +scoreOther + "/3");
		
        System.out.println("\n\t\t   \\ \t|\t \t|\t \t|\t \t|");
        System.out.println("\t\t    \\ X\t|\t1\t|\t2\t|\t3\t|");
        System.out.println("\t\t   Y \\\t|\t \t|\t \t|\t \t|");
        System.out.println("\t\t---------------------------------------------------------");
        for(int i = 0; i < 3; i++){
        	System.out.println("\t\t   \t|\t \t|\t \t|\t \t|");
        	System.out.print("\t\t   " + (i+1) +"\t|");
        	
            for(int j = 0; j < 3; j++){
            	String symbole = " ";
            	if(grille[i][j] == 1)
            		symbole = "X";
            	else if(grille[i][j] == 2)
            		symbole = "O";
            	
                System.out.print("\t" + symbole + "\t|");
            }
        	System.out.println("\n\t\t \t|\t \t|\t \t|\t \t|");
            System.out.println("\t\t---------------------------------------------------------");
        }
    }
    
	@Override
    public int[] getCoordonnees() throws RemoteException {
        int[] res = new int[2];
        Scanner sc = new Scanner(System.in);
        String res0 = "";
        do{
        System.out.print("\n\t\t Y value: ");
        res0 = sc.nextLine();
        } while(!this.isInteger(res0));
        
        res[0] = Integer.parseInt(res0) - 1;
        
        String res1 = "";
        do{
            System.out.print("\n\t\t X value: ");
            res1 = sc.nextLine();
        }while(!this.isInteger(res1));
        res[1] = Integer.parseInt(res1) - 1;

        return res;
    }
	
	private boolean isInteger( String input ) {
	    try {
	        Integer.parseInt( input );
	        return true;
	    }
	    catch( Exception e ) {
	    	System.out.println("\t\tPlease enter a number");
	        return false;
	    }
	}

	@Override
	public int choicePlayer() throws RemoteException {
		Scanner sc = new Scanner(System.in);
		String menu = "\t\tWhat do you want to do ?\n"
				+ "\t\t\t1: Play against another user\n"
				+ "\t\t\t2: Play against an easy IA\n"
				+ "\t\t\t3: Play against an unbeatable IA\n"
				+ "\t\t\t4: Quit the game";

		System.out.println(menu);
		boolean b = true;
		String res = "";
		while(b){
			System.out.print("\t\t> ");
			res = sc.nextLine();
			if(res.equals("1")) return 1;
			else if(res.equals("2")) return 2;
			else if(res.equals("3")) return 3;
			else if(res.equals("4")) return 4;
			else System.out.println("\t\tPlease enter a valid number");
		}
		return 1;
	}

	@Override
	public void quitGame() throws RemoteException {
		System.exit(0);
	}

	@Override
	public void connectPlayer(GameEngine ge) throws RemoteException {
		new ThreadUser(ge, this).start();
	}
}
