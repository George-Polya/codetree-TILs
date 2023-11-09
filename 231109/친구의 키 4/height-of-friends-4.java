import java.io.*;
import java.util.*;
public class Main {
	static int n,m;
	static StringTokenizer st;
	static ArrayList<Integer> adj[];
	static boolean visited[];
	static int inDegree[];
	static class Pair{
		int first, second;
		public Pair(int first, int second) {
			this.first = first;
			this.second = second;
		}
	}
	
	static Pair edgeList[];
	
	static boolean hasCycle(int target) {
		for(int i = 1; i<=n; i++) {
			adj[i] = new ArrayList<>();
			inDegree[i] = 0;
			visited[i] = false;
		}
		
		for(int i =1 ; i<=target;i++) {
			Pair edge = edgeList[i];
			adj[edge.first].add(edge.second);
			inDegree[edge.second]++;
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
		for(int i = 1; i<=n;i++) {
			if(!visited[i])
				cycle = true;
		}
		return cycle;
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		edgeList = new Pair[m+1];
		adj = new ArrayList[n+1];
		inDegree = new int[n+1];
		visited = new boolean[n+1];
		for(int i = 1; i<=m; i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			edgeList[i] = new Pair(a,b);
		}
		
		int left = 1;
		int right = m;
		int ans = 0;
		while(left <= right) {
			int mid = (left + right) / 2;
			
			if(hasCycle(mid)) {
				ans = mid;
				right = mid -1;
			}else {
				left = mid + 1;
			}
		}
		
		System.out.println(ans == 0 ? "Consistent" : ans);
	}
}