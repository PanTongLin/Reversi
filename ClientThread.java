package pack;

import java.io.*;
import java.net.Socket;
import javax.swing.*;
import pack.comp.*;

public class ClientThread extends Thread
{
    private Socket m_socket;
	private ConnectFrame connectFrame;
	private DataOutputStream output;
	private BufferedReader input;
	private ChatFrame chatFrame;
	private String name;
    
    public ClientThread(String ip, int port,ConnectFrame connectFrame)
    {
		this.connectFrame=connectFrame;
        try
        {
            m_socket = new Socket(ip, port);
        }
        catch (IOException e)
        {
			JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    @Override
    public void run()
    {
        try
        {
            if (m_socket != null)
            {
				JOptionPane.showMessageDialog(connectFrame,"Connect success!","connectInfo",JOptionPane.PLAIN_MESSAGE);
				connectFrame.setGrid(ReversiBoard.Grid.white);
				//connectFrame.setVisible(false);
				name=JOptionPane.showInputDialog("your name");
				connectFrame.showChatPanel();
                
                output= new DataOutputStream(m_socket.getOutputStream());
				input= new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
				
				
				
				while(ReversiFrame.getMode() == ReversiFrame.Mode.Connect)
				{
					String inputDate=input.readLine();
					//connectFrame.appendChatArea(inputDate);//
					if(inputDate!=null)
						switch(inputDate.charAt(0))
						{
							case '1':
								JOptionPane.showMessageDialog(null, "Your opponent give up\n You win");
								connectFrame.newGame();
								break;
							case '2':
								connectFrame.closeFrame();
								m_socket.close();
								JOptionPane.showMessageDialog(null, "Opponent disconnect");
								break;
							case '3':
								int inputInt=Integer.parseInt(inputDate.substring(1));
								connectFrame.placeOnBoard(inputInt/10,inputInt%10,connectFrame.getOpponentGrid());
								connectFrame.repaintReversiPanel();
								connectFrame.gameJudgment();
								break;
							case '4':
								connectFrame.appendChatArea(inputDate.substring(1));
								break;
						}
					
				}
				
                m_socket.close();
            }
        }
        catch (IOException e)
        {
            if(m_socket.isConnected())
				JOptionPane.showMessageDialog(null, e.getMessage());
			else
				JOptionPane.showMessageDialog(null, "Opponent disconnect");
        }
    }
	
	public void sendMove(int x,int y)
	{
		try
		{
			output.writeBytes("3"+x+y+"\n");
			output.flush();
		}
		catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
	}
	
	public void sendGiveUp()
	{
		try
		{
			output.writeBytes("1"+"\n");
			output.flush();
		}
		catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
	}
	
	public void disconnect()
	{
		try
		{
			output.writeBytes("2"+"\n");
			output.flush();
			m_socket.close();
		}
		catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
	}
	
	public void sendMessage(String text)
	{
		try
		{
			String sendText=name+":"+text;
			while(sendText.length()>35)
			{
				connectFrame.appendChatArea(sendText.substring(0,35));
				output.writeBytes("4"+sendText.substring(0,35)+"\n");
				output.flush();
				sendText=sendText.substring(35);
			}
			connectFrame.appendChatArea(sendText);
			output.writeBytes("4"+sendText+"\n");
			output.flush();
		}
		catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
	}
	
	public boolean isConnected()
	{
		return m_socket.isConnected();
	}
}