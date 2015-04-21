/**
 * Player Class
 * Sets all the data for a player
 * @author Jonathon To
 * @version 3.25.15
 */

public abstract class Player
	{
	String node ="", name="";
	int pieceCount = 0;

	//creates a player's information
	public Player(String name, String node)
	{ 
		super();
		this.node = node;
		this.name = name;
	}

	//sets what the user's game node will  be
	public void setNode(String nodeString)
	{
		node = nodeString;
	}

	//sets player's name
	public void setName(String nameString)
	{
		name = nameString;
	}

	//sets what the player's current number of pieces on the board is
	public void setPieceCount(int i)
	{
		pieceCount = i;
	}
	
	//resets number of player's pieces to zero
	public void clearPieceCount()
	{
		pieceCount = 0;
	}
	
	//increase piece count by one
	public void incPiece()
	{
		pieceCount++;
	}

	public String getMove(Game dataFromGame, Board dataFromBoard)
	{
		return "";
	}

	//returns player and node	
	public String toString()
	{
		return name + " ("+node+")";
	}

	//returns player's node
	public String getNode()
	{
		return node;
	}

	//returns player's name
	public String getName()
	{
		return name;
	}

	//returns how many pieces the player has
	public int getPieceCount()
	{
		return pieceCount;
	}
}