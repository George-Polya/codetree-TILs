import java.io.*;
import java.util.*;

public class Main{
	static int n,m;
	static int MAX_N = 10;
	static int board[][];
	static int dp[][] = new int[MAX_N+1][1 <<MAX_N];
	static StringTokenizer st;
	
	static boolean isOverlap(int j, int k) {
		if( (k & j) > 0)
			return true;
		
		for(int x = 0; x < m-1; x++) {
			if( (((k >> x) & 1) == 1) && (((k >> (x+1) ) & 1) == 1))
				return true;
		}
		return false;
	}
	
	static boolean isWall(int i,int k) {
		for(int x = 0; x< m; x++) {
			if((((k >> x) & 1) == 1) && board[i+1][x+1] == 1)
				return true;
		}
		return false;
	}
	
	static int getNum(int k) {
		int ret = 0;
		for(int x = 0; x< m; x++) {
			if (((k >> x) & 1) == 1)
				ret++;
		}
		return ret;
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		board = new int[n+1][m+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1;x<=m;x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		for(int i = 0; i < n;i++) {
			Arrays.fill(dp[i], -1);
		}
		dp[0][0] = 0;
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < (1<<m);j++) {
				if(dp[i][j] == -1)
					continue;
				
				for(int k = 0; k < (1<<m);k++) {
					if(isOverlap(j,k))
						continue;
					
					if(isWall(i, k))
						continue;
					
					
					dp[i + 1][k] = Math.max(dp[i + 1][k], dp[i][j] + getNum(k));
				}
			}
		}
		
		int ans = 0;
		for(int i = 0; i < (1 << m);i++) {
			ans = Math.max(ans, dp[n][i]);
		}
		System.out.println(ans);
	}
}