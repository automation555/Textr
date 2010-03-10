import java.awt.event.*;

public class RevertController implements ActionListener {
    private Editor editor;
    
    public RevertController(Editor editor) {
        this.editor = editor;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (editor.buffer().modified()) editor.buffer().execute("revert");
    }
}