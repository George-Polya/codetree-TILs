import java.util.*;
import java.io.*;
public class Main {
	static int n,m;
	static HashSet<Integer> set = new HashSet<>();
	static int dp[][];
	static StringTokenizer st;
	static int INT_MAX = (int)1e9;
	static int arr[];
	static int getDist(int x,int y) {
		if(x == 0)
			return 0;
		return Math.abs(arr[x] - arr[y]);
	}
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		arr = new int[n+1];
		st = new StringTokenizer(br.readLine());
		for(int i = 1; i<=n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		if(m != 0) {
			st = new StringTokenizer(br.readLine());
			for(int i = 0; i < m ;i++) {
				set.add(Integer.parseInt(st.nextToken()));
			}
		}
		
		dp = new int[n+1][n+1];
		for(int i = 0; i <=n; i++) {
			Arrays.fill(dp[i], INT_MAX);
		}
		
		dp[0][0] = 0;
		
		for(int i = 0;i <=n; i++) {
			for(int j = 0; j <=n; j++) {
				int next = Math.max(i, j) + 1;
				if(next == n+1)
					continue;
				if(set.contains(next)) {
					dp[next][j] = Math.min(dp[next][j], dp[i][j] + getDist(i,next));
				}else {
					dp[next][j] = Math.min(dp[next][j], dp[i][j] + getDist(i,next));
					dp[i][next] = Math.min(dp[i][next], dp[i][j] + getDist(j,next));
				}
			}
		}
		
		int ans = INT_MAX;
		for(int i = 1; i<=n; i++) {
			ans = Math.min(ans, dp[i][n]);
		}
		System.out.println(ans);
	}
	
}