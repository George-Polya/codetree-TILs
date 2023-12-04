import java.util.*;
import java.io.*;
public class Main {
	static int n,m;
	static int points[];
	static int INT_MAX = (int)2e9;
	static int dp[][][];
	static StringTokenizer st;
	
	static int dist(int x, int y) {
		if(x == 0)
			return 0;
		return Math.abs(points[x] - points[y]);
	}
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		points = new int[n+1];
		st = new StringTokenizer(br.readLine());
		for(int i = 1; i<=n;i++) {
			points[i] = Integer.parseInt(st.nextToken());
		}
		
		dp = new int[n+1][n+1][m+1];
		for(int i = 0; i<=n;i++) {
			for(int j = 0; j<=n;j++) {
				for(int k = 0; k<=m; k++) {
					dp[i][j][k] = INT_MAX;
				}
			}
		}
		
		dp[0][0][0] = 0;
		
		for(int i = 0; i <=n;i++) {
			for(int j = 0; j<=n;j++) {
				int next = Math.max(i, j) + 1;
				if(next == n+1)
					continue;
				for(int k = 0; k <=m; k++) {
					
					dp[next][j][k] = Math.min(dp[next][j][k], dp[i][j][k] + dist(i,next));
					if(k!=m)
						dp[next][j][k+1] = Math.min(dp[next][j][k+1], dp[i][j][k]);

					dp[i][next][k] = Math.min(dp[i][next][k], dp[i][j][k] + dist(j,next));
					if(k!=m)
						dp[i][next][k+1] = Math.min(dp[i][next][k+1], dp[i][j][k]);
							
				}
			}
		}
		
		
		int ans = INT_MAX;
		for(int i = 1; i<=n;i++) {
			ans = Math.min(ans,  dp[i][n][m]);
		}
		
		System.out.println(ans);
	}
	
	
	
}