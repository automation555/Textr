import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class BufferPanel extends JPanel
{
    private Buffer buffer;
    private BufferTextArea textArea;
    private BufferStatusBar statusBar;
    private BorderLayout borderLayout;

    public BufferPanel(Buffer buffer)
    {
        this.buffer = buffer;
        setLayout(new BorderLayout());
        
        textArea = new BufferTextArea(buffer);
        statusBar = new BufferStatusBar(buffer);
        this.add(textArea, BorderLayout.CENTER);
        this.add(statusBar, BorderLayout.SOUTH);
    }
}
