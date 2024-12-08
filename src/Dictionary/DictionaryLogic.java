package Dictionary;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

/**
 * This class contains the model/logic behind the Dictionary program. All the functional parts of the program (which
 * contains a GUI, handled in a separate class) are contained in this class.
 */

public class DictionaryLogic
{
    /*
     * The primary dictionary of the whole program.
     * A hashmap makes the most sense for the case where the name of the relevant object (the word) is directly tied
     * to the value (the meaning - or in this case, the WordEntry which contains all details about the word).
     */
    private final Map<String, WordEntry> dictionary;


    public DictionaryLogic()
     {
         this.dictionary = new HashMap<String, WordEntry>();
     }

    /**
     * Adds a unique word to the dictionary. If the word already exists, throw WordDuplicatedError.
     * @param newWord word to be added
     * @param meaning the meaning of the word
     * @throws WordDuplicatedError if the word is already present in the dictionary
     */
     public void addWord(String newWord, String meaning) throws WordDuplicatedError
     {
        WordEntry newEntry = new WordEntry(newWord, meaning);
        // containsKey uses .equals method of the related object (String in this case)
        if(this.dictionary.containsKey(newWord))
            throw new WordDuplicatedError(newWord);

        // add to dictionary if the word is in valid syntax and unique
        dictionary.put(newWord, newEntry);
     }

    /**
     * Returns the WordEntry of the corresponding searchWord if it is defined in the dictionary, so this is the first
     * method called after FIND is clicked. If this method fails to find the word, null is returned.
     * However, this is not the termination point of FIND as there might be similar words to searchWord.
     * This similarity is defined as other words in the dictionary that contain the keyword searchWord.
     * @param searchWord word to find in the dictionary
     * @return the matched WordEntry in the dictionary or null if not found
     */
     public WordEntry findWord(String searchWord)
     {
         // try to find the word in the dictionary. if not present, throw WordNotFound
         // so that words by frequency can be attempted (if such words exist)
         WordEntry matchedWord = dictionary.getOrDefault(searchWord, null);
         if(matchedWord == null)
             return null;
         //matchedWord.incrementFrequency();
         return matchedWord;
     }

    /**
     * Returns the top 3 (or 2 or 1) similar words to the keyword searchWord. If this fails (there are no words
     * containing searchWord in the dictionary), an empty WordEntry List is returned.
     * @param searchWord the keyword to search for in the dictionary
     * @return List of WordEntries that are the most frequently searched words similar to searchWord.
     */
     public List<WordEntry> findWordByFreq(String searchWord)
     {
         // try to find all words that contain the searchWord for the frequency search
         // sort this in descending frequency and pick the top 3/2/1, as necessary
         // if no word (s) that contain the searchWord exist, throws WordNotFoundError

         List<String> containingWords = new ArrayList<String>();

         // populate containingWords with words containing searchWord (this is the filtered list)
         for(String word : dictionary.keySet())
             if(word.contains(searchWord))
                 containingWords.add(word);

         if(containingWords.isEmpty())
             return new ArrayList<WordEntry>();

         // sort by descending searched frequency / alphabetize at the same time
         for(int i = 0; i < containingWords.size() - 1; i++)
         {
             // invariant: every word after index i must be lower (or equal) in frequency
             int maxInd = i;
             for(int j = i + 1; j < containingWords.size(); j++)
             {
                 WordEntry curr = dictionary.get(containingWords.get(j));
                 WordEntry max = dictionary.get(containingWords.get(maxInd));

                 // frequency comparison
                 if(curr.getFrequencySearched() > max.getFrequencySearched())
                     maxInd = j;
                 // if frequencies are the same, sort alphabetically (ascending)
                 else if(curr.getFrequencySearched() == max.getFrequencySearched() &&
                        (curr.getWordName().compareTo(max.getWordName()) < 0))
                 {
                    maxInd = j;
                 }
             }
             // swap elements if necessary
             String temp = containingWords.get(i);
             containingWords.set(i, containingWords.get(maxInd));
             containingWords.set(maxInd, temp);
         }
         int resultSize = Math.min(containingWords.size(), 3);
         List<WordEntry> result = new ArrayList<WordEntry>();
         for(int i = 0; i < resultSize; i++)
         {
             WordEntry res = dictionary.get(containingWords.get(i));
             res.incrementFrequency();
             result.add(res);
         }
         return result;
     }

