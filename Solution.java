import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// +==============================+
// | @author Isac Canedo          |
// +==============================+

public class Solution {
  private static final int R = 26;
  private final String WORDS_PATH = "words";
  private final String TESTCASES_PATH = "testcases";

  private static class Node{
    boolean isWord = false;
    Node[] next = new Node[R];
  }

  private static class Dictionary{
    Node root;

    Dictionary(List<String> words) {
      for (String word : words) {
        put(word);
      }
    }

    public void put(String key) {
      root = put(root, key, 0);
    }

    private Node put(Node x, String key, int d) {
      if (x == null) x = new Node();
      if (d == key.length()) {
        x.isWord = true;
        return x;
      }
      int idx  = key.charAt(d) - 'a';
      x.next[idx] = put(x.next[idx], key, d + 1);
      return x;
    }
  }

  private List<String> solver(String characters, Dictionary dict) {
    List<String> result = new ArrayList<>();

    int[] count = new int[R];
    for (char c : characters.toLowerCase().toCharArray()) {
      count[c - 'a'] ++;
    }
    // search for all anagrams
    backtracking(result, new StringBuilder(), count, characters.length(), dict.root);
    return result;
  }

  // find all words base on the trie
  private void backtracking(List<String> result, StringBuilder path, int[] count, int target, Node node) {
    if (path.length() == target) {
      if (node.isWord) result.add(path.toString());
      return;
    }
    for (int i = 0; i < R; i ++) {
      if (count[i] > 0 && node.next[i] != null){
        count[i] --;
        path.append((char)('a' + i));
        backtracking(result, path, count, target, node.next[i]);
        path.deleteCharAt(path.length() - 1);
        count[i] ++;
      }
    }
  }


  private boolean isValid(String word) {
    // character verification
    for (char c : word.toCharArray()) {
      if (c < 'a' || c > 'z') return false;
    }
    return true;
  }

  // load the words to build the trie for the dictionary
  private List<String> loadWords() {
    List<String> words = new ArrayList<>();
    try {
      Scanner scanner = new Scanner(new File(WORDS_PATH));
      while (scanner.hasNextLine()) {
        String word = scanner.nextLine();
        if (isValid(word)) {
          words.add(word.toLowerCase());
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return words;
  }

  // loading test case
  private List<String> loadTestCases() {
    List<String> testcases = new ArrayList<>();
    try {
      Scanner scanner = new Scanner(new File(TESTCASES_PATH));
      while (scanner.hasNextLine()){
        testcases.add(scanner.nextLine().toLowerCase());
      }
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    return testcases;
  }


  public static void main(String[] args) {
    Solution solution = new Solution();
    Dictionary dict = new Solution.Dictionary(solution.loadWords());
    for (String tc : solution.loadTestCases()) {
      System.out.println(String.format("Testcase : %s", tc));
      System.out.print("result: ");
      System.out.print(String.join(" ", solution.solver(tc, dict)));
      System.out.print("\n\n");
    }
  }
}
