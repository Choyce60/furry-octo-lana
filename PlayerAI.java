/**
 * PlayerAI Class
 * Creates a computer player that randomly chooses a move based on the valid moves
 * @author Jeremy Choyce
 * @author Jonathon To
 * @version 3.25.15
 */

import java.util.Vector;

public class PlayerAI extends Player
{
	private int maxDepth = 4;
	private Evaluator evaluator = new Evaluator();
	//creates a mock AI player for a user to play against.
	public PlayerAI(String name, String node)
	{
		super(name, node);
	}
	
	// the mock AI makes a random move based upon the all the valid move that are left
	public String getMove(Game dataFromGame, Board dataFromBoard)
	{
		dataFromBoard.display();
		Vector v = dataFromGame.legalMoves(dataFromBoard);
		int index = (int) Math.floor(Math.random() * v.size());
		
		return (String) v.get(index); 
	}
	
	public String getMove1(Game game1, Board currentBoard)
	{
		currentBoard.display();
		Vector v = game1.legalMoves(currentBoard);
		Vector finalMove = new Vector();
		Integer finalScore = new Integer(-1);
		Integer alpha = new Integer(Integer.MIN_VALUE);
		Integer beta = new Integer(Integer.MAX_VALUE);
		maxDecision(game1, 0, finalScore, finalMove, alpha, beta);
		
		
		return (String) finalMove.get(0);
	}
	
	private void maxDecision(Game game, int depth, Integer finalScore, Vector finalMove, Integer alpha, Integer beta) 
	{
		if (depth >= maxDepth) 
		{
			finalScore = evaluator.evaluate(game,game.getCurrentState());
		}
		else 
		{
			Vector legalMoves = game.legalMoves(game.getCurrentState());
			if (legalMoves.size() == 0) 
			{
				finalScore = evaluator.evaluate(game,game.getCurrentState());
			}
			else {
				int maxScore = Integer.MIN_VALUE;
				int bestMove = -1;
				for (int i = 0; i < legalMoves.size(); i++) {
					Game newGame = game;
					newGame.makeMove((String) legalMoves.get(i), newGame.getCurrentState());
					Integer score = new Integer(0);
					Vector newMove = new Vector();
					minDecision(newGame, depth + 1, score, finalMove, alpha, beta);
					if (score >= beta) {
						return;
					}
					if (score > maxScore) {
						maxScore = score;
						bestMove = i;
					}
					if (score > alpha) {
						alpha = score;
					}
				}
				finalScore = maxScore;
				finalMove.add(legalMoves.get(bestMove));
			}
		}
	}
			
			private void minDecision(Game game, int depth, Integer finalScore, Vector finalMove, Integer alpha, Integer beta) 
			{
				if (depth >= maxDepth) {
					finalScore = evaluator.evaluate(game,game.getCurrentState());
				}
				else {
					Vector legalMoves = game.legalMoves(game.getCurrentState());
					if (legalMoves.size() == 0) {
						finalScore = evaluator.evaluate(game,game.getCurrentState());
					}
					else {
						int minScore = Integer.MAX_VALUE;
						int bestMove = -1;
						for (int i = 0; i < legalMoves.size(); i++) {
							Game newGame = game;
							newGame.makeMove((String) legalMoves.get(i), newGame.getCurrentState());
							Integer score = new Integer(0);
							Vector newMove = new Vector();
							minDecision(newGame, depth + 1, score, finalMove, alpha, beta);
							if (score <= alpha) {
								return;
							}
							if (score < minScore) {
								minScore = score;
								bestMove = i;
							}
							if (score < beta) {
								score = beta;
							}
							
						}
						finalScore = minScore;
						finalMove.add(legalMoves.get(bestMove));
					}
				}
			}
	}
