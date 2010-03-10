import java.util.*;
import java.util.concurrent.*;

/**
 * The purpose of this class is to manage animation, and prevent the textarea from getting updated unnecessarily often.
 */
public class AnimationManager implements Runnable {

    private static MatrixAnimater matrixAnimater;
    public boolean running;
    private float counter;

    public AnimationManager(MatrixAnimater matrixAnimater) {
        this.matrixAnimater = matrixAnimater;
        counter = matrixAnimater.getAnnounceCount();
        running = true;
    }
    
    public void startProcess() {
        Thread t = new Thread(this);
        t.start();
    }
    
    public void run() {
        while (running) {
            try { Thread.sleep(33); }
            catch (Exception e) { }

            if (matrixAnimater.getAnnounceCount() > counter) {
                matrixAnimater.render();
                counter = matrixAnimater.getAnnounceCount();
            }
        }

        matrixAnimater.animationFinished();
    }
    
}