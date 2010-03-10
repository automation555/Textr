import java.util.*;

public abstract class Executor
{
    private Map<String, Operation> operations;

    protected Executor()
    {
        operations = new HashMap<String, Operation>();
    }

    /**
     * Execute a command with given arguments. Return true on success.
     *
     * The strange ... notation after String means that this method can
     * take a variable number of string arguments after the command.
     *
     * For example, both of these are accepted by the compiler:
     *
     * editor.execute("open", "hello.txt");
     * editor.execute("close");
     */
    protected boolean execute(String command, String... args)
    {
        Operation op = operations.get(command);
        return op != null && op.execute(args);
    }

    protected void registerOperation(Operation operation)
    {
        operations.put(operation.name(), operation);
    }
}
