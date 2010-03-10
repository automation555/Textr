import java.util.*;

public class Log {
    private static LinkedList<LogMessage> logQueue;
    private Thread LogWriter;
    private boolean verbose;

    public Log(boolean verbose) {
        this.verbose = verbose;
        logQueue = new LinkedList<LogMessage>();
        
        Thread logWriter = new LogWriter();
        logWriter.start();
    }    
    
    private static class LogMessage {
        private int logLevel;
        private int threadId;
        private String logText;

        public LogMessage(int logLevel, int threadId, String logText) {
            this.logLevel = logLevel;
            this.threadId = threadId;
            this.logText = logText;
        }

        public int level() { return logLevel; }
        public int threadId() { return threadId; }
        public String text() { return logText; }
    }
    
    private class LogWriter extends Thread {
        public void run() {
            while(true) {
                while(!logQueue.isEmpty()) {
                    LogMessage m = logQueue.removeFirst();
                        switch(m.level()){
                            case 0:
                                if (verbose)
                                    System.out.print("DEBUG: ");
                                break;
                            case 1:
                                System.out.print("ERROR: ");
                                break;
                        }
                        System.out.println(m.text());
                }
                try { Thread.sleep(100); }
                catch (Exception e) {}
            }
        }
    }
   
    public static synchronized void newMessage(int lvl, String txt) {
        logQueue.add(new LogMessage(lvl, (int)Thread.currentThread().toString().hashCode(), txt));
    }
}