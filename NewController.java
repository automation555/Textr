import java.awt.event.*;

public class NewController implements ActionListener {
    private Editor editor;

    public NewController(Editor editor) {
        this.editor = editor;
    }

    public void actionPerformed(ActionEvent e) {
        // Open a new file
        editor.execute("new", "");
    }
}