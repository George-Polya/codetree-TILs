import java.util.*;
import java.io.*;

public class Main {
	static StringTokenizer st;
	static int n,k;
	static int dp[][][];
	static int L = 0, R = 1;
	static int INT_MIN = Integer.MIN_VALUE;
	static String str;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		dp = new int[n+1][k+1][2];
		
		for(int i = 0; i<=n;i++)
			for(int j = 0; j<=k;j++)
				Arrays.fill(dp[i][j], INT_MIN);
		
		/*
		 * dp[i][cnt] : 
		 */
		dp[0][0][L] = 0;
		dp[0][1][R] = 0;
		
		str = "#"+ br.readLine();
		
		for(int i = 0; i < n; i++) {
			for(int cnt = 0; cnt <=k; cnt++) {
				if(dp[i][cnt][L] != INT_MIN) {
					if(str.charAt(i+1) == 'L') {
						dp[i+1][cnt][L] = Math.max(dp[i+1][cnt][L], dp[i][cnt][L] + 1); // 현재가 L이고 다음이 L일때 이동하지 않는 경우 
						if(cnt < k) // 이동하는 경우 
							dp[i+1][cnt+1][R] = Math.max(dp[i+1][cnt+1][R], dp[i][cnt][L]); // 현재가 L이고 다음이 L이지만 이동하는 경우  
					}else {
						// 현재가 L이고 다음이 R이지만 이동하지 않는 경우
						dp[i+1][cnt][L] = Math.max(dp[i+1][cnt][R], dp[i][cnt][L]);
						
						
						// 현재가 L이고 다음이 R이므로 이동하는 경우 
						if(cnt < k)
							dp[i+1][cnt+1][R] = Math.max(dp[i+1][cnt+1][R], dp[i][cnt][L] + 1);
					}
				}
				
				
				if(dp[i][cnt][R] != INT_MIN) {
					if(str.charAt(i+1) == 'L') { 
						// 현재가 R이고 다음이 L이지만 이동하지 않는 경우
						dp[i+1][cnt][R] = Math.max(dp[i+1][cnt][R], dp[i][cnt][R]);
						
						
						// 현재가 R이고 다음이 L이므로 이동하는 경우
						if(cnt < k)
							dp[i+1][cnt+1][L] = Math.max(dp[i+1][cnt+1][L], dp[i][cnt][R] + 1);
						
					}else {
						// 현재가 R이고 다음이 R이므로 이동하지 않는 경우
						dp[i+1][cnt][R] = Math.max(dp[i+1][cnt][R], dp[i][cnt][R] + 1);
						
						
						// 현재가 R이고 다음이 R이지만 이동하는 경우 
						if(cnt < k)
							dp[i+1][cnt+1][L] = Math.max(dp[i+1][cnt+1][L], dp[i][cnt][R]);
						
						
					}
				}
				
				
			}
			
		}
		int ans = INT_MIN;
		for(int i = 0; i<=k; i++) {
			for(int j = 0; j < 2; j++) {
//				System.out.println(dp[n][i][j]);
				ans = Math.max(ans, dp[n][i][j]);
			}
		}
		System.out.println(ans);
		
		
		
	}
}