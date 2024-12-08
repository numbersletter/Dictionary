package Dictionary;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.List;

public class Dictionary
{
    public JPanel contentPanel;
    public JLabel searchHistoryLabel;
    public JButton FINDButton;
    public JButton ADDButton;
    public JTextField TextNewWord;
    public JTextField TextOriginalWord;
    public JTextField TextFreqWord1;
    public JTextField TextFreqWord2;
    public JTextField TextFreqWord3;
    public JButton IMPORTButton;
    public JButton EXPORTButton;
    public JTextField TextFilePath;
    public JTextArea TextArea;
    public JButton CLEARButton;
    public JButton MODIFYButton;
    public JButton REMOVEButton;
    public JList<String> searchHistoryList;
    private JButton IMPORTPATHButton;
    private JButton EXPORTPATHButton;
    private JFileChooser fileChooser;

    private boolean resetTextAreaIfKeyed;

    private final DictionaryLogic logic;
    SearchHistoryStack<String> searchHistory;

    /**
     * Initializes the GUI elements and action listeners.
     */
    public Dictionary()
    {
        resetTextAreaIfKeyed = false;
        logic = new DictionaryLogic();
        searchHistory = new SearchHistoryStack<String>(10);

        // initialize GUI here
        setupTextArea();
        setupClearBtn();

        setupAddBtn();
        setupFindBtn();
        setupRemoveBtn();
        setupModifyBtn();
        setupImportBtn();
        setupExportBtn();

        // file chooser for fun
        setupFileChooser();
    }

