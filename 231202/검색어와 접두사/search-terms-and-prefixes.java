import java.util.*;
import java.io.*;
public class Main {
	static int n,m;
	static String s;
	static StringTokenizer st;
	
	static class TrieNode{
		boolean isEnd;
		int num;
		TrieNode children[] = new TrieNode[26];
		
		public TrieNode() {
			isEnd = false;
			num = 0;
			for(int i = 0; i < 26; i++) {
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
			t.num++;
		}
	}
	
	static void search(String s) {
		StringBuilder sb = new StringBuilder();
		
		TrieNode t = root;
		for(int i = 0; i < s.length();i++) {
			if(t!= null) {
				int idx = s.charAt(i) - 'a';
				
				t = t.children[idx];
			}
			
			if(t != null)
				sb.append(t.num).append(' ');
			else
				sb.append(0).append(' ');
		}
		System.out.println(sb);
	}
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i < n;i++) {
			insertWord(st.nextToken());
		}
		
		s = br.readLine();
		
		search(s);
		
	}
	
}