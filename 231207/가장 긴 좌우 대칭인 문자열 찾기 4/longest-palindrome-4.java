import java.util.*;
import java.io.*;


public class Main {
	static int n;
	static String text;
	static boolean dp[][];
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		text = br.readLine();
		n = text.length();
		text = "#" + text;
		dp = new boolean[n+1][n+1];
		for(int i = 1; i<=n;i++) {
			dp[i][i] = true;
		}
		
		for(int i = 1; i<n;i++) {
			dp[i][i+1] = (text.charAt(i) == text.charAt(i+1));
		}
		
		int ans = 0;
		for(int len = 3; len <= n; len++) {
			for(int i = 1; i<=n; i++) {
				int j = i + len - 1;
				if(j > n)
					continue;
				
				if(dp[i+1][j-1] && text.charAt(i) == text.charAt(j)) {
					dp[i][j] = true;
					ans = len;
				}
				
			}
		}
		
		System.out.println(ans);
		
		
	}
}