package pack.comp;


public class ReversiBoard
{
    public enum Grid{space,black,white};//define three status on the board grids
    private Grid board[][] = new Grid[8][8];
    public Computer computer;
    public boolean isBlackTurn;
    public ReversiBoard()
    {
        initial();
        computer = new Computer(this);
    }

    private void initial()//initial the board status
    {
        for(int i=0; i<8; i++) //don't use enhance for here, something terrible will happen QQ(FxxK u enhance for)
            for(int j=0; j<8; j++)
                board[i][j] = Grid.space;
        board[3][4] = Grid.black;
        board[4][3] = Grid.black;
	    board[3][3] = Grid.white;
        board[4][4] = Grid.white;

        isBlackTurn = true;
    }

    public Grid[][] getBoard()
    {
        return board;
    }

    public Grid getBoardStatus(int i,int j) //return status on grid(i,j)
    {
        return board[i][j];
    }

    public int getScore(Grid player) //caculate and return the score of the player
    {
        int score = 0;
        for(Grid row[] : board)
            for(Grid grid : row)
                if(grid == player)
                    score++;
        return score;
    }

    public void placeOnBoard(int i,int j,Grid status)//place on grid(i,j) and call changeturn
    {
        board[i][j]=status;
        revereBetween(i,j,status);
        //changeTurn();
    }

    public void revereBetween(int i,int j,Grid turn)//reverse the opponent's disks between your disks
    {
		int directions[][]={{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1}};

		for(int[] direction:directions)
		{
			int x=i+direction[0],y=j+direction[1];

			while(x>=0&&x<8&&y>=0&&y<8&&board[x][y]!=turn&&board[x][y]!=Grid.space)
			{
				x+=direction[0];
				y+=direction[1];
			}
			if(x>=0&&x<8&&y>=0&&y<8&&board[x][y]==turn)
				do
				{
					board[x][y]=turn;
					x-=direction[0];
					y-=direction[1];
				}while(board[x][y]!=turn);
		}
    }

    public void changeTurn()
    {
        isBlackTurn = !isBlackTurn;
    }

    public void reset()
    {
        initial();
    }

    public boolean placeable(int i,int j,Grid turn)//check whether grid(i,j) can be placed by this player
    {
		int directions[][]={{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1}};

        if(board[i][j] != Grid.space)
            return false;
		for(int[] direction:directions)
		{
			int x=i+direction[0],y=j+direction[1];

			if(x>=0&&x<8&&y>=0&&y<8&&board[x][y]==turn)
				continue;
			while(x>=0&&x<8&&y>=0&&y<8&&board[x][y]!=turn&&board[x][y]!=Grid.space)
			{
				x+=direction[0];
				y+=direction[1];
			}
			if(x>=0&&x<8&&y>=0&&y<8&&board[x][y]==turn)
					return true;
		}
        return false;
    }

    public boolean isEndGame()
    {
        boolean isBoardFull = true;
        for(Grid row[] : board)
            for(Grid grid : row)
                if(grid != Grid.space)
                {
                    isBoardFull = false;
                    break;
                }

        if(isBoardFull)
            return true;
        else if(needPass(Grid.black) && needPass(Grid.white)) //if both side can't have next step
            return true;
        else
            return false;
    }

    public boolean needPass(Grid turn)
    {
        for(int i=0; i<8; i++)
            for(int j=0; j<8; j++)
                if(board[i][j] == Grid.space)
                    if(placeable(i,j,turn))
                        return false;

        return true;

    }
}
