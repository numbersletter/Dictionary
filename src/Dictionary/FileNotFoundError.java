package Dictionary;

/**
 * This exception is thrown whenever the supplied file path is not valid for importing from or exporting
 * the dictionary.
 */

public class FileNotFoundError extends RuntimeException
{
    public FileNotFoundError() { super("The entered file path does not exist."); }
}
