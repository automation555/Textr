import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EditorButtonsPanel extends JMenuBar implements EditorListener
{
    private Editor editor;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, viewMenu, settingsMenu, tabsMenu;
    private JMenu switchToTabSubMenu;
    private JMenuItem menuItem;
    private JMenuItem btnRevert;
    private ArrayList<JMenu> bufferMenus;
    private ArrayList<JMenuItem> bufferMenuItems;

    public EditorButtonsPanel(Editor editor)
    {
        this.editor = editor;
        editor.addListener(this);

        // These menus are based on other menu systems I have seen in text editors.

        bufferMenus = new ArrayList<JMenu>();
        bufferMenuItems = new ArrayList<JMenuItem>();

        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        bufferMenus.add(editMenu);
        menuBar.add(editMenu);

        viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        bufferMenus.add(viewMenu);
        menuBar.add(viewMenu);

        settingsMenu = new JMenu("Settings");
        settingsMenu.setMnemonic(KeyEvent.VK_S);
        menuBar.add(settingsMenu);

        tabsMenu = new JMenu("Tabs");
        tabsMenu.setMnemonic(KeyEvent.VK_T);
        bufferMenus.add(tabsMenu);
        menuBar.add(tabsMenu);

        // fileMenu items
        menuItem = new JMenuItem("New"); // Working
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new NewController(editor));
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Open"); // Working
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new OpenController(editor, this));
        fileMenu.add(menuItem);

        btnRevert = new JMenuItem("Revert"); // Working
        btnRevert.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        btnRevert.addActionListener(new RevertController(editor));
        fileMenu.add(btnRevert);

        menuItem = new JMenuItem("Save"); // Working
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new SaveController(editor, this, false));
        bufferMenuItems.add(menuItem);
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Save As..."); // Working
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        menuItem.addActionListener(new SaveController(editor, this, true));
        bufferMenuItems.add(menuItem);
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Close"); // Working
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new CloseController(editor));
        bufferMenuItems.add(menuItem);
        fileMenu.add(menuItem);

        fileMenu.addSeparator();

        menuItem = new JMenuItem("Exit"); // Working
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        menuItem.addActionListener(new QuitController(editor));
        fileMenu.add(menuItem);

        // editMenu items
        menuItem = new JMenuItem("Cut");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new TextSelectionController(editor, "cut"));
        editMenu.add(menuItem);

        menuItem = new JMenuItem("Copy");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new TextSelectionController(editor, "copy"));
        editMenu.add(menuItem);

        menuItem = new JMenuItem("Copy All");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        menuItem.addActionListener(new TextSelectionController(editor, "copy all"));
        editMenu.add(menuItem);

        menuItem = new JMenuItem("Paste");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new TextSelectionController(editor, "paste"));
        editMenu.add(menuItem);

        editMenu.addSeparator();

        menuItem = new JMenuItem("Select All");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new TextSelectionController(editor, "all"));
        editMenu.add(menuItem);

        // viewMenu items
        menuItem = new JMenuItem("Zoom In"); // Working
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ZoomController(editor, 1));
        viewMenu.add(menuItem);

        menuItem = new JMenuItem("Zoom Out"); // Working
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ZoomController(editor, -1));
        viewMenu.add(menuItem);

        menuItem = new JMenuItem("Reset Zoom"); // Working
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ZoomController(editor, 0));
        viewMenu.add(menuItem);

        //settingsMenu items      
        JCheckBox menuBox = new JCheckBox("Matrix Mode"); // Working
        menuBox.addActionListener(new MatrixModeController(editor));
        settingsMenu.add(menuBox);

        // tabsMenu items

        switchToTabSubMenu = new JMenu("Switch to tab"); // Working
        tabsMenu.add(switchToTabSubMenu);        

        menuItem = new JMenuItem("Close current tab"); // Working
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new TabsController(editor));
        tabsMenu.add(menuItem);

        menuItem = new JMenuItem("Close all tabs"); // Working
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        menuItem.addActionListener(new TabsController(editor, "all"));
        tabsMenu.add(menuItem);

        add(menuBar);
        updateButtons();
    }

    private void updateButtons() {
        boolean state = editor.buffersExist() ? true : false;
        for (JMenu menu : bufferMenus)
            menu.setEnabled(state);
        for (JMenuItem bufferItem : bufferMenuItems)
            bufferItem.setEnabled(state);
//         for (JMenuItem saveItem : saveMenuItems)
//             saveItem.setEnabled((editor.buffer() == null) ? false : editor.buffer().modified());
//         btnRevert.setEnabled((editor.buffer() == null) ? false : editor.buffer().modified());
    }
    
    public void updateBufferList() {
        // Rebuild the buffer menu
        switchToTabSubMenu.removeAll();
        JMenuItem bufferItem;
        
        for(int i=0; i< editor.buffers().size(); i++) {
            bufferItem = new JMenuItem(editor.buffer(i).fileName());
            bufferItem.addActionListener(new BufferMenuController(editor, i));
            switchToTabSubMenu.add(bufferItem);
        }
    }

    public void bufferAdded(int index) {
        updateBufferList();
    }
    public void bufferRemoved(int index) {
        updateBufferList();
    }
    public void bufferSwitched(int index) {
        updateButtons();
    }
}
