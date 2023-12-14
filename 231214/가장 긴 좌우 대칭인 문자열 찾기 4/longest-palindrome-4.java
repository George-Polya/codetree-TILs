import java.io.*;
import java.util.*;

public class Main{
	static String str;
	static int n;
	static boolean dp[][];
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		str = br.readLine();
		n = str.length();
		dp = new boolean[n][n];
		for(int i = 0; i < n; i++) {
			dp[i][i] = true;
			if(i+1 != n) {
				dp[i][i+1] = str.charAt(i) == str.charAt(i+1);
			}
		}
		
		int ans = 0;
		for(int len = 3; len <= n; len++) {
			for(int i = 0; i < n; i++) {
				int j = i + len - 1;
				if(j >= n)
					continue;
				
				if(str.charAt(i) == str.charAt(j) && dp[i+1][j-1]) {
					dp[i][j] = true;
					ans = len;
				}
			}
		}
		System.out.println(ans);
	}
}