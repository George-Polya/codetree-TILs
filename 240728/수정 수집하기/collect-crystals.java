import java.util.*;
import java.io.*;

public class Main {
	
	static StringTokenizer st;
	static int n,k;
	static int dp[][][];
	static String str;
	static int L = 0, R = 1;
 	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		dp = new int[n+1][k+1][2];
		str = br.readLine();
		str = "#" + str;
		
		dp[1][0][L] = str.charAt(1) == 'L' ? 1 : 0;
		dp[1][0][R] = str.charAt(1) == 'L' ? 0 : 1;
		
		for(int i = 2; i<=n; i++) {
//			System.out.println("----");
			for(int cnt = 0; cnt<=k; cnt++) {
				if(str.charAt(i) == 'L') {
					dp[i][cnt][L] = dp[i-1][cnt][L] + 1;
					dp[i][cnt][R] = dp[i-1][cnt][R];
					if(cnt > 0) {
						dp[i][cnt][L] = Math.max(dp[i][cnt][L], dp[i-1][cnt-1][R] + 1);
					}
				}else {
					dp[i][cnt][L] = dp[i-1][cnt][L];
					dp[i][cnt][R] = dp[i-1][cnt][R] + 1;
					if(cnt > 0)
						dp[i][cnt][R] = Math.max(dp[i][cnt][R], dp[i-1][cnt-1][L] + 1);
				}
//				System.out.printf("(i,cnt): (%d, %d) L : %d, R: %d\n", i,cnt,dp[i][cnt][L], dp[i][cnt][R]);
			}
		}
		
//		System.out.println(dp[n][k][L]+" "+dp[n][k][R]);
//		System.out.println(dp[n][0][L]+" "+dp[n][0][R]);
		int ans = 0;
		for(int i = 0; i<=k;i++) {
			ans = Math.max(ans, dp[n][i][L]);
			ans = Math.max(ans, dp[n][i][R]);
		}
		System.out.println(ans);
	}
    
}