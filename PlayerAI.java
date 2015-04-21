/**
 * PlayerAI Class
 * Creates a computer player that randomly chooses a move based on the valid moves
 * @author Jonathon To
 * @version 3.25.15
 */
import java.util.Vector;

public class PlayerAI extends Player
{
	//creates a mock AI player for a user to play against.
	public PlayerAI(String name, String node)
	{
		super(name, node);
	}
	
	// the mock AI makes a random move based upon the all the valid move that are left
	/*
	dataFromBoard.display();
	Vector v = dataFromGame.legalMoves(dataFromBoard);
	int index = (int) Math.floor(Math.random() * v.size());
	//System.out.println(dataFromBoard.getNodeAt(0, 0).charAt(0));
	return (String) v.get(index); 
	*/
	public String getMove(Game dataFromGame, Board dataFromBoard)
	{	        
	 //------------------------------------------------------------------------------
		final int maxDepth = 4;
        final Evaluator evaluator = new Evaluator();
		{
                String finalMove = "-1, -1";
                Integer finalScore = new Integer(-1);
                Integer alpha = new Integer(Integer.MIN_VALUE);
                Integer beta = new Integer(Integer.MAX_VALUE);
				
                if (game.getCurrentPlayer().getColor() == 'o')
                        maxDecision(dataFromGame, 0, finalScore, finalMove, alpha, beta);
                else
                        minDecision(dataFromGame, 0, finalScore, finalMove, alpha, beta);
						
                return finalMove;
        }
        
        private void maxDecision(Game dataFromGame, Board dataFromBoard, int depth, Integer finalScore, MoveI finalMove, Integer alpha, Integer beta)
		{
                if (depth >= maxDepth)
                        finalScore = evaluator.evaluate(dataFromGame, dataFromBoard);
                else
				{
                        Vector<MoveI> legalMoves = game.getLegalMoves(COLOR.DARK);
                        if (legalMoves.size() == 0)
                                finalScore = evaluator.evaluate(game);
                        else
						{
                                int maxScore = Integer.MIN_VALUE;
                                int bestMove = -1;
                                for (int i = 0; i < legalMoves.size(); i++)
								{
                                        GameI newGame = new Game(game);
                                        newGame.applyMove(legalMoves.get(i), false);
                                        Integer score = new Integer(0);
                                        MoveI move = new Move(-1, -1);
                                        minDecision(newGame, depth + 1, score, move, alpha, beta);
                                        if (score >= beta)
                                                return;
                                        else if (score > maxScore)
											{
                                                maxScore = score;
                                                bestMove = i;
											}
                                        else if (score > alpha)
                                                alpha = score;
                                }
                                finalScore = maxScore;
                                finalMove.setMove(legalMoves.get(bestMove).getMoveI(), legalMoves.get(bestMove).getMoveJ());
                        }
                }
        }
        
        private void minDecision(GameI game, int depth, Integer finalScore, MoveI finalMove, Integer alpha, Integer beta) {
                if (depth >= maxDepth) {
                        finalScore = evaluator.evaluate(game);
                }
                else {
                        Vector<MoveI> legalMoves = game.getLegalMoves(COLOR.LIGHT);
                        if (legalMoves.size() == 0) {
                                finalScore = evaluator.evaluate(game);
                        }
                        else {
                                int minScore = Integer.MAX_VALUE;
                                int bestMove = -1;
                                for (int i = 0; i < legalMoves.size(); i++)
                                {
                                        GameI newGame = new Game(game);
                                        newGame.applyMove(legalMoves.get(i), false);
                                        Integer score = new Integer(0);
                                        MoveI move = new Move(-1, -1);
                                        minDecision(newGame, depth + 1, score, move, alpha, beta);
                                        if (score <= alpha)
                                                return;
                                        if (score < minScore) {
                                                minScore = score;
                                                bestMove = i;
                                        }
                                        if (score < beta)
                                                score = beta;                                       
                                }
                                finalScore = minScore;
                                finalMove.setMove(legalMoves.get(bestMove).getMoveI(), legalMoves.get(bestMove).getMoveJ());
                        }
                }
        }
	
		return "0,2";
	//------------------------------------------------------------------------------
	}
}