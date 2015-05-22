package ru.rintd.view;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * подключение к сети
 * @author sheihar
 *
 */
public class CorbaConnectorFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4807354058685285315L;
	
	private JTextField addresField = new JTextField("127.0.0.1");
	private JTextField portField = new JTextField("38011");
	private JButton connect = new JButton("Connect");
	
	public CorbaConnectorFrame(){
		JPanel jPanel = new JPanel();
		this.setContentPane(jPanel);
		GridLayout gridLayout = new GridLayout(0, 2);
		jPanel.setLayout(gridLayout);

		jPanel.add(new JLabel("Addres: "));
		jPanel.add(addresField);
		jPanel.add(new JLabel("Port: "));
		jPanel.add(portField);
		jPanel.add(connect);
	}

	public JTextField getAddresField() {
		return addresField;
	}

	public JTextField getPortField() {
		return portField;
	}
	
	public void setConnectAction(ActionListener actionListener){
		connect.addActionListener(actionListener);
	}
	
}
