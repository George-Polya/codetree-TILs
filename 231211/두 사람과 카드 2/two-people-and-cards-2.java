import java.io.*;
import java.util.*;

public class Main {
	static StringTokenizer st;
	static int n,m;
	static boolean notUsed[];
	static int arr[], dp[][];
	static int INT_MAX = (int)2e9;
	static int dist(int i, int j) {
		if( i == 0)
			return 0;
		
		return Math.abs(arr[i] - arr[j]);
	}
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		arr = new int[n+1];
		st = new StringTokenizer(br.readLine());
		for(int i = 1; i<=n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		notUsed = new boolean[n+1];
		st = new StringTokenizer(br.readLine());
		for(int i = 1; i<=m; i++) {
			notUsed[Integer.parseInt(st.nextToken())] = true;
		}
		
		dp = new int[n+1][n+1];
		for(int i = 0; i <=n ;i++) {
			for(int j = 0; j<=n; j++) {
				dp[i][j] = INT_MAX;
			}
		}
		
		dp[0][0] = 0;
		for(int i = 0; i <= n;i++) {
			for(int j = 0; j<=n; j++) {
				int next = Math.max(i, j) + 1;
				
				if(next == n + 1)
					continue;
				
				dp[next][j] = Math.min(dp[next][j], dp[i][j] + dist(i,next));
				if(!notUsed[next]) {
					dp[i][next] = Math.min(dp[i][next], dp[i][j] + dist(j,next));
				}
					
			}
		}
		
		int ans = INT_MAX;
		for(int i = 0; i <= n; i++) {
			ans = Math.min(ans, dp[i][n]);
			ans = Math.min(ans, dp[n][i]);
		}
		
		System.out.println(ans);
	}
}