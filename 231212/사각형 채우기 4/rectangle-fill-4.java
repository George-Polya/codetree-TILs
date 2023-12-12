import java.io.*;
import java.util.*;

public class Main {
	static int n;
	static int dp[][][][];
	static int mod = 10007;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		dp = new int[n+2][2][2][2];
		
		dp[1][0][0][0] = 1;
		
		for(int i = 1; i<=n;i ++) {
			dp[i+1][1][0][0] = (dp[i+1][1][0][0] +  dp[i][0][0][0]) % mod;
			dp[i+1][0][0][1] = (dp[i+1][0][0][1] + dp[i][0][0][0])  % mod;
			dp[i+1][1][1][1] = (dp[i+1][1][1][1] + dp[i][0][0][0])  % mod;
			
			dp[i+1][0][0][0] =  (dp[i+1][0][0][0] + dp[i][1][0][0]) % mod;
			dp[i+1][0][1][1] = (dp[i+1][0][1][1] + dp[i][1][0][0]) % mod;
			
			dp[i+1][0][0][0] = (dp[i+1][0][0][0] + dp[i][0][0][1]) % mod;
			dp[i+1][1][1][0] = (dp[i+1][1][1][0] + dp[i][0][0][1]) % mod;
			
			dp[i+1][1][0][1] = (dp[i+1][1][1][0] + dp[i][0][1][0]) % mod;
			
			dp[i+1][0][0][1] = (dp[i+1][0][0][1] + dp[i][1][1][0]) % mod;
			
			dp[i+1][1][0][0] = (dp[i+1][1][0][0] + dp[i][0][1][1]) % mod;
			
			dp[i+1][0][1][0] = (dp[i+1][0][1][0] + dp[i][1][0][1]) % mod;
			
			dp[i+1][0][0][0] = (dp[i+1][0][0][0] + dp[i][1][1][1]) % mod;
		}
		
		System.out.println(dp[n+1][0][0][0]);
		
	}
	
}