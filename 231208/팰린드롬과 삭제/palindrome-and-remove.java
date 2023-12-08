import java.util.*;
import java.io.*;


public class Main {
	static String str;
	static int n;
	static int dp[][];
	static int INT_MAX = (int)1e9;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		str = br.readLine();
		n = str.length();
		dp = new int[n][n];
		for(int i = 0; i<n;i++)
			Arrays.fill(dp[i], INT_MAX);
		
		dp[0][0] = 0;
		for(int i = 0; i < n - 1; i++) {
			dp[i][i+1] = (str.charAt(i) == str.charAt(i+1)) ? 0 : 1;
		}
		
		for(int len = 3; len <=n; len++) {
			for(int i = 0; i < n ; i++) {
				int j = i + len - 1;
				if(j >= n)
					continue;
				
				dp[i][j] = Math.min(dp[i][j], dp[i+1][j] + 1);
				dp[i][j] = Math.min(dp[i][j], dp[i][j-1] + 1);
				
				if(str.charAt(i) == str.charAt(j)) {
					dp[i][j] = Math.min(dp[i][j], dp[i+1][j-1]);
				}
			}
		}
		
		System.out.println(dp[0][n-1]);
		
	}
}