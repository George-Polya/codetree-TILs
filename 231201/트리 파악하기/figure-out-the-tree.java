import java.util.*;
import java.io.*;
public class Main {
	static int n;
	static StringTokenizer st;
	static class TrieNode{
		TreeMap<String, TrieNode> children = new TreeMap<>();
	}
	
	static TrieNode root = new TrieNode();
	
	
	static void insertWord(String words[]) {
		TrieNode t = root;
		for(String word : words) {
			if(!t.children.containsKey(word)) {
				t.children.put(word, new TrieNode());
			}
			
			t = t.children.get(word);
		}
	}
	
	static StringBuilder sb = new StringBuilder();
	
	static void dfs(TrieNode node ,int depth) {
		for(String key : node.children.keySet()) {
//			sb.append("-".repeat(2 * depth)).append(key).append('\n');
			for(int i = 0; i < depth;i++) {
				sb.append("--");
			}
			sb.append(key).append('\n');
			dfs(node.children.get(key), depth + 1);
		}
	}
	
	static String listOfWords[][];
	
    public static void main(String[] args) throws IOException{
//        String S = "abcd"; // 주어진 문자열
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	
    	n = Integer.parseInt(br.readLine());
    	listOfWords = new String[n][];
    	
    	for(int i = 0; i < n ;i++) {
    		st = new StringTokenizer(br.readLine());
    		int num = Integer.parseInt(st.nextToken());
    		listOfWords[i] = new String[num];
    		for(int j = 0; j < num; j++) {
    			listOfWords[i][j] = st.nextToken();
    		}
    	}
    	
    	for(String[] words : listOfWords) {
    		insertWord(words);
    	}
    	
    	
    	dfs(root,0);
    	System.out.println(sb);
    }

  
}