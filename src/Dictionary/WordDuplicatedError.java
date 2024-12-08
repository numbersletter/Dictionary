package Dictionary;

/**
 * This runtime exception is thrown when trying to add a word that already exists in the dictionary.
 */
public class WordDuplicatedError extends RuntimeException
{
    public WordDuplicatedError(String word) { super("The word " + word + " already exists in the dictionary."); }
}
