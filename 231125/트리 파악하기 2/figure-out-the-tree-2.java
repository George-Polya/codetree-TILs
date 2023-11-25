import java.util.*;
import java.io.*;
public class Main {
	static int n;
	static StringTokenizer st;
	static class TrieNode{
		char value;
		boolean isEnd;
		TrieNode[] children = new TrieNode[26];
		
		
		public TrieNode(char value) {
			this.value = value;
			isEnd = false;
			for(int i = 0; i < 26;i++) {
				children[i] = null;
			}
		}
	}
	
	static TrieNode root = new TrieNode('-');
	
	static void insertWord(String str) {
		TrieNode t = root;
		
		for(int i = 0; i < str.length();i++) {
			int idx = str.charAt(i) - 'A';
			if(t.children[idx] == null)
				t.children[idx] = new TrieNode(str.charAt(i));
			t = t.children[idx];
		}
		t.isEnd = true;
	}
	
	static StringBuilder sb = new StringBuilder();
	static void dfs(TrieNode cur, String str) {
		if(cur != root) {
//			sb.append(str + cur.value).append('\n');
			System.out.println(str + cur.value);
		}
		
		if(cur.isEnd)
			return;
		
		for(TrieNode child : cur.children) {
			if(child == null)
				continue;
			if(cur == root)
				dfs(child, str);
			else
				dfs(child, str + "--");
		}
	}
	
    public static void main(String[] args) throws IOException{
//        String S = "abcd"; // 주어진 문자열
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	
    	n = Integer.parseInt(br.readLine());
    	for(int i = 0; i < n; i++) {
    		st = new StringTokenizer(br.readLine());
    		int num = Integer.parseInt(st.nextToken());
    		String input = "";
    		for(int j = 0; j < num; j++) {
    			input += st.nextToken().charAt(0);
    		}
    		
    		insertWord(input);
    	}
    	
    	
    	dfs(root, "");
    	System.out.println(sb);
    }

  
}