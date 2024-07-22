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
				System.out.printf("%-10d", board[cnt][i] < 0  ? 0 : board[cnt][i]);
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
		
		dp[0][2] = arr[2];
		dp[2][2] = arr[1] + arr[2];
		dp[1][1] = arr[1];
		
		for(int cnt=1; cnt<=3;cnt++) {
			for(int i = 3; i<=n; i++) {
//				System.out.printf("%d %d\n", dp[cnt-1][i-1], dp[cnt][i-2]);
				if(cnt <= i)
					dp[cnt][i] = Math.max(dp[cnt-1][i-1], dp[cnt][i-2]) + arr[i];
			}
		}
		
//		printBoard(dp);
		
		int ans = INT_MIN;
		for(int cnt = 0; cnt<=3;cnt++) {
			for(int i = 0; i<=n; i++)
				ans = Math.max(ans, dp[cnt][i]);
		}
		System.out.println(ans);
	}
}