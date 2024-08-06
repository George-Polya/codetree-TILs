import java.util.*;
import java.io.*;

public class Main {
	static String A,B;
	static int a,b;
	static int dp[][];
	static int INT_MAX = Integer.MAX_VALUE;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		A = br.readLine();
		B = br.readLine();
		a = A.length();
		b = B.length();
		A = "#" + A;
		B = "#" + B;
		dp = new int[a+1][b+1];
		for(int i = 0; i<=a; i++) {
			Arrays.fill(dp[i], INT_MAX);
		}
		
		dp[0][0] = 0;
		for(int i = 1; i<=a;i++)
			dp[i][0] = i;
		for(int i = 1; i<=b;i++)
			dp[0][i] = i;
		
		for(int i = 1; i<= a;i++) {
			for(int j = 1; j<=b;j++) {
				if(A.charAt(i) == B.charAt(j))
					dp[i][j] = Math.min(dp[i][j], dp[i-1][j-1]);
				else
					dp[i][j] = Math.min(dp[i-1][j], dp[i][j-1]) + 1;
			}
		}
		
		System.out.println(dp[a][b]);
		
	}
}