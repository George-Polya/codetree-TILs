import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
	static StringTokenizer st;
	static int n;
	static int arr[], dp[][];
	static int INT_MIN = Integer.MIN_VALUE;
	
	static void printBoard(int board[][]) {
		for(int cnt = 0; cnt <= 3; cnt++) {
			for(int i=0;i<=n;i++) {
				System.out.printf("%3d", board[cnt][i] == INT_MIN ? 0 : board[cnt][i]);
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		arr = new int[n+1];
		st = new StringTokenizer(br.readLine());
		for(int i=1;i<=n;i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		dp = new int[3+1][n+1];
		for(int i=0;i<=3;i++) {
			Arrays.fill(dp[i], INT_MIN);
		}
		
		for(int i = 0; i<=n;i++) {
			if(i % 2 ==0)
				dp[0][i] = arr[i];
		}
		dp[1][1] = 1;
		
		for(int cnt=1; cnt<=3;cnt++) {
			for(int i = 2; i<=n; i++) {
//				System.out.printf("%d %d\n", dp[cnt-1][i-1], dp[cnt][i-2]);
				dp[cnt][i] = Math.max(dp[cnt-1][i-1], dp[cnt][i-2]) + arr[i];
			}
		}
//		printBoard(dp);
		
		int ans = 0;
		for(int cnt = 0; cnt<=3;cnt++) {
			for(int i = 0; i<=n; i++)
				ans = Math.max(ans, dp[cnt][i]);
		}
		System.out.println(ans);
	}
}