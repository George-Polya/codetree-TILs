import java.io.*;
import java.util.*;

public class Main{
	static int n;
	static int arr[], sum[][], dp[][];
	static StringTokenizer st;
	static int INT_MIN = -(int)1e9;
	
	static int findMax(int i, int j) {
		if(dp[i][j] != INT_MIN) {
			return dp[i][j];
		}
		
		if(i == j) {
			return dp[i][j] = 0;
		}
		
		int best = INT_MIN;
		for(int k = i; k<j; k++) {
			best = Math.max(best, findMax(i,k) + findMax(k+1, j) + Math.abs(sum[i][k] - sum[k+1][j]));
		}
		
		return dp[i][j] = best;
	}
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		arr = new int[n];
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i < n;i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		sum = new int[n][n];
		
		for(int i = 0; i < n; i++) {
			sum[i][i] = arr[i];
			for(int j = i + 1; j<n;j++) {
				sum[i][j] = sum[i][j-1] + arr[j];
			}
		}
		
		dp = new int[n][n];
		for(int i = 0; i <n ;i++) {
			Arrays.fill(dp[i], INT_MIN);
		}
//		
//		for(int i = 0; i < n ;i++) {
//			dp[i] = 0;
//		}
		
		System.out.println(findMax(0, n-1));
		
	}
}