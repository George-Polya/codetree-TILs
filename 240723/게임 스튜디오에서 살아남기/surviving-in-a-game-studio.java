import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class Main {
	static int n;
	static int MOD = (int)1e9 + 7;
	static int dp[][][];
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		
		dp = new int[n+1][4][4];
		dp[1][1][0] = 1;
		dp[1][0][0] = 1;
		dp[1][0][1] = 1;
		
		
		for(int i = 1 ; i<n;i++) {
			for(int t = 0; t<3; t++) {
				for(int b = 0; b<3; b++) {
					dp[i+1][t+1][0] = (dp[i+1][t+1][0] + dp[i][t][b]) % MOD;
					dp[i+1][t][0] = (dp[i+1][t][0] + dp[i][t][b]) % MOD;
					dp[i+1][t][b+1] = (dp[i+1][t][b+1] + dp[i][t][b]) % MOD; 
				}
			}
		}
		
		int ans = 0;
		for(int t = 0; t< 3; t++) {
			for(int b= 0; b<3; b++) {
				ans = (ans + dp[n][t][b]) % MOD;
  			}
		}
		System.out.println(ans);
	}
}