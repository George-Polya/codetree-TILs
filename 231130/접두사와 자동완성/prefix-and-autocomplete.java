import java.io.*;
import java.util.*;

public class Main{
	static int n;
	static StringTokenizer st;
	static class TrieNode{
		int childCnt;
		boolean isEnd;
		TrieNode children[] = new TrieNode[26];
		
		public TrieNode() {
			isEnd = false;
			childCnt = 0;
			for(int i = 0; i < 26;i++) {
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
				t.childCnt += 1;
				t.children[idx] = new TrieNode();
			}
			
			t = t.children[idx];
		}
		
		t.isEnd = true;
	}
	
	static String words[];
	
	static int getAllChildCnt(String str) {
		TrieNode t = root;
		
		int ret = 0;
		for(int i = 0; i < str.length();i++) {
			int idx = str.charAt(i) - 'a';
			if(t.children[idx] == null)
				continue;
			
			if(t.children[idx].childCnt == 1)
				ret++;
			t = t.children[idx];
			
		}
		return ret;
	}
	
	
	static int search(String str) {
		return str.length() - getAllChildCnt(str); 
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		words = new String[n];
		st = new StringTokenizer(br.readLine());
		
		for(int i = 0; i < n ;i++) {
			words[i] = st.nextToken();
			insertWord(words[i]);
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < n; i++) {
			sb.append(search(words[i])).append(' ');
		}
		System.out.println(sb);
		
	}
}