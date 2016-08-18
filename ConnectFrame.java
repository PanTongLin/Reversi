package pack;

import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import pack.comp.*;

public class ConnectFrame extends JFrame
{
	private ConnectFrame thisFrame;
	private ServerThread server;
	private ClientThread client;
	private CardLayout card;
	private JPanel connectPanel;
	private JPanel serverPanel;
	private JPanel clientPanel;
	private JPanel chatPanel;
	private JTextArea chatArea;
	private JTextField speakArea;
	private JScrollPane chatAreaScrollPane;
	private JLabel serverLabel;
	private JTextField clientText;
	private JButton serverButtom;
	private JButton clientButtom;
	private JButton serverCancelButtom;
	private JButton clientConnectButtom;
	private JButton clientCancelButtom;
	private ReversiBoard.Grid grid;
	private ReversiBoard reversiBoard;
    private ReversiPanel reversiPanel;
	private ReversiFrame reversiFrame;
	private Container container;
	boolean isServer;


	public ConnectFrame(ReversiFrame reversiFrame,ReversiPanel reversiPanel)
	{
		this.reversiFrame=reversiFrame;
		this.reversiPanel=reversiPanel;
		this.reversiBoard=reversiPanel.getReversiBoard();
		thisFrame=this;
		container = getContentPane();
		server=new ServerThread(8000,thisFrame);
		server.start();
		server.suspend();

		//setup connect panel

		connectPanel=new JPanel();
		serverButtom=new JButton("wait for Opponent");
		serverButtom.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					isServer=true;
					server.resume();
					try
					{serverLabel.setText("     your IP:"+InetAddress.getLocalHost().getHostAddress()+"     wait for connecting...");}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
					card.show(container, "serverPanel");
				}
			}
		);
		clientButtom=new JButton("connect to Opponent");
		clientButtom.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					isServer=false;
					card.show(container, "clientPanel");
				}
			}
		);
		connectPanel.setLayout(new GridLayout(2, 1,10,10));
		connectPanel.add(serverButtom);
		connectPanel.add(clientButtom);

		//setup server panel

		serverPanel=new JPanel();
		serverLabel=new JLabel();
		serverCancelButtom=new JButton("cancel");
		serverCancelButtom.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					server.suspend();
					card.show(container, "connectPanel");
				}
			}
		);
		serverPanel.setLayout(new GridLayout(2,1,10,10));
		serverPanel.add(serverLabel);
		serverPanel.add(serverCancelButtom);

		//setup client panel

		clientPanel=new JPanel();
		clientText=new JTextField(15);
		clientText.setText("replace with your opponent's IP");
		clientText.addMouseListener
		(
			new MouseListener()
			{
				public void mouseClicked(MouseEvent e){clientText.setText("");}
				public void mouseEntered(MouseEvent e){}
				public void mouseExited(MouseEvent e){}
				public void mousePressed(MouseEvent e){}
				public void mouseReleased(MouseEvent e){}
			}
		);
		clientConnectButtom=new JButton("connect");
		clientConnectButtom.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					client=new ClientThread(clientText.getText(), 8000,thisFrame);
					client.start();
				}
			}
		);
		clientCancelButtom=new JButton("cancel");
		clientCancelButtom.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					card.show(container, "connectPanel");
				}
			}
		);
		clientPanel.setLayout(new GridLayout(3, 1,10,10));
		clientPanel.add(clientText);
		clientPanel.add(clientConnectButtom);
		clientPanel.add(clientCancelButtom);
		
		//setup chatPanel
		
		chatPanel=new JPanel();
		chatArea=new JTextArea(10,23);
		chatArea.setEditable(false);
		chatAreaScrollPane=new JScrollPane(chatArea);
		chatAreaScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		speakArea=new JTextField(23);
		speakArea.addKeyListener
		(
			new KeyListener()
			{
				public void keyTyped(KeyEvent e) {}
				public void keyPressed(KeyEvent e) 
				{
					if(e.getKeyCode()==KeyEvent.VK_ENTER)
					{
						if(isServer)
							server.sendMessage(speakArea.getText());
						else
							client.sendMessage(speakArea.getText());
						speakArea.setText("");
					}
				}
				public void keyReleased(KeyEvent e) {}
			}
		);
		chatPanel.add(chatAreaScrollPane);
		chatPanel.add(speakArea);

		//connectFrame setting

		card=new CardLayout();
		setLayout(card);
		add(connectPanel,"connectPanel");
		add(serverPanel,"serverPanel");
		add(clientPanel,"clientPanel");
		add(chatPanel,"chatPanel");
		addWindowListener
		(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent event)
				{
					reversiFrame.resetMode();
					server.stop();
					dispose();
				}
			}
		);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(300,300);
		setVisible(true);
	}

	public void setGrid(ReversiBoard.Grid grid)
	{
		this.grid=grid;
	}

	public ReversiBoard.Grid getGrid()
	{
		return grid;
	}

	public ReversiBoard.Grid getOpponentGrid()
	{
		if(grid==ReversiBoard.Grid.black)
			return ReversiBoard.Grid.white;
		else
			return ReversiBoard.Grid.black;
	}

	public boolean isMyTurn()
	{
		return (reversiBoard.isBlackTurn&&getGrid()==ReversiBoard.Grid.black)||(!reversiBoard.isBlackTurn&&getGrid()==ReversiBoard.Grid.white);
	}

	public void sendMove(int x,int y)
	{
		if(isServer)
			server.sendMove(x,y);
		else
			client.sendMove(x,y);
	}

	public void placeOnBoard(int x,int y,ReversiBoard.Grid grid)
	{
		reversiBoard.placeOnBoard(x,y,grid);
		reversiBoard.changeTurn();
		reversiPanel.setPlaceMark(x,y);
	}

	public void repaintReversiPanel()
	{
		reversiPanel.repaint();
	}
	public void gameJudgment()
	{
		reversiPanel.gameJudgment();
	}

	public void sendGiveUp()
	{
		if(isServer)
			server.sendGiveUp();
		else
			client.sendGiveUp();
	}

	public void newGame()
	{
		reversiPanel.newGame();
	}

	public void disconnect()
	{
		setVisible(false);
		if(isServer)
			server.disconnect();
		else
			client.disconnect();
	}

	public void closeFrame()
	{
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	public void showChatPanel()
	{
		card.show(container, "chatPanel");
	}
	
	public void appendChatArea(String text)
	{
		chatArea.append(text+"\n");
		chatAreaScrollPane.getVerticalScrollBar().setValue(chatAreaScrollPane.getVerticalScrollBar().getMaximum()+1);
	}
}
