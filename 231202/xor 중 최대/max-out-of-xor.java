import java.util.*;
import java.io.*;
public class Main {
	static int n;
	static String arr[];
	static StringTokenizer st;
	
	static int p2[] = new int[32];	
	
	
	static String convert2String(int x) {
		String ret = "";
		
		for(int i = 30; i>=0; i--) {
			if(x >= p2[i]) {
				x -= p2[i];
				ret += "1";
			}else
				ret += "0";
		}
		return ret;
	}
	
	
	static class TrieNode{
		TrieNode children[] = new TrieNode[2];
		
		public TrieNode() {
			for(int i = 0; i < 2; i++) {
				children[i] = null;
			}
		}
	}
	static TrieNode root = new TrieNode();
	
	static void insertWord(String str) {
		TrieNode t = root;
		
		for(int i = 0; i < str.length(); i++) {
			int idx = str.charAt(i) - '0';
			
			if(t.children[idx] == null)
				t.children[idx] = new TrieNode();
			t = t.children[idx];
		}
	}
	
	static int searchWord(String s) {
		String ret = "";
		
		TrieNode t = root;
		
		for(int i = 0; i < s.length();i++) {
			int idx = s.charAt(i) -'0';
			
			if(t.children[1 - idx] != null) {
				t = t.children[1-idx];
				ret += "1";
			}else {
				t = t.children[idx];
				ret += "0";
			}
		}
		
		int value = 0;
		
		for(int i = 0; i < ret.length();i++) {
			value *= 2;
			value += (ret.charAt(i) - '0');
		}
		
		return value;
	}
	
	public static void main(String[] args) throws IOException{
		p2[0] = 1;
		for(int i = 1; i<=30;i++) {
			p2[i] = p2[i-1] * 2;
		}
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		st = new StringTokenizer(br.readLine());
		arr = new String[n];
		for(int i = 0; i < n ;i++) {
			int num = Integer.parseInt(st.nextToken());
			arr[i] = convert2String(num);
			insertWord(arr[i]);
		}
		
		int ans = 0;
		
		for(int i = 0; i < n ;i++) {
			int num = searchWord(arr[i]);
			ans = Math.max(ans, num);
		}
		
		System.out.println(ans);
		
		
		
	}
	
}