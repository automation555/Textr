import javax.swing.*;
import java.awt.*;

public class BufferStatusBar extends JLabel implements BufferListener
{
    private Buffer buffer;
    private String defaultText;

    public BufferStatusBar(Buffer buffer)
    {
        defaultText = "Viewing: " + buffer.fileName();
        this.buffer = buffer;
        buffer.addListener(this);
        setText(defaultText);
    }

    
    public void bufferModificationCleared() {
        defaultText = "Viewing: " + buffer.fileName();
        setText(defaultText);
    }
    
    public void textRemoved(int start, int end) {
        setText(defaultText + " [+]");
    }
    
    public void textInserted(int start, String text) {
        setText(defaultText + " [+]");
    }
    
    public void fontSizeChanged(int direction) { }
}