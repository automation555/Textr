import javax.swing.*;
import javax.swing.event.*;

public class TabController implements ChangeListener {
    private Editor editor;
    private JTabbedPane tabPane;
    private int index;
    
    public TabController(Editor editor, JTabbedPane tabPane) {
        this.editor = editor;
        this.tabPane = tabPane;
    }
    
    public void stateChanged(ChangeEvent e) {
        editor.switchBuffer(tabPane.getSelectedIndex());
    }
}