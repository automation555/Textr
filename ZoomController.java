import java.awt.event.*;

public class ZoomController implements ActionListener {
    private Editor editor;
    private int direction;
    public ZoomController(Editor editor, int direction) {
        this.editor = editor;
        this.direction = direction;
    }
    
    public void actionPerformed(ActionEvent e) {
        editor.buffer().setFontSize(direction);
    }
}
