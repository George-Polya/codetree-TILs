import java.util.*;
import java.io.*;
public class Main {
	static int n;
	static String arr[];
	static StringTokenizer st;
	
	
	static class TrieNode{
		TrieNode children[] = new TrieNode[26];
		
		public TrieNode() {
			for(int i = 0; i < 26; i++) {
				children[i] = null;
			}
		}
	}
	static TrieNode root = new TrieNode();
	
	static void insertWord(String str) {
		TrieNode t = root;
		
		for(int i = 0; i < str.length(); i++) {
			int idx = str.charAt(i) - 'a';
			
			if(t.children[idx] == null)
				t.children[idx] = new TrieNode();
			t = t.children[idx];
		}
	}
	
	static int m = 4;
	static char board[][] = new char[m+1][m+1];
	
	static boolean visited[][] = new boolean[m+1][m+1];
	static void initialize() {
		for(int y=1; y<=m;y++) {
			Arrays.fill(visited[y], false);
		}
	}
	
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
	}
	
	static int dy[] = {-1,-1,0,1,1,1,0,-1};
	static int dx[] = {0,1,1,1,0,-1,-1,-1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>m || x<=0 || x>m;
	}
	
	static int ans;
	
	static void dfs(int y, int x, TrieNode t, int len) {
		ans = Math.max(ans, len);
		char value = board[y][x];
		int idx = value - 'a';
		
		if(t.children[idx] != null) {
			t = t.children[idx];
			for(int dir = 0; dir < 8; dir++) {
				int ny = y + dy[dir];
				int nx = x + dx[dir];
				if(OOB(ny,nx) || visited[ny][nx])
					continue;
				visited[ny][nx] = true;
				dfs(ny,nx,t, len + 1);
				visited[ny][nx] = false;
			}
		}
		
	}
	
	
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		st = new StringTokenizer(br.readLine());
		arr = new String[n];
		for(int i = 0; i < n ;i++) {
			arr[i] = st.nextToken();
			insertWord(arr[i]);
		}
		
		
		for(int y=1; y<=m; y++) {
			String str = br.readLine();
			for(int x=1; x<=m; x++) {
				board[y][x] = str.charAt(x-1);
			}
		}
		
		for(int y= 1; y<=m;y++) {
			for(int x=1; x<=m; x++) {
				TrieNode t = root;
				visited[y][x] = true;
				dfs(y,x,t, 0);
				visited[y][x] = false;
			}
		}
		
		System.out.println(ans);
		
	}
	
}