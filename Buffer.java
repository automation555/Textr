import java.util.*;
import java.io.*;
import javax.swing.*;

public class Buffer extends Executor
{
    // 
    // ATTRIBUTES
    //

    private String fileName;
    private StringBuffer text;
    private String originalText;
    private boolean modified;
    private boolean matrix;
    private boolean onDisk;
    private List<BufferListener> bufferListeners;
    
    // Bit of hax, textArea reference really needs to be available via buffer.
    private JTextArea textArea;

    //
    // CONSTRUCTORS
    //

    public Buffer(String fileName, boolean matrix)
    {
        this(fileName, matrix, new StringBuffer());
    }

    public Buffer(String fileName, boolean matrix, StringBuffer text)
    {
        if (fileName == null || fileName.trim().length() == 0)
        throw new IllegalArgumentException("Invalid file name: " + fileName);

        this.matrix = matrix;
        this.fileName = fileName;
        this.text = text;
        
        textArea = new JTextArea();
        
        recordOriginalText();

        bufferListeners = new ArrayList<BufferListener>();

        registerOperation(new SaveOperation());
        registerOperation(new RevertOperation());
    }

    //
    // FUNCTIONS
    // 

    public boolean modified() { return modified; }
    public String text() { return text.toString(); }
    public StringBuffer textBuffer() { return text; }
    public String fileName() { return fileName; }
    public String toString() { return fileName; }
    public boolean matrix() { return matrix; }
    public JTextArea textArea() { return textArea; }


    // 
    // PROCEDURES
    //

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public void setFontSize(int direction) {
        notifyFontSizeChanged(direction);
    }

    public void setMatrix(boolean state) {
        matrix = state;
    }
    
    public void insertText(int position, String newText)
    {
        text.insert(position, newText);
        modified = true;
        notifyTextInserted(position, newText);
    }

    public void removeText(int startPos, int endPos)
    {
        text.delete(startPos, endPos);
        modified = true;
        notifyTextRemoved(startPos, endPos);
    }

    private void clearModification()
    {
        modified = false;
        notifyBufferModificationCleared();
    }

    private void recordOriginalText()
    {
        originalText = text.toString();
    }

    //
    // LISTENER CODE
    //

    public void addListener(BufferListener listener) {
        bufferListeners.add(listener);
    }

    private void notifyFontSizeChanged(int direction) {
        for (BufferListener listener : bufferListeners)
            listener.fontSizeChanged(direction);
    }

    private void notifyTextInserted(int position, String text) {
        for (BufferListener listener : bufferListeners)
            listener.textInserted(position, text);
    }

    private void notifyTextRemoved(int startPos, int endPos) {
        for (BufferListener listener : bufferListeners)
            listener.textRemoved(startPos, endPos);
    }

    private void notifyBufferModificationCleared() {
        for (BufferListener listener : bufferListeners)
            listener.bufferModificationCleared();
    }

    // 
    // OPERATIONS
    //

    public class SaveOperation extends Operation
    {
        public SaveOperation() { super("save"); }
        public boolean execute(String... args)
        {
            
            Util.writeFile(fileName, text);
            recordOriginalText();
            clearModification();
            return true;
        }
    }

    public class RevertOperation extends Operation
    {
        public RevertOperation() { super("revert"); }
        public boolean execute(String... args)
        {
            removeText(0, text.length());
            insertText(0, originalText);
            clearModification();
            onDisk = true;
            return true;
        }
    }
}
