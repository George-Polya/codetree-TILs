import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class Main {
	static int n,k;
	static StringTokenizer st;
	static int arr[], dp[][];
	static int INT_MIN = Integer.MIN_VALUE;
	static void printBoard(int board[][]) {
		for(int i = 0; i <= k;i++) {
			for(int j = 0; j<=n; j++)
				System.out.printf("%3d", board[i][j] < 0 ? 0 : board[i][j]);
			System.out.println();
		}
	}
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		arr = new int[n+1];
		dp = new int[k+1][n+1];
		
		st = new StringTokenizer(br.readLine());
		for(int i = 1; i<=n; i++)
			arr[i] = Integer.parseInt(st.nextToken());
		
		for(int i = 0; i <=k; i++) {
			Arrays.fill(dp[i], INT_MIN);
		}
		
		dp[0][0] = 0;
		for(int i = 1; i<=n; i++) {
			if(arr[i] >= 0)
				dp[0][i] = arr[i];
		}
		
		for(int cnt = 0; cnt <=k ;cnt++) {
			for(int i = 1; i<=n; i++) {
				if(arr[i] >=0 && dp[cnt][i-1] != INT_MIN) {
					dp[cnt][i] = Math.max(dp[cnt][i], dp[cnt][i-1] + arr[i]);
				}else if(arr[i] < 0) {
//					if(cnt > 0 && dp[cnt][i-1] != INT_MIN)
//						dp[cnt][i] = Math.max(dp[cnt][i], dp[cnt][i-1]);
					
					if(cnt > 0 && dp[cnt-1][i-1] != INT_MIN)
						dp[cnt][i] = Math.max(dp[cnt][i], dp[cnt-1][i-1] + arr[i]);
				}
			}
		}
		
//		printBoard(dp);
		int ans = 0;
		for(int cnt = 0; cnt <= k; cnt++) {
			for(int i = 0; i<=n; i++)
				ans = Math.max(ans, dp[cnt][i]);
		}
		System.out.println(ans);
		
		
	}
}