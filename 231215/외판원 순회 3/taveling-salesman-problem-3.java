import java.io.*;
import java.util.*;

public class Main{
	static int n,kk;
	static int cost[][];
	static StringTokenizer st;
	static int INT_MAX = (int)1e9;
	static int dp[][];
	
	static boolean check(int bit) {
		int cnt = 0;
		
		for(int j = 1; j<n;j++) {
			if( ((bit >> j) & 1) == 1)
				cnt++;
		}
		
		return cnt != kk;
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		kk = Integer.parseInt(st.nextToken());
		
		cost = new int[n][n];
		for(int y= 0; y<n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x = 0; x< n; x++) {
				cost[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		dp = new int[1 << n][n];
		for(int i = 0; i < (1<<n); i++) {
			Arrays.fill(dp[i], INT_MAX);
		}
		
		dp[1][0] = 0;
		
		for(int i = 0; i < (1 << n); i++) {
			for(int j = 0; j < n ;j++) {
				if( ((i >> j) & 1) == 0)
					continue;
				
				for(int k = 0; k < n; k++) {
					if( ((i >> k) & 1) == 1  )
						continue;
					
					if(cost[j][k] == 0)
						continue;
					
					dp[i + (1 << k)][k] = Math.min(dp[i + (1<<k)][k], dp[i][j] + cost[j][k]);
				}
			}
		}
		
		int ans = INT_MAX;
		
		for(int i = 0 ; i < (1 << n); i++) {
			
			
			if(check(i))
				continue;
			
			for(int j = 0; j < n ;j++) {
				
				if(((i >> j) & 1) == 0)
					continue;
				
				if(cost[j][0] == 0)
					continue;
				
				ans = Math.min(ans,  dp[i][j] + cost[j][0]);
			}
		}
		System.out.println(ans);
		
	}
}