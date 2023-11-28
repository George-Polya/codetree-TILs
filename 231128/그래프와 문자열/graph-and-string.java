import java.io.*;
import java.util.*;

public class Main{
	static int n,l;
	static String pattern;
	static StringTokenizer st;
	static class Node{
		int id;
		char value;
		public Node(int id, char value) {
			this.id = id;
			this.value = value;
		}
	}
	
	static ArrayList<Node> adj[];
	
	static int mod[] = {(int)1e9 + 7, (int)1e9+9};
	static int p[] = {31, 37};
	static long pPow[][];
	static long pH[];
	static int ans;
	static char path[];
	static void dfs(int cur,int depth, String str, long h1, long h2) {
		long tH[] = new long[] {h1,h2};
		if(depth == l) {
			for(int k = 0; k < 2; k++) {
				for(int i = 0; i < l; i++) {
//					tH[k] = (tH[k] + toInt(path[i]) * pPow[k][l - i - 1]) % mod[k];
					tH[k] = (tH[k] + toInt(str.charAt(i)) * pPow[k][l - i - 1]) % mod[k];
				}
			}
			
			
			
		}else if(depth > l) {
			for(int k = 0; k < 2; k++) {
//				tH[k] = (tH[k] * p[k] - toInt(path[depth - 1 - l]) * pPow[k][l] + toInt(path[depth - 1]) ) % mod[k];
				tH[k] = (tH[k] * p[k] - toInt(str.charAt(depth - 1 - l)) * pPow[k][l] + toInt(str.charAt(depth - 1)) ) % mod[k];
				
				if(tH[k] < 0 )
					tH[k] += mod[k];
			}
		}
		
		if(tH[0] == pH[0] && tH[1] == pH[1])
			ans++;
		
		for(Node nxt : adj[cur]) {
			path[depth] = nxt.value;
			dfs(nxt.id, depth + 1, str + nxt.value, tH[0], tH[1]);
		}
	}
	
	static int toInt(char ch) {
		return ch - 'a' + 1;
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
		
		for(int i = 0; i < n- 1; i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			char value = st.nextToken().charAt(0);
			adj[a].add(new Node(b, value));
		}
		
		pPow = new long[2][n+1];
		for(int k = 0; k < 2; k++) {
			pPow[k][0] = 1;
			for(int i = 1; i<=n; i++) {
				pPow[k][i] = (pPow[k][i-1] * p[k]) % mod[k]; 
			}
		}
		
		pH = new long[2];
		for(int k = 0; k < 2; k++) {
			for(int i = 0; i < l; i++) {
				pH[k] = (pH[k] + toInt(pattern.charAt(i)) * pPow[k][l - i - 1]) % mod[k];
			}
		}
		
		path = new char[n+1];
		dfs(1,0,"",0,0);
		System.out.println(ans);
	}
}