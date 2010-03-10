import java.awt.event.*;

public class BufferMenuController implements ActionListener {
    private Editor editor;
    private int bufferId;
    
    public BufferMenuController(Editor editor, int bufferId) {
        this.editor = editor;
        this.bufferId = bufferId;
    }
    
    public void actionPerformed(ActionEvent e) {
        editor.switchBuffer(bufferId);
    }

}