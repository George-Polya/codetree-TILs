import java.io.*;
import java.util.*;

public class Main{
	static int n;
	static int dp[][][];
	static int mod = 10007;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		dp = new int[n+1][1 << 10][10];
		for(int i = 1; i < 10; i++) {
			dp[1][1 << i][i] = 1;
		}
		
		for(int i = 1; i<n;i++) {
			for(int j = 0; j < (1 << 10); j++) {
				for(int k = 0; k < 10; k++) {
					if(dp[i][j][k] == 0)
						continue;
					
			
					if(k >= 1) {
						dp[i+1][j | (1 << (k-1))][k-1] += dp[i][j][k];
						dp[i+1][j | (1 << (k-1))][k-1] %= mod;
					}
					
					if( k + 1 < 10) {
						dp[i+1][j | (1<<(k+1))][k+1] += dp[i][j][k];
						dp[i+1][j | (1<<(k+1))][k+1] %= mod;
					}
				}
			}
		}
		
		int sum = 0;
		for(int i = 0; i < 10; i++) {
			sum += dp[n][(1 << 10) - 1][i];
			sum %= mod;
		}
		System.out.println(sum);
	}
}