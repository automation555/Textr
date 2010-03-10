import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class EditorWindow extends JFrame {

    private Editor editor;
    private EditorButtonsPanel editorButtonsPanel;
    private BufferSwitchPanel bufferSwitchPanel;
    private BorderLayout layout;
    
    private JButton testButton1;
    private JButton testButton2;

    public EditorWindow(Editor editor)
    {
        this.editor = editor;
        
        setTitle("textR (by James Ducker, Student #10639615)");
        setIconImage(new ImageIcon("rainbow.png").getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        editorButtonsPanel = new EditorButtonsPanel(editor);
        bufferSwitchPanel = new BufferSwitchPanel(editor);

        layout = new BorderLayout();
        setLayout(layout);

        setJMenuBar(editorButtonsPanel);
        add(bufferSwitchPanel, layout.CENTER);

        setPreferredSize(new Dimension(750, 500));
        editor.execute("new", "");
        pack();
    }
    
//     private void frameWasResized(ComponentEvent e) {
//         setLayout(borderLayout);
//         doLayout();
//     }
}
