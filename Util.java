import java.io.*;

/**
 * This class contains various utility methods.
 *
 * You may consider adding more methods here, e.g. to test if a fileName is
 * valid.
 */
public class Util
{
    private Util() {} // don't let people instantiate this class

    public static StringBuffer readFile(String fileName)
    {
        StringBuffer text = new StringBuffer();
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = in.readLine()) != null)
                text.append(line).append((char)'\n');
            in.close();

            int lastPos = text.length() - 1;
            if (lastPos >= 0 && text.charAt(lastPos) == '\n')
                text.delete(lastPos, lastPos + 1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // Ignore errors for this assignment
        }
        return text;
    }

    public static void writeFile(String fileName, StringBuffer text)
    {
        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(text.toString(), 0, text.length());
            if (text.length() > 0)
                out.newLine();
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // Ignore errors for this assignment
        }
    }
}
