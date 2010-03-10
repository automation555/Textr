import java.awt.event.*;
import javax.swing.AbstractButton;

public class MatrixModeController implements ActionListener {
    private Editor editor;

    public MatrixModeController(Editor editor) {
        this.editor = editor;
    }

    public void actionPerformed(ActionEvent e) {
        AbstractButton btn = (AbstractButton) e.getSource();
        editor.toggleMatrix(btn.isSelected());
    }
}