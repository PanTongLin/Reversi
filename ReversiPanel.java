package pack;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import pack.comp.*;

public class ReversiPanel extends JPanel implements MouseListener
{
    private final int GRIDSIZE = 100;//the size of each grid is 800/8=100

    private ReversiBoard reversiBoard;
    private ReversiDisk reversiDisk;
    private Graphics boardGraphics;
	public ConnectFrame connectFrame;
    private boolean needPutMark = false;
    private int markX = -1;
    private int markY = -1;
    private Insets insets;
    private JLabel hintJLabel;


    public ReversiPanel()
    {
        reversiBoard = new ReversiBoard();
        reversiDisk = new ReversiDisk();
        addMouseListener(this);
        setBackground(new Color(91,67,55));
        setSize(803,801);
        setLayout(null);

        insets = this.getInsets();
        hintJLabel = new JLabel(reversiDisk.getHintFrameImageIcon());
        this.add(hintJLabel);
        hintJLabel.setVisible(false);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        boardGraphics = g;
        drawPanel();
    }

    public void drawPanel()
    {
        drawGrids();
        if(needPutMark)
            drawPlaceMark(markX,markY);

        ReversiFrame.setScore(reversiBoard.getScore(ReversiBoard.Grid.black),reversiBoard.getScore(ReversiBoard.Grid.white));
    }

    public void drawGrids()
    {
        for(int i=0; i<=8; i++)
        {
            boardGraphics.drawLine(i*GRIDSIZE,0,i*GRIDSIZE,800);//draw virtical lines of grids
            boardGraphics.drawLine(0,i*GRIDSIZE,800,i*GRIDSIZE);//draw horizontal lines of grids
        }

        for (int i=0; i<8; i++)
            for(int j=0; j<8; j++)
            {
                if(reversiBoard.getBoardStatus(i,j) == ReversiBoard.Grid.black)
                {
                    boardGraphics.drawImage(reversiDisk.getDiskImageIcon(Color.BLACK).getImage(),i*GRIDSIZE,j*GRIDSIZE,GRIDSIZE,GRIDSIZE,null);

                }
                else if(reversiBoard.getBoardStatus(i,j) == ReversiBoard.Grid.white)
                {
                    boardGraphics.drawImage(reversiDisk.getDiskImageIcon(Color.WHITE).getImage(),i*GRIDSIZE,j*GRIDSIZE,GRIDSIZE,GRIDSIZE,null);

                }
            }
    }

    public void drawPlaceMark(int i,int j)
    {
        boardGraphics.drawImage(reversiDisk.getDiskFrameImageIcon().getImage(),i*GRIDSIZE,j*GRIDSIZE,GRIDSIZE,GRIDSIZE,null);
    }

    public void setPlaceMark(int i,int j)
    {
        needPutMark = true;
        markX = i;
        markY = j;
    }

    public void mouseClicked(MouseEvent e)
    {
        int x = e.getX()/100;//get index of grid on the board
        int y = e.getY()/100;

		if(ReversiFrame.getMode()==ReversiFrame.Mode.Connect)
		{
			if(connectFrame.isMyTurn()&& reversiBoard.placeable(x,y,connectFrame.getGrid()))
			{
				reversiBoard.placeOnBoard(x,y,connectFrame.getGrid());
                reversiBoard.changeTurn();
				connectFrame.sendMove(x,y);
                setPlaceMark(x,y);
			}
		}
		else
		{
			if(reversiBoard.isBlackTurn && reversiBoard.placeable(x,y,ReversiBoard.Grid.black))
			{
				reversiBoard.placeOnBoard(x,y,ReversiBoard.Grid.black);
                reversiBoard.changeTurn();
                setPlaceMark(x,y);
                hintJLabel.setVisible(false);

				if(ReversiFrame.getMode() == ReversiFrame.Mode.PVE)
				{
					repaint();
					computerMove();
					return;
				}

			}
			else if(!reversiBoard.isBlackTurn && reversiBoard.placeable(x,y,ReversiBoard.Grid.white))
            {
                reversiBoard.placeOnBoard(x,y,ReversiBoard.Grid.white);
                reversiBoard.changeTurn();
                setPlaceMark(x,y);
            }

		}

        repaint();

        gameJudgment();

    }

