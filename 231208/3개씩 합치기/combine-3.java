import java.util.*;
import java.io.*;


public class Main {
	static int n;
	static int dp[][], arr[];
	static StringTokenizer st;
	static int INT_MAX = (int)1e9;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		arr = new int[n];
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i < n ;i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		dp = new int[n][n];
		for(int i = 0; i <n;i++) {
			Arrays.fill(dp[i], INT_MAX);
		}
		
		for(int i = 0; i < n; i++) {
			dp[i][i] = 0;
			if(i + 1 != n)
				dp[i][i+1] = 0;
		}
		
		for(int len = 2; len <=n; len++) {
			for(int i = 0; i < n; i++) {
				int j = i + len - 1;
				if(j >= n)
					continue;
				
				for(int k = i + 1; k<j; k++) {
					dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k][j] + arr[i]* arr[k] * arr[j]);
				}
			}
		}
		System.out.println(dp[0][n-1]);
	}
}