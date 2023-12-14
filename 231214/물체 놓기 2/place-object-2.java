import java.io.*;
import java.util.*;

public class Main{
	static int n,k;
	static StringTokenizer st;
	static int cost[], arr[][];
	static int INT_MAX = (int)2e9;

	static int dp[];
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		cost = new int[n+1];
		for(int i = 1; i<=n; i++) {
			cost[i] = Integer.parseInt(br.readLine());
		}
		
		arr = new int[n+1][n+1];
		for(int y= 1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				arr[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		dp = new int[1 << n];
		Arrays.fill(dp, INT_MAX);
		dp[0]=  0;
		
		for(int i = 0; i < 1 << n;i++) {
			for(int j = 1; j<=n; j++) {
				if((i & ( 1 << (j-1) )) != 0)
					continue;
//				int val = dp[i];
				int minCost = cost[j];
				for(int l = 1; l <= n; l++) {
					if( ((i >> (l-1)) & 1) ==1 ) {
						minCost = Math.min(minCost, arr[l][j]);
					}
				}
				dp[i + (1 << (j-1))] = Math.min(dp[i + (1<<(j-1))], dp[i] + minCost);
			}
		}
		int ans = INT_MAX;
		for(int i = 0; i < 1 << n;i++) {
			int num = 0;
			for(int j = 1; j<=n; j++) {
				if(( i & (1 << (j-1))) != 0)
					num++;
			}
			
			if(num >= k)
				ans = Math.min(ans, dp[i]);
		}
		System.out.println(ans);
	}
}