    public void mouseEntered(MouseEvent e)
    {

    }

    public void mouseExited(MouseEvent e)
    {

    }

    public void mousePressed(MouseEvent e)
    {

    }

    public void mouseReleased(MouseEvent e)
    {

    }

    public void newGame()
    {
        reversiBoard.reset();
        needPutMark = false;
        hintJLabel.setVisible(false);
        repaint();
    }

    public void computerMove()
    {
        if(reversiBoard.computer.findMove(reversiBoard, ReversiBoard.Grid.white, ReversiFrame.getDifficultyNumber()))
        {
            reversiBoard.placeOnBoard(reversiBoard.computer.getXMove(),reversiBoard.computer.getYMove(),ReversiBoard.Grid.white);
            reversiBoard.changeTurn();
            setPlaceMark(reversiBoard.computer.getXMove(),reversiBoard.computer.getYMove());
        }
        repaint();
        gameJudgment();
    }

    public void gameJudgment()
    {
        if(reversiBoard.isEndGame())
        {
            showWinner();
            newGame();

        }
        else if(reversiBoard.needPass(reversiBoard.isBlackTurn ? ReversiBoard.Grid.black : ReversiBoard.Grid.white))
        {
            showPass();
            reversiBoard.changeTurn();

            if(ReversiFrame.getMode() == ReversiFrame.Mode.PVE && !reversiBoard.isBlackTurn)//when game mode is PVE and is computer's turn
                computerMove();


        }
    }

    public void showWinner()
    {
        String winnerString = "";
        if(reversiBoard.getScore(ReversiBoard.Grid.black) == reversiBoard.getScore(ReversiBoard.Grid.white))
            JOptionPane.showMessageDialog(null, "Tie");
        else
        {
            if(reversiBoard.getScore(ReversiBoard.Grid.black) > reversiBoard.getScore(ReversiBoard.Grid.white))
            {
                if(ReversiFrame.getMode() == ReversiFrame.Mode.PVP || ReversiFrame.getMode() == ReversiFrame.Mode.Connect)
                    winnerString = "Black";
                else if(ReversiFrame.getMode() == ReversiFrame.Mode.PVE)
                    winnerString = "You";
            }
            else if(reversiBoard.getScore(ReversiBoard.Grid.black) < reversiBoard.getScore(ReversiBoard.Grid.white))
            {
                if(ReversiFrame.getMode() == ReversiFrame.Mode.PVP || ReversiFrame.getMode() == ReversiFrame.Mode.Connect)
                    winnerString = "White";
                else if(ReversiFrame.getMode() == ReversiFrame.Mode.PVE)
                    winnerString = "Computer";
            }


            JOptionPane.showMessageDialog(null, winnerString + " won");
        }
    }

    public void showPass()
    {
        String passPlayerString = "";
        if(ReversiFrame.getMode() == ReversiFrame.Mode.PVP || ReversiFrame.getMode() == ReversiFrame.Mode.Connect)
        {
            if(reversiBoard.isBlackTurn)
                passPlayerString = "Black";
            else
                passPlayerString = "White";
        }

        else if(ReversiFrame.getMode() == ReversiFrame.Mode.PVE)
        {
            if(reversiBoard.isBlackTurn)
                passPlayerString = "You";
            else
                passPlayerString = "Computer";
        }

        JOptionPane.showMessageDialog(null, passPlayerString + " pass");

    }

    public void showHint()
    {
        int hintX = -1,hintY = -1;

        if(reversiBoard.computer.findMove(reversiBoard, ReversiBoard.Grid.black, 6)) //hint's strength is equal to "Hard"
        {
            hintX = reversiBoard.computer.getXMove();
            hintY = reversiBoard.computer.getYMove();
            hintJLabel.setVisible(true);
            hintJLabel.setBounds(insets.left + hintX*GRIDSIZE, insets.top + hintY*GRIDSIZE, GRIDSIZE, GRIDSIZE);
            repaint();
        }

    }

	public ReversiBoard getReversiBoard()
	{
		return reversiBoard;
	}

	public void setConnectFrame(ConnectFrame connectFrame)
	{
		this.connectFrame=connectFrame;
	}
}
