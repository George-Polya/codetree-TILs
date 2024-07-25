import java.io.*;
import java.util.*;


public class Main {
	static StringTokenizer st;
	static int arr[][],dp[][][][];
	static int n;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		
		arr = new int[n+1][2];
		dp = new int[n+1][11+1][9+1][2];
		
		for(int i = 1; i<=n; i++) {
			st = new StringTokenizer(br.readLine());
			arr[i][0] = Integer.parseInt(st.nextToken());
			arr[i][1] = Integer.parseInt(st.nextToken());
		}
		
		for(int i = 1; i<=n; i++) {
			for(int s = 0; s<=11; s++) {
				for(int b = 0; b<=9; b++) {
					if(s + b <= i) {
						if(s > 0) {
							dp[i][s][b][1] = Math.max(dp[i][s][b][1],
									Math.max(dp[i-1][s-1][b][0], dp[i-1][s-1][b][1] + arr[i][0])
							);
						}
							
						if(b > 0) {
							dp[i][s][b][1] = Math.max(dp[i][s][b][1],
									Math.max(dp[i-1][s][b-1][0], dp[i-1][s][b-1][1] + arr[i][1])
							);
						}
							
						
						dp[i][s][b][0] = Math.max(
								dp[i-1][s][b][0], dp[i-1][s][b][1]
						);
						
					}
				}
			}
		}
		
		
//		System.out.println(dp[n][11][9]);
		int ans = 0;
		for(int i=20; i<=n;i++) {
			for(int j = 0; j < 2;j++) {
				ans = Math.max(ans, dp[i][11][9][j]);
			}
		}
		System.out.println(ans);
	}
}