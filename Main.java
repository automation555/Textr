public class Main
{
    private static boolean verbose;

    /**
     * The main entry point into the program.
     *
     * To run,
     *
     * In BlueJ: rightclick on Main and select main.
     * In DOS/UNIX: type "java Main"
     */
    public static void main(String[] args)
    {
        verbose = (args.length > 0) ? (args[0] == "true") ? true : false : false;
        new Log(verbose);
        
        Editor editor = new Editor();
        EditorWindow window = new EditorWindow(editor);
        window.setVisible(true);
    }
}