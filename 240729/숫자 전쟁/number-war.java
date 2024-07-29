import java.util.*;
import java.io.*;

public class Main{
	static StringTokenizer st;
	static int n, A[],B[], dp[][];
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		A = new int[n+1];
		B = new int[n+1];
		dp = new int[n+1][n+1];
		
		st = new StringTokenizer(br.readLine());
		for(int i = 1; i<=n; i++)
			A[i] = Integer.parseInt(st.nextToken());
		
		st = new StringTokenizer(br.readLine());
		for(int i = 1; i<=n; i++)
			B[i] = Integer.parseInt(st.nextToken());
		
		for(int y = 0; y<=n;y++)
			Arrays.fill(dp[y], -1);
		
		dp[0][0] = 0;
		
		for(int a = 0; a< n; a++) {
			for(int b=0; b<n; b++) {
				
				if(dp[a][b] == -1)
					continue;
				
				if(A[a+1] < B[b+1])
					dp[a+1][b] = Math.max(dp[a+1][b], dp[a][b]);
				else if(A[a+1]>B[b+1])
					dp[a][b+1] = Math.max(dp[a][b+1], dp[a][b] + B[b+1]);
				else
					dp[a+1][b+1] = Math.max(dp[a+1][b+1], dp[a][b]);
				
				dp[a+1][b+1] = Math.max(dp[a+1][b+1], dp[a][b]);
			}
		}
		
		int ans = 0;
		for(int y=0;y<=n; y++) {
			for(int x=0; x<=n; x++) {
//				System.out.print(dp[y][x] +" ");
				ans = Math.max(ans, dp[y][x]);
			}
//			System.out.println();
		}
		
//		for(int x = 1; x<=n; x++)
//			ans = Math.max(ans, dp[n][x]);
		
		System.out.println(ans);
	}
}