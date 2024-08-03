import java.io.*;
import java.util.*;

public class Main {
	static int n, dp[][];
	static String a,b;
	static int INF = Integer.MAX_VALUE / 2;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		a = br.readLine();
		b = br.readLine();
		a = '#' + a;
		b = '#' + b;
		
		dp = new int[n+1][10];
		for(int i = 0; i<=n;i++)
			Arrays.fill(dp[i], INF);
		
		dp[0][0] = 0;
		
		for(int i = 0; i< n;i++) {
			for(int cnt = 0; cnt <=9; cnt++) {
				if(dp[i][cnt] == INF)
					continue;
				
//				System.out.println("-----");
				int cur = (a.charAt(i+1) - '0' + cnt) % 10;
				int target = b.charAt(i+1) - '0';
				
				int cost = (target - cur + 10) % 10;
				
				int nxt = (cnt + cost) % 10;
				dp[i + 1][nxt] = Math.min(dp[i + 1][nxt], dp[i][cnt] + cost);
				
				int cost2 = (cur - target + 10) % 10;
				dp[i+1][cnt] = Math.min(dp[i + 1][cnt], dp[i][cnt] + cost2);
//				System.out.printf("i+1: %d, nj: %d, cnt: %d, cost1: %d, cost2: %d\n", i+1,nj, cnt,cost, cost2);
//				System.out.printf("i+1: %d, nj: %d, cost1: %d\n", i+1,nxt, cost);
//				System.out.printf("i+1: %d, cnt: %d, cost2: %d\n", i+1, cnt, cost2);
			}
		}
		int ans = INF;
		for(int cnt = 0; cnt<=9; cnt++)
			ans = Math.min(ans, dp[n][cnt]);
		System.out.println(ans);
		
	}
}