import java.rmi.RemoteException;
import java.util.Random;
import java.lang.Thread;

public class MorpionIA extends Thread{
	private Player _player;
	private int _currentPlayer;
	private int _startingPlayer;
	private int[][] _map;
	private int scoreJoueur;
	private int scoreIA;
	private GameEngine _ge;
	private int _levelIA;
	
	public MorpionIA(Player p, GameEngine ge, int level){
		this._player = p;
		Random r = new Random();
		this._startingPlayer = r.nextInt(2);
		this._map = new int[3][3];
		this._ge = ge;
		this.scoreJoueur = 0;
		this.scoreIA = 0;
		this._levelIA = level;
	}
	
	public void run(){
        try{
        	_player.sendMessage("Starting game against IA");
        }catch(Exception e){
            
        }
        for(int nbManche = 1; nbManche <= 10 && scoreIA < 3 && scoreJoueur < 3; nbManche++){
	        try {
				this._player.sendMessage("Starting of Round " + nbManche);
			} catch (RemoteException e2) {
				e2.printStackTrace();
			}
	        this.init();
	        int winner = 0;
	        while(winner == 0){
	        	showMapPlayer(nbManche);
	        	
	        	int[] res = new int[2];
				try {
					if(this._currentPlayer == 1)
						_player.sendMessage("IA is playing ...");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				
				do{
	        		if(this._currentPlayer == 0){
	        			try {
		        			res = _player.getCoordonnees();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
	        		}else{
	        			res = moveIA();
	        			try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	        		}
	        		
		        	
				}while(!checkPosition(res[0], res[1]));
				
	        	this._map[res[0]][res[1]] = _currentPlayer + 1;
	        	winner = endGame();
	        	_currentPlayer = (this._currentPlayer + 1)%2;
	        }
	        
	        showMapPlayer(nbManche);
	    	_currentPlayer = (this._currentPlayer + 1)%2;
	        if(winner > -1){
	            try {
	            	if((winner-1) == 0){
	            		this._player.sendMessage("You win this round");
	            		scoreJoueur++;
	            	}
	            	else{
	            		this._player.sendMessage("You loose this round");
	            		scoreIA++;
	            	}
	    		} catch (RemoteException e) {
	    			e.printStackTrace();
	    		}
	        }else{
	            try {
	    			this._player.sendMessage("Draw");
	    		} catch (RemoteException e) {
	    			e.printStackTrace();
	    		}
	        }
        }
        
        String res = "";
        if(this.scoreJoueur > this.scoreIA){
        	res = " _  _  _____  __  __    _    _  ____  _  _ \n"
				+ "\t\t( \\/ )(  _  )(  )(  )  ( \\/\\/ )(_  _)( \\( )\n"
				+ "\t\t \\  /  )(_)(  )(__)(    )    (  _)(_  )  (\n" 
				+ "\t\t (__) (_____)(______)  (__/\\__)(____)(_)\\_)\n";

        }else if(this.scoreJoueur < this.scoreIA){
        	res = " _  _  _____  __  __    __    _____  _____  ___  ____ \n"
				+"\t\t( \\/ )(  _  )(  )(  )  (  )  (  _  )(  _  )/ __)( ___)\n"
				+ "\t\t \\  /  )(_)(  )(__)(    )(__  )(_)(  )(_)( \\__ \\ )__)\n"
				+ "\t\t (__) (_____)(______)  (____)(_____)(_____)(___/(____)\n";
        }else{
        	res = " ____  ____    __    _    _ \n"
				+ "\t\t(  _ \\(  _ \\  /__\\  ( \\/\\/ )\n"
				+ "\t\t )(_) ))   / /(__)\\  )    ( \n"
				+ "\t\t(____/(_)\\_)(__)(__)(__/\\__)\n";
        }
        
    	try {
			this._player.sendMessage(res);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
        
    	try {
			this._player.connectPlayer(this._ge);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void init(){
		this._startingPlayer = (this._startingPlayer + 1)%2;
		this._currentPlayer = this._startingPlayer;
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				this._map[i][j] = 0;
	}
	
	private void showMapPlayer(int nbManche){
		try {
			this._player.printGrille(this._map, 0,"IA", this.scoreJoueur, this.scoreIA, nbManche);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkPosition(int posX, int posY){
		if(posX >= 0 && posX < 3 && posY >=0 && posY < 3){
			if(this._map[posX][posY] == 0)
				return true;
		}
		try {
			this._player.sendMessage("Please enter a valid position");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private int endGame(){
		if(_map[0][0] == _map[0][1] && _map[0][1] == _map[0][2] && _map[0][0] > 0) return _map[0][0];
		if(_map[1][0] == _map[1][1] && _map[1][1] == _map[1][2] && _map[1][0] > 0) return _map[1][0];
		if(_map[2][0] == _map[2][1] && _map[2][1] == _map[2][2] && _map[2][0] > 0) return _map[2][0];
		
		if(_map[0][0] == _map[1][0] && _map[1][0] == _map[2][0] && _map[0][0] > 0) return _map[0][0];
		if(_map[0][1] == _map[1][1] && _map[1][1] == _map[2][1] && _map[0][1] > 0) return _map[0][1];
		if(_map[0][2] == _map[1][2] && _map[1][2] == _map[2][2] && _map[0][2] > 0) return _map[0][2];
		
		if(_map[0][0] == _map[1][1] && _map[1][1] == _map[2][2] && _map[0][0] > 0) return _map[0][0];
		if(_map[0][2] == _map[1][1] && _map[1][1] == _map[2][0] && _map[1][1] > 0) return _map[1][1];

		//Test to see if the map is full
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				if(this._map[i][j] == 0) return 0;
		
		return -1;
	}
	
	private int[] moveIA(){
		if(this._levelIA == 1) return this.IANoob();
		else return this.IAMinMax(this._map, 2, 1);
	}
	
	private int[] IANoob(){
		int[][] freeCase = new int [9][2];
		int compteur = 0;
		for(int i = 0; i < 3; i++){
			for(int j=0; j < 3; j++){
				if(this._map[i][j] == 0){
					freeCase[compteur][0] = i;
					freeCase[compteur++][1] = j;
				}
			}
		}
		Random r = new Random();
		return freeCase[r.nextInt(compteur)]; 
	}
	
	private int[] IAMinMax(int[][] grille, int player, int prof){
		int[][] freeCase = EmptyCases();
		int nbCaseVide = 0;
		for(int i = 0; i < 9; i++) if(freeCase[i][0] != -1) nbCaseVide++;
		
		if(nbCaseVide == 0){
			int winner = endGame();
			int[] res = new int[3];
			res[0] = -1;
			res[1] = -1;
			if(winner == 1) res[2] = -10;
			else if(winner == 2) res[2] = 10;
			else res[2] = 0;
			return res;
		}
		int winOrNot = endGame();
		if(winOrNot > 0){
			int[] res = new int[3];
			res[0] = -1;
			res[1] = -1;
			if(winOrNot == 1) res[2] = -10;
			else res[2] = 10;
			return res;
		}
		
		int[] scoreFreeCase = new int[nbCaseVide];		
		for(int i = 0; i < nbCaseVide; i++){
			grille[freeCase[i][0]][freeCase[i][1]] = player;
			
			if(player == 1){
				int[] result = IAMinMax(grille, 2, prof+1);
				scoreFreeCase[i] = result[2];
			}else{
				int[] result = IAMinMax(grille, 1, prof+1);
				scoreFreeCase[i] = result[2];
			}
			grille[freeCase[i][0]][freeCase[i][1]] = 0;
		}
		
		int bestMoove = 0;
		int bestScore = 0;
		if(player == 2){
			bestScore = -10000;
			for(int i = 0; i < nbCaseVide; i++){
				if(scoreFreeCase[i] > bestScore){
					bestScore = scoreFreeCase[i];
					bestMoove = i;
				}
			}
		}else{
			bestScore = 10000;
			for(int i = 0; i < nbCaseVide; i++){
				if(scoreFreeCase[i] < bestScore){
					bestScore = scoreFreeCase[i];
					bestMoove = i;
				}
			}
		}
		int[] returnTab = new int[3];
		returnTab[0] = freeCase[bestMoove][0];
		returnTab[1] = freeCase[bestMoove][1];
		returnTab[2] = bestScore;
		return returnTab;
	}
	
	private int[][] EmptyCases(){
		int compteur = 0;
		int[][] freeCase = new int [9][2];
		for(int i = 0; i < 9; i++){
			freeCase[i][0] = -1;
			freeCase[i][1] = -1;
		}
		
		for(int i = 0; i < 3; i++){
			for(int j=0; j < 3; j++){
				if(this._map[i][j] == 0){
					freeCase[compteur][0] = i;
					freeCase[compteur++][1] = j;
				}
			}
		}
		return freeCase;
	}
}
