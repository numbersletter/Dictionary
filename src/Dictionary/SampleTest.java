package Dictionary;

import java.awt.*;

public class SampleTest {
    public static void main(String[] args) {
        Dictionary myDictionary = new Dictionary();

        // Test for ADD
        // InvalidWordError
        myDictionary.TextNewWord.setText("s1mple");
        myDictionary.TextArea.setText("Best AWPer");
        try {
            myDictionary.ADDButton.doClick();
        } catch (InvalidWordError ex) {
            System.out.println("InvalidWordError passed");
        }

        // WordDuplicatedError
        myDictionary.TextNewWord.setText("niko");
        myDictionary.TextArea.setText("Fortunate entry fragger");
        myDictionary.ADDButton.doClick();

        myDictionary.TextNewWord.setText("niko");
        myDictionary.TextArea.setText("Rifler");
        try {
            myDictionary.ADDButton.doClick();
        } catch (WordDuplicatedError ex) {
            System.out.println("WordDuplicatedError passed");
        }

        // Valid ADD
        myDictionary = new Dictionary();

        myDictionary.TextNewWord.setText("niko");
        myDictionary.TextArea.setText("Fortunate entry fragger");
        myDictionary.ADDButton.doClick();

        System.out.println("Word niko ADD successfully.");

        // Test for CLEAR button
        myDictionary = new Dictionary();

        myDictionary.TextNewWord.setText("niko");
        myDictionary.TextOriginalWord.setText("nikoo");
        myDictionary.TextFreqWord1.setText("aaa");
        myDictionary.TextFreqWord2.setText("bbb");
        myDictionary.TextFreqWord3.setText("ccc");
        myDictionary.TextArea.setText("Fortunate entry fragger");

        myDictionary.CLEARButton.doClick();
        String tmp0 = myDictionary.TextNewWord.getText();
        String tmp1 = myDictionary.TextOriginalWord.getText();
        String tmp2 = myDictionary.TextFreqWord1.getText();
        String tmp3 = myDictionary.TextFreqWord2.getText();
        String tmp4 = myDictionary.TextFreqWord3.getText();
        String tmp5 = myDictionary.TextArea.getText();

        if (tmp0.equals("") && tmp1.equals("") && tmp2.equals("") && tmp3.equals("") && tmp4.equals("") && tmp5.equals("")){
            System.out.println("CLEAR button test passed");
        }

        // Test for FIND button
        myDictionary = new Dictionary();
        // "No Word Matched."
        myDictionary.TextNewWord.setText("NIKO");
        myDictionary.FINDButton.doClick();
        if (myDictionary.TextArea.getText().equals("No Word Matched.")) {
            System.out.println("No Word Matched test passed");
        }

        // Valid FIND Test 1
        myDictionary = new Dictionary();

        myDictionary.TextNewWord.setText("niko");
        myDictionary.TextArea.setText("Fortunate entry fragger");
        myDictionary.ADDButton.doClick();

        myDictionary.CLEARButton.doClick();

        myDictionary.TextNewWord.setText("nIko");
        myDictionary.TextArea.setText("Least Fortunate entry fragger");
        myDictionary.ADDButton.doClick();

        myDictionary.CLEARButton.doClick();

        myDictionary.TextNewWord.setText("nIKO");
        myDictionary.TextArea.setText("So-so Fortunate entry fragger");
        myDictionary.ADDButton.doClick();

        myDictionary.CLEARButton.doClick();

        myDictionary.TextNewWord.setText("ni"); // return niko
        myDictionary.FINDButton.doClick();

        if (myDictionary.TextFreqWord1.getText().equals("niko")) {
            System.out.println("FIND Test 1 passed");
        }

        // Valid FIND Test 2
        myDictionary = new Dictionary();

        myDictionary.TextNewWord.setText("niko");
        myDictionary.TextArea.setText("Fortunate entry fragger");
        myDictionary.ADDButton.doClick();

        myDictionary.CLEARButton.doClick();

        myDictionary.TextNewWord.setText("nIko");
        myDictionary.TextArea.setText("Least Fortunate entry fragger");
        myDictionary.ADDButton.doClick();

        myDictionary.CLEARButton.doClick();

        myDictionary.TextNewWord.setText("nIKO");
        myDictionary.TextArea.setText("So-so Fortunate entry fragger");
        myDictionary.ADDButton.doClick();

        myDictionary.CLEARButton.doClick();

        for (int numi = 0; numi < 3; numi++) {
            // freq of "niko" is 3
            myDictionary.TextNewWord.setText("niko");
            myDictionary.FINDButton.doClick();
        }

        for (int numi = 0; numi < 4; numi++) {
            // freq of "nIko" is 4
            myDictionary.TextNewWord.setText("nIko");
            myDictionary.FINDButton.doClick();
        }

        for (int numi = 0; numi < 5; numi++) {
            // freq of "nIKO" is 5
            myDictionary.TextNewWord.setText("nIKO");
            myDictionary.FINDButton.doClick();
        }

        myDictionary.CLEARButton.doClick();
        if(myDictionary.searchHistoryList.getModel().getElementAt(0).equals("nIKO")&&
                myDictionary.searchHistoryList.getModel().getElementAt(1).equals("nIko")&&
                myDictionary.searchHistoryList.getModel().getElementAt(2).equals("niko")){
            System.out.println("Search History Test passed");
        }
        else{
            System.out.println("Search History Test failed");
        }

        myDictionary.TextNewWord.setText("n");
        myDictionary.FINDButton.doClick();

        if (myDictionary.TextFreqWord1.getText().equals("nIKO")
                && myDictionary.TextFreqWord2.getText().equals("nIko")
                && myDictionary.TextFreqWord3.getText().equals("niko")) {
            System.out.println("FIND Test 2 passed");
        }

        // Test for REMOVE button
        // test for WordNotFoundError
        myDictionary = new Dictionary();

        myDictionary.TextNewWord.setText("NIKO");
        try {
            myDictionary.REMOVEButton.doClick();
        } catch (WordNotFoundError ex) {
            System.out.println("WordNotFoundError passed");
        }

        // Valid REMOVE
        myDictionary = new Dictionary();

        myDictionary.TextNewWord.setText("niko");
        myDictionary.TextArea.setText("Fortunate entry fragger");
        myDictionary.ADDButton.doClick();

        System.out.println("Word niko added.");

        myDictionary.CLEARButton.doClick();
        myDictionary.TextNewWord.setText("niko");
        myDictionary.REMOVEButton.doClick();

        System.out.println("REMOVE Test passed");

        // Test for MODIFY Button
        myDictionary = new Dictionary();

        myDictionary.TextNewWord.setText("niko");
        myDictionary.TextArea.setText("Fortunate entry fragger");
        myDictionary.ADDButton.doClick();

        myDictionary.CLEARButton.doClick();

        myDictionary.TextOriginalWord.setText("niko");
        myDictionary.TextNewWord.setText("NIKO");
        myDictionary.MODIFYButton.doClick();

        myDictionary.CLEARButton.doClick();

        myDictionary.TextNewWord.setText("NIKO");
        myDictionary.FINDButton.doClick();

        if (myDictionary.TextFreqWord1.getText().equals("NIKO")) {
            System.out.println("MODIFY Button Test passed");
        }

        // Test for IMPORT and EXPORT Button
        myDictionary = new Dictionary();
        myDictionary.TextFilePath.setText("./src/input.txt"); // change your path to input.txt
        myDictionary.IMPORTButton.doClick();

        for (int numi = 0; numi < 3; numi++) {
            // freq of "niko" is 3
            myDictionary.TextNewWord.setText("niko");
            myDictionary.FINDButton.doClick();
        }

        for (int numi = 0; numi < 4; numi++) {
            // freq of "nIko" is 4
            myDictionary.TextNewWord.setText("nIko");
            myDictionary.FINDButton.doClick();
        }

        for (int numi = 0; numi < 5; numi++) {
            // freq of "nIKO" is 5
            myDictionary.TextNewWord.setText("nIKO");
            myDictionary.FINDButton.doClick();
        }

        myDictionary.TextFilePath.setText("./src/output.txt"); // change your path to output.txt
        myDictionary.EXPORTButton.doClick();
        // compare your output.txt with output_ref.txt
    }
}