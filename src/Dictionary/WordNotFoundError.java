package Dictionary;

/**
 * This exception is thrown by the GUI when the given modify or remove string is not found in the dictionary.
 * This is NOT thrown when FIND is clicked and the dictionary doesn't contain the given word. In that case,
 * the text area just states there are no matching words.
 */
public class WordNotFoundError extends RuntimeException
{
    public WordNotFoundError(String word)
    {
        super("The word " + word + "  is not found in the dictionary.");
    }
}
