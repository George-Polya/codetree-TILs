import java.util.*;
import java.io.*;


public class Main {
	static int n;
	static class Trie{
		TreeMap<String, Trie> children = new TreeMap<>();
	}
	
	static Trie root = new Trie();
	static StringTokenizer st;
	
	static void insertWords(String words[]) {
		Trie t = root;
		
		for(String word : words) {
			if(!t.children.containsKey(word)) {
				t.children.put(word, new Trie());
			}
			t = t.children.get(word);
		}
	}
	
	static StringBuilder sb = new StringBuilder();
	static void dfs(Trie cur, int depth) {
		for(String key : cur.children.keySet()) {
			for(int i = 0; i < 2 * depth;i++)
				sb.append("-");
			sb.append(key).append('\n');
			
			
			dfs(cur.children.get(key), depth + 1);
		}
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		
		for(int i = 0; i < n ;i++) {
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			String words[] = new String[num];
			for(int j = 0; j<num; j++) {
				words[j] = st.nextToken();
			}
//			System.out.println(Arrays.toString(words));
			insertWords(words);
		}
		
		
		dfs(root,0);
		System.out.println(sb);
	}
}