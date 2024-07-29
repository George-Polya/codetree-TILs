import java.util.*;
import java.io.*;

public class Main {
	
	static StringTokenizer st;
	static int n,k;
	static int dp[][][];
	static String str;
	static int L = 0, R = 1;
	static int INF = 987654321;
 	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		dp = new int[n+1][k+1][2];
		str = br.readLine();
		str = "#" + str;
		for(int i =0;i<=n;i++) {
			for(int cnt = 0; cnt<=k; cnt++)
				dp[i][cnt][0] = dp[i][cnt][1] = -INF;
		}
		
		
		
		// 엘라는 초기에 왼쪽 샘터에 서있다.
		dp[0][0][L] = 0;
		dp[0][1][R] = 0;
		
		
		for(int i = 0; i<n; i++) {
			for(int cnt = 0; cnt<=k; cnt++) {
				if(dp[i][cnt][L] != -INF) {
					if(str.charAt(i+1) == 'L') {
						dp[i+1][cnt][L] = Math.max(dp[i+1][cnt][L], dp[i][cnt][L] + 1);
						if(cnt < k)
							dp[i+1][cnt+1][R] = Math.max(dp[i+1][cnt+1][R], dp[i][cnt][L]);
					}else {
						dp[i+1][cnt][L] = Math.max(dp[i+1][cnt][L], dp[i][cnt][L]);
						if(cnt < k)
							dp[i+1][cnt+1][R] = Math.max(dp[i+1][cnt+1][R], dp[i+1][cnt][L] + 1);
					}
				}
				
				if(dp[i][cnt][R] != -INF) {
					if(str.charAt(i+1) == 'L') {
						dp[i+1][cnt][R] = Math.max(dp[i+1][cnt][R], dp[i][cnt][R]);
						if(cnt < k)
							dp[i+1][cnt+1][L] = Math.max(dp[i+1][cnt+1][L], dp[i][cnt][R] + 1);
					}else {
						dp[i+1][cnt][R] = Math.max(dp[i+1][cnt][R], dp[i][cnt][R] + 1);
						if(cnt < k)
							dp[i+1][cnt+1][L] = Math.max(dp[i+1][cnt+1][L], dp[i][cnt][R]);
					}
				}
			}
		}
		
		int ans = 0;
		for(int i = 0; i<=k;i++) {
			ans = Math.max(ans, dp[n][i][L]);
			ans = Math.max(ans, dp[n][i][R]);
		}
		System.out.println(ans);
	}
    
}