import java.io.*;
import java.util.*;


public class Main {
	static int n;
	static int dp[][];
	static int MOD = (int)1e9+7;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		
		dp = new int[n+1][10];
		
		for(int i=1;i<=9;i++)
			dp[1][i] = 1;
		
		for(int i = 2; i<=n; i++) {
			for(int k = 0; k <=9;k++) {
				if(k == 0) {
					dp[i][k] = (dp[i][k] + dp[i-1][k+1]) % MOD;
				}else if(k == 9) {
					dp[i][k] = (dp[i][k] + dp[i-1][k-1]) % MOD;
				}else {
					dp[i][k] = (dp[i][k] + dp[i-1][k+1] + dp[i-1][k-1]) % MOD;
				}
			}
		}
		
		int ans = 0;
		for(int i = 0; i<=9;i++)
			ans += dp[n][i];
		System.out.println(ans);
		
	}
}