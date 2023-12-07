import java.util.*;
import java.io.*;


public class Main {
	static int n, arr[], merged[][], dp[][];
	static StringTokenizer st;
	static int INT_MAX = (int)2e9;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		arr = new int[n+1];
		st = new StringTokenizer(br.readLine());
		for(int i = 1; i<=n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		merged = new int[n+1][n+1];
		for(int i = 1; i<=n;i++) {
			merged[i][i] = arr[i];
			for(int j = i + 1; j<=n;j++) {
				merged[i][j] = merged[i][j-1] + arr[j];
			}
		}
		
		dp = new int[n+1][n+1];
		for(int i = 1; i<=n; i++) {
			for(int j = 1; j<=n; j++) {
				dp[i][j] = INT_MAX;
			}
		}
		
		for(int i = 1; i<=n; i++)
			dp[i][i] = 0;
		
		for(int len = 2; len <= n; len++) {
			for(int start = 1; start <= n; start++) {
				int end = start + len - 1;
				if(end > n)
					continue;
				
				for(int k = start; k < end; k++) {
					int cost = dp[start][k] + dp[k+1][end] + merged[start][k] + merged[k+1][end];
					dp[start][end] = Math.min(dp[start][end], cost);
				}
			}
		}
		
		System.out.println(dp[1][n]);
	}
}