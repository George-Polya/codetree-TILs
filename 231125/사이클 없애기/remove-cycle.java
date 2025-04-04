import java.util.*;
import java.io.*;
public class Main {
	static int n,m1,m2;
	static StringTokenizer st;
	
	static ArrayList<Integer> adj[];
	static int inDegree[];
	static boolean visited[];
    public static void main(String[] args) throws IOException{
//        String S = "abcd"; // 주어진 문자열
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	n = Integer.parseInt(st.nextToken());
    	m1 = Integer.parseInt(st.nextToken());
    	m2 = Integer.parseInt(st.nextToken());
    	adj = new ArrayList[n+1];
    	for(int i = 1; i<=n; i++) {
    		adj[i] = new ArrayList<>();
    	}
    	
    	inDegree = new int[n+1];
    	
    	for(int i=0; i< m1;i++) {
    		st = new StringTokenizer(br.readLine());
    		int a = Integer.parseInt(st.nextToken());
    		int b = Integer.parseInt(st.nextToken());
    		adj[a].add(b);
    		inDegree[b]++;
    	}
    	
    	for(int i = 0; i < m2;i++) {
    		st = new StringTokenizer(br.readLine());
    		int a = Integer.parseInt(st.nextToken());
    		int b = Integer.parseInt(st.nextToken());
    	}
    	
    	
    	Queue<Integer> q = new LinkedList<>();
    	
    	for(int i = 1; i<=n; i++) {
    		if(inDegree[i] == 0)
    			q.add(i);
    	}
    	
    	
    	visited = new boolean[n+1];
    	
    	while(!q.isEmpty()) {
    		int cur = q.poll();
    		visited[cur] = true;
    		
    		for(int nxt : adj[cur]) {
    			inDegree[nxt]--;
    			if(inDegree[nxt] == 0) {
    				q.add(nxt);
    			}
    		}
    		
    	}
    	
    	boolean cycle = false;
    	
    	for(int i = 1; i<=n; i++) {
    		if(!visited[i])
    			cycle= true;
    	}
    	
    	System.out.println(cycle ? "No" : "Yes");
    	
    }

  
}