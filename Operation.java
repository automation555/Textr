public abstract class Operation
{
    private String name;

    public Operation(String name)
    {
        this.name = name;
    }

    public String name()
    {
        return name;
    }

    public abstract boolean execute(String... args);
}