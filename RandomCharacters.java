import java.util.*;
import java.util.concurrent.*;

public class RandomCharacters implements Runnable {

    private MatrixAnimater matrixWindow;
    private int pos;
    private long startDelay;
    private long updateInterval;
    private char[] randomData;
    private char outputValue;
    private int frames;
    private boolean running;

    public RandomCharacters(int pos, MatrixAnimater matrixWindow) {
        this.pos = pos;
        this.matrixWindow = matrixWindow;

        updateInterval = 20 + matrixWindow.randomNumber(200);
        startDelay = 500+matrixWindow.randomNumber(3000);
        frames = 5 + matrixWindow.randomNumber(200);
        
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
        running = true;
        char newChar = randomData[matrixWindow.randomNumber(randomData.length)];
        for(int i=0; i < frames; i++) {
            sleep(updateInterval);
            if (newChar != ' ')
                newChar = randomData[matrixWindow.randomNumber(randomData.length)];
            outputValue = newChar;
        }
        running = false;
    }
    
    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) { }
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public int getPos() {
        return pos;
    }
    
    public char getValue() {
        return outputValue;
    }
}