import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.AbstractDocument.*;

/**
 * This component displays an editable text area for some buffer which you
 * supply.
 */
public class BufferTextArea extends JPanel
{
    private Buffer buffer;
    private JTextArea textArea;
    private MatrixAnimater theMatrix;

    public BufferTextArea(Buffer buffer)
    {
        this.buffer = buffer;
        textArea = buffer.textArea();

        setLayout(new BorderLayout());

        JScrollPane textScrollPane = new JScrollPane(textArea);
        add(textScrollPane, BorderLayout.CENTER);

        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setMargin(new Insets(5, 5, 5, 5));

        if (buffer.matrix()) {
            MatrixAnimater theMatrix = new MatrixAnimater(this, textArea, buffer.textBuffer());
        } else {
            renderComplete();
        }
    }
    
    public void renderComplete() {
        textArea.setColumns(0);
        textArea.setRows(0);
        textArea.setText(""); // wtf was going on with that
        textArea.setText(buffer.text());
        ((AbstractDocument)textArea.getDocument()).setDocumentFilter(new TextAreaFilter()); 
    }

    /**
     * This inner class does the actual work of manipulating the text area.
     */
    private class TextAreaFilter extends DocumentFilter implements BufferListener
    {
        private DocumentFilter.FilterBypass fb;
        private AttributeSet attrs;

        public TextAreaFilter() {
            buffer.addListener(this);
        }

        // Our BufferListener implementations...
        public void textInserted(int position, String text) {
            try { super.insertString(fb, position, text, attrs); }
            catch (Exception e) { e.printStackTrace(); }
        }
        public void textRemoved(int startPos, int endPos) {
            try { super.remove(fb, startPos, endPos - startPos); }
            catch (Exception e) { e.printStackTrace(); }
        }
        public void bufferModificationCleared() { }  // duly ignored

        /**
         * Updates the font size of the main textArea. Minimum size 6, max 72, default 14.
         */
        public void fontSizeChanged(int direction) {
            // Ok, look, I'm the first to admit I got really carried away with ternary operators here... Sorry.
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 
                direction == 0 ? 14 : textArea.getFont().getSize() +
                ((textArea.getFont().getSize() > 6 && direction < 0) || (textArea.getFont().getSize() <= 72 && direction > 0) ? 2 * direction : 0)
            ));
        }

        // A bit of hackery...
        private void store(DocumentFilter.FilterBypass fb, AttributeSet set) {
            this.fb = fb;
            this.attrs = attrs;
        }
        public void insertString(DocumentFilter.FilterBypass fb, int position, String text, AttributeSet attrs)
            throws BadLocationException
        {
            store(fb, attrs);
            buffer.insertText(position, text);
        }
        public void remove(DocumentFilter.FilterBypass fb, int position, int length) throws BadLocationException {
            store(fb, attrs);
            buffer.removeText(position, position + length);
        }
        public void replace(DocumentFilter.FilterBypass fb, int position, int length, String text, AttributeSet attrs) throws BadLocationException {
            store(fb, attrs);
            if (length > 0) buffer.removeText(position, position + length);
            buffer.insertText(position, text);
        }
    }
}
