package com.stevesun.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a list of words, please write a program that returns all concatenated words in the given list of words.

 A concatenated word is defined as a string that is comprised entirely of at least two shorter words in the given array.

 Example:
 Input: ["cat","cats","catsdogcats","dog","dogcatsdog","hippopotamuses","rat","ratcatdogcat"]

 Output: ["catsdogcats","dogcatsdog","ratcatdogcat"]

 Explanation: "catsdogcats" can be concatenated by "cats", "dog" and "cats";
 "dogcatsdog" can be concatenated by "dog", "cats" and "dog";
 "ratcatdogcat" can be concatenated by "rat", "cat", "dog" and "cat".
 Note:
 The number of elements of the given array will not exceed 10,000
 The length sum of elements in the given array will not exceed 600,000.
 All the input string will only include lower case letters.
 The returned elements order does not matter.

 */
public class ConcatenatedWords {

	private TrieNode root;
	private int maxWordLen;

	public List<String> findAllConcatenatedWordsInADict(String[] words) {
		ResultType result = buildTrie(words);
		root = result.root;
		maxWordLen = result.maxWordLen;

		List<String> validConcatenatedWords = new ArrayList();
		for (String word : words) {
			if (word == null || word.length() == 0) continue;
			remove(word, root);/** every word is comprised of every word itself, thus this word itself needs to be removed first for checking it*/
			int n = word.length();
			boolean[] dp = new boolean[n + 1];
			dp[0] = true;

			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= i && j <= maxWordLen; j++) {
					if (!dp[i - j])
						continue;

					String subWord = word.substring(i - j, i);
					if (contains(subWord, root)) {
						dp[i] = true;
						break;
					}
				}
			}

			if(dp[n]) validConcatenatedWords.add(word);
			undoRemove(word, root);
		}
		return validConcatenatedWords;
	}

	public ResultType buildTrie(String[] words) {
		ResultType result = new ResultType();

		TrieNode root = new TrieNode();
		int maxWordLen = 0;

		for(String word : words){
			maxWordLen = Math.max(maxWordLen, word.length());
			char[] chars = word.toCharArray();
			TrieNode node = root;
			for(int i = 0; i < chars.length; i++){
				char c = chars[i];
				if(node.children[c - 'a'] == null){
					node.children[c - 'a'] = new TrieNode();
				}
				node = node.children[c - 'a'];
			}
			node.isWord = true;
		}

		result.root = root;
		result.maxWordLen = maxWordLen;
		return result;
	}

	public class ResultType{
		int maxWordLen;
		TrieNode root;
	}

	// Returns true if the word is in the trie.
	public boolean contains(String word, TrieNode root) {
		TrieNode node = root;
		for(int i = 0; i < word.length(); i++){
			if(node.children[word.charAt(i) - 'a'] == null) return false;
			node = node.children[word.charAt(i) - 'a'];
		}
		return node.isWord;
	}

	// mark that word on
	public void undoRemove(String word, TrieNode root) {
		TrieNode node = root;
		for(int i = 0; i < word.length(); i++){
			node = node.children[word.charAt(i) - 'a'];
		}
		node.isWord = true;
	}

	// mark that word off, we are not really deleting that word
	public void remove(String word, TrieNode root) {
		TrieNode node = root;
		for(int i = 0; i < word.length(); i++){
			node = node.children[word.charAt(i) - 'a'];
		}
		node.isWord = false;
	}

	class TrieNode {
		boolean isWord;
		TrieNode[] children = new TrieNode[26];

		public TrieNode() {}
	}
}
