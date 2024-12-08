package Dictionary;

import java.io.PrintWriter;

/**
 * Class to contain a word's information (name, meaning, frequency) and methods to manipulate these variables.
 */

public class WordEntry implements Comparable<WordEntry>
{
    private final String wordName;
    private final String wordMeaning;
    private int frequencySearched;

    /**
     * Construction of a new word for the dictionary.
     * @param name the literal word
     * @param meaning meaning of the word
     * @throws InvalidWordError if the name is invalid
     */
    public WordEntry(String name, String meaning) throws InvalidWordError
    {
        if(!StringValidator(name))
            throw new InvalidWordError(name);
        this.wordName = name;
        this.wordMeaning = meaning;
        this.frequencySearched = 0;
    }

    /**
     * Construction of a new word with a prior frequency.
     * @param name the literal word
     * @param meaning meaning of the word
     * @param frequency frequency the word has been searched
     * @throws InvalidWordError if the name is invalid
     */
    public WordEntry(String name, String meaning, int frequency) throws InvalidWordError
    {
        if(!StringValidator(name))
            throw new InvalidWordError(name);
        this.wordName = name;
        this.wordMeaning = meaning;
        this.frequencySearched = frequency;
    }

    public void incrementFrequency()
    {
        frequencySearched++;
    }

    public String getWordName()
    {
        return wordName;
    }

    public String getWordMeaning()
    {
        return wordMeaning;
    }

    public int getFrequencySearched()
    {
        return frequencySearched;
    }

    public void writeWordEntryToStream(PrintWriter out)
    {
        out.print(this.wordName + "\n" + this.frequencySearched + "\n" + this.wordMeaning);
    }

    /**
     * Returns a negative integer, zero, or a positive integer if this WordEntry is less than, equal to,
     * or greater than the specified object. A WordEntry is greater than another if its frequency searched is greater
     * than the other word entry. If the frequencies are equal, comparison is made with respect to ascending
     * alphabetical order. This is useful for quickly sorting a list of WordEntry using the stream API.
     * @param other the object to be compared.
     * @return positive integer if this WordEntry has a greater frequency than other WordEntry or if the other word
     * lexicographically follows this word (if the frequencies are the same)
     */
    public int compareTo(WordEntry other)
    {
        int compareFreq = Integer.compare(other.getFrequencySearched(), this.getFrequencySearched());
        if(compareFreq != 0)
            return compareFreq;
        // if they have the same freq, use alphabetical order
        return this.getWordName().compareTo(other.getWordName());
    }

    public String toString()
    {
        return (this.wordName + " : " + this.frequencySearched);
    }

    /**
     * Checks if the string label is only comprised of the English alphabet.
     * @param label the string to check
     * @return true if label is only made of the English alphabet (a valid string)
     */
    private boolean StringValidator(String label)
    {
        return ((label != null) && label.matches("[a-zA-Z]+"));
    }


}
