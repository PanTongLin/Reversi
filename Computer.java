package pack.comp;

import java.util.Random;
import pack.ReversiFrame;

public class Computer
{
    private class FindMaxResult
    {
		int nb,nw,max;
	};
    private int xMove = 0;
    private int yMove = 0;
    private ReversiBoard reversiBoard;

    public Computer(ReversiBoard reversiBoard)
    {
        this.reversiBoard = reversiBoard;
    }

    //findMove() is used for first checking whether each grid placeable then call findMax() to caculate the "score" of this grid
    //after defining the depth that will be search according to the difficulty,and finally setting the step which will be moved
    //
    //findMax() is a recursive function that used for search the score with strategy() by assuming moving and predicting opponent's move
    //
    //strategy() caculate the value of a board's status
    public boolean findMove(ReversiBoard reversiBoard, ReversiBoard.Grid player, int level)
    {
        ReversiBoard.Grid tempBoard[][] = new ReversiBoard.Grid[8][8];
        int tempCounter[] = {reversiBoard.getScore(ReversiBoard.Grid.black),reversiBoard.getScore(ReversiBoard.Grid.white)};
        int nb, nw, min = Integer.MAX_VALUE, n_min = 1;
        boolean founded = false;
        FindMaxResult findMaxResult = new FindMaxResult();
        Random random = new Random();

        for(int i=0; i<8; i++)
            System.arraycopy(reversiBoard.getBoard()[i], 0, tempBoard[i], 0, 8);


        if(tempCounter[0] + tempCounter[1] >= 52 + level)
        {
            level = tempCounter[0] + tempCounter[1] -52;
            if(level >5)
                level = 5;
        }

        for(int i=0; i<8; i++)
            for(int j=0; j<8; j++)
                if(reversiBoard.placeable(i,j,player))
                {
                    reversiBoard.placeOnBoard(i,j,player);
                    if(player == ReversiBoard.Grid.white)
                        findMaxResult = findMax(level-1, ReversiBoard.Grid.black, player);
                    else //used for get hint
                        findMaxResult = findMax(level-1, ReversiBoard.Grid.white, player);

                    if(!founded || min > findMaxResult.max)
                    {
                        min = findMaxResult.max;
                        nw = findMaxResult.nw;
                        nb = findMaxResult.nb;
                        xMove = i;
                        yMove = j;
                        founded =true;
                    }
                    else if(min == findMaxResult.max)//if there are more then one choice, ramdomly pick
                    {
                        n_min++;
                        if(random.nextInt(n_min) == 0)
                        {
                            nw = findMaxResult.nw;
                            nb = findMaxResult.nb;
                            xMove = i;
                            yMove = j;
                        }
                    }
                    for (int k = 0 ; k < 8 ; k++)
						System.arraycopy(tempBoard[k], 0, reversiBoard.getBoard()[k], 0, 8);
                }
        return founded;
    }

    private FindMaxResult findMax(int level, ReversiBoard.Grid player, ReversiBoard.Grid opponent)
    {
        ReversiBoard.Grid tempBoard[][] = new ReversiBoard.Grid[8][8];
        int tempCounter[] = {reversiBoard.getScore(ReversiBoard.Grid.black),reversiBoard.getScore(ReversiBoard.Grid.white)};
        int min = Integer.MAX_VALUE,score,tnb,tnw;
        FindMaxResult findMaxResult = new FindMaxResult();

        for (int i = 0 ; i < 8 ; i++)
			System.arraycopy(reversiBoard.getBoard()[i], 0, tempBoard[i], 0, 8);

        findMaxResult.nb = tempCounter[0];
        findMaxResult.nw = tempCounter[1];

        level--;

        for(int i=0; i<8; i++)
            for(int j=0; j<8; j++)
                if(reversiBoard.placeable(i,j,player))
                {
                    reversiBoard.placeOnBoard(i,j,player);
                    if(level != 0)
                    {
                        FindMaxResult tFindMaxResult = findMax(level,opponent,player);
                        tnb = tFindMaxResult.nb;
                        tnw = tFindMaxResult.nw;
                        score = tFindMaxResult.max;
                    }
                    else
                    {
                        tnb = tempCounter[0];
                        tnw = tempCounter[1];
                        score = tempCounter[opponent.ordinal()-1] - tempCounter[player.ordinal()-1] + strategy(player,opponent);
                    }

                    if(min > score)
                    {
                        min = score;
                        findMaxResult.nb = tnb;
                        findMaxResult.nw = tnw;
                    }
                    for (int k = 0 ; k < 8 ; k++)
						System.arraycopy(tempBoard[k], 0,reversiBoard.getBoard()[k], 0, 8);
                    tempCounter[0] = reversiBoard.getScore(ReversiBoard.Grid.black);
                    tempCounter[1] = reversiBoard.getScore(ReversiBoard.Grid.white);
                }
        findMaxResult.max = -min;
        return findMaxResult;

    }

    private int strategy(ReversiBoard.Grid player, ReversiBoard.Grid opponent)
    {
        int strat = 0;
        ReversiBoard.Grid board[][] = reversiBoard.getBoard();
        for(int i=0; i<8; i++)
            if(board[i][0] == opponent) //opponent at left side
                strat++;
            else if(board[i][0] == player) //player at left side
                strat--;
        for(int i=0; i<8; i++)
            if(board[i][7] == opponent) //opponent at right side
                strat++;
            else if(board[i][7] == player) //player at right side
                strat--;
        for(int i=0; i<8; i++)
            if(board[0][i] == opponent) //opponent at top side
                strat++;
            else if(board[0][i] == player) //player at top side
                strat--;
        for(int i=0; i<8; i++)
            if(board[7][i] == opponent) //opponent at bottom side
                strat++;
            else if(board[7][i] == player) //player at bottom side
                strat--;

        return strat;
    }

    public int getXMove()
    {
        return xMove;
    }

    public int getYMove()
    {
        return yMove;
    }
}
