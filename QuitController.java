import java.awt.event.*;

public class QuitController implements ActionListener {
    private Editor editor;
    
    public QuitController(Editor editor) {
        this.editor = editor;
    }

    public void actionPerformed(ActionEvent e) {
        editor.execute("quit");
    }
}