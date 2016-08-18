package pack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ReversiFrame extends JFrame
{
    public enum Mode{PVE,PVP,Connect};
    public enum Difficulty{Easy,Medium,Hard};
    private static Mode mode = Mode.PVE;
    private static Difficulty difficulty = Difficulty.Medium;
    private static JLabel black_scoreJLabel,white_scoreJLabel;
    private ReversiPanel reversiPanel;
    private JMenuBar menuBar;
    private JMenu optionJMenu,modeJMenu,difficultyJMenu;
    private JMenuItem new_gameJMenuItem,hintJMenuItem,chatBoxJMenuItem;
    private ButtonGroup modeButtonGroup,difficultyButtonGroup;
    private JRadioButtonMenuItem PVEJRadioButtonMenuItem,PVPJRadioButtonMenuItem,ConnectJRadioButtonMenuItem,difficultyJRadioButtonMenuItem;
	private ConnectFrame connectFrame;
	private ReversiFrame thisFrame;

    public ReversiFrame()
    {
        super("Reversi");
		thisFrame=this;

        reversiPanel = new ReversiPanel();
        //reversiPanel.setFocusable(true);

        //setup score panel
        black_scoreJLabel = new JLabel("2");//game start with 2 black disks and 2 white disks
        black_scoreJLabel.setForeground(Color.BLACK);
        black_scoreJLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        white_scoreJLabel = new JLabel("2");
        white_scoreJLabel.setForeground(Color.WHITE);
        JPanel scoreBarPanel = new JPanel();
        white_scoreJLabel.setFont(new Font("Dialog", Font.BOLD, 20));;
        scoreBarPanel.setBackground(Color.GRAY);
        scoreBarPanel.setLayout(new FlowLayout());
        scoreBarPanel.add(black_scoreJLabel);
        scoreBarPanel.add(white_scoreJLabel);

        reversiPanel.setMinimumSize(new Dimension(803,801));
        scoreBarPanel.setMinimumSize(new Dimension(803, 30));

        //setup the menu bar
        menuConstructer();
        this.setJMenuBar(menuBar);

        this.add(reversiPanel);
        this.add(scoreBarPanel,BorderLayout.SOUTH);

		//setup the application frame
		addWindowListener
		(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent event)
				{
					if(connectFrame!=null&&connectFrame.isVisible())
						connectFrame.disconnect();
					dispose();
					System.exit(0);
				}
			}
		);
        setSize(803,900);
        setVisible(true);
        setResizable(false);
    }

    private void menuConstructer()
    {
        optionJMenu = new JMenu("Option");

        ActionListener newActionListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
				if(mode == Mode.Connect)
				{
					showGiveUpDialog();
				}
				else
					reversiPanel.newGame();
            }
        };
        new_gameJMenuItem = new JMenuItem("New");
        optionJMenu.add(new_gameJMenuItem).addActionListener(newActionListener);
		
		ActionListener chatActionListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
				if(mode == Mode.Connect)
				{
					connectFrame.setVisible(true);
				}
            }
        };
        chatBoxJMenuItem = new JMenuItem("ChatBox");
        optionJMenu.add(chatBoxJMenuItem).addActionListener(chatActionListener);

        ActionListener modeActionListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JMenuItem source = (JMenuItem)(e.getSource());
                if(source.getText() == "PVE")
                {
					if(mode==Mode.Connect)
					{
						if(showGiveUpDialog()==0)
						{
							connectFrame.disconnect();
							mode = Mode.PVE;
						}
					}
					else
						mode = Mode.PVE;
				}
                else if(source.getText() == "PVP")
                {
					if(mode==Mode.Connect)
					{
						if(showGiveUpDialog()==0)
						{
							connectFrame.disconnect();
							mode = Mode.PVP;
						}
					}
					else
					mode = Mode.PVP;
				}
                else if(source.getText() == "Connect")
				{
					mode = Mode.Connect;
					connectFrame=new ConnectFrame(thisFrame,reversiPanel);
					reversiPanel.setConnectFrame(connectFrame);
				}
				reversiPanel.newGame();
            }
        };

        modeJMenu = new JMenu("Mode");
        optionJMenu.add(modeJMenu);
        modeButtonGroup = new ButtonGroup();
        PVEJRadioButtonMenuItem = new JRadioButtonMenuItem("PVE",true);
        modeButtonGroup.add(PVEJRadioButtonMenuItem);
        modeJMenu.add(PVEJRadioButtonMenuItem).addActionListener(modeActionListener);
        PVPJRadioButtonMenuItem = new JRadioButtonMenuItem("PVP");
        modeButtonGroup.add(PVPJRadioButtonMenuItem);
        modeJMenu.add(PVPJRadioButtonMenuItem).addActionListener(modeActionListener);
        ConnectJRadioButtonMenuItem = new JRadioButtonMenuItem("Connect");
        modeButtonGroup.add(ConnectJRadioButtonMenuItem);
        modeJMenu.add(ConnectJRadioButtonMenuItem).addActionListener(modeActionListener);

        ActionListener difficultyActionListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JMenuItem source = (JMenuItem)(e.getSource());
                if(source.getText() == "Easy")
                    difficulty = Difficulty.Easy;
                else if(source.getText() == "Medium")
                    difficulty = Difficulty.Medium;
                else if(source.getText() == "Hard")
                    difficulty = Difficulty.Hard;
            }
        };

        difficultyJMenu = new JMenu("Difficulty");
        optionJMenu.add(difficultyJMenu);
        difficultyButtonGroup = new ButtonGroup();
        difficultyJRadioButtonMenuItem = new JRadioButtonMenuItem("Easy");
        difficultyButtonGroup.add(difficultyJRadioButtonMenuItem);
        difficultyJMenu.add(difficultyJRadioButtonMenuItem);
        difficultyJRadioButtonMenuItem = new JRadioButtonMenuItem("Medium",true);
        difficultyButtonGroup.add(difficultyJRadioButtonMenuItem);
        difficultyJMenu.add(difficultyJRadioButtonMenuItem);
        difficultyJRadioButtonMenuItem = new JRadioButtonMenuItem("Hard");
        difficultyButtonGroup.add(difficultyJRadioButtonMenuItem);
        difficultyJMenu.add(difficultyJRadioButtonMenuItem);

        menuBar = new JMenuBar();
        menuBar.add(optionJMenu);

        ActionListener hintActionListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(mode == Mode.PVE)
                    reversiPanel.showHint();

            }
        };
        hintJMenuItem = new JMenuItem("Hint");
        hintJMenuItem.addActionListener(hintActionListener);
        menuBar.add(hintJMenuItem);

    }

    public static void setScore(int blackScore,int whiteScore)
    {
        black_scoreJLabel.setText(""+blackScore);
        white_scoreJLabel.setText(""+whiteScore);
    }

    public static int getDifficultyNumber()
    {
        switch(difficulty)
        {
            case Easy:
                return 1;
            case Medium:
                return 3;
            case Hard:
                return 6;
            default:
                return 3;
        }
    }

    public static Mode getMode()
    {
        return mode;
    }

	public int showGiveUpDialog()
	{
		if(JOptionPane.showConfirmDialog(null,"Do you want to give up?","giveUpMessage",JOptionPane.YES_NO_OPTION)==0)
		{
			connectFrame.sendGiveUp();
			JOptionPane.showMessageDialog(null, "You lose");
			reversiPanel.newGame();
			return 0;
		}
		return 1;
	}

	public void resetMode()
	{
		modeButtonGroup.setSelected(PVEJRadioButtonMenuItem.getModel(),true);
		mode = Mode.PVE;
	}

}
