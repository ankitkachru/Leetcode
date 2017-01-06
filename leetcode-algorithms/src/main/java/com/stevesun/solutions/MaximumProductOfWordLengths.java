package com.stevesun.solutions;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**318. Maximum Product of Word Lengths  QuestionEditorial Solution  My Submissions
Total Accepted: 29054
Total Submissions: 71485
Difficulty: Medium
Given a string array words, find the maximum value of length(word[i]) * length(word[j]) where the two words do not share common letters. You may assume that each word will contain only lower case letters. If no such two words exist, return 0.

Example 1:
Given ["abcw", "baz", "foo", "bar", "xtfn", "abcdef"]
Return 16
The two words can be "abcw", "xtfn".

Example 2:
Given ["a", "ab", "abc", "d", "cd", "bcd", "abcd"]
Return 4
The two words can be "ab", "cd".

Example 3:
Given ["a", "aa", "aaa", "aaaa"]
Return 0
No such pair of words.*/
public class MaximumProductOfWordLengths {
    //Inspired by this awesome post: https://discuss.leetcode.com/topic/35539/java-easy-version-to-understand
    //Idea: this question states that all words consisted of lower case (total only 26 unique chars), 
    //this is a big hint that we could use integer (total 32 bits) to represent each char
    //values[i] means how many unique characters this string words[i] has
    public int maxProduct(String[] words){
        if(words == null || words.length == 0) return 0;
        int len = words.length;
        int[] values = new int[len]; 
        for(int i = 0; i < words.length; i++){
            String word = words[i];
            for(int j = 0; j < words[i].length(); j++){
                values[i] |= 1 << (word.charAt(j) -'a');//the reason for left shift by this number "word.charAt(j) -'a'" is for 'a', otherwise 'a' - 'a' will be zero and 'a' will be missed out.
            }
        }
        int maxProduct = 0;
        for(int i = 0; i < words.length; i++){
            for(int j = 0; j < words.length; j++){
                //check if values[i] AND values[j] equals to zero, this means they share NO common chars
                if((values[i] & values[j]) == 0 && words[i].length() * words[j].length() > maxProduct) maxProduct = words[i].length()*words[j].length();
            }
        }
        return maxProduct;
    }
    
    //This is still failed due to TLE, O(n^3) algorithm is the core defect, you'll have to come up with a faster one!
    public int maxProduct_with_pruning(String[] words) {
        int maxProduct = 0;
        //use a customized comparator to make the words list sorted in descending order, brilliant!
        Arrays.sort(words, new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {
                if(o1.length() > o2.length()) return -1;
                else if(o1.length() < o2.length()) return 1;
                else return 0;
            }
        });
        for(int i = 0; i < words.length-1; i++){
            String currWord = words[i];
            int currWordLen = currWord.length();
            if(maxProduct > currWordLen * words[i+1].length()) break;//pruning
            char[] chars = currWord.toCharArray();
            Set<Character> set = new HashSet();
            for(char c : chars) set.add(c);
            for(int j = i+1; j < words.length; j++){
                char[] chars2 = words[j].toCharArray();
                boolean valid = true;
                for(char c : chars2){
                    if(set.contains(c)) {
                        valid = false;
                        break;
                    }
                }
                if(valid){
                    int thisWordLen = words[j].length();
                    maxProduct = Math.max(maxProduct, thisWordLen*currWordLen);
                }
            }
        }
        return maxProduct;
    }
    
    /**My natural idea is an O(n^3) algorithm, I thought of Trie, but I don't think it applies well to this question.
     * This following algorithm made it pass 173/174 test cases, as expected, failed by the last extreme test cases due to TLE.*/
    public int maxProduct_most_brute_force(String[] words) {
        int maxProduct = 0;
        for(int i = 0; i < words.length-1; i++){
            String currWord = words[i];
            int currWordLen = currWord.length();
            char[] chars = currWord.toCharArray();
            Set<Character> set = new HashSet();
            for(char c : chars) set.add(c);
            for(int j = i+1; j < words.length; j++){
                char[] chars2 = words[j].toCharArray();
                boolean valid = true;
                for(char c : chars2){
                    if(set.contains(c)) {
                        valid = false;
                        break;
                    }
                }
                if(valid){
                    int thisWordLen = words[j].length();
                    maxProduct = Math.max(maxProduct, thisWordLen*currWordLen);
                }
            }
        }
        return maxProduct;
    }
    
    public static void main(String...strings){
        MaximumProductOfWordLengths test = new MaximumProductOfWordLengths();
        String[] words = new String[]{"abcw","baz","foo","bar","xtfn","abcdef"};
//        System.out.println(test.maxProduct_with_pruning(words));
//        System.out.println(test.maxProduct(words));
        
        //The following is to understand what does left shift by 1 mean:
        //the tricky part is to understand how it's written for me:
        // "x << y" means left shift x by y bits
        //left shift is equivalent to multiplication of powers of 2, so "4 << 1" equals to " 4 * 2^1"
        //similarly, "4 << 3" equals to "4 * 2^3" which equals "4 * 8"
        String sample = "f";
        int bits = 0, shiftLeftByHowMany = 0, shiftLeftResult = 0;
        for(int j = 0; j < sample.length(); j++){
            shiftLeftByHowMany = sample.charAt(j) -'a';
            shiftLeftResult = 1 << shiftLeftByHowMany;
            bits |= 1 << (sample.charAt(j) -'a');//this means shift left 1 by "sample.charAt(j) -'a'" bits 
            System.out.println("nonShiftLeft = " + shiftLeftByHowMany + "\tnonShiftLeft binary form is: " + Integer.toBinaryString(shiftLeftByHowMany)
                    + "\nshiftLeft = " + shiftLeftResult + "\tshiftLeft binary form is: " + Integer.toBinaryString(shiftLeftResult) 
                    + "\nbits = " + bits + "\tbits binary form is: " + Integer.toBinaryString(bits));
            System.out.println(shiftLeftResult == (1 * Math.pow(2, shiftLeftByHowMany)));
        }
        
        //similarly, right shift is written like this: "x >> y", means shift x by y bits
        //4 >> 3 equals 4 * 2^3, see below:
        System.out.println(4*8 == (4 * Math.pow(2, 3)));
    }
}
