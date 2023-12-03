import java.io.*;
import java.util.*;

public class Main{
	static int n;
	static int points[];
	static int dp[][];
	static int INT_MAX = (int)2e9;
	static int getDist(int x,int y) {
		if(x == 0)
			return 0;
		return Math.abs(points[x] - points[y]);
	}
	static StringTokenizer st;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		
		points = new int[n+1];
		st = new StringTokenizer(br.readLine());
		for(int i = 1; i<=n; i++)
			points[i] = Integer.parseInt(st.nextToken());
		
		dp = new int[n+1][n+1];
		for(int i=0;i<=n;i++) {
			for(int j = 0; j<=n; j++) {
				dp[i][j] = INT_MAX;
			}
		}
		
		dp[0][0] = 0;
		
		for(int i = 0; i <=n;i++) {
			for(int j = 0; j<=n; j++) {
				int next = Math.max(i, j)+1;
				if(next == n + 1)
					continue;
				
				
				dp[next][j] = Math.min(dp[next][j], dp[i][j] + getDist(i,next));
				dp[i][next] = Math.min(dp[i][next], dp[i][j] + getDist(j,next));
			}
 		}
		
		int ans = INT_MAX;
		for(int i = 1; i<=n; i++) {
			ans = Math.min(ans, dp[i][n]);
		}
		System.out.println(ans);
	}
}