import javax.swing.*;
import java.awt.*;

/**
 * This panel contains a stack of buffer panels but the user will only ever be
 * able to see the top one.
 *
 * To switch the view to show a different buffer panel, use the switchView(i)
 * method, where 'i' is the index of the buffer to show.
 */
public class BufferSwitchPanel extends JPanel implements EditorListener, BufferListener {
    private Editor editor;
    private JTabbedPane tabPane;

    public BufferSwitchPanel(Editor editor)
    {
        this.editor = editor;
        editor.addListener(this);
        
        setLayout(new BorderLayout());
        
        tabPane = new JTabbedPane();
        tabPane.addChangeListener(new TabController(editor, tabPane));
        add(tabPane, BorderLayout.CENTER);
    }

    // BEGIN EditorListener methods
    public void bufferAdded(int index) {
        addBufferPanel(index);
        switchView(index);
    }
    public void bufferRemoved(int index) {
        removeBufferPanel(index);
        switchView(index-1);
    }
    public void bufferSwitched(int index) {
        switchView(index);
    }
    // END EditorListener methods

    /**
     * Show the i'th buffer tab.
     */
    private void switchView(int i) {
        if (i >= 0) tabPane.setSelectedIndex(i);
    }

    /**
     * Add a tab to this stack for the i'th buffer.
     */
    private void addBufferPanel(int i)
    {
        editor.buffer(i).addListener(this);
        BufferPanel bufferPanel = new BufferPanel(editor.buffer(i));
        // Icon from: http://www.famfamfam.com/lab/icons/mini/
        tabPane.addTab(bufferPanelName(i), new ImageIcon("page_text.gif"), bufferPanel);
        revalidate();
    }
    
    public void updateTabTitle(int index) {
        String title[] = bufferPanelName(index).split("/");
        if (title.length == 0)
            title = bufferPanelName(index).split("\\");
        tabPane.setTitleAt(index, title[title.length-1]);
    }

    /**
     * Remove the i'th buffer panel.
     */
    private void removeBufferPanel(int i)
    {
        tabPane.remove(i);
    }

    /**
     * The string name of each panel (used internally only).
     */
    private String bufferPanelName(int i)
    {
        // These names must be unique. i.e. it will not work if
        // Two buffers are opened with the same fileName.
        return editor.buffer(i).fileName();
    }
    
    public void fontSizeChanged(int dir) {}
    public void bufferModificationCleared() {
        int ct = tabPane.getSelectedIndex();
        
        updateTabTitle(ct);
    }
    public void textRemoved(int s, int f){}
    public void textInserted(int s, String str) {}
}
