package ru.rintd.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

import ru.rintd.controller.AppPreferences;

public class PropertiesFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5338029151316600807L;
	
	private JButton save = new JButton("Save");
	private JButton cancel = new JButton("Cancel");
	private NumberFormatter format = new NumberFormatter(NumberFormat.getInstance());
	private JFormattedTextField windWidth = new JFormattedTextField(format);
	private JFormattedTextField windHeight = new JFormattedTextField(format);
	private JCheckBox isGrid = new JCheckBox("");
	
	private JPanel jPanel = new JPanel();

	public PropertiesFrame(AppPreferences appPreferences) {

		format.setValueClass(Integer.class);
		//format.setMinimum(2);
		//format.setMaximum(4);
		//format.setCommitsOnValidEdit(true);
		
		this.setContentPane(jPanel);
		this.setSize(new Dimension(300, 300));
		this.setVisible(false);

		windHeight.setText("" + appPreferences.windowHeight);
		windWidth.setText(""+appPreferences.windowWidth);
		if (appPreferences.planGrid)
			isGrid.setSelected(true);
		else
			isGrid.setSelected(false);
		
		setElements();

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);

			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
				//super.mouseClicked(e);
			}
		});
		
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					appPreferences.windowHeight = (int) format.stringToValue(windHeight.getText());
					appPreferences.windowWidth = (int) format.stringToValue(windWidth.getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				appPreferences.planGrid = isGrid.isSelected();
				
			}
		});
	}

	private void setElements() {

		GridLayout gridLayout = new GridLayout(0, 2);
		jPanel.setLayout(gridLayout);
		
		jPanel.add(new JLabel("Window width: "));
		jPanel.add(windWidth);
		jPanel.add(new JLabel("Window height: "));
		jPanel.add(windHeight);
		jPanel.add(new JLabel("Show grid: "));
		jPanel.add(isGrid);
		
		jPanel.add(save);
		jPanel.add(cancel);
		
	}
	
}
