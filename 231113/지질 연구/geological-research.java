import java.io.*;
import java.util.*;
public class Main {
	static int n,m;
	static StringTokenizer st;
	static int inDegree[];
	static boolean visited[];
	static ArrayList<Integer> adj1[], adj2[];
	static int dp[];
	static Queue<Integer> q = new LinkedList<>();
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		inDegree = new int[n+1];
		visited = new boolean[n+1];
		dp = new int[n+1];
		adj1 = new ArrayList[n+1];
		adj2 = new ArrayList[n+1];
		for(int i = 1; i<=n; i++) {
			adj1[i] = new ArrayList<>();
			adj2[i] = new ArrayList<>();
		}
		
		for(int i = 0; i<m;i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			adj1[a].add(b);
			inDegree[b]++;
			adj2[b].add(a);
		}
		
		for(int i = 1; i<=n; i++) {
			if(inDegree[i] == 0) {
				q.add(i);
				dp[i] = 1;
			}
		}
		
		while(!q.isEmpty()) {
			int cur = q.poll();
			
			int maxPressure = 0;
			for(int nxt : adj2[cur])
				maxPressure = Math.max(maxPressure, dp[nxt]);
			int cnt = 0;
			for(int nxt : adj2[cur])
				if(dp[nxt] == maxPressure)
					cnt++;
			
			dp[cur] = cnt == 1 ? maxPressure : maxPressure+1;
			
			for(int nxt : adj1[cur]) {
				inDegree[nxt]--;
				if(inDegree[nxt] == 0)
					q.add(nxt);
			}
		}
		
		int ans = 0;
		for(int i = 1; i <=n; i++)
			ans = Math.max(ans, dp[i]);
//		System.out.println(Arrays.toString(dp));
		System.out.println(ans);
		
		
	}
}