import java.io.*;
import java.util.*;

public class Main{
	
	static int n;
	static StringTokenizer st;
	static int dist[][];
	
	
//	static int INT_MAX = (int)1e9;
	static long LONG_MAX = (long)1e16;
	static long dp[][];
	
	static class Pair{
		int x,y;
		public Pair(int x,int y) {
			this.x = x;
			this.y = y;
		}
	}

	static ArrayList<Pair> pairs = new ArrayList<>();
	
	static int getDist(Pair p1, Pair p2) {
		int x1 = p1.x;
		int y1 = p1.y;
		
		int x2 = p2.x;
		int y2 = p2.y;
		
		return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		for(int i = 1; i<=n; i++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			pairs.add(new Pair(x,y));
		}
		
		
		Collections.sort(pairs, new Comparator<Pair>() {
			@Override
			public int compare(Pair p1, Pair p2) {
				return p1.x - p2.x;
			}
		});
		
		dist = new int[n+1][n+1];
		for(int i = 1; i<=n; i++) {
			for(int j = 1; j<=n; j++) {
				if(i==j)
					continue;
				int distance = getDist(pairs.get(i-1), pairs.get(j-1));
				dist[i][j] = distance;
			}
		}
		
		dp = new long[n+1][n+1];
		for(int y=1;y<=n;y++) {
			Arrays.fill(dp[y], LONG_MAX);
		}
		
		dp[1][1] = 0;
		
		for(int i=1; i<=n; i++) {
			for(int j=1; j<=n;j++) {
				int next = Math.max(i, j) + 1;
				
				if(next == n+1)
					continue;
				
				dp[next][j] = Math.min(dp[next][j], dp[i][j] + dist[i][next]);
				dp[i][next] = Math.min(dp[i][next], dp[i][j] + dist[j][next]);
			}
		}
		
		long ans = LONG_MAX;
		for(int i = 1; i<n; i++) {
			ans = Math.min(ans, dp[i][n] + dist[i][n]);
		}
		System.out.println(ans);
	}
}