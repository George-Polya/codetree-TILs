import java.util.*;
import java.io.*;

public class Main {
	static int board[][], dp[][];
	static StringTokenizer st;
	static int n,m;
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
//		System.out.println(n);
		board = new int[n+1][m+1];
		dp = new int[n+1][m+1];
		
		for(int y=1; y<=n; y++) {
			st= new StringTokenizer(br.readLine());
			for(int x=1; x<=m; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		for(int x = 1; x<=m;x++) {
			dp[1][x] = board[1][x];
		}
		
		for(int y = 2; y<=n; y++) {
			for(int x=1; x<=m; x++) {
				for(int k =1; k<=m; k++) {
					if(x == k)
						continue;
					dp[y][x] = Math.max(dp[y][x], dp[y-1][k] + board[y][x]);
				}
			}
		}
		
		
		
		int ans = 0;
		for(int x=1;x<=m;x++) {
			ans = Math.max(ans,dp[n][x]);
		}
		System.out.println(ans);
	}
}