public class Evaluator
{
        public int evaluate(Game gamedata, Board dataFromBoard)
        {
                int score = 0;
                if (gamedata.gameOverTest(dataFromBoard))
                {
                        // disks difference
                        score = gamedata.count[0] - gamedata.count[1];
                }
                else
                {
                        // mobility
                        int humanMobility = gamedata.count[0];
                        int cpuMobility = gamedata.count[1];
                        int mobility = humanMobility - cpuMobility;
                        // stability
                        int humanStability = 0;
                        int cpuStability = 0;
                        for (int i = 0; i < 8; i++)
                        {
                                for (int j = 0; j < 8; j++)
                                {
                                        if (dataFromBoard.getNodeAt(i, j).charAt(0) != '.')
                                        {
                                        	if((stableDirection(gamedata, dataFromBoard, i, j, 1, 0) || stableDirection(gamedata, dataFromBoard,  i, j, -1, 0) ) && 
                                    				(stableDirection(gamedata, dataFromBoard,  i, j, 0, 1) || stableDirection(gamedata, dataFromBoard,  i, j, 0, -1) ) &&
                                                     (stableDirection(gamedata, dataFromBoard,  i, j, 1, 1) || stableDirection(gamedata, dataFromBoard,  i, j, -1, -1) ) &&
                                                     (stableDirection(gamedata, dataFromBoard,  i, j, 1, -1) || stableDirection(gamedata, dataFromBoard,  i, j, -1, 1) ) )
                                                {
                                                
                                                        if (dataFromBoard.getNodeAt(i, j).charAt(0) == 'o')
                                                        	cpuStability++;
                                                        else
                                                        	humanStability++; 
                                                }
                                        }
                                }
                        }
                        int stability = humanStability - cpuStability;
                        // disks difference
                        int disksDiff = gamedata.count[0] - gamedata.count[1]; //count[1] is the cpu ai
                        // total score
                        score = (100 * mobility) + (10 * stability) + (1 * disksDiff);
                }
                return score;
        }
        
        private boolean stableDirection(Game gamedata, Board dataFromBoard, int i, int j, int x, int y)
        {
                
                boolean stable = true;
                while (i+x < 8 && //board size is 8
                	   j+y < 8 &&
                	   i+x >= 0 &&
                	   j+y >= 0 && stable) 
                
                {
                	
                		if (dataFromBoard.getNodeAt(i+x, j+y).charAt(0) != 'o')
                        {
                                stable = false;
                        }
                        
                        i = i + x;
                        j = j + y;
                }
                return stable;
        }

}
