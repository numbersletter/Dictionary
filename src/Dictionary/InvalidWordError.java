package Dictionary;

public class InvalidWordError extends RuntimeException
{
    public InvalidWordError(String word) { super("The word " + word + " is not a valid word."); }
}
