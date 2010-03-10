import java.awt.event.*;

public class CloseController implements ActionListener {
    private Editor editor;
    public CloseController(Editor editor) {
        this.editor = editor;
    }

    public void actionPerformed(ActionEvent e) {
        // Close the file currently in focus
        editor.execute("close");
    }
}