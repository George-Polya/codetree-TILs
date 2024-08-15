import java.util.*;
import java.io.*;


public class Main {
	static int n,m;
	static String S, T;
	static int dp[][];
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		S = br.readLine();
		T = br.readLine();
		
		n = S.length();
		m = T.length();
		
		S = "#"+S;
		T = "#"+T;
		dp = new int[n+1][m+1];
		for(int i = 0; i<=n; i++) {
			Arrays.fill(dp[i], 987654321);
		}
		
		dp[0][0] = 0;
		for(int i = 0; i <=n; i++) {
			dp[i][0] = i;
		}
		
		for(int j = 0; j<=m; j++) {
			dp[0][j] = j;
		}
		
		for(int i = 1; i<=n; i++) {
			for(int j = 1; j<=m; j++) {
				if(S.charAt(i) == T.charAt(j)) {
					dp[i][j] = dp[i-1][j-1] + 1;
				}else {
					dp[i][j] = Math.min(dp[i-1][j], dp[i][j-1])+1;
				}
			}
		}
		System.out.println(dp[n][m]);
	}
}