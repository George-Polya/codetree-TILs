import java.io.*;
import java.util.*;

public class Main {
	static int n;
	static int elapsedTimes[];
	static class Node{
		int idx,cost;
		public Node(int idx, int cost) {
			this.idx = idx;
			this.cost = cost;
		}
	}
	
	static ArrayList<Integer> adj[];
	
	static int dp[], inDegree[];
	static StringTokenizer st;
	static Queue<Integer> q = new LinkedList<>();
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		elapsedTimes = new int[n+1];
		adj = new ArrayList[n+1];
		inDegree = new int[n+1];
		for(int i = 1; i<=n;i++) {
			adj[i] = new ArrayList<>();
		}
		for(int i = 1; i<=n; i++) {
			st = new StringTokenizer(br.readLine());
			elapsedTimes[i] = Integer.parseInt(st.nextToken());
			int len = Integer.parseInt(st.nextToken());
			for(int j = 0; j < len;j++) {
				int prev = Integer.parseInt(st.nextToken());
				adj[prev].add(i);
				inDegree[i]++;
			}
		}
		
		dp = new int[n+1];
		
		for(int i = 1; i<=n; i++) {
			if(inDegree[i] == 0) {
				q.add(i);
				dp[i] = elapsedTimes[i];
			}
		}
		
		
		while(!q.isEmpty()) {
			int cur = q.poll();
			
			for(int nxt : adj[cur]) {
				inDegree[nxt]--;
				dp[nxt] = Math.max(dp[nxt], dp[cur] + elapsedTimes[nxt]);
				if(inDegree[nxt] == 0)
					q.add(nxt);
			}
		}
		
		int ans = 0;
		for(int i = 1; i<=n; i++)
			ans = Math.max(ans, dp[i]);
		System.out.println(ans);
	}
}