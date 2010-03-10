// STUDENTS MUST NOT MODIFY THIS INTERFACE
public interface BufferListener
{
    public void textInserted(int position, String text);
    public void textRemoved(int startPos, int endPos);
    public void bufferModificationCleared();
    public void fontSizeChanged(int direction);
}
