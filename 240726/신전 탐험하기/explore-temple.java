import java.io.*;
import java.util.*;


public class Main {
	static int n;
	static int board[][], dp[][];
	static StringTokenizer st;
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		
		board = new int[n+1][3];
		dp = new int[n+1][3];
		
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x= 0; x<3; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		for(int x=0;x<3;x++)
			dp[1][x] = board[1][x];
		
		for(int y = 2; y<=n; y++) {
			for(int x= 0; x< 3; x++) {
				for(int i = 0; i < 3; i++) {
					if(x == i)
						continue;
					dp[y][x] = Math.max(dp[y][x], dp[y-1][i] + board[y][x]);
				}
			}
		}
		
//		printBoard(dp);
		
		int ans = 0;
		for(int i = 0; i < 3;i++)
			ans = Math.max(ans, dp[n][i]);
		System.out.println(ans);
	}
}