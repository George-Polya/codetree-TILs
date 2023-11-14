import java.io.*;
import java.util.*;
public class Main {
	static int n,m;
	static int dp[][],inDegree[];
	static StringTokenizer st;
	static class Node{
		int idx, cost;
		public Node(int idx, int cost) {
			this.idx = idx;
			this.cost = cost;
		}
	}
	static ArrayList<Node> adj[];
	static Queue<Integer> q = new LinkedList<>();
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
//		dp = new int[n+1][n+1];
		inDegree = new int[n+1];
		adj = new ArrayList[n+1];
		for(int i = 1; i <=n; i++)
			adj[i] = new ArrayList<>();
		
		for(int i = 0;i < m ;i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			int cost = Integer.parseInt(st.nextToken());
			adj[b].add(new Node(a,cost));
			inDegree[a]++;
			
		}
		
//		int cnt = 0;
		dp = new int[n+1][n+1];
		for(int i = 1; i<=n; i++) {
			if(inDegree[i] == 0) {
//				cnt++;
				q.add(i);
				dp[i][i] = 1;
			}
		}
		
//		System.out.println(cnt);
		
		while(!q.isEmpty()) {
			int cur = q.poll();
			
			for(Node nxt : adj[cur]) {
				inDegree[nxt.idx]--;
				
				for(int i = 1; i<=n; i++) {
					dp[nxt.idx][i] += dp[cur][i] * nxt.cost;
				}
				
				if(inDegree[nxt.idx]==0)
					q.add(nxt.idx);
			}
		}
		
		
//		System.out.println(Arrays.toString(dp[n]));
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i<=n; i++) {
			if(dp[n][i] == 0)
				continue;
			sb.append(i).append(' ').append(dp[n][i]).append('\n');
		}
		System.out.println(sb);
		
		
	}
}