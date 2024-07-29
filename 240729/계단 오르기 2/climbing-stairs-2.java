import java.util.*;
import java.io.*;

public class Main{
	static StringTokenizer st;
	static int n,arr[],dp[][];
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		st = new StringTokenizer(br.readLine());
		arr = new int[n+1];
		for(int i = 1; i<=n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		dp = new int[n+1][3+1]; // dp[i][cnt] : i번째 계단을 고려했고, 1계단오르기를 cnt만큼 수행했을 때,얻을 수 있는 동전의 최대 개수
		dp[1][1] = arr[1];
		dp[2][0] = arr[2];
		dp[2][2] = arr[1]+arr[2];
		
		for(int i=3; i<=n; i++) {
			for(int cnt = 0; cnt<=3; cnt++) {
				// 2 계단 오른 경우
				if(dp[i-2][cnt] != 0)
					dp[i][cnt] = Math.max(dp[i][cnt], dp[i-2][cnt] + arr[i]);
				
				if(cnt > 0 && dp[i-1][cnt - 1] != 0)
					dp[i][cnt] = Math.max(dp[i][cnt], dp[i-1][cnt-1] + arr[i]);
			}
		}
		int ans = 0;
		for(int i=0;i<=3;i++)
			ans = Math.max(ans, dp[n][i]);
		System.out.println(ans);

	}
}