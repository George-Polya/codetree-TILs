import java.util.*;
import java.io.*;


public class Main {
	static StringTokenizer st;
	static int n;
	static String pattern;
	static class Node{
		int id;
		char ch;
		public Node(int id, char ch) {
			this.id = id;
			this.ch = ch;
		}
	}
	
	static ArrayList<Node> adj[];
	static int p[] = {31,37};
	static int mod[] ={(int)1e9+7, (int)1e9+9};
	static long pPow[][];
	static long pH[];
	static int l;
	static int toInt(char ch) {
		return ch - 'a' + 1;
	}
	
	static int ans;
	static void dfs(int cur,int depth, String text, long h1, long h2) {
		long tH[] = new long[] {h1,h2};
		if(depth == l) {
			for(int k = 0; k < 2; k++) {
				for(int i = 0; i < l; i++) {
					tH[k] = (tH[k] + toInt(text.charAt(i)) * pPow[k][l - i - 1]) % mod[k];
				}
			}
		}else if(depth > l) {
			for(int k = 0; k < 2; k++) {
				tH[k] = (tH[k] * p[k] - toInt(text.charAt(depth - l - 1)) * pPow[k][l] + toInt(text.charAt(depth - 1))) % mod[k];
				if(tH[k] < 0)
					tH[k] += mod[k];
			}
		}
		
		if(tH[0] == pH[0] && tH[1] == pH[1])
			ans++;
		
		for(Node nxt : adj[cur]) {
			dfs(nxt.id, depth + 1, text + nxt.ch, tH[0], tH[1]);
		}
		
	}
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		pattern = st.nextToken();
		l = pattern.length();
		adj = new ArrayList[n+1];
		for(int i = 1; i<=n; i++) {
			adj[i] = new ArrayList<>();
		}
		
		for(int i = 0; i < n-1;i++) {
			st = new StringTokenizer(br.readLine());
			int from = Integer.parseInt(st.nextToken());
			int to = Integer.parseInt(st.nextToken());
			char ch = st.nextToken().charAt(0);
			
			adj[from].add(new Node(to, ch));
		}
		
		
		pPow = new long[2][n+1];
		
		for(int k =0 ;k < 2; k++) {
			pPow[k][0] = 1;
			for(int i = 1; i<=n; i++) {
				pPow[k][i] = (pPow[k][i-1] * p[k]) % mod[k];
			}
		}
		
		
		pH = new long[2];
		for(int k = 0; k < 2; k++) {
			for(int i = 0; i <l ;i++ ) {
				pH[k] = (pH[k] + toInt(pattern.charAt(i)) * pPow[k][l - i - 1]) % mod[k];
			}
		}
		
		dfs(1,0,"",0,0);
		
		System.out.println(ans);
		
	}
}