    /**
     * Modify the word name of an existing word without changing its meaning or search frequency.
     * @param replacementWord new word name
     * @param oldWord old word name to replace
     * @throws WordNotFoundError if the supplied old word is not found.
     * @throws InvalidWordError if the supplied replacement word is in a valid alphabetical format
     */
     public void modifyMeaning(String replacementWord, String oldWord) throws WordNotFoundError, InvalidWordError
     {
         if(!dictionary.containsKey(oldWord))
             throw new WordNotFoundError(oldWord);
         WordEntry newWord = new WordEntry(replacementWord, dictionary.get(oldWord).getWordMeaning(),
                 dictionary.get(oldWord).getFrequencySearched());
         dictionary.remove(oldWord);
         dictionary.put(replacementWord, newWord);
     }

    /**
     * Removes the given word from the dictionary, if it exists.
     * @param toRemove word to remove
     * @throws WordNotFoundError if the word is not found in the dictionary
     */
     public void removeWord(String toRemove) throws WordNotFoundError
     {
         if(!dictionary.containsKey(toRemove))
             throw new WordNotFoundError(toRemove);
         dictionary.remove(toRemove);
     }

    /**
     * Removes all entries in the dictionary.
     */
    public void removeAllWords()
     {
         while(!dictionary.isEmpty())
         {
             // make sure to use iterator so that we can get subsequent elements even after deleting (iterator
             // is updated while removing)
             String key = dictionary.keySet().iterator().next();
             dictionary.remove(key);
         }
     }

    /**
     * Import an appropriately formatted (word-meaning-newline) text file for the dictionary.
     * Rather than appending to the dictionary, the dictionary is cleared prior to updating it.
     * @param filePath the file path of the input text file
     * @throws FileNotFoundError if the file cannot be found for any reason
     * @throws WordDuplicatedError exception from addWord() that must be caught by caller
     * @throws InvalidWordError exception from addWord() that must be caught by caller
     */
     public void importFileToDict(String filePath) throws FileNotFoundError, WordDuplicatedError, InvalidWordError
     {
         // first delete everything from current dictionary
         this.removeAllWords();

         try(BufferedReader in = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8)))
         {
             // format is word followed by the meaning
             String word, meaning;

             // for now, I assume the importing file is in the correct format. in future case, this
             // should be error-handled (if word-meaning structure isn't strictly followed, throw error and halt)
             while(((word = in.readLine()) != null) && ((meaning = in.readLine()) != null))
             {
                 this.addWord(word, meaning);
                 in.readLine();
             }
         }
         catch (IOException notFound)
         {
            throw new FileNotFoundError();
         }
     }

     public void exportFileFromDict(String filePath) throws FileNotFoundError
     {
         // use PrintWriter to print strings in TEXT format, in this case, to a file
         try(PrintWriter out = new PrintWriter(filePath, StandardCharsets.UTF_8))
         {
             // sort output in descending order of frequency
             // unlike the implementation for FIND, I utilize the stream API here
             // sorted() uses the fact that each word entry has a comparable implementation (see WordEntry.compareTo())
             List<WordEntry> wordEntries = dictionary.values().stream().sorted().toList();
             for(int i = 0; i < wordEntries.size() - 1; i++)
             {
                 wordEntries.get(i).writeWordEntryToStream(out);
                 out.println("\n");
             }
             wordEntries.getLast().writeWordEntryToStream(out);
         }
         catch (IOException e)
         {
             throw new FileNotFoundError();
         }
     }

    /**
     * String format of the dictionary
     * @return string construction of the dictionary containing all relevant fields of the entry
     */
    public String toString()
     {
         StringBuilder strConstruction = new StringBuilder();

         for(Map.Entry<String, WordEntry> entry : dictionary.entrySet())
         {
             strConstruction.append(entry.getValue().getWordName() + "\n" + entry.getValue().getFrequencySearched() +
                     "\n" + entry.getValue().getWordMeaning() + "\n\n");
         }

         return strConstruction.toString();
     }

     public void printDictionary()
     {
         System.out.println(dictionary);
     }
}
