import java.util.*;
import java.io.*;


public class Main {
	static int n;
	static String text;
	static int dp[][];
	static int length[][];
	static int symmetry(int start, int end) {
		int len = end - start + 1;
		int radius = len / 2;
		
		for(int i = 0; i < radius; i++) {
			if(text.charAt(start + i) != text.charAt(end - i))
				return 0;
		}
		return len;
	}
	
	static int findMax(int i, int j) {
		if(dp[i][j] != -1)
			return dp[i][j];
		
		if(i == j) {
			return dp[i][j] = 1;
		}
		
		int best = -1;
		for(int k = i; k<j;k++) {
			int len = Math.max(length[i][j], Math.max(findMax(i,k), findMax(k+1,j)));
			best = Math.max(best,len);
		}
		
		return dp[i][j] = best;
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		text = br.readLine();
		n = text.length();
		text = "#" + text;
		length = new int[n+1][n+1];
		
		for(int i = 1; i<=n; i++) {
			length[i][i] = 1;
			for(int j = i + 1; j<=n; j++) {
				length[i][j] = symmetry(i,j);
			}
		}
		dp = new int[n+1][n+1];
		for(int i = 1; i<=n; i++) {
			for(int j = 1; j<=n; j++)
				dp[i][j] = -1;
		}
		
		System.out.println(findMax(1,n));
		
		
	}
}