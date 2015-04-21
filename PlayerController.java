/**
 * PlayerController Class 
 * Handles all the exceptions, user inputs and prompts
 * @author Jonathon To
 * @version 3.25.15
 */

import java.io.*;
import java.util.Vector;

public class PlayerController extends Player
	{
	public PlayerController(String name, String node)
	{
		super(name,node);
	}
	
	public String getMove(Game dataFromGame, Board dataFromBoard)
	{
		boolean isValidMove = false;
		String move;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		Vector legal = dataFromGame.legalMoves(dataFromBoard);
		dataFromGame.display(dataFromBoard);
		
		//prompt the user for input and show the current legal moves
		System.out.println("\nMake your move or enter 'quit' to exit the game. \nMoves you can make: " + legal);
		
		try
		{			
			move = in.readLine();
			if(move.equals("quit")) {
				System.out.println("Exiting Program");
				System.exit(1); // quits out of the game			
		}
			else
			{
				for(int i=0;i<legal.size();i++)
				{
					isValidMove = isValidMove || ((String)legal.get(i)).equals(move);
				}

				if(!isValidMove)
				{
					System.out.println("Can't move there, try again.");				
					move = getMove(dataFromGame, dataFromBoard);	
				}
			}
		}

		catch(IOException e)
		{
			System.out.println("Can't move there, try again.");
			move = getMove(dataFromGame, dataFromBoard);
		}

		return move;	
	}	
}