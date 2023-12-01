import java.util.*;
import java.io.*;
public class Main {
	static String text;
	static int n, m;
	static int MOD = (int)1e9 + 7;
	static StringTokenizer st;
	
	static class TrieNode{
		boolean isEnd;
		TrieNode children[] = new TrieNode[26];
		
		public TrieNode() {
			isEnd = false;
			for(int i = 0; i <26; i++) {
				children[i] = null;
			}
		}
	}
	
	static TrieNode root = new TrieNode();
	
	static void insertWord(String str) {
		TrieNode t = root;
		
		for(int i = 0; i < str.length();i++) {
			int idx = str.charAt(i) - 'a';
			
			if(t.children[idx] == null) {
				t.children[idx] = new TrieNode();
			}
			
			t = t.children[idx];
		}
		
		t.isEnd = true;
	}
	
	
	static int dp[];
	
	static void updateDP(String str,int idx) {
		TrieNode t = root;
		
		for(int i = idx; i < str.length();i++) {
			int index = str.charAt(i) - 'a';
			
			t = t.children[index];
			
			if(t == null)
				break;
			
			if(t.isEnd) {
				dp[i+1] += dp[idx];
				dp[i+1] %= MOD;
			}
			
		}
	}
    public static void main(String[] args) throws IOException{
//        String S = "abcd"; // 주어진 문자열
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	text = st.nextToken();
    	n = text.length();
    	m = Integer.parseInt(st.nextToken());
    	st = new StringTokenizer(br.readLine());
    	for(int i = 0; i < m ;i++) {
    		insertWord(st.nextToken());
    	}
    	
    	dp = new int[n+1];
    	dp[0] = 1;
    	
    	for(int i = 0; i < n ;i++) {
    		updateDP(text, i);
    	}
    	
    	System.out.println(dp[n]);
    }

  
}