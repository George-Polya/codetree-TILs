import java.util.*;
import java.io.*;
public class Main {
	static int n,m;
	static StringTokenizer st;
	static class Edge{
		int a,b;
		public Edge(int a,int b) {
			this.a = a;
			this.b = b;
		}
	}
	
	static Edge edgeList[];
	
	static boolean check(int target) {
		ArrayList<Integer> adj[] = new ArrayList[n+1];
		for(int i = 1; i<=n; i++) {
			adj[i] = new ArrayList<>();
		}
		
		boolean visited[] = new boolean[n+1];
		int inDegree[] = new int[n+1]; 
		
		for(int i = 1; i <= target;i++) {
			Edge e = edgeList[i];
			
			adj[e.a].add(e.b);
			inDegree[e.b]++;
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
				cycle = true;
		}
		
		return cycle;
		
	}
	
    public static void main(String[] args) throws IOException{
//        String S = "abcd"; // 주어진 문자열
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	n = Integer.parseInt(st.nextToken());
    	m = Integer.parseInt(st.nextToken());
    	edgeList = new Edge[m + 1];
    	
    	for(int i = 1; i<=m;i++) {
    		st = new StringTokenizer(br.readLine());
    		int a = Integer.parseInt(st.nextToken());
    		int b = Integer.parseInt(st.nextToken());
    		edgeList[i] = new Edge(a,b);
    	}
    	
    	
    	int l = 1, r = n;
    	
    	int ans = 0;
    	while(l<=r) {
    		int mid = (l + r) / 2;
    		
    		if(check(mid)) {
    			r = mid - 1;
    			ans = mid;
    		}else {
    			l = mid + 1;
    		}
    	}
    	
    	System.out.println(ans == 0 ? "Consistent" : ans);
    }

  
}