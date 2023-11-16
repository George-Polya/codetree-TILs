import java.io.*;
import java.util.*;

public class Main {
	static int n,m;
	static StringTokenizer st;
	static int dp[], inDegree[], prev[];
	static ArrayList<Integer> adj[];
	static int INT_MIN = Integer.MIN_VALUE;
	static Queue<Integer> q = new LinkedList<>();
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		dp = new int[n+1];
		prev = new int[n+1];
		adj = new ArrayList[n+1];
		for(int i = 1; i <=n;i++) {
			adj[i]= new ArrayList<>();
			dp[i] = INT_MIN;
			prev[i] = -1;
		}
		inDegree = new int[n+1];
		
		
		for(int i = 0; i <m;i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			adj[b].add(a);
			inDegree[a]++;
		}
		
		for(int i = 1; i<=n;i++) {
//			Collections.sort(adj[i]);
			if(inDegree[i] == 0)
				q.add(i);
		}
		
		
		dp[n] = 0;
		
		while(!q.isEmpty()) {
			int cur = q.poll();
			
			for(int nxt : adj[cur]) {
				if(dp[cur] != INT_MIN) {
					if(dp[nxt] < dp[cur] + 1) {
						dp[nxt] = dp[cur] + 1;
						prev[nxt] = cur;
					}else if(dp[nxt] == dp[cur] +1 && prev[nxt] > cur) {
						prev[nxt] = cur;
					}
				}
				inDegree[nxt]--;
				
				if(inDegree[nxt] == 0)
					q.add(nxt);
			}
		}
		
		if(dp[1] == INT_MIN) {
			System.out.println(-1);
		}else {
			StringBuilder sb = new StringBuilder();
			sb.append(dp[1]).append('\n');
			
			int end = 1;
			while(end != -1) {
				sb.append(end).append(' ');
				end = prev[end];
			}
			
			
			System.out.println(sb);
		}
		
	}

}