import java.io.*;
import java.util.*;

public class Main {
	static int n,m;
	static StringTokenizer st;
	static int inDegree[];
	static boolean visited[];
	static Queue<Integer> q = new LinkedList<>();
	
	static class Edge{
		int first, second;
		public Edge(int first, int second) {
			this.first = first;
			this.second = second;
		}
	}
	
	static Edge edgeList[];
	static ArrayList<Integer> adj[];
	static boolean check(int target) {
		
		for(int i = 1; i<=n; i++) {
			inDegree[i] = 0;
			visited[i] = false;
			adj[i] = new ArrayList<>();
		}
		
		for(int i = 1; i<=target; i++) {
			int a = edgeList[i].first;
			int b = edgeList[i].second;
			adj[a].add(b);
			inDegree[b]++;
		}
		
		Queue<Integer> q = new LinkedList<>();
		for(int i = 1; i<=n; i++) {
			if(inDegree[i] == 0)
				q.add(i);
		}
		
		
		while(!q.isEmpty()) {
			int cur = q.poll();
			visited[cur] = true;
			
			for(int nxt : adj[cur]) {
				inDegree[nxt]--;
				if(inDegree[nxt] == 0)
					q.add(nxt);
			}
		}
		
		boolean cycle = false;
		
		for(int i = 1; i<=n; i++) {
			if(!visited[i])
				cycle= true;
		}
		return cycle;
		
	}
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		adj = new ArrayList[n+1];
		inDegree = new int[n+1];
		visited = new boolean[n+1];
		
		edgeList = new Edge[m+1];
		for(int i = 1; i<=m; i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			edgeList[i] = new Edge(a,b);
		}
		
		
		int left = 1;
		int right = n;
		int ans = 0;
		
		while(left <= right) {
			int mid = (left + right) / 2;
			
			
			if(check(mid)) {
				right = mid - 1;
				ans = mid;
			}else {
				left = mid + 1;
			}
		}
		
		System.out.println(ans == 0 ? "Consistent" : ans);
		
	}

}