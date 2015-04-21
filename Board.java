import java.util.*;

/**
 * The Board Class
 * 
 * This class deals with the functionality of the 
 * board display and node data regarding the board.
 * The use of a hashtable is in place of a 2d array given that
 * the data structure allows for more efficient node storage and
 * manipulation.
 * 
 * @author Justin Lok
 * @version 3.23.15
 */

public class Board {
	
	Hashtable board; // Stores the board
	Hashtable nodes; // Stores the node information
	int rows, cols;
	String toMove;
	String[] players = new String[2]; // Two players
	
	/**
	 * Initializes the default board and assigns the player and AI a type.
	 * By default, a blank space is denoted with a " . ", the player is
	 * denoted with a " black dot (u25CF) " and the AI is denoted with an " o ".
	 * 
	 * @param rows - number of rows
	 * @param cols - number of columns
	 */
	public Board(int rows, int cols) {
		for(int i = 0; i < players.length; i++) {
			if (i == 0) {
				players[i] = "Human (\u2022)";
			}
			else
				players[i] = "AI (o)";
		}
		
		toMove = players[0]; // Player moves first
		
		this.rows = rows;
		this.cols = cols;
		
		board = new Hashtable();
		nodes = new Hashtable();
		nodes.put("default","."); // Denotes an empty space
		nodes.put(players[0],"\u2022"); // The player
		nodes.put(players[1],"o"); // The AI
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				board.put(makeIndex(i,j),"."); // The grid of the board
			}
		}
	}
	
	/**
	 * Check whether or not the given position is next to the opponent's position.
	 * 
	 * @param pos - the given position
	 * @return true or false - whether or not the adjacent spot is next to the opponent
	 */
	public boolean isAdjacentToEnemy(String pos) {
		int r = getRow(pos); 
		int c = getCol(pos);
		String opponent = getNode(getNotToMove());
		for(int i =- 1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(i == 0 && j == 0) {
					continue;
				}
				if(getNodeAt(r+i,c+j).equals(opponent)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Performs a move at the specified position and refactors the board accordingly.
	 * 
	 * @param pos - the given position
	 * @param flankPieces - the vector that contains the flank positions
	 * @throws Exception - required for the setNode method
	 */
	public void makeMove(String pos, Vector flankPieces) throws Exception {
		String move = "";
		if(flankPieces == null) {
			// Figure out what positions are flanking positions for the player
			flankPieces = flankingPositions(pos,getNode(toMove),getNode(getNotToMove()));
		}
		int cols = this.getCol(pos);
		int rows = this.getRow(pos);
		for(int i = 0; i < flankPieces.size(); i++) {
			move = (String)flankPieces.get(i);
			flipPieces(pos,move);	
		}
		setNode(rows,cols,toMove); // Refactor the board
	}
	
	/**
	 * This method determines what pieces should be flipped and calls the flip method.
	 * 
	 * @param p1 - the Player
	 * @param p2 - the AI
	 */
	public void flipPieces(String p1, String p2) throws Exception {
		//System.out.println("Pieces:"+p1+":"+p2);
		int x0 = getCol(p1), y0 = getRow(p1), x1 = getCol(p2), y1 = getRow(p2),
			slope = (x0-x1 != 0) ? (y1-y0) / (x1-x0) : 0, lgX, lgY, smX, smY;
		String larger, smaller;
		if (x0 - x1 == 0) { // Vertical line
			if(y1 > y0) {
				larger = p2; smaller = p1; lgX = x1; lgY = y1; smX = x0; smY = y0;
			}
			else {
				larger = p1; smaller = p2; lgX = x0; lgY = y0; smX = x1; smY = y1;
			}
			for(int i = smY+1; i < lgY; i++) {
				flip(makeIndex(i,x1));
			}
		} else {
			if(x0 > x1) {
				larger = p1; smaller = p2; lgX = x0; lgY = y0; smX = x1; smY = y1;
			}
			else {
				larger = p2; smaller = p1; lgX = x1; lgY = y1; smX = x0; smY = y0;
			}
					
			if(y1-y0 == 0) { // Horizontal line
				for(int i = smX+1; i < lgX; i++) {
					flip(makeIndex(i*slope+smY,i));
				}				
			} else { // Diagonal
				int b = lgY - slope * lgX;
				for(int i = smX+1; i < lgX; i++) {
					flip(makeIndex(i*slope+b,i));
				}
			}
		}
	}
	
	/**
	 * Performs a flip of the pieces at the specified location.
	 * 
	 * @param node - the nodes on the board
	 * @throws Exception - required for the setNode method
	 */
	public void flip(String node) throws Exception {
		if(isFree(node)) {
			throw new Exception("Cannot flip free square:" + node);
		}
		else if(!isWithinBounds(node)) throw new Exception("Cannot flip unbounded square.");
		setNode(getRow(node),getCol(node),
			(getNodeAt(node) == getNode(players[0])) ? players[1] : players[0]);		  
	}
	
	/**
	 * Determines what node positions are flanking positions, which determines a valid move.
	 * 
	 * @param pos - the current position
	 * @param node - the nodes on the board
	 * @return v - the vector that contains the flank positions for the opponent
	 */
	public Vector flankingPositions(String pos, String node, String enemy) {
		Vector v = new Vector();
		int row = getRow(pos); 
		int col = getCol(pos);
		
		//System.out.println("Enemy:"+enemy);
		
		// Check/Search diagonally
		for(int i=1;i<rows;i++) 
			if (!isWithinBounds(row+i,col+i) || (i==1 && !areNodesEqual(enemy,row+i,col+i)) || 
					flankingPositionsHelper(row+i,col+i,node,v) || !enemy.equals(getNodeAt(row+i,col+i))) break;
		for(int i=1;i<rows;i++) 
			if (!isWithinBounds(row-i,col-i) || (i==1 && !areNodesEqual(enemy,row-i,col-i)) || 
					flankingPositionsHelper(row-i,col-i,node,v) || !enemy.equals(getNodeAt(row-i,col-i))) break;
		for(int i=1;i<rows;i++) 
			if (!isWithinBounds(row+i,col-i) || (i==1 && !areNodesEqual(enemy,row+i,col-i)) || 
					flankingPositionsHelper(row+i,col-i,node,v) || !enemy.equals(getNodeAt(row+i,col-i))) break;
		for(int i=1;i<rows;i++) 
			if (!isWithinBounds(row-i,col+i) || (i==1 && !areNodesEqual(enemy,row-i,col+i)) || 
					flankingPositionsHelper(row-i,col+i,node,v) || !enemy.equals(getNodeAt(row-i,col+i))) break;

		// Check pieces to left of row,col
		for(int i=1;i<cols;i++)	
			if (!isWithinBounds(row,col-i) || (i==1 && !areNodesEqual(enemy,row,col-i)) || 
					flankingPositionsHelper(row,col-i,node,v) || !enemy.equals(getNodeAt(row,col-i))) break;
		// Check pieces to right of row,col
		for(int i=1;i<cols;i++)
			
			if (!isWithinBounds(row,col+i) || (i==1 && !areNodesEqual(enemy,row,col+i)) || 
					flankingPositionsHelper(row,col+i,node,v) || !enemy.equals(getNodeAt(row,col+i))) break;
		// Check below move
		for(int i=1;i<rows;i++) 
			if(!isWithinBounds(row-i,col) || (i==1 && !areNodesEqual(enemy,row-i,col)) || 
					flankingPositionsHelper(row-i,col,node,v) || !enemy.equals(getNodeAt(row-i,col))) break;
		// Check above move
		for(int i=1;i<rows;i++) 
			if(!isWithinBounds(row+i,col) || (i==1 && !areNodesEqual(enemy,row+i,col)) || 
					flankingPositionsHelper(row+i,col,node,v) || !enemy.equals(getNodeAt(row+i,col))) break;

		//System.out.println("Pos:"+pos+"Flank:"+v);		
		return v;
	}
	
	/**
	 * Helper for the flankingPositions method
	 * 
	 * @param row - the given row
	 * @param col - the given column
	 * @param node - the given node 
	 * @param v - the given vector for moves to be stored in
	 * @return match - whether or not the given node matches the given index (row,col)
	 */
	public boolean flankingPositionsHelper(int row, int col, String node, Vector v){
		String index = makeIndex(row,col); 
		boolean match = getNodeAt(index).equals(node);
		if(match) {
			v.add(index);
		}
		return match;
	}
	
	/**
	 * Check whether or not the node is the player's or the AI's (its own node).
	 * 
	 * @param node - the node given
	 * @param rows - number of rows
	 * @param cols - number of columns
	 * @return true or false - whether the specified node is its own
	 */
	public boolean areNodesEqual(String node, int rows, int cols) {
		return node.equals(getNodeAt(rows,cols));
	}
	
	/**
	 * Retrieve the node at the specified location.
	 * 
	 * @param rows - the number of rows
	 * @param cols - the number of columns
	 * @return the node at the specified location
	 */
	public String getNodeAt(int rows, int cols) {
		return getNodeAt(makeIndex(rows,cols));
	}
	
	/**
	 * Check whether two arbitrary node locations' values are equal to each other.
	 *  
	 * @param pos1 - the first position
	 * @param pos2 - the second position
	 * @return true or false - whether or not the positions contain the same node value
	 */
	public boolean arePositionsEqual(String pos1, String pos2) {
		return getNodeAt(pos1).equals(getNodeAt(pos2));
	}
	
	/**
	 * Determines and keeps track of the available moves for the current turn 
	 * which is then used to perform the ideal move (for the AI).
	 * 
	 * @param move - the specified move given by the player
	 * @param node - the node information
	 * @param moves - a vector that contains the possible moves available
	 * @return count - the number of moves available
	 */
	public int countAdjacentNodes(String move, String node, Vector moves) {
		int count = 0;
		// TODO: This will be used for the heuristics
		return count;
	}
	
	/**
	 * Will return the node information located in the specified location.
	 * 
	 * @param move - the specified location
	 * @return the node at the specified location
	 */
	public String getNodeAt(String move) {
		String k;
		Enumeration e = board.keys();
		while(e.hasMoreElements()){
			k = (String) e.nextElement();
			if(k.equals(move))
				return (String) board.get(k);
		}
		return "Error";
	}
	
	/**
	 * Check whether a node is available for a valid move.
	 * 
	 * @param move - the specified location
	 * @return true or false - whether or not the move is valid
	 */
	public boolean isFree(String move) {
		return this.isWithinBounds(move) && getNodeAt(move).equals(getNode("default"));
	}
	
	/**
	 * Switches turns after the player is done with their turn.
	 */
	public void nextPlayer() {
		toMove = (toMove.equals(players[0])) ? players[1] : players[0];
	}
	
	/**
	 * Create a hash index using the specified row and column position.
	 * 
	 * @param row - row position
	 * @param col - column position
	 * @return a string with the given values
	 */
	public String makeIndex(int row, int col) {
		return makeIndex(String.valueOf(row),String.valueOf(col));
	}
	
	/**
	 * Put the given row and column values into a string format.
	 * 
	 * @param row - the row position
	 * @param col - the column position
	 * @return the row and column in "row,column" format
	 */
	public String makeIndex(String row, String col){
		return row + "," + col;
	}
	
	/**
	 * Retrieve the row value from the string given.
	 * 
	 * @param move - the given string
	 * @return the row value
	 */
	public int getRow(String move) {
		return Integer.parseInt(move.substring(0,move.indexOf(",")));
	}
	
	/**
	 * Retrieve the column value from the string given.
	 * 
	 * @param move - the given string
	 * @return the column value
	 */
	public int getCol(String move) {
		int comma = move.indexOf(",");
		return Integer.parseInt(move.substring(comma+1,move.length()));
	}
	
	/**
	 * Check to make sure the given location is not out of bounds.
	 * 
	 * @param move - the given location
	 * @return true or false - whether the location is in bounds or not
	 */
	public boolean isWithinBounds(String move){
		return isWithinBounds(getRow(move),getCol(move));
	}
	
	/**
	 * Determines whether the given location is out of bounds.
	 * 
	 * @param row - the row position
	 * @param col - the column position
	 * @return true or false - whether the location is in bounds or not
	 */
	public boolean isWithinBounds(int row, int col) {
		String k;
		Enumeration e = board.keys();
		while(e.hasMoreElements()){
			k = (String) e.nextElement();
			if(k.equals(makeIndex(row,col))){
				return true;
			}
				
		}
		return false;
	}
	
	/**
	 * Resets the node information at the specified location.
	 * 
	 * @param row - the row position
	 * @param col - the col position
	 * @throws Exception - required for the setNode method
	 */
	public void restoreDefault(int row, int col) throws Exception {
		setNode(row,col,"default");
	}
	
	/**
	 * Set the node information at the specified location (for the isFull method).
	 * 
	 * @param row - the row position
	 * @param col - the column position
	 * @param key - the node location in the board hashtable
	 * @throws Exception - for out of bounds error
	 */
	public void setNode(int row, int col, String key) throws Exception {
		String index = makeIndex(row,col);
		if(!isWithinBounds(index)) {
			throw new Exception("Error: Out of Bounds");
		}
		board.put(index,nodes.get(key));
	}
	
	/**
	 * Retrieve the node information at the specified location (for the isFull method).
	 * 
	 * @param move - the specified location
	 * @return the node information of the specified location
	 */
	public String getNode(String move) {
		String k;
		Enumeration e = nodes.keys();
		while(e.hasMoreElements()){
			k = (String) e.nextElement();
			if(k.equals(move)) {
				return (String) nodes.get(k);
			}
		}
		return "Error";
	}
	
	/**
	 * Set the board using the given hashtable.
	 * 
	 * @param hashtable - the given hashtable
	 */
	public void setBoard(Hashtable hashtable) {
		board = hashtable;
	}

	/**
	 * Set the number of columns.
	 * 
	 * @param n - the specified number of columns
	 */
	public void setCols(int n) {
		cols = n;
	}

	/**
	 * Set the number of rows.
	 * 
	 * @param n - the specified number of rows
	 */
	public void setRows(int n) {
		rows = n;
	}
	
	/**
	 * Set the nodes given the hashtable.
	 * 
	 * @param hashtable - the given hashtable
	 */
	public void setNodes(Hashtable hashtable) {
		nodes = hashtable;
	}

	/**
	 * Sets whose turn it is.
	 * 
	 * @param player - the player's whose turn it is
	 */
	public void setToMove(String player) {
		toMove = player;
	}
	
	/**
	 * Retrieve the board.
	 * 
	 * @return board - the board
	 */
	public Hashtable getBoard() {
		return board;
	}

	/**
	 * Retrieve the columns.
	 * 
	 * @return cols - the columns
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * Retrieve the rows.
	 * 
	 * @return rows - the rows
	 */
	public int getRows() {
		return rows;
	}
	
	/**
	 * Retrieve the nodes.
	 * 
	 * @return nodes - the nodes
	 */
	public Hashtable getNodes() {
		return nodes;
	}

	/**
	 * Retrieve whose turn it is.
	 * 
	 * @return toMove - whose turn it is
	 */
	public String getToMove() {
		return toMove;
	}
	
	/**
	 * Retrieve the player whose turn it is not.
	 * 
	 * @return the opponent's turn
	 */
	public String getNotToMove() {
		return (toMove != players[0]) ? players[0] : players[1];
	}
	
	/**
	 * Retrieve the player's names.
	 * 
	 * @param i - the player to be returned
	 * @return the name of the player
	 */
	public String getPlayerName(int i){
		return players[i];
	}
	
	/**
	 * Displays the board and all the node information.
	 */
	public void display(){
		System.out.println("\nBoard - To Move: " + toMove + "\n"); // Whose turn it is
		String boardline;
		System.out.print("  "); // Graphical spacing
		for(int j = 0; j < cols; j++){
			System.out.print(" " + j ); // Column values
		}
		System.out.println(""); // Graphical spacing
		//for(int i = rows-1; i >= 0; i--) {
		for(int i = 0; i <= 7; i++) {
			boardline=" ";
			for(int j = 0; j < cols; j++) {
				boardline += getNodeAt(makeIndex(i,j)) + " "; // Node information
			}
			System.out.println((i) + " " + boardline); // Row values
		}
	}
	
	/**
	 * Check whether or not there are any valid moves left (and if the game is complete).
	 * 
	 * @return true or false - whether or not there are any valid moves left
	 */
	public boolean isFull(){
		for(int r = 0; r < rows; r++) {
			for(int c = 0;c < cols; c++) {
				if(getNodeAt(r,c).equals(getNode("default"))) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Make a copy of the current board.
	 * 
	 * @return board - the copied board
	 */
	public Object makeCopy() {
		Board board = new Board(this.rows, this.cols);
		board.setBoard((Hashtable) this.getBoard().clone());
		board.setRows(this.rows); // Rows
		board.setCols(this.cols); // Columns
		board.setNodes(this.nodes); // Nodes
		board.setToMove(this.toMove); // Whose turn it is
		return board;
	}
}