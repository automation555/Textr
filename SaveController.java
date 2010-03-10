import java.awt.event.*;
import javax.swing.JFileChooser;

public class SaveController implements ActionListener {
    private Editor editor;
    private EditorButtonsPanel ebp;
    private boolean saveAs;

    public SaveController(Editor editor, EditorButtonsPanel ebp) {
        this(editor, ebp, false);
    }
    
    public SaveController(Editor editor, EditorButtonsPanel ebp, boolean saveAs) {
        this.ebp = ebp;
        this.editor = editor;
        this.saveAs = saveAs;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (saveAs || editor.buffer().fileName().substring(0,1).equals("$")) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            int returnVal = chooser.showSaveDialog(ebp);
            if (returnVal == JFileChooser.APPROVE_OPTION)
            try {
                editor.buffer().setFileName(chooser.getSelectedFile().getCanonicalPath());
            } catch(Exception ex) {}
        }
        editor.buffer().execute("save");
        ebp.updateBufferList();
    }
}
