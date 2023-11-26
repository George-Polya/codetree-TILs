import java.util.*;
import java.io.*;
public class Main {
	static int n;
	static StringTokenizer st;
	static class TrieNode{
		int len, num;
		boolean isEnd;
		char value;
		TrieNode children[] = new TrieNode[26];
		public TrieNode(char value) {
			this.value = value;
			len = 0;
			num = 0;
			isEnd = false;
			for(int i = 0; i < 26; i++)
				children[i] = null;
		}
	}
	static TrieNode root = new TrieNode('-');
	static void insertWord(String str) {
		TrieNode t = root;
		for(int i = 0; i < str.length(); i++) {
			int idx = str.charAt(i) - 'a';
			
			if(t.children[idx] == null) {
				t.children[idx] = new TrieNode(str.charAt(i));
				t.children[idx].len = t.len + 1;
			}
			
			t = t.children[idx];
			t.num++;
		}
	}
	static int ans;
	static void dfs(TrieNode cur) {
		for(TrieNode child : cur.children) {
			if(child == null)
				continue;
//			System.out.println(child.value+" "+child.len);
			ans = Math.max(ans, child.len * child.num);
//			System.out.printf("%c %d %d\n", child.value, child.len, child.num);
			dfs(child);
		}
	}
	
    public static void main(String[] args) throws IOException{
//        String S = "abcd"; // 주어진 문자열
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	n = Integer.parseInt(br.readLine());
    	st = new StringTokenizer(br.readLine());
    	
    	for(int i = 0; i < n; i++) {
    		insertWord(st.nextToken());
    	}
    	
    	dfs(root);
    	System.out.println(ans);
    }

  
}