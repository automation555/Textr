import java.awt.event.*;
import java.awt.Point;
import javax.swing.*;
import javax.swing.text.*;

public class TextSelectionController implements ActionListener {
    private Editor editor;
    private String select;
    private JTextArea textArea;
    
    public TextSelectionController(Editor editor, String select) {
        this.editor = editor;
        this.select = select;
        textArea = editor.buffer() != null ? editor.buffer().textArea() : null;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (textArea == null) textArea = editor.buffer().textArea();
        if (select == "all") {
            textArea.selectAll();
        } else if (select == "copy") {
            textArea.copy();
        } else if (select == "copy all") {

            int p = textArea.getCaretPosition();
            textArea.selectAll();
            textArea.copy();
            textArea.setCaretPosition(p);

        } else if (select == "cut") {
            textArea.cut();
        } else if (select == "paste") {
            textArea.paste();
        }
    }
}