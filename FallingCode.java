import java.util.*;
import java.util.concurrent.*;

/**
 * This class replaces the old MatrixLetter class, as that class was designed to do something else. and is very disorganised.
 * Not that you ever even saw that class... believe me it was atrocious!
 */
public class FallingCode implements Runnable {
    private MatrixAnimater matrixWindow;
    private CopyOnWriteArrayList<Character> input;
    private CopyOnWriteArrayList<Character> original;
    private char[] randomData;
    private long startDelay;
    private int col;
    
    private long targetTime;    // in milliseconds
    private int variance;       // in percent (0 - 100)
    private int frames;         // number of frames
    private int framerate;      // in milliseconds

    public FallingCode(ArrayList<Character> exInput, int col, MatrixAnimater matrixWindow) {
        input = new CopyOnWriteArrayList<Character>(exInput);
        original = new CopyOnWriteArrayList<Character>(exInput);
        this.col = col;
        this.matrixWindow = matrixWindow;

        // This is an algorithm that will ensure all threads finish within a certain range of each other
        targetTime = 10000;
        variance = 1;
        framerate = 30 + rnd(200);
        frames = matrixWindow.height() + (((1 + rnd(1)) % 2 == 0) ? (int)(targetTime / framerate + (targetTime / framerate) * rnd(variance)) : (int)(targetTime / framerate - (targetTime / framerate) * rnd(variance)) );

        startDelay = rnd(1500);
        startDelay = (startDelay < 20) ? 0 : 1000+rnd(1000);
        
        // Set random character data
        randomData = new char[] {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '`',
            '[', ']', '{', '}', ';', ':', '?', '/', '.', ',', ' '
        };
    }
    
    public void startProcess() {
        Thread t = new Thread(this);
        t.start();
    }
    
    public void run() {
        sleep(startDelay);
        CopyOnWriteArrayList<Character> fallingCode = new CopyOnWriteArrayList<Character>();
        int stringLength = rnd(300);
        int whiteLength = rnd(250);
        int whitespace = 0;
        int stringspace = 0;
        for (int i=0; i < frames; i++) {
            sleep(framerate);
            if (i < frames-matrixWindow.height()) {
                if (stringspace < stringLength) {
                    fallingCode.add(0, randomData[rnd(randomData.length)]);
                    stringspace++;
                } else {
                    if (1+rnd(29)%30 == 0) {
                        fallingCode.add(0, randomData[rnd(randomData.length)]);
                    } else {
                        fallingCode.add(0, ' ');
                    }
                    whitespace++;
                    if (whitespace >= whiteLength) {
                        stringspace=0; whitespace=0;
                    }
                }
            } else {  
                fallingCode.add(0, matrixWindow.getChar(col, frames-i-1));
            }
            while (fallingCode.size() > input.size())
                fallingCode.remove(fallingCode.size()-1);
                
            pushUpdate(fallingCode);
        }
        matrixWindow.threadFinished();
    }

    public void pushUpdate() {
        matrixWindow.announce(input, col);
    }
    
    public void pushUpdate(CopyOnWriteArrayList<Character> customInput) {
        matrixWindow.announce(customInput, col);
    }

    private void sleep(long ms) { try { Thread.sleep(ms); } catch (Exception e) {} }
    private int rnd(int i) { return matrixWindow.randomNumber(i); }
}