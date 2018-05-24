import java.rmi.RemoteException;
import java.util.Random;
import java.lang.Thread;

public class Morpion extends Thread{
	private Player[] _players;
	private int _currentPlayer;
	private int _startingPlayer;
	private int[] _scores;
	private int[][] _map;
	private GameEngine _ge;
	
	public Morpion(Player p1, Player p2, GameEngine ge){
		this._players = new Player[2];
		this._players[0] = p1;
		this._players[1] = p2;
		Random r = new Random();
		this._startingPlayer = r.nextInt(2);
		this._map = new int[3][3];
		this._ge = ge;
		this._scores = new int[2];;
		this._scores[0] = 0;
		this._scores[1] = 0;
	}
	
	public void run(){
        try{
        	_players[0].sendMessage("Starting game against " + _players[1].getName());
        	_players[1].sendMessage("Starting game against " + _players[0].getName());
        }catch(Exception e){
            
        }
        
        for(int nbManche = 1; nbManche <= 10 && this._scores[0] < 3 && this._scores[1] < 3; nbManche++){
	        try {
				this._players[0].sendMessage("Starting round " + nbManche);
				this._players[1].sendMessage("Starting round " + nbManche);
			} catch (RemoteException e2) {
				e2.printStackTrace();
			}
	        
		    this.init();
		    int winner = 0;
		    while(winner == 0){
		    	showMapPlayer(nbManche);
		    	
		    	int[] res = new int[2];
				try {
					_players[(_currentPlayer+1)%2].sendMessage(_players[_currentPlayer].getName() + " is playing ...");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				
				do{
		        	try {
						res = _players[_currentPlayer].getCoordonnees();
					} catch (RemoteException e) {
						e.printStackTrace();
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
					this._players[winner-1].sendMessage("You win this round !!");
			        this._players[(winner)%2].sendMessage("You loose this round !!");
			        this._scores[winner-1]++;
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }else{
		        try {
					this._players[0].sendMessage("Draw");
			        this._players[1].sendMessage("Draw");
				} catch (RemoteException e) {
					e.printStackTrace();
				}
		    }
        }
        
        
        String res0 = "";
        String res1 = "";
        String win = " _  _  _____  __  __    _    _  ____  _  _ \n"
				+ "\t\t( \\/ )(  _  )(  )(  )  ( \\/\\/ )(_  _)( \\( )\n"
				+ "\t\t \\  /  )(_)(  )(__)(    )    (  _)(_  )  (\n" 
				+ "\t\t (__) (_____)(______)  (__/\\__)(____)(_)\\_)\n";
        
        String loose = " _  _  _____  __  __    __    _____  _____  ___  ____ \n"
				+"\t\t( \\/ )(  _  )(  )(  )  (  )  (  _  )(  _  )/ __)( ___)\n"
				+ "\t\t \\  /  )(_)(  )(__)(    )(__  )(_)(  )(_)( \\__ \\ )__)\n"
				+ "\t\t (__) (_____)(______)  (____)(_____)(_____)(___/(____)\n";
        String draw = " ____  ____    __    _    _ \n"
				+ "\t\t(  _ \\(  _ \\  /__\\  ( \\/\\/ )\n"
				+ "\t\t )(_) ))   / /(__)\\  )    ( \n"
				+ "\t\t(____/(_)\\_)(__)(__)(__/\\__)\n";
        if(this._scores[0] > this._scores[1]){
        	res0 = win;
        	res1 = loose;
        }else if(this._scores[0] < this._scores[1]){
        	res0 = loose;
        	res1 = win;
        }else{
        	res0 = draw;
        	res1 = draw;
        }
        
    	try {
			this._players[0].sendMessage(res0);
			this._players[1].sendMessage(res1);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
        try {
        	this._players[0].connectPlayer(this._ge);
        	this._players[1].connectPlayer(this._ge);
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
			this._players[0].printGrille(this._map, 0,this._players[1].getName(), this._scores[0], this._scores[1], nbManche);
			this._players[1].printGrille(this._map, 1, this._players[0].getName(),this._scores[1], this._scores[0], nbManche);
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
			this._players[this._currentPlayer].sendMessage("Please enter a valid position");
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
}
