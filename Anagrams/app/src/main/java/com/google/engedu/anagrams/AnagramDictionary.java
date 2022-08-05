/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
// https://stackoverflow.com/questions/16780294/how-to-print-to-the-console-in-android-studio
public class AnagramDictionary {
    /**
     * This class will store the valid words from the text file
     * and handle selecting and checking words for the game.
     * */
    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>(); // Refactor AnagramDictionary to give words of increasing length.
    private int wordLength = DEFAULT_WORD_LENGTH; // Refactor AnagramDictionary to give words of increasing length.

    public AnagramDictionary(Reader reader) throws IOException {
        /**
         * The constructor. It should store the words in the
         * appropriate data structures
         * */
        BufferedReader in = new BufferedReader(reader);
        String line;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = this.sortLetters(word);
            // As you process the input words, call sortLetters on each of them then check whether lettersToWord already
            // contains an entry for that key. If it does, add the current word to ArrayList at that key. Otherwise, create
            // a new ArrayList, add the word to it and store in the HashMap with the corresponding key.
            if (!lettersToWord.containsKey(sortedWord)) {
                ArrayList<String> anagrams = new ArrayList<>();
                anagrams.add(word);
                lettersToWord.put(sortedWord, anagrams);
            } else {
                lettersToWord.get(sortedWord).add(word);
            }
            // Refactoring: This refactor starts in the constructor where, in addition to populating wordList, you should
            // also store each word in a HashMap (let's call it sizeToWords) that maps word length to an ArrayList of all
            // words of that length. This means, for example, you should be able to get all four-letter words in the dictionary
            // by calling sizeToWords.get(4).
            int lengthOfInputWord = word.length();
            if (!sizeToWords.containsKey(lengthOfInputWord)) {
                ArrayList<String> wordsOfSameLength = new ArrayList<>();
                wordsOfSameLength.add(word);
                sizeToWords.put(lengthOfInputWord, wordsOfSameLength);
            } else {
                sizeToWords.get(lengthOfInputWord).add(word);
            }
        }

        // Run app in debug mode to print to console
        Log.d("BEFORE OPTIMIZATION", "wordList length: " + String.valueOf(wordList.size()));
        Log.d("BEFORE OPTIMIZATION", "lettersToWord length: " + String.valueOf(lettersToWord.size()));

        // Optimize word selection by removing words that do not have enough anagrams from the pool of possible starter words.
        // Note that those words should still remain in wordSet since they can still be used as anagrams to other words.

        // Run app in debug mode to print to console
        Log.d("AFTER OPTIMIZATION", "wordList length: " + String.valueOf(wordList.size()));
        Log.d("AFTER OPTIMIZATION", "lettersToWord length: " + String.valueOf(lettersToWord.size()));
    }

    public boolean isGoodWord(String word, String base) {
        /**
         * Asserts that the given word is in the dictionary and
         * isn't formed by adding a letter to the start or end of
         * the base word.
         *
         * 1. Checks the provided word is a valid dictionary word (i.e., in wordSet), and
         * 2. Checks the word does not contain the base word as a substring.
         * */
        return wordSet.contains(word) && !word.contains(base);
    }

    public List<String> getAnagrams(String targetWord) {
        /**
         * Creates a list of all possible anagrams of a given word.
         * */
        ArrayList<String> result = new ArrayList<String>();
        String sortedTargetWord = this.sortLetters(targetWord);
        // Our strategy for now will be straight-forward: just
        // compare each string in wordList to the input word to
        // determine if they are anagrams.
        for (String word : wordList) {
            if (sortedTargetWord.equals(this.sortLetters(word))) result.add(word);
        }
        return result;
    }

    private String sortLetters(String word) {
        /**
         * There are different strategies that you could employ to
         * determine whether two strings are anagrams of each other
         * but for our purpose you will create a helper function that
         * takes a String and returns another String with the same
         * letters in alphabetical order. Determining whether two
         * strings are anagrams is then a simple matter of checking
         * that they are the same length (for the sake of speed) and
         * checking that the sorted versions of their letters are
         * equal.
         * */
        char[] wordCharacterArray = word.toCharArray(); // Convert word string to character array
        Arrays.sort(wordCharacterArray); // Sort character array in alphabetical order
        String sortedString = new String(wordCharacterArray); // Convert sorted character array to string
        return sortedString;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        /**
         * Creates a list of all possible words that can be formed
         * by adding one letter to the given word.
         * */
        ArrayList<String> result = new ArrayList<String>();
        // Be sure to instantiate a new ArrayList as your return value
        // then check the given word + each letter of the alphabet one
        // by one against the entries in lettersToWord.
        for (char letter = 'a'; letter <= 'z'; letter++) {
            String possibleWord = this.sortLetters(word + letter);
            if (lettersToWord.containsKey(possibleWord)) result.addAll(lettersToWord.get(possibleWord));
        }
        return result;
    }

    public String pickGoodStarterWord() {
        /**
         * Randomly selects a word with at least the desired number of anagrams.
         * Pick a random starting point in the wordList array and check each word
         * in the array until you find one that has at least MIN_NUM_ANAGRAMS anagrams.
         * Be sure to handle wrapping around to the start of the array if needed.
         * */
        ArrayList<String> wordsOfSameLength = sizeToWords.get(this.wordLength);
        int randomStartingPoint = random.nextInt(wordsOfSameLength.size());
        while (this.getAnagramsWithOneMoreLetter(wordsOfSameLength.get(randomStartingPoint)).size() < MIN_NUM_ANAGRAMS) {
            randomStartingPoint = (randomStartingPoint + 1) % wordsOfSameLength.size();
        }
        // Increment word length so that the next invocation will return a larger word.
        if (this.wordLength < MAX_WORD_LENGTH) this.wordLength++;
        return wordsOfSameLength.get(randomStartingPoint);
    }
}