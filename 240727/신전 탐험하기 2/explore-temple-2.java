import java.io.*;
import java.util.*;


public class Main {
	static int n;
	static int board[][],dp[][][];
	static StringTokenizer st;
	static int LEFT = 0, MID = 1, RIGHT = 2;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		board = new int[n+1][3];
		dp = new int[n+1][3][3];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x= 0; x<3; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		dp[1][LEFT][LEFT] = board[1][LEFT];
		dp[1][MID][MID] = board[1][MID];
		dp[1][RIGHT][RIGHT] = board[1][RIGHT];
		
//		for(int i = 2; i<=n; i++) {
//			for(int a = 0; a<3;a++) {
//				for(int b = 0; b<3; b++) {
//					if(a == b)
//						continue;
//					
//					for(int c= 0; c<3; c++) {
//						if(a == c)
//							continue;
//						
//						dp[i][a][c] = Math.max(dp[i][a][c], dp[i-1][b][c] + board[i][a]);
//					}
//				}
//			}
//		}
		
//		for(int i = 1; i < n; i++) {
//			for(int a= 0; a < 3; a++) {
//				for(int b= 0; b<3; b++) {
//					for(int c= 0; c<3;c++) {
//						if(b == c)
//							continue;
//						dp[i+1][a][c] = Math.max(dp[i+1][a][c], dp[i][a][b] + board[i+1][c]);
//					}
//				}
//			}
//		}

		for(int i =2;i<=n; i++) {
			for(int a = 0 ;a<3; a++) {
				for(int b = 0; b< 3; b++) {
					for(int c = 0; c<3; c++) {
						if(b == c)
							continue;
						dp[i][a][c] = Math.max(dp[i][a][c], dp[i-1][a][b] + board[i][c]);
					}
				}
			}
		}
		
		
		int ans = 0;
		for(int y=0; y<3; y++) {
			for(int x= 0; x<3; x++) {
				if(y == x)
					continue;
				ans = Math.max(ans, dp[n][y][x]);
			}
		}
		System.out.println(ans);
	}
}