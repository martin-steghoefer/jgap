/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */

package org.jgap.gui;

import java.util.*;
import javax.swing.*;
import org.jgap.*;
import info.clearthought.layout.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GUI for the JGAP Configurator.
 * @author Siddhartha Azad.
 */
public class ConfigFrame extends JFrame implements IConfigInfo {
	
	/**
	 * Constructor
	 * @param title The title of the frame.
	 * */
	ConfigFrame(String title) {
		super(title);
		panels = new ArrayList();
		textProps = new ArrayList();
		listProps = new ArrayList();
		listGroups = new ArrayList();
		textGroups = new ArrayList();
		cbl = new ConfigButtonListener(this);
	}
	
	/**
	 * Does the initial setup of the JFrame and shows it.
	 * @param _conHandler The configuration handler from which this 
	 * ConfigFrame
	 * would get information.
	 * @author Siddhartha Azad.
	 */
	public void createAndShowGUI(ConfigurationHandler _conHandler) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		conHandler = _conHandler;
		
		// display
		this.pack();
		this.setVisible(true);
		this.setBounds(100, 100, 300, 300);
		this.setSize(500, 300);
		this.setup();
		this.show();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Getter for the Configuration Information on this frame.
	 * @return The ConfigData object containing the configuration 
	 * information
	 * on this frame.
	 * */
	public ConfigData getConfigData() {
		ConfigData cd = new ConfigData();
		// add lists
		ArrayList values;
		try {
			for(Iterator lIter = listGroups.iterator(); lIter.hasNext();) {
				ListGroup lg = (ListGroup)lIter.next();
				values = new ArrayList();
				for(Enumeration e = lg.getOutListModel().elements() ;
					e.hasMoreElements() ;) {
					String val = (String)e.nextElement();
					values.add(val);
				}
				cd.addListData(lg.getProp().getName(), values);
			}
			
			// add textFields
			TextGroup tg;
			for(Iterator tIter = textGroups.iterator(); tIter.hasNext();) {
				tg = (TextGroup)tIter.next();
				cd.addTextData(tg.getProp().getName(), tg.getTextField().getText());
			}
		}
		catch(ClassCastException cex) {
			cex.printStackTrace();
		}
		return cd;
	}
	/**
	 * Get the config file to write to.
	 * @return The config file name to write to.
	 * @author Siddhartha Azad.
	 */
	public String getFileName() {
		String fName = fileName.getText();
		// use a default file name
		if(fName.equals(""))
			fName = "jgap.con";
		return fName;
	} 
	
	
	/**
	 * Setup the GUI.
	 * There are 3 maximum panels at this time. The first one contains JLists if
	 * there are configurable values that can be choosen from a list of items.
	 * The second panel contains all values configurable via a JTextField. The
	 * third panel contains the filename and configure button.
	 * @author Siddhartha Azad.
	 */
	private void setup() {
		int numLists = 0, numTexts = 0;
		ArrayList props = conHandler.getConfigProperties();
		for(Iterator iter = props.iterator(); iter.hasNext();) {
			try {
				ConfigProperty prop = (ConfigProperty)iter.next();
				if(prop.getWidget().equals("JList")) {
					numLists++;
					listProps.add(prop);
				}
				else if(prop.getWidget().equals("JTextField")) {
					numTexts++;
					textProps.add(prop);
				}
				else {
					// Only JLists and JTextFields allowed at this point
					System.err.println("ConfigFrame::setup():Invalid Widget name "
							+ prop.getWidget());
				}
			}
			catch(ClassCastException cex) {
				cex.printStackTrace();
			}
		}
		// If no known widgets are present, a GUI cannot be rendered
		if(numLists == 0 && numTexts == 0) {
			JOptionPane.showMessageDialog( null ,
					"No Configurable Properties in this Configuration",
					"Configuration Information",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		// 2 panels at least, 1 for the widgets and 1 in the end for the
		// Frame specific buttons
		int numPanels = 2;
		if(numLists > 0 && numTexts > 0)
			numPanels = 3;
		// add the appropriate number of panels
		addWidgets(numPanels, numLists, numTexts);
	}
	
	
	/**
	 * Add the widgets to the frame.
	 * @param numPanels Number of panels to add.
	 * @param numLists Number of lists to add.
	 * @param numTexts Number of text boxes to add.
	 * */
	private void addWidgets(int numPanels, int numLists, int numTexts) {
		try {
			int numRows = numPanels;
			numPanels = 3;
			// TableLayout setup for the panels on the frame
			double [][] tableArray = new double[2][numPanels];
			double perPanel = (double)(1.0/(double)numPanels);
			int i = 0;
			for(i = 0; i < numPanels-1; i++)
				tableArray[1][i] = perPanel;
			// give the remaining space to the last row
			tableArray[1][i] = TableLayout.FILL;
			// single column can take all the space available
			tableArray[0][0] = TableLayout.FILL;
			this.getContentPane().setLayout(new TableLayout(tableArray));
			// add the panels to the frame now
			int panelsAdded = 0;
			JPanel panel;
			// if we have lists to add
			if(numLists > 0) {
				double panelSize[][];
				// for every input list there's an output list and the buttons
				// hence 3 columns for every list
				int numCols = 3 * numLists;
				// TableLayout setup for the list panel
				panelSize = new double [2][numCols];
				double space = (double)(1.0/(double)numLists);
				// 40% space to the lists, 20% to the buttons
				double listSpace = space * 0.4;
				double buttonSpace = space * 0.2;
				for(int itr = 0; itr < numLists; itr++) {
					panelSize[0][3*itr] = listSpace;
					panelSize[0][3*itr+1] = buttonSpace;
					panelSize[0][3*itr+2] = listSpace;
				}
				// single row can take all the space
				panelSize[1][0] = TableLayout.FILL;
				listPanel = new JPanel();
				panels.add(listPanel);
				listPanel.setLayout(new TableLayout(panelSize));
				this.getContentPane().add(listPanel, new TableLayoutConstraints(
						0, panelsAdded, 0, panelsAdded, 
						TableLayout.FULL, TableLayout.FULL));
				// increment number of panels added
				panelsAdded++;
				// add the lists to the panel
				Iterator iter = listProps.iterator(), valIter;
				ConfigProperty prop;
				ListGroup lg;
				for(int itr1 = 0; itr1 < numLists && iter.hasNext(); itr1++) {
					lg = new ListGroup();
					listGroups.add(lg);
					prop = (ConfigProperty)iter.next();
					lg.setProp(prop);
					listPanel.add(lg.getListScroller(),
							new TableLayoutConstraints(3*itr1, 0, 3*itr1, 0, 
							TableLayout.CENTER, TableLayout.CENTER));
					// add the button to move data from outlist back to list
					listPanel.add(lg.getLButton(),
							new TableLayoutConstraints(3*itr1+1, 0, 3*itr1+1, 0, 
							TableLayout.CENTER, TableLayout.TOP));
					// add the button to move data from list to outlist
					listPanel.add(lg.getRButton(),
							new TableLayoutConstraints(3*itr1+1, 0, 3*itr1+1, 0, 
							TableLayout.CENTER, TableLayout.BOTTOM));
					// added the item values to the list
					valIter = prop.getValuesIter();
					while(valIter.hasNext())
						lg.getListModel().addElement(valIter.next());
					listPanel.add(lg.getOutListScroller(),
							new TableLayoutConstraints(3*itr1 + 2 , 0,
							3*itr1 + 2, 0, 
							TableLayout.CENTER, TableLayout.CENTER));
				}
			}
			
			// add the textFields
			
			if(numTexts > 0) {
				double panelSize[][];
				int numCols = numTexts*2;
				panelSize = new double [2][numCols];
				// TableLayout setup for the JTextFields panel
				double perText = (double)(1.0/(double)numCols);
				int itr = 0;
				// add the panel for the texts fields
				for(itr = 0; itr < numCols-1; itr++)
					panelSize[0][itr] = perText;
				panelSize[0][itr] = TableLayout.FILL;
				// single row
				panelSize[1][0] = TableLayout.FILL;
				textPanel = new JPanel();
				panels.add(textPanel);
				textPanel.setLayout(new TableLayout(panelSize));
				this.getContentPane().add(textPanel, new TableLayoutConstraints(
						0, panelsAdded, 0, panelsAdded, 
						TableLayout.FULL, TableLayout.FULL));
				panelsAdded++;
				// add the text fields to the panel
				TextGroup tg;
				Iterator iter = textProps.iterator(), valIter;
				ConfigProperty prop;
				for(int itr1 = 0; itr1 < numTexts && iter.hasNext(); itr1++) {
					tg = new TextGroup();
					textGroups.add(tg);
					prop = (ConfigProperty)iter.next();
					tg.setProp(prop);
					JLabel label = tg.getLabel();
					label.setText(prop.getName());
					textPanel.add(label,
							new TableLayoutConstraints(itr1, 0, itr1, 0, 
							TableLayout.RIGHT, TableLayout.CENTER));
					textPanel.add(tg.getTextField(),
							new TableLayoutConstraints(itr1+1, 0, itr1+1, 0, 
							TableLayout.LEFT, TableLayout.CENTER));
				}
			}
			// add the configure button
			double panelSize[][];
			panelSize = new double [2][2];
			// percentage per column for the tablelayout
			panelSize[0][0] = .5;
			panelSize[0][1] = .5;
			// single row
			panelSize[1][0] = TableLayout.FILL;
			configPanel = new JPanel();
			panels.add(configPanel);
			configPanel.setLayout(new TableLayout(panelSize));
			this.getContentPane().add(configPanel, new TableLayoutConstraints(
					0, panelsAdded, 0, panelsAdded, 
					TableLayout.FULL, TableLayout.FULL));
			// add the textfield for the config file name
			fileName = new JTextField("jgap.con");
			configPanel.add(fileName,
					new TableLayoutConstraints(0, 0, 0, 0, 
					TableLayout.RIGHT, TableLayout.CENTER));
			configButton = new JButton("Generate");
			configButton.addActionListener(cbl);
			configPanel.add(configButton,
					new TableLayoutConstraints(1, 0, 1, 0, 
					TableLayout.LEFT, TableLayout.CENTER));
			
		}
		catch(Exception ex) {
			JOptionPane.showMessageDialog( null ,
					"Exception"+ex.toString(),
					"This is the title",
					JOptionPane.INFORMATION_MESSAGE);
			ex.printStackTrace();
		}
	}
	
	/**
	 * This class groups the property data structure along with the JLists
	 * associated with it.
	 * @author Siddhartha Azad
	 * */
	public class ListGroup {
		/**
		 * Constructor responsible for creating all items that go on the list
		 * panel.
		 */
		ListGroup() {
			// create the List of values
			listModel = new DefaultListModel();
			list = new JList(listModel);
			list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			list.setVisibleRowCount(-1);
			listScroller = new JScrollPane(list);
			listScroller.setPreferredSize(new Dimension(250, 80));
	
			// create the output list
			outListModel = new DefaultListModel();
			outList= new JList(outListModel);
			outList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			outList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			outList.setVisibleRowCount(-1);
			outListScroller = new JScrollPane(outList);
			outListScroller.setPreferredSize(new Dimension(250, 80));
			
			// The buttons to move data to/from outList
			listBL = new ListButtonListener(this);
			lButton = new JButton("<-");
			lButton.addActionListener(listBL);
			rButton = new JButton("->");
			rButton.addActionListener(listBL);
		}
		
		/**
		 * Getter for the ConfigProperty object associated with this ListGroup.
		 * @return The ConfigProperty object associated with this ListGroup.
		 * @author Siddhartha Azad.
		 * */
		public ConfigProperty getProp() {
			return prop;
		} 
		
		/**
		 * Setter for the ConfigProperty object associated with this ListGroup.
		 * This object is used to retrieve the values that the list is initialized
		 * with.
		 * @param _prop The ConfigProperty object associated with this ListGroup.
		 * @author Siddhartha Azad.
		 * */
		public void setProp(ConfigProperty _prop) {
			prop = _prop;
		}
		
		/**
		 * Getter for the list.
		 * @return The JList containing the items to select from.
		 * @author Siddhartha Azad.
		 * */
		public JList getList() {
			return list;
		}
		
		/**
		 * Getter for the list's associated model.
		 * @return DefaultListModel for the list.
		 * @author Siddhartha Azad.
		 * */
		public DefaultListModel getListModel() {
			return listModel;
		}
		
		/**
		 * Getter for the scroller for the list.
		 * @return scroller for the list.
		 * @author Siddhartha Azad.
		 * */
		public JScrollPane getListScroller() {
			return listScroller;
		}
			
		/**
		 * Getter for the output list
		 * @return Output JList.
		 * @author Siddhartha Azad.
		 * */
		public JList getOutList() {
			return outList;
		}
		
		/**
		 * Getter for the output list's associated model.
		 * @return DefaultListModel for the output list.
		 * @author Siddhartha Azad.
		 * */
		public DefaultListModel getOutListModel() {
			return outListModel;
		}
		
		/**
		 * Getter for the scroller for the output list.
		 * @return scroller for the output list.
		 * @author Siddhartha Azad.
		 * */
		public JScrollPane getOutListScroller() {
			return outListScroller;
		}
		
		/**
		 * Return left button for this ListGroup
		 * @return The button to move items from outlist to list.
		 * @author Siddhartha Azad.
		 * */
		public JButton getLButton() {
			return lButton;
		}
		
		/**
		 * Return right button for this ListGroup
		 * @return The button to move items from list to outlist.
		 * @author Siddhartha Azad.
		 * */
		public JButton getRButton() {
			return rButton;
		}
		
		/**
		 * Move selected items from the output list back to the list.
		 * @author Siddhartha Azad.
		 * */
		public void leftButtonPressed() {
			int indices [] = outList.getSelectedIndices();
			for(int i = 0; i < indices.length; i++) {
				String removed = (String)outListModel.remove(indices[0]);
				listModel.addElement(removed);
			}
		}
		
		/**
		 * Move selected items from list to the output list.
		 * @author Siddhartha Azad.
		 */
		public void rightButtonPressed() {
			int indices [] = list.getSelectedIndices();
			for(int i = 0; i < indices.length; i++) {
				String removed = (String)listModel.remove(indices[0]);
				outListModel.addElement(removed);
			}
		}
		
		// list that will display the available items
		private JList list;
		// model for list
		private DefaultListModel listModel;
		public JScrollPane listScroller;
		// list that will display the selected items
		private JList outList;
		// model for outList
		private DefaultListModel outListModel;
		private JScrollPane outListScroller;
		// buttons to move data to/from lists
		private JButton lButton;
		private JButton rButton;
		// property object associated with this ListGroup
		private ConfigProperty prop;
		private ListButtonListener listBL;
		
	}
	
	/**
	 * This class groups the property data structure along with the JLists
	 * associated with it.
	 * @author Siddhartha Azad
	 * */
	class TextGroup {
		TextGroup() {
			textField = new JTextField(20);
			label = new JLabel(); 
		}
		
		public ConfigProperty getProp() {
			return prop;
		} 
		
		public void setProp(ConfigProperty _prop) {
			prop = _prop;
		}
		
		public JTextField getTextField() {
			return textField;
		}
		
		public JLabel getLabel() {
			return label;
		}
		
		private JTextField textField;
		private JLabel label;
		private ConfigProperty prop;
	}
	
	class ConfigButtonListener implements ActionListener {
		ConfigButtonListener(ConfigFrame _frame) {
			frame = _frame;
		}
		public void actionPerformed(ActionEvent e) {
			// configButton is pressed
			ConfigWriter.instance().write(frame);
		}
		ConfigFrame frame;
		
	}
	
	public class ListButtonListener implements ActionListener {
		ListButtonListener(ListGroup _lg) {
			lg = _lg;
		}
		public void actionPerformed(ActionEvent e) {
			// one of the list buttons is pressed
			if(e.getActionCommand().equals("<-")) {
				// from outList to list
				lg.leftButtonPressed();
			}
			else {
				// from list to outList
				lg.rightButtonPressed();
			}
			
		}
		private ListGroup lg;
		
	}
	
	// data memebers of class ConfigFrame
	private ConfigurationHandler conHandler;
	// list of JPanel objects added in this frame
	private ArrayList panels;
	// ListBox properties
	ArrayList listProps;
	// TextBox properties
	ArrayList textProps;
//	 list of ListGroups
	ArrayList listGroups;
//	 list of TextGroups
	ArrayList textGroups;
	private JPanel listPanel;
	private JPanel textPanel;
	private JPanel configPanel;
	private JButton configButton;
	private ConfigButtonListener cbl;
	private JTextField fileName;
}
