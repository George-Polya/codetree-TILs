import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
	static StringTokenizer st;
	static int n,A[],B[],dp[][];
	
	static void printBoard(int board[][]) {
		for(int y= 1;y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		A = new int[n+1];
		B = new int[n+1];
		
		st = new StringTokenizer(br.readLine());
		for(int i=1;i<=n;i++)
			A[i] = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(br.readLine());
		for(int i=1;i<=n;i++)
			B[i] = Integer.parseInt(st.nextToken());
		
		
		/*
		 * dp[a][b] : p1이 a번째카드까지 버리고, p2가 b번째카드까지 버렸을때 얻을 수 있는 최대 점수 
		 */
		dp = new int[n+1][n+1]; 
		for(int y=0; y<=n; y++)
			Arrays.fill(dp[y], -1);
		
		dp[0][0] = 0;
		
		for(int a = 0; a<n; a++) {
			for(int b = 0; b<n; b++) {
				if(dp[a][b] == -1) 
					continue;
				
				if(A[a+1] < B[b+1]) // p1의 카드가 더 작은 경우
					dp[a+1][b] = Math.max(dp[a+1][b], dp[a][b]);
				if(A[a+1] > B[b+1])
					dp[a][b+1] = Math.max(dp[a][b+1], dp[a][b] + B[b+1]);
				
				// 카드 버리기
				dp[a+1][b+1] = Math.max(dp[a+1][b+1], dp[a][b]);
				
			}
		}
		
//		printBoard(dp);
		
		int ans = 0;
		for(int i = 0; i<=n;i++) {
			ans = Math.max(ans, dp[i][n]);
			ans = Math.max(ans, dp[n][i]);
		}
		System.out.println(ans);
		
	}
}