import java.util.*;
import java.io.*;
public class Main {
	static int q;
	static int board[][];
	static StringTokenizer st;
	static class Node{
		int id, dist;
		public Node(int id, int dist) {
			this.id= id;
			this.dist = dist;
		}
	}
	
	static int n,m;
	static List<Node> adj[];
	
	static int INF = Integer.MAX_VALUE;
	static int dist[];
	static Set<Integer> enrolled = new HashSet<>();
	static Set<Integer> canceled = new HashSet<>();
	static int start = 0;
	static void init() {
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		dist = new int[n];
		board = new int[n][n];
		adj = new ArrayList[n];
		for(int i = 0; i < n;i++) {
			adj[i] = new ArrayList<>();
			Arrays.fill(board[i], INF);
			board[i][i] = 0;
		}
		
		for(int i = 0; i <m;i++) {
			int u = Integer.parseInt(st.nextToken());
			int v = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			board[u][v] = Math.min(board[u][v], w);
			board[v][u] = Math.min(board[v][u], w);
		}
		
		for(int u = 0 ;u <n; u++) {
			for(int v = 0; v <n;v++) {
				if(board[u][v]  == INF)
					continue;
				adj[u].add(new Node(v, board[u][v]));
			}
		}
		
		
		dijkstra();
	}
	
	static class Package{
		int id, revenue, profit, dest;
		
		public Package(int id, int revenue, int dest,int profit) {
			this.id = id; 
			this.revenue = revenue;
			this.dest = dest;
			this.profit = profit;
		}
		
		public String toString() {
			return id+" "+revenue+" "+profit+" "+dest;
		}
	}
	
	static PriorityQueue<Package> pq = new PriorityQueue<>((p1,p2)->{
		if(p1.profit != p2.profit)
			return Integer.compare(p2.profit, p1.profit);
		return p1.id - p2.id;
	});
	
	static void addPackage(int id, int revenue, int dest) {
		int profit = revenue - dist[dest];
		pq.add(new Package(id, revenue, dest, profit));
		enrolled.add(id);
	}
	
	
	static void cancel(int id) {
		if(enrolled.contains(id))
			canceled.add(id);
	}
	
	static void dijkstra() {
		Arrays.fill(dist, INF);
		
		PriorityQueue<Node> pq = new PriorityQueue<>((n1,n2)->{
			return n1.dist - n2.dist;
		});
		
		dist[start] = 0;
		pq.add(new Node(start, dist[start]));
		
		while(!pq.isEmpty()) {
			Node cur = pq.poll();
			
			if(dist[cur.id] < cur.dist)
				continue;
			
			for(Node nxt : adj[cur.id]) {
				if(dist[nxt.id] <= dist[cur.id] + nxt.dist)
					continue;
				dist[nxt.id] = dist[cur.id] + nxt.dist;
				pq.add(new Node(nxt.id, dist[nxt.id]));
			}
			
		}
//		System.out.println("dist: "+Arrays.toString(dist));
		
	}
	
	static void changeStart(int s) {
		start = s;
		dijkstra();
		List<Package> temp = new ArrayList<>();
		while(!pq.isEmpty()) {
			temp.add(pq.poll());
		}
		
		for(Package pack : temp) {
			addPackage(pack.id, pack.revenue,pack.dest);
		}
		
	}
	
	static int sell() {
//		System.out.println("package Q:" +pq);
		while(!pq.isEmpty()) {
			Package p = pq.peek();
			
			if(p.profit < 0)
				break;
			pq.poll();
			if(!canceled.contains(p.id))
				return p.id;
		}
		return -1;
	}
	
	public static void main(String args[]) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		q = Integer.parseInt(br.readLine());
		for(int query=1; query<=q; query++) {
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());
			switch(cmd) {
			case 100:{
				init();
			}
			break;
			case 200:{
				int id = Integer.parseInt(st.nextToken());
				int revenue = Integer.parseInt(st.nextToken());
				int dest = Integer.parseInt(st.nextToken());
				addPackage(id, revenue, dest);
			}
			break;
			case 300:{
				int id = Integer.parseInt(st.nextToken());
				cancel(id);
			}
			break;
			case 400:{
				System.out.println(sell());
			}
			break;
			case 500:{
				int s = Integer.parseInt(st.nextToken());
				changeStart(s);
			}
			break;
			}
		}
	}
}