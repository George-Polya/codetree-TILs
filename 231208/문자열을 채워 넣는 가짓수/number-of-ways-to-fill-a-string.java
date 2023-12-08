import java.util.*;
import java.io.*;


public class Main {
	static int n,m;
	static String text;
	static class TrieNode{
		boolean isEnd;
		TrieNode children[] = new TrieNode[26];
		
		public TrieNode() {
			isEnd = false;
			for(int i = 0; i < 26; i++) {
				children[i] = null;
			}
		}
	}
	static StringTokenizer st;
	static TrieNode root = new TrieNode();
	
	static void insertWord(String word) {
		TrieNode t = root;
		
		for(int i = 0; i < word.length();i++) {
			int index = word.charAt(i) - 'a';
			if(t.children[index] == null)
				t.children[index] = new TrieNode();
			t = t.children[index];
		}
		
		t.isEnd = true;
	}
	
	static int dp[];
	static int MOD = (int)1e9+7;
	
	static void updateDP(int idx) {
		TrieNode t = root;
		
		for(int i = idx; i < text.length();i++) {
			int index = text.charAt(i) - 'a';
			t = t.children[index];
			if(t == null) {
				break;
			}
			
			if(t.isEnd) {
				dp[i + 1] += dp[idx];
				dp[i + 1] %= MOD;
			}
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		text = st.nextToken();
		n = text.length();
		m = Integer.parseInt(st.nextToken());
		
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i < m ;i++) {
			insertWord(st.nextToken());
		}
		
		dp = new int[n + 1];
		dp[0] = 1;
		
		for(int i = 0; i < n; i++) {
			updateDP(i);
		}
		
		System.out.println(dp[n]);
		
	}
}