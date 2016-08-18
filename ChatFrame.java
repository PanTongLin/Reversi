package pack;

import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import pack.comp.*;
import java.io.*;
import java.util.Date;;

public class ChatFrame extends JFrame
{
	DataInputStream input;
	DataOutputStream output;
	JPanel chatPanel;
	JPanel outPanel;
	JTextArea chatText;
	JTextArea outText;
	JButton enterButton;
	JScrollPane chatTextScrollPane;
	
	public ChatFrame(DataInputStream input,DataOutputStream output)
	{
		this.input=input;
		this.output=output;
		chatPanel=new JPanel();
		chatText=new JTextArea(10,25);
		chatText.setEditable(false);
		chatTextScrollPane=new JScrollPane(chatText);
		chatPanel.add(chatTextScrollPane);
		outPanel=new JPanel();
		outText=new JTextArea(3,20);
		enterButton=new JButton("enter");
		enterButton.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					
				}
			}
		);
		outPanel.setLayout(new BorderLayout());
		outPanel.add(outText,BorderLayout.CENTER);
		outPanel.add(enterButton,BorderLayout.EAST);
		
		setLayout(new BorderLayout());
		add(chatPanel,BorderLayout.CENTER);
		add(outPanel,BorderLayout.SOUTH);
		setSize(300,300);
		setVisible(true);
	}
}