import java.util.Hashtable;
import java.util.Vector;
import java.io.*;

/**
 * The Game Class
 * 
 * @author Jeremy Choyce
 * @version 3.25.15
 */
public class Game
{
	Board initialBoard;
	Board currentState;
	boolean quit = false;
	boolean allocatedPlayers=false;
	Board nextBoard = null;
	Vector players;
	int [] count = new int[2];
	
	public Game() 
	{
		initialBoard = new Board(8,8);
		try
		{
			initialBoard.setNode(3,3,initialBoard.getPlayerName(1));
			initialBoard.setNode(3,4,initialBoard.getPlayerName(0));
			initialBoard.setNode(4,3,initialBoard.getPlayerName(0));
			initialBoard.setNode(4,4,initialBoard.getPlayerName(1));
		}
		catch(Exception e)
		{
			System.out.println("Could not initialize board. Error:" + e.getMessage());
		}
		setCurrentState((Board)getInitial());
	}
	
	/**
	 * gets a list of the legal moves available
	 * @param board - the current board information
	 * @return a list of legal moves 
	 */
	public static Vector legalMoves(Board board){
		Vector v = new Vector();
		String index;
		for(int r = 0;r<board.getRows();r++)
		{
			for(int c=0;c<board.getCols();c++)
			{
				index = board.makeIndex(r,c);
				if(board.isFree(index)&&board.isAdjacentToEnemy(index)&& board.flankingPositions(index,board.getNode(board.getToMove()),board.getNode(board.getNotToMove())).size() > 0)
				{
					v.add(index);
				}
			}
		}
		return v;	
	}
	
	/**
	 * applies move to the board
	 * @param move - a string of the form "row,col"
	 * @param board - the current board state
	 * @return board - the resulting board after a move was made
	 */
	public Board makeMove(String move,Board board)
	{
		try
		{
			board.makeMove(move,null);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return board; // return board after move
	}
	
	/**
	 * returns true if the game is over
	 * @param board - board state
	 * @return true if the game is over
	 */
	public boolean gameOverTest(Board board)
	{
		Board copy = (Board) board.makeCopy();
		copy.nextPlayer(); // Check the next turn as well
		return board.isFull() ||(this.legalMoves(board).size() == 0 && this.legalMoves(copy).size() == 0);
	}
	
	/**
	* @return the String move of "row,col"
	*/
	public String toMove(Board board)
	{
		return board.getToMove();
	}
	
	/**
	* displays the current board state
	*/
	public void display(Board board)
	{
		board.display();
	}
	
	/**
	 * implements the opponent's search
	 * @param board - board state
	 * @return legal and viable moves
	 */
	public Hashtable successors(Board board)
	{
		Hashtable h = new Hashtable();
		Vector legal = legalMoves(board);
		String move;
		while(legal.size() > 0)
		{
			move = (String) legal.remove(0);
			h.put(move,makeMove(move,board)); 
		}
		return h;
	}
	
	/**
	* @return the initial board state
	*/
	public Board getInitial()
	{
		return (Board) this.initialBoard;
	}
	
	/**
	 * Begins the game
	 * @param players
	 */
	public void playGame(Vector players)
	{			
		String move;
		Vector moves;
		count[0] = count[1] = 0;
		while (true)
		{
			for(int i=0;i<players.size();i++)
			{
				moves = legalMoves(currentState);
				//System.out.println("Legal moves:"+moves);
				if(moves.size() == 0)
				{
					System.out.println("Your turn is forfeited.");
					currentState.display();
					System.out.println("------------------------------");
					currentState.nextPlayer();
					continue;
				}
				move = ((Player) players.get(i)).getMove(this,currentState);
				currentState = makeMove(move,currentState);
				currentState.nextPlayer();
				if(gameOverTest(currentState))
				{
					currentState.display();
					for(int r=0;r<currentState.getRows();r++)
						for(int c=0;c<currentState.getCols();c++)
							if(currentState.getNodeAt(r,c)!=currentState.getNode("default"))
								count[currentState.getNodeAt(r,c).equals("\u2022")?0:1]++;
					for(int j=0;j<players.size();j++)
					{
						Player p = (Player) players.get(j);
						System.out.println(p.name + " (" + p.node + ")" + ": " + count[j] + " pieces");
					}
					if(count[0]==count[1])
						System.out.println("The game is a tie.");
					else
						//System.out.println("\nPlayer " + (count[0]>count[1]?1:2) + " has won!");
						if (count[0] > count[1]) {
							System.out.println("\nYou've won!");
						}
						else
							System.out.println("\nThe AI has won!");
					return;
				}
			}
		}
	}
	
	/**
	* @return the current board state
	*/
	public Board getCurrentState()
	{
		return currentState;
	}
	
	/**
	 * @param c - the board's state
	 */
	public void setCurrentState(Board state)
	{
		currentState = state;
	}
	
	/**
	* set the next board state
	*/
	public void setToLoad(Board board)
	{
		nextBoard = board;
	}
	
	/**
	* set the game to quit
	*/
	public void quit()
	{
		quit = true;
	}
	
	/**
	* @return quitting or not
	*/
	public boolean willQuit()
	{
		return quit;
	}
	
	/**
	* Sets the players
	*/
	public void setPlayers(Vector players)
	{
		players = players;
		allocatedPlayers = true;
	}
	
	/**
	* @return allocated players
	*/
	public boolean playersAreSet()
	{
		return allocatedPlayers;
	}
	
	/**
	* Player versus AI begins
	*/
	public static void main(String args[])
	{
		Game game = new Game();
		Vector players = new Vector();
		players.add(new PlayerController("Human","x"));
		players.add(new PlayerAI("AI","o"));
		game.playGame(players);
	}
}