    private void setupImportFileDialogBtn()
    {
        IMPORTPATHButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int result = fileChooser.showOpenDialog(contentPanel);
                if(result == JFileChooser.APPROVE_OPTION)
                {
                    String fileName = fileChooser.getSelectedFile().getPath();
                    TextFilePath.setText(fileName);
                    IMPORTButton.doClick();
                }
            }
        });
    }

    private void setupExportFileDialogBtn()
    {
        EXPORTPATHButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                fileChooser.setSelectedFile(new File("output.txt"));
                int result = fileChooser.showSaveDialog(contentPanel);
                if(result == JFileChooser.APPROVE_OPTION)
                {
                    String fileName;
                    if(!fileChooser.getSelectedFile().getPath().contains("."))
                        fileName = fileChooser.getSelectedFile().getPath() + ".txt";
                    else
                        fileName = fileChooser.getSelectedFile().getPath();
                    TextFilePath.setText(fileName);
                    EXPORTButton.doClick();
                }
                fileChooser.setSelectedFile(new File(""));
            }
        });
    }

    private void setupFileChooser()
    {
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        setupImportFileDialogBtn();
        setupExportFileDialogBtn();
    }

    /**
     * Initializes the export button listener and binds corresponding logic.
     * @throws FileNotFoundError if the path to export file is invalid or inaccessible in any way
     */
    private void setupExportBtn() throws FileNotFoundError
    {
        EXPORTButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    logic.exportFileFromDict(TextFilePath.getText());
                } catch (FileNotFoundError except)
                {
                    printErrorTextArea(except.getMessage());
                    throw except;
                }
            }
        });
    }

    /**
     * Initializes the import button listener and binds logic.
     * @throws FileNotFoundError if the file to be imported cannot be found for any reason
     * @throws WordDuplicatedError whenever a duplicate word appears in the importing dictionary
     * @throws InvalidWordError when the word in the importing dictionary is in invalid syntax
     */
    private void setupImportBtn() throws FileNotFoundError, WordDuplicatedError, InvalidWordError
    {
        IMPORTButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    // this sets the entire dictionary to the imported one (prior entries deleted)
                    logic.importFileToDict(TextFilePath.getText());
                    //printTextArea(String.valueOf(logic));
                }
                catch(FileNotFoundError | WordDuplicatedError | InvalidWordError except)
                {
                    printErrorTextArea(except.getMessage());
                    throw except;
                }
            }
        });
    }

    /**
     * Initializes the modify button listener and binds logic.
     * @throws WordNotFoundError if the word to modify cannot be found
     * @throws InvalidWordError if the new word is in invalid syntax
     */
    private void setupModifyBtn() throws WordNotFoundError, InvalidWordError
    {
        MODIFYButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    logic.modifyMeaning(TextNewWord.getText(), TextOriginalWord.getText());
                }
                catch (WordNotFoundError | InvalidWordError except)
                {
                    printErrorTextArea(except.getMessage());
                    throw except;
                }
            }
        });
    }

    /**
     * Initializes the remove button listener and binds logic.
     * @throws WordNotFoundError when the word to be removed cannot be found
     */
    private void setupRemoveBtn() throws WordNotFoundError
    {
        REMOVEButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    logic.removeWord(TextNewWord.getText());
                    printTextArea("'" + TextNewWord.getText() + "' has been removed from the dictionary.");
                } catch (WordNotFoundError ex)
                {
                    printErrorTextArea(ex.getMessage());
                    throw ex;
                }
            }
        });
    }

    /**
     * Initializes the find button listener and binds logic. If the word cannot be found, a message is displayed
     * on the text area. The three most frequently searched similar (which may includes the same word, if it exists)
     * is displayed on the corresponding text field. The search history is updated with these results.
     */
    private void setupFindBtn()
    {
        FINDButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                WordEntry fullMatch;
                List<WordEntry> wordByFreq;

                fullMatch = logic.findWord(TextNewWord.getText());
                wordByFreq = logic.findWordByFreq(TextNewWord.getText());

                if(fullMatch == null)
                    printTextArea("No Word Matched.");
                else
                    printTextArea(fullMatch.getWordMeaning());

                clearFreqWordFields();
                switch (wordByFreq.size())
                {
                    case 3:
                        TextFreqWord3.setText(wordByFreq.get(2).getWordName());
                    case 2:
                        TextFreqWord2.setText(wordByFreq.get(1).getWordName());
                    case 1:
                        TextFreqWord1.setText(wordByFreq.get(0).getWordName());
                        break;
                }
                // update search history list
                for(WordEntry word : wordByFreq.reversed())
                {
                    searchHistory.push(word.getWordName());
                }
                if(fullMatch != null)
                    searchHistory.push(fullMatch.getWordName());
                String[] stuff = searchHistory.stackToArray(String.class);
                searchHistoryList.setListData(stuff);

            }
        });
    }

    /**
     * Initializes the clear button listener.
     */
    private void setupClearBtn()
    {
        CLEARButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Clear all text fields (assuming not including the search history)
                resetTextAreaIfKeyed = false;
                TextArea.setForeground(Color.BLACK);
                TextNewWord.setText("");
                TextOriginalWord.setText("");
                TextFreqWord1.setText("");
                TextFreqWord2.setText("");
                TextFreqWord3.setText("");
                TextFilePath.setText("");
                TextArea.setText("");
            }
        });
    }

    /**
     * Initializes the add word button and binds logic.
     * @throws WordDuplicatedError if the word to add already exists in the dictionary
     * @throws InvalidWordError if the word to add is in invalid syntax
     */
    private void setupAddBtn() throws WordDuplicatedError, InvalidWordError
    {
        ADDButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    logic.addWord(TextNewWord.getText(), TextArea.getText());

                }
                catch (WordDuplicatedError | InvalidWordError except)
                {
                    printErrorTextArea(except.getMessage());
                    throw except;
                }
            }
        });
    }

    /**
     * Initializes the text area component of the GUI. Some automation when errors or other meanings are displayed
     * is set up here. For example, if the program writes a meaning of a word to this text area, a subsequent
     * user key press in the area results in the text area being reset for a meaning entry for a new word.
     */
    private void setupTextArea()
    {
        TextArea.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e)
            {
                if(resetTextAreaIfKeyed)
                {
                    TextArea.setForeground(Color.BLACK);
                    TextArea.setText("");
                    resetTextAreaIfKeyed = false;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    /**
     * Print the exception method in red in the text area.
     * @param message exception to print
     */
    private void printErrorTextArea(String message)
    {
        resetTextAreaIfKeyed = true;
        TextArea.setForeground(Color.red);
        TextArea.setText(message);
    }

    /**
     * Helper method to clear the 3 frequently searched word text fields.
     */
    private void clearFreqWordFields()
    {
        TextFreqWord1.setText("");
        TextFreqWord2.setText("");
        TextFreqWord3.setText("");
    }

    /**
     * Used for printing non errors on to the text area
     * @param message to be printed on text area
     */
    private void printTextArea(String message)
    {
        resetTextAreaIfKeyed = true;
        TextArea.setForeground(Color.black);
        TextArea.setText(message);
    }

    public static void main(String[] args)
    {
        EventQueue.invokeLater(() ->
        {
            JFrame mainFrame = new JFrame("Dictionary");
            mainFrame.setSize(370, 700);
            mainFrame.setMinimumSize(new Dimension(370,700));
            mainFrame.setContentPane(new Dictionary().contentPanel);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.pack();
            mainFrame.setVisible(true);
        });
    }
}
