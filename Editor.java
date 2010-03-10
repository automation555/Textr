import java.util.*;
import java.io.*;

public class Editor extends Executor
{
    // 
    // ATTRIBUTES
    //

    private List<Buffer> buffers;
    private int bufferIdx;
    private List<EditorListener> editorListeners;
    private int newCount;
    private boolean matrixMode;

    // 
    // CONSTRUCTORS
    //

    public Editor()
    {
        buffers = new ArrayList<Buffer>();
        editorListeners = new ArrayList<EditorListener>();
        bufferIdx = -1;
        newCount = 0;
        matrixMode = false;

        registerOperation(new NewOperation());
        registerOperation(new OpenOperation());
        registerOperation(new CloseOperation());
        registerOperation(new QuitOperation());
    }

    // 
    // FUNCTIONS
    //
    public List<Buffer> buffers() { return buffers; }
    public int bufferIndex() { return bufferIdx; }
    public Buffer buffer(int index) { return buffers.get(index); }
    public Buffer buffer() { return withinBufferRange(bufferIdx) ? buffer(bufferIdx) : null; }
    public boolean buffersExist() { return buffers.size() > 0 ? true : false; }
    private boolean withinBufferRange(int i) { return i >= 0 && i < buffers.size(); }
    public int newCount() { return newCount; }

    // 
    // PROCEDURES
    //

    public void switchBuffer(int index) {
        bufferIdx = index;
        notifyBufferSwitched(index);
    }
    
    public void toggleMatrix(boolean state) {
        matrixMode = state;
        for (Buffer b : buffers)
            b.setMatrix(state);
    }
    
    // 
    // LISTENER CODE
    //

    public void addListener(EditorListener listener) {
        editorListeners.add(listener);
    }

    private void notifyBufferSwitched(int index) {
        for (EditorListener listener : editorListeners) {
            listener.bufferSwitched(index);
        }
    }

    private void notifyBufferAdded(int index) {
        for (EditorListener listener : editorListeners) {
            listener.bufferAdded(index);
        }
    }

    private void notifyBufferRemoved(int index) {
        for (EditorListener listener : editorListeners) {
            listener.bufferRemoved(index);
        }
    }

    // 
    // OPERATIONS
    //

    public class CloseOperation extends Operation {
        public CloseOperation() { super("close"); }
        public boolean execute(String... args)
        {
            if (buffer() == null)
                return false;
            int idx = bufferIdx;
            int targetIdx = (idx == buffers.size() - 1) ? idx - 1 : idx;
            buffers.remove(idx);
            notifyBufferRemoved(idx);
            switchBuffer(targetIdx);
            return true;
        }
    }

    public abstract class BufferCreationOperation extends Operation
    {
        public BufferCreationOperation(String name) { super(name); }
        public boolean execute(String... args)
        {
            int newIdx = buffers.size();
            buffers.add(newIdx, createBuffer(args[0]));
            notifyBufferAdded(newIdx);
            switchBuffer(newIdx);
            return true;
        }

        protected abstract Buffer createBuffer(String fileName);
    }

    public class NewOperation extends BufferCreationOperation {
        public NewOperation() { super("new"); }
        public Buffer createBuffer(String fileName) {
            newCount++;
            return new Buffer("$Untitled-" + newCount, matrixMode);
        }
    }

    public class OpenOperation extends BufferCreationOperation {
        public OpenOperation() { super("open"); }
        public Buffer createBuffer(String fileName) {
            return new Buffer(fileName, matrixMode, Util.readFile(fileName));
        }
    }

    public class QuitOperation extends Operation {
        public QuitOperation() { super("quit"); }
        public boolean execute(String... args) { System.exit(0); return true; }
    }
}
