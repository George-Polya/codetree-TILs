import java.io.*;
import java.util.*;

public class Main {
	static int n;
	static StringTokenizer st;
	static int a[][];
	static int dp[][];
	static int INT_MAX = (int)2e9;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		a = new int[n][n];
		for(int y=  0; y<n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x = 0; x<n; x++) {
				a[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		dp = new int[1<<n][n];
		for(int i = 0; i < 1<<n;i++) {
			Arrays.fill(dp[i], INT_MAX);
		}
		
		dp[1 << 0][0] = 0;
		
		for(int i = 0; i < 1 << n;i++) {
			for(int j = 0; j < n;j++) {
				if( (i &(1<<j)) == 0)
					continue;
				
				
				for(int k = 0; k <n;k++) {
					if((i & (1<<k)) !=0 )
						continue;
					
					if(( (i >> k) & 1) == 1)
						continue;
					
					if(a[j][k] == 0)
						continue;
					dp[i + (1 << k)][k] = Math.min(dp[i+(1<<k)][k], dp[i][j] + a[j][k]);
				}
			}
			
			
		}
		
		int ans = INT_MAX;
		
		for(int i = 1; i<n;i++) {
			if(a[i][0] == 0)
				continue;
			
			ans = Math.min(ans, dp[(1<<n) - 1][i] + a[i][0]);
		}
		System.out.println(ans);
		
	}
	
}