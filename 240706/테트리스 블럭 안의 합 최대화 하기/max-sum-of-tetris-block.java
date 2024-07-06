import java.util.*;
import java.io.*;

public class Main {
	static StringTokenizer st;
	static int n,m;
	static int board[][];
	static boolean visited[][];
	static int ans;
	
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<= 0 || x>m;
	}
	
	static void dfs(int y,int x,int cnt, int sum) {
		
		if(cnt == 3) {
			ans = Math.max(ans, sum);
			return;
		}
		
		for(int dir = 0; dir<4;dir++) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			if(OOB(ny,nx) || visited[ny][nx])
				continue;
			visited[ny][nx] = true;
			dfs(ny,nx,cnt+1, sum + board[ny][nx]);
			visited[ny][nx] = false;
		}
	}
	
	static int calc(int y,int x,int dir) {
		int ret = board[y][x];
		for(int moveDir = 0; moveDir< 4;moveDir++) {
			if(dir == moveDir)
				continue;
			int ny = y + dy[moveDir];
			int nx = x + dx[moveDir];
			if(OOB(ny,nx))
				return 0;
			ret += board[ny][nx];
		}
		return ret;
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		board = new int[n+1][m+1];
		
		for(int y=1;y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=m; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		visited = new boolean[n+1][m+1];
		
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=m; x++) {
				visited[y][x] = true;
				dfs(y,x,0,board[y][x]);
				visited[y][x] = false;
				for(int dir = 0; dir< 4; dir++) {
					int sum = calc(y,x,dir);
					ans = Math.max(ans, sum);
				}
			}
		}
		System.out.println(ans);
	}
}