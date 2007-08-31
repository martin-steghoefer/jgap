/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gui;

import java.util.*;

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import org.jgap.data.config.*;

import info.clearthought.layout.*;

/**
 * GUI for the JGAP Configurator.
 *
 * @author Siddhartha Azad
 * @since 2.3
 */
public class ConfigFrame
    extends JFrame
    implements IConfigInfo {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.19 $";

  // data members of class ConfigFrame
  private Object m_conHandler;

  private boolean m_isRoot;

  // list of JPanel objects added in this frame
  private List m_panels;

  // ListBox properties
  private List m_listProps;

  // TextBox properties
  private List m_textProps;

  // list of ListGroups
  private List m_listGroups;

  // list of TextGroups
  private List m_textGroups;

  private JPanel m_listPanel;

  private JPanel m_textPanel;

  private JPanel m_configPanel;

  private JButton m_configButton;

  private ConfigButtonListener m_cbl;

  private JTextField m_fileName;

  private JButton m_configureButton;

  private JTextField m_configItem;

  private Configurable m_conObj;

  // the parent frame of this frame
  private ConfigFrame m_parent;

  // default name for the config file
  private static final String m_defaultConfigFile = "jgap.con";

  /**
   * Constructor
   * @param a_parent
   * @param a_title the title of the frame
   * @param a_isRoot
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  ConfigFrame(final ConfigFrame a_parent, final String a_title,
              final boolean a_isRoot) {
    super(a_title);
    m_panels = Collections.synchronizedList(new ArrayList());
    m_textProps = Collections.synchronizedList(new ArrayList());
    m_listProps = Collections.synchronizedList(new ArrayList());
    m_listGroups = Collections.synchronizedList(new ArrayList());
    m_textGroups = Collections.synchronizedList(new ArrayList());
    m_cbl = new ConfigButtonListener(this);
    m_isRoot = a_isRoot;
    m_parent = a_parent;
  }

  /**
   * Does the initial setup of the JFrame and shows it.
   * @param a_conHandler the configuration handler from which this ConfigFrame
   * would get information
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public void createAndShowGUI(final Object a_conHandler) {
    JFrame.setDefaultLookAndFeelDecorated(true);
    m_conHandler = a_conHandler;
    // display
    pack();
    setVisible(true);
    setBounds(100, 100, 300, 300);
    setSize(500, 300);
    try {
      MetaConfig mt = MetaConfig.getInstance();
    }
    catch (MetaConfigException mcEx) {
      JOptionPane.showMessageDialog(null,
                                    "Exception while parsing JGAP Meta"
                                    + " Config file "
                                    + mcEx.getMessage(),
                                    "Meta Config Exception",
                                    JOptionPane.ERROR_MESSAGE);
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(null,
                                    "Exception while parsing JGAP Meta Config"
                                    + " file "
                                    + ex.getMessage(),
                                    "Meta Config Exception",
                                    JOptionPane.ERROR_MESSAGE);
    }
    setup();
    show();
    if (m_isRoot) {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    else {
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
  }

  /**
   * Getter for the Configuration Information on this frame.
   * @return The ConfigData object containing the configuration
   * information on this frame
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public ConfigData getConfigData() {
    ConfigData cd = new ConfigData();
    cd.setNS(m_conHandler.getClass().getName());
    // add lists
    List values;
    try {
      Iterator lIter = m_listGroups.iterator();
      while (lIter.hasNext()) {
        ListGroup lg = (ListGroup) lIter.next();
        values = Collections.synchronizedList(new ArrayList());
        Enumeration e = lg.getOutListModel().elements();
        while (e.hasMoreElements()) {
          String val = (String) e.nextElement();
          values.add(val);
        }
        cd.addListData(lg.getProp().getName(), values);
      }
      // add textFields
      TextGroup tg;
      Iterator tIter = m_textGroups.iterator();
      while (tIter.hasNext()) {
        tg = (TextGroup) tIter.next();
        cd.addTextData(tg.getProp().getName(), tg.getTextField().getText());
      }
    }
    catch (ClassCastException cex) {
      JOptionPane.showMessageDialog(null, cex.getMessage(),
                                    "ConfigFrame.getConfigData():Configuration"
                                    + " Error",
                                    JOptionPane.INFORMATION_MESSAGE);
    }
    return cd;
  }

  /**
   * Get the config file to write to.
   * @return the config file name to write to
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public String getFileName() {
    // only the root frame has the text box for the filename
    if (m_isRoot) {
      String fName = m_fileName.getText();
      // use a default file name
      if (fName.equals("")) {
        fName = ConfigFrame.m_defaultConfigFile;
      }
      return fName;
    }
    else {
      return m_parent.getFileName();
    }
  }

  /**
   * Setup the GUI.
   * There are 3 maximum panels at this time. The first one contains JLists if
   * there are configurable values that can be choosen from a list of items.
   * The second panel contains all values configurable via a JTextField. The
   * third panel contains the filename and configure button.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  private void setup() {
    int numLists = 0, numTexts = 0;
    List props = null;
    try {
      /** @todo find a better way to get the classname than getNS() */
      props = MetaConfig.getInstance().getConfigProperty(m_conHandler.getClass().getName());
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(),
                                    "Configuration Error: Could not get"
                                    + " properties for class "
                                    + m_conHandler.getClass().getName(),
                                    JOptionPane.INFORMATION_MESSAGE);
    }
    if (props == null) {
      JOptionPane.showMessageDialog(null,
                                    "setup():No Configurable Properties in"
                                    + " this Configuration",
                                    "Configuration Message",
                                    JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    Iterator iter = props.iterator();
    while (iter.hasNext()) {
      try {
        ConfigProperty prop = (ConfigProperty) iter.next();
        if (prop.getWidget().equals("JList")) {
          numLists++;
          m_listProps.add(prop);
        }
        else if (prop.getWidget().equals("JTextField")) {
          numTexts++;
          m_textProps.add(prop);
        }
        else {
          // Only JLists and JTextFields allowed at this point
          JOptionPane.showMessageDialog(null,
                                        "Unknown Widget " + prop.getWidget(),
                                        "Configuration Error",
                                        JOptionPane.INFORMATION_MESSAGE);
        }
      }
      catch (ClassCastException cex) {
        JOptionPane.showMessageDialog(null,
                                      cex.getMessage(),
                                      "ConfigError.setup():Configuration Error:"
                                      + " Invalid cast",
                                      JOptionPane.INFORMATION_MESSAGE);
      }
    }
    // If no known widgets are present, a GUI cannot be rendered
    if (numLists == 0 && numTexts == 0) {
      JOptionPane.showMessageDialog(null,
                                    "No Configurable Properties in this"
                                    + " Configuration",
                                    "Configuration Information",
                                    JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    // 2 panels at least, 1 for the widgets and 1 in the end for the
    // Frame specific buttons
    int numPanels = 2;
    if (numLists > 0 && numTexts > 0) {
      numPanels = 3;
    }
    // add the appropriate number of panels
    addWidgets(numPanels, numLists, numTexts);
  }

  /**
   * Add the widgets to the frame.
   *
   * @param a_numPanels Number of panels to add
   * @param a_numLists Number of lists to add
   * @param a_numTexts Number of text boxes to add
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  private void addWidgets(int a_numPanels, final int a_numLists,
                          final int a_numTexts) {
    try {
      a_numPanels = 3;
      // TableLayout setup for the panels on the frame
      double[][] tableArray = new double[2][a_numPanels];
      double perPanel = (double) (1.0 / (double) a_numPanels);
      int i = 0;
      for (i = 0; i < a_numPanels - 1; i++) {
        tableArray[1][i] = perPanel;
      }
      // give the remaining space to the last row
      tableArray[1][i] = TableLayout.FILL;
      // single column can take all the space available
      tableArray[0][0] = TableLayout.FILL;
      getContentPane().setLayout(new TableLayout(tableArray));
      // add the panels to the frame now
      int panelsAdded = 0;
      // if we have lists to add
      if (a_numLists > 0) {
        double[][] panelSize;
        // for every input list there's an output list and the buttons
        // hence 3 columns for every list
        int numCols = 3 * a_numLists;
        // TableLayout setup for the list panel
        panelSize = new double[2][numCols];
        double space = (double) (1.0 / (double) a_numLists);
        // 40% space to the lists, 20% to the buttons
        double listSpace = space * 0.4;
        double buttonSpace = space * 0.2;
        for (int itr = 0; itr < a_numLists; itr++) {
          panelSize[0][3 * itr] = listSpace;
          panelSize[0][3 * itr + 1] = buttonSpace;
          panelSize[0][3 * itr + 2] = listSpace;
        }
        // single row can take all the space
        panelSize[1][0] = TableLayout.FILL;
        m_listPanel = new JPanel();
        m_panels.add(m_listPanel);
        m_listPanel.setLayout(new TableLayout(panelSize));
        getContentPane().add(m_listPanel, new TableLayoutConstraints(
            0, panelsAdded, 0, panelsAdded,
            TableLayout.FULL, TableLayout.FULL));
        // increment number of panels added
        panelsAdded++;
        // add the lists to the panel
        Iterator iter = m_listProps.iterator(), valIter;
        ConfigProperty prop;
        ListGroup lg;
        for (int itr1 = 0; itr1 < a_numLists && iter.hasNext(); itr1++) {
          lg = new ListGroup(this);
          m_listGroups.add(lg);
          prop = (ConfigProperty) iter.next();
          lg.setProp(prop);
          m_listPanel.add(lg.getListScroller(),
                          new TableLayoutConstraints(3 * itr1, 0, 3 * itr1, 0,
              TableLayout.CENTER, TableLayout.CENTER));
          // add the button to move data from outlist back to list
          m_listPanel.add(lg.getLButton(),
                          new TableLayoutConstraints(3 * itr1 + 1, 0,
              3 * itr1 + 1, 0,
              TableLayout.CENTER, TableLayout.TOP));
          // add the button to move data from list to outlist
          m_listPanel.add(lg.getRButton(),
                          new TableLayoutConstraints(3 * itr1 + 1, 0,
              3 * itr1 + 1, 0,
              TableLayout.CENTER, TableLayout.BOTTOM));
          // added the item values to the list
          valIter = prop.getValuesIter();
          while (valIter.hasNext()) {
            lg.getListModel().addElement(valIter.next());
          }
          m_listPanel.add(lg.getOutListScroller(),
                          new TableLayoutConstraints(3 * itr1 + 2, 0,
              3 * itr1 + 2, 0,
              TableLayout.CENTER, TableLayout.CENTER));
        }
      }
      // add the textFields
      if (a_numTexts > 0) {
        double[][] panelSize;
        int numCols = a_numTexts * 2;
        panelSize = new double[2][numCols];
        // TableLayout setup for the JTextFields panel
        double perText = (double) (1.0 / (double) numCols);
        int itr = 0;
        // add the panel for the texts fields
        for (itr = 0; itr < numCols - 1; itr++) {
          panelSize[0][itr] = perText;
        }
        panelSize[0][itr] = TableLayout.FILL;
        // single row
        panelSize[1][0] = TableLayout.FILL;
        m_textPanel = new JPanel();
        m_panels.add(m_textPanel);
        m_textPanel.setLayout(new TableLayout(panelSize));
        getContentPane().add(m_textPanel, new TableLayoutConstraints(
            0, panelsAdded, 0, panelsAdded,
            TableLayout.FULL, TableLayout.FULL));
        panelsAdded++;
        // add the text fields to the panel
        TextGroup tg;
        Iterator iter = m_textProps.iterator(), valIter;
        ConfigProperty prop;
        for (int itr1 = 0; itr1 < a_numTexts && iter.hasNext(); itr1++) {
          tg = new TextGroup();
          m_textGroups.add(tg);
          prop = (ConfigProperty) iter.next();
          tg.setProp(prop);
          JLabel label = tg.getLabel();
          label.setText(prop.getName());
          m_textPanel.add(label,
                          new TableLayoutConstraints(itr1, 0, itr1, 0,
              TableLayout.RIGHT, TableLayout.CENTER));
          m_textPanel.add(tg.getTextField(),
                          new TableLayoutConstraints(itr1 + 1, 0, itr1 + 1, 0,
              TableLayout.LEFT, TableLayout.CENTER));
        }
      }
      // add the configure button
      double[][] panelSize;
      panelSize = new double[2][4];
      // percentage per column for the tablelayout
      panelSize[0][0] = .25;
      panelSize[0][1] = .25;
      panelSize[0][2] = .25;
      panelSize[0][3] = .25;
      // single row
      panelSize[1][0] = TableLayout.FILL;
      m_configPanel = new JPanel();
      m_panels.add(m_configPanel);
      m_configPanel.setLayout(new TableLayout(panelSize));
      getContentPane().add(m_configPanel, new TableLayoutConstraints(
          0, panelsAdded, 0, panelsAdded,
          TableLayout.FULL, TableLayout.FULL));
      // add the textfield for the config file name
      m_configItem = new JTextField(50);
      m_configPanel.add(m_configItem,
                        new TableLayoutConstraints(0, 0, 0, 0,
          TableLayout.RIGHT,
          TableLayout.CENTER));
      m_configureButton = new JButton("Configure");
      m_configureButton.addActionListener(m_cbl);
      m_configPanel.add(m_configureButton,
                        new TableLayoutConstraints(1, 0, 1, 0,
          TableLayout.LEFT,
          TableLayout.CENTER));
      if (m_isRoot) {
        m_fileName = new JTextField("jgap.con");
        m_configPanel.add(m_fileName,
                          new TableLayoutConstraints(2, 0, 2, 0,
            TableLayout.RIGHT, TableLayout.CENTER));
        m_configButton = new JButton("Generate");
        m_configButton.addActionListener(m_cbl);
        m_configPanel.add(m_configButton,
                          new TableLayoutConstraints(3, 0, 3, 0,
            TableLayout.LEFT, TableLayout.CENTER));
      }
      else {
        m_configButton = new JButton("Save Configuration");
        m_configButton.addActionListener(m_cbl);
        m_configPanel.add(m_configButton,
                          new TableLayoutConstraints(3, 0, 3, 0,
            TableLayout.LEFT, TableLayout.CENTER));
      }
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(null,
                                    "Exception" + ex.toString(),
                                    "This is the title",
                                    JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * This class groups the property data structure along with the JLists
   * associated with it.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public class ListGroup {
    // list that will display the available items
    private JList m_list;

    // model for list
    private DefaultListModel m_listModel;

    private JScrollPane m_listScroller;

    // list that will display the selected items
    private JList m_outList;

    // model for outList
    private DefaultListModel m_outListModel;

    private JScrollPane m_outListScroller;

    private ConfigListSelectionListener m_outListListener;

    // buttons to move data to/from lists
    private JButton m_lButton;

    private JButton m_rButton;

    // property object associated with this ListGroup
    private ConfigProperty m_prop;

    private ListButtonListener m_listBL;

    private ConfigFrame m_frame;

    /**
     * Constructor responsible for creating all items that go on the list
     * panel.
     *
     * @author Siddhartha Azad
     * @since 2.3
     */
    ListGroup(final ConfigFrame a_frame) {
      m_frame = a_frame;
      // create the List of values
      m_listModel = new DefaultListModel();
      m_list = new JList(m_listModel);
      m_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
      m_list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
      m_list.setVisibleRowCount( -1);
      m_listScroller = new JScrollPane(m_list);
      m_listScroller.setPreferredSize(new Dimension(250, 80));
      // create the output list
      m_outListModel = new DefaultListModel();
      m_outList = new JList(m_outListModel);
      m_outList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      m_outList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
      m_outList.setVisibleRowCount( -1);
      m_outListScroller = new JScrollPane(m_outList);
      m_outListListener = new ConfigListSelectionListener(m_frame, m_outList);
      m_outList.getSelectionModel().addListSelectionListener(m_outListListener);
      m_outListScroller.setPreferredSize(new Dimension(250, 80));
      // The buttons to move data to/from outList
      m_listBL = new ListButtonListener(this);
      m_lButton = new JButton("<-");
      m_lButton.addActionListener(m_listBL);
      m_rButton = new JButton("->");
      m_rButton.addActionListener(m_listBL);
    }

    /**
     * Getter for the ConfigProperty object associated with this ListGroup.
     * @return the ConfigProperty object associated with this ListGroup
     *
     * @author Siddhartha Azad
     * @since 2.3
     */
    public ConfigProperty getProp() {
      return m_prop;
    }

    /**
     * Setter for the ConfigProperty object associated with this ListGroup.
     * This object is used to retrieve the values that the list is initialized
     * with.
     * @param a_prop the ConfigProperty object associated with this ListGroup
     *
     * @author Siddhartha Azad
     * @since 2.3
     */
    public void setProp(final ConfigProperty a_prop) {
      m_prop = a_prop;
    }

    /**
     * @return the JList containing the items to select from
     *
     * @author Siddhartha Azad
     * @since 2.3
     */
    public JList getList() {
      return m_list;
    }

    /**
     * @return DefaultListModel for the list
     *
     * @author Siddhartha Azad
     * @since 2.3
     */
    public DefaultListModel getListModel() {
      return m_listModel;
    }

    /**
     * @return scroller for the list
     *
     * @author Siddhartha Azad
     * @since 2.3
     */
    public JScrollPane getListScroller() {
      return m_listScroller;
    }

    /**
     * @return Output JList
     *
     * @author Siddhartha Azad
     * @since 2.3
     */
    public JList getOutList() {
      return m_outList;
    }

    /**
     * Getter for the output list's associated model.
     * @return DefaultListModel for the output list
     *
     * @author Siddhartha Azad
     * @since 2.3
     */
    public DefaultListModel getOutListModel() {
      return m_outListModel;
    }

    /**
     * @return scroller for the output list
     *
     * @author Siddhartha Azad
     * @since 2.3
     */
    public JScrollPane getOutListScroller() {
      return m_outListScroller;
    }

    /**
     * @return the button to move items from outlist to list
     *
     * @author Siddhartha Azad
     * @since 2.3
     */
    public JButton getLButton() {
      return m_lButton;
    }

    /**
     * @return the button to move items from list to outlist
     *
     * @author Siddhartha Azad
     * @since 2.3
     */
    public JButton getRButton() {
      return m_rButton;
    }

    /**
     * Move selected items from the output list back to the list.
     *
     * @author Siddhartha Azad
     * @since 2.3
     */
    public void leftButtonPressed() {
      int[] indices = m_outList.getSelectedIndices();
      for (int i = 0; i < indices.length; i++) {
        String removed = (String) m_outListModel.remove(indices[0]);
        m_listModel.addElement(removed);
      }
    }

    /**
     * Move selected items from list to the output list.
     *
     * @author Siddhartha Azad
     * @since 2.3
     */
    public void rightButtonPressed() {
      int[] indices = m_list.getSelectedIndices();
      for (int i = 0; i < indices.length; i++) {
        String removed = (String) m_listModel.remove(indices[0]);
        m_outListModel.addElement(removed);
      }
    }
  }
  /**
   * This class groups the property data structure along with the JLists
   * associated with it.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  class TextGroup {
    private JTextField m_textField;

    private JLabel m_label;

    private ConfigProperty m_prop;

    TextGroup() {
      m_textField = new JTextField(20);
      m_label = new JLabel();
    }

    public ConfigProperty getProp() {
      return m_prop;
    }

    public void setProp(final ConfigProperty a_prop) {
      m_prop = a_prop;
    }

    public JTextField getTextField() {
      return m_textField;
    }

    public JLabel getLabel() {
      return m_label;
    }
  }
  /**
   * Listener for the Configure button.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  class ConfigButtonListener
      implements ActionListener {
    private ConfigFrame m_frame;

    ConfigButtonListener(final ConfigFrame a_frame) {
      m_frame = a_frame;
    }

    public void actionPerformed(final ActionEvent a_e) {
      // configButton is pressed
      if (a_e.getActionCommand().equals("Configure")) {
        String conStr = m_configItem.getText();
        if (conStr.equals("")) {
          JOptionPane.showMessageDialog(null,
                                        "Configurable name is empty, cannot"
                                        + " configure.",
                                        "Configuration Error",
                                        JOptionPane.INFORMATION_MESSAGE);
        }
        else {
          try {
            Class conClass;
            m_conObj = null;
            try {
              conClass = Class.forName(conStr);
            }
            catch (ClassNotFoundException cnfEx) {
              JOptionPane.showMessageDialog(null,
                                            cnfEx.getMessage(),
                                            "Configuration Error: Class not"
                                            + " found",
                                            JOptionPane.INFORMATION_MESSAGE);
              return;
            }
            try {
              m_conObj = (Configurable) conClass.
                  newInstance();
            }
            catch (Exception ex) {
              JOptionPane.showMessageDialog(null,
                                            ex.getMessage(),
                                            "Configuration Error:Could not"
                                            + " create object",
                                            JOptionPane.INFORMATION_MESSAGE);
              return;
            }
            try {
              SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                  try {
                    GUIManager.getInstance().showFrame(m_frame, m_conObj);
                  }
                  catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                                                  "Configuration Error:Could"
                                                  + " not create new Frame",
                                                  JOptionPane.ERROR_MESSAGE);
                  }
                }
              });
            }
            catch (Exception ex) {
              JOptionPane.showMessageDialog(null, ex.getMessage(),
                                            "Configuration Error:Could not"
                                            + " create new frame",
                                            JOptionPane.ERROR_MESSAGE);
            }
          }
          catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                                          ex.getMessage(),
                                          "Configuration Error",
                                          JOptionPane.INFORMATION_MESSAGE);
          }
        }
      }
      else {
        // generate the config file
        ConfigWriter.getInstance().write(m_frame);
      }
    }
  }
  /**
   * Listener for list buttons to move items around.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public class ListButtonListener
      implements ActionListener {
    private ListGroup m_lg;

    ListButtonListener(final ListGroup a_lg) {
      m_lg = a_lg;
    }

    public void actionPerformed(final ActionEvent a_e) {
      // one of the list buttons is pressed
      if (a_e.getActionCommand().equals("<-")) {
        // from outList to list
        m_lg.leftButtonPressed();
      }
      else {
        // from list to outList
        m_lg.rightButtonPressed();
      }
    }
  }
  /**
   * Listener for changes in the list of items.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public class ConfigListSelectionListener
      implements ListSelectionListener {
    private JList m_list;

    private ConfigFrame m_frame;

    public ConfigListSelectionListener(final ConfigFrame a_frame,
                                       final JList a_list) {
      m_list = a_list;
      m_frame = a_frame;
    }

    public void valueChanged(final ListSelectionEvent a_e) {
      Object[] values = m_list.getSelectedValues();
      if (values.length > 0) {
        String value = (String) values[0];
        notifySelection(value);
      }
    }
  }
  /**
   * Notify the frame that a value has been selected in the output list for
   * further configuration.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  private void notifySelection(final String a_value) {
    m_configItem.setText(a_value);
  }
}
