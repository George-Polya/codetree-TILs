import java.io.*;
import java.util.*;

public class Main{
	static String convert2String(int x) {
		String ret = "";
		for(int i = 30; i>=0; i--) {
			if(x >= p2[i]) {
				x -= p2[i];
				ret += "1";
			}else {
				ret += "0";
			}
		}
		return ret;
	}
	
	static int p2[] = new int[32];
	static int n;
	static StringTokenizer st;
	static int arr[];
	
	static class Trie{
		boolean isEnd;
		Trie children[] = new Trie[2];
		
		public Trie() {
			isEnd = false;
			for(int i = 0; i < 2; i++) {
				children[i] = null;
			}
		}
	}
	static Trie root = new Trie();
	
	static void insertWord(String str) {
		Trie t = root;
		
		for(int i = 0; i< str.length();i++) {
			int idx = str.charAt(i) - '0';
			
			if(t.children[idx] == null) {
				t.children[idx] = new Trie();
			}
			
			t = t.children[idx];
		}
		
		t.isEnd = true;
	}
	
	static String texts[];
	
	static int ans;
	
	static int str2Num(String text) {
		int ret = 0;
		for(int i = 0; i < text.length();i++) {
			ret *= 2;
			ret += (text.charAt(i) - '0');
		}
		
		return ret;
	}
	
	static void search(String text) {
		Trie t = root;
		String ret = "";
		for(int i = 0; i < text.length(); i++) {
			int idx = text.charAt(i) - '0';
			
			if(t.children[1-idx] != null) {
				ret += "1";
				t = t.children[1-idx];
			}else {
				ret += "0";
				t = t.children[idx];
			}
		}
		
		
		int num = str2Num(ret);
		ans = Math.max(ans,  num);
		
	}
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		p2[0] = 1;
		for(int i = 1; i<32;i++) {
			p2[i] = p2[i-1] * 2;
		}
		n = Integer.parseInt(br.readLine());
		arr = new int[n];
		texts = new String[n];
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i < n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
			texts[i] = convert2String(arr[i]);
			insertWord(texts[i]);
		}
		
		for(int i = 0; i < n;i++) {
			search(texts[i]);
		}
		
		System.out.println(ans);
		
	}
}