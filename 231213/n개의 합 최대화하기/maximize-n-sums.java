import java.io.*;
import java.util.*;

public class Main {
	static int n;
	static int dp[][], board[][];
	static StringTokenizer st;
	static int INT_MIN = -(int)2e9;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		board = new int[n][n];
		for(int y=0; y<n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=0; x<n ;x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		dp = new int[ 1 << n][n + 1];
		for(int  y=0;y< (1 << n);y++) {
			Arrays.fill(dp[y], INT_MIN);
		}
		
		dp[0][0] = 0;
		
		for(int i = 0; i < (1 << n);i++) {
			for(int j = 0; j < n ;j++) {
				if(dp[i][j] == INT_MIN)
					continue;
				
				for(int k = 0; k < n; k++) {
					if( ((i >> k) & 1) == 1)
						continue;
					dp[i + (1<<k)][j + 1] = Math.max(dp[i + (1<<k)][j + 1], dp[i][j] + board[j][k]);
				}
			}
		}
		
		System.out.println(dp[(1 << n) - 1][n]);
	}
}