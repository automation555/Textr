import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;


public class OpenController implements ActionListener {

    private Editor editor;
    private EditorButtonsPanel ebp;
    public OpenController(Editor editor, EditorButtonsPanel ebp) {
        this.editor = editor;
        this.ebp = ebp;
    }
    
    public void actionPerformed(ActionEvent e) {
        // Open file
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(ebp);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
//             if (editor.buffer().fileName().equals("Untitled-1") &! editor.buffer().modified())
//                 editor.execute("close");
            try {
                editor.execute("open", chooser.getSelectedFile().getCanonicalPath());
            } catch (Exception ex) { }
        }
    }
}