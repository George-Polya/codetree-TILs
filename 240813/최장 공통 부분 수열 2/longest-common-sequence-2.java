import java.util.*;
import java.io.*;


public class Main {
	static StringTokenizer st;
	static String A, B;
	static int n,m;
	static String dp[][];
	static int INT_MIN = Integer.MIN_VALUE;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		A = br.readLine();
		B = br.readLine();
		n = A.length();
		m = B.length();
		A = "#" + A;
		B = "#" + B;
		
		dp = new String[n+1][m+1];
//		for(int i = 0; i<=n; i++)
//			Arrays.fill(dp[i], INT_MIN);
		
		dp[0][0] = "";
		for(int i = 0; i<=n; i++) {
			for(int j = 0; j<=m; j++) {
				dp[i][0] = "";
				dp[0][j] = "";
			}
		}
		
		
		for(int i = 1; i<=n; i++) {
			for(int j =1; j<=m; j++) {
				if(A.charAt(i) == B.charAt(j)) {
					dp[i][j] = dp[i-1][j-1] + A.charAt(i);
				}else {
					if(dp[i-1][j].length() > dp[i][j-1].length())
						dp[i][j] = dp[i-1][j];
					else
						dp[i][j] = dp[i][j-1];
//					dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
				}
			}
		}
		
		System.out.println(dp[n][m]);
	}
}