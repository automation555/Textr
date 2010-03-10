import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.*;

public class MatrixAnimater {
    private static MatrixAnimater matrixAnimater;
    private AnimationManager animationManager;
    private JTextArea textArea;
    private BufferTextArea bta;
    private JButton btnQuit;
    private StringBuffer inputFile, outputFile;
    private CopyOnWriteArrayList<String> inputArray;
    private ArrayList<Character> chars;
    private ArrayList<RandomCharacters> rndChars;
    private Random generator;
    private String renderOutput;
    private float announceCount;
    private int width, height, threadCount;
    private boolean animating;
    
    public MatrixAnimater(BufferTextArea bta, JTextArea textArea, StringBuffer inputFile) {       
        matrixAnimater = this;
        this.inputFile = inputFile;
        this.textArea = textArea;
        this.bta = bta;
        
        width = 120; // hax, getColumns() doesn't work
        height = 60;
        // Set up the JTextArea
         textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));


        textArea.setEditable(false);

        generator = new Random();
        renderOutput = "";
        announceCount = 0;
        threadCount = 0;

        createArrays();
        animating = true;
        animate();
    }
  
    public int width() { return width; }
    public int height() { return height; }
    
    /**
     * Prepares data for passing to threads.
     */
    private void createArrays() {
        chars = new ArrayList<Character>();
        inputArray = new CopyOnWriteArrayList<String>();
        outputFile = new StringBuffer();

        String[] iArr = inputFile.toString().split("\n");
        int loops = (height > iArr.length) ? iArr.length : height;
        for (int i=0; i < loops; i++)
            inputArray.add(iArr[i]);

        while (inputArray.size() < height)
            inputArray.add(fillString(' ', width));

        // Enhanced for loop seems to fail here...
        // possibly because expression is only evaluated once
        for (int i=0; i < inputArray.size(); i++) {
            // Adjust line lengths to suit textarea
            if (inputArray.get(i).length() <= width)
                inputArray.set(i, inputArray.get(i) + fillString(' ', width - inputArray.get(i).length()));
            else if (inputArray.get(i).length() > width)
                inputArray.set(i, inputArray.get(i).substring(0, width));

            outputFile.append(fillString(' ', width) + "\n");
        }
        
        rndChars = new ArrayList<RandomCharacters>();
        // What chance do we want of a char generating a randomcharacter?
        // This was too CPU intensive (on an Intel Core2 Quad Q6600!!), and sometimes left artifacts.
//         for (int r=0; r < inputArray.size(); r++) {
//             for (int c=0; c < inputArray.get(r).length(); c++) {
//                 if (1+ randomNumber(99) > 95)
//                     rndChars.add(new RandomCharacters(c + (r * (inputArray.get(r).length() + 1)), matrixAnimater));
//             }
//         }
        
    }

    /**
     * Handles animation threads etc.
     */
    private void animate() {
        renderOutput = outputFile.toString();
        // Instantiate the animation management class
        animationManager = new AnimationManager(matrixAnimater);
        animationManager.startProcess();
        // Use very simple maths to arrange columns of characters.
        ArrayList<FallingCode> threads = new ArrayList<FallingCode>();      
        for (int col=0; col < width; col++)
            threads.add(new FallingCode(new ArrayList<Character>(getChars(col)), col, matrixAnimater));    
        makeGreen();
        startThreads(threads);  // Start the threads
    }

    private void makeGreen() {
        // Assumes Textarea is black and white to begin with.
//         for (int i=255; i >= 0; i=i-5) {
//             try { Thread.sleep(30); }
//             catch (Exception e) { }
//             textArea.setBackground(new Color(i, i, i));
//         }
        textArea.setBackground(new Color(0, 0, 0));
        textArea.setForeground(new Color(77, 220, 68));
    }

    private void startThreads(ArrayList<FallingCode> threads) {
        for (FallingCode thread : threads) {
            thread.startProcess();
            threadCount++;
        }
        
        for(RandomCharacters thread : rndChars)
            thread.startProcess();
    }
    
    /**
     * A thread calls this when it has completed all other tasks.
     */
    public synchronized void threadFinished() {
        threadCount--;
        if (threadCount == 0) animationManager.running = false;
    }

    /**
     * Puts the original file back into the text area
     */
    public void animationFinished() {
        textArea.setText(inputFile.toString());
        for (int i=0; i < 256; i=i+5) {
            try { Thread.sleep(30); }
            catch (Exception e) { }
            textArea.setBackground(new Color(i, i, i));
            textArea.setForeground(new Color((77-(77*(i/255))) > 0 ? (int)(77-(77*(i/255))) : 0, (220-(220*(i/255))) > 0 ? (int)(220-(220*(i/255))) : 0, (68-(68*(i/255))) > 0 ? (int)(68-(68*(i/255))) : 0));
        }
        textArea.setForeground(new Color(0, 0, 0));
        textArea.setBackground(new Color(255, 255, 255));
        textArea.setEditable(true);
        animating = false;
        bta.renderComplete();
    }
    
    public boolean animating() { return animating; }

    /**
     * Returns a single char based on its position in a given column.
     */
    public synchronized char getChar(int col, int pos) { return inputArray.get(pos).charAt(col); }
    
    /**
     * Returns a column of characters for a given column.
     */
    public synchronized ArrayList<Character> getChars(int col) {
        ArrayList<Character> output = new ArrayList<Character>();
        for (String s : inputArray)
            output.add(s.charAt(col));

        return output;
    }

    private static String fillString(char fillChar, int count){
        char[] chars = new char[count];
        while (count>0) chars[--count] = fillChar;
        return new String(chars);
    }

    /**
     * Keeps output rendering at a constant framerate.
     */
    public synchronized void render() { textArea.setText(renderOutput); }
    
    /**
     * Returns announceCount, which represents the number of times threads have reported back to MatrixWindow.
     */
    public float getAnnounceCount() { return announceCount; }
    
    /**
     * Updates the StringBuffer outputFile with new data given to it.
     */
    public synchronized void announce(CopyOnWriteArrayList<Character> input, int col) {
        for (int r=0; r<input.size(); r++) {
            int charPos = col + (width + 1) * r;
            outputFile.setCharAt(charPos, input.get(r));
        }

        for (RandomCharacters t : rndChars)
            if (t.isRunning()) outputFile.setCharAt(t.getPos(), t.getValue());
        renderOutput = outputFile.toString();
        announceCount++;
    }

    /**
     * Generates random numbers between 0 and a given number.
     */
    public synchronized int randomNumber(int range) {
        return generator.nextInt(range);
    }
}