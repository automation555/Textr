import java.awt.event.*;

public class TabsController implements ActionListener {
    private String mode;
    private Editor editor;

    public TabsController(Editor editor) {
        this(editor, "current");
    }
    
    public TabsController(Editor editor, String mode) {
        this.editor = editor;
        this.mode = mode;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (mode == "current") {
            editor.execute("close");
        } else {
            while (editor.buffersExist())
                editor.execute("close");
        }
    }
}