import java.io.*;
import java.util.*;

public class Main{
	static String text;
	static int n;
	static int dp[][];
	
	static int mod = (int)1e9 + 7;
	
	static int getPairNum(char left, char right) {
		if(left == '(') {
			if(right == ')' || right == '?')
				return 1;
			return 0;
		}
		if(left == '{') {
			if(right == '}' || right == '?')
				return 1;
			return 0;
		}
		if(left == '[') {
			if(right == ']' || right == '?')
				return 1;
			return 0;
		}
		if(left == '?') {
			if(right == '?')
				return 3;
			if(right == ')' || right == '}' || right == ']')
				return 1;
			
			return 0;
		}
		return 0;
	}
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		text = br.readLine();
		n = text.length();
		dp = new int[n+1][n+1];
		
		for(int i = 0; i<n;i++) {
			dp[i][i] = 0;
			dp[i+1][i] = 1;
		}
		
		for(int len = 2; len<=n; len++) {
			for(int i = 0; i<n; i++) {
				int j = i + len - 1;
				
				if(j >=n)
					continue;
				
				for(int k = i + 1; k<=j; k++) {
					dp[i][j] += getPairNum(text.charAt(i), text.charAt(k)) * dp[i+1][k-1] * dp[k+1][j];
					dp[i][j] %= mod;
				}
			}
		}
		
		System.out.println(dp[0][n-1]);
	}
	
}