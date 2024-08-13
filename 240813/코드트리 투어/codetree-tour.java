import java.util.*;
import java.io.*;


public class Main {
	static int n,m,q;
	static int INF = 987654321;
	static int adj[][];
	static int dist[];
	static StringTokenizer st;
	static int start;
	static class Product implements Comparable<Product>{
		int id, revenue, dest;
		int cost, profit;
		public Product(int id,int revenue, int dest) {
			this.id = id;
			this.revenue = revenue;
			this.dest = dest;
		}
		
		public Product(int id,int revenue, int dest, int cost) {
			this.id = id;
			this.revenue = revenue;
			this.dest = dest;
			this.cost = cost;
			this.profit = revenue - cost;
		}
		
		
		public int compareTo(Product p) {
			if(profit != p.profit)
				return p.profit - profit;
			return id - p.id;
		}
		
		public String toString() {
			return String.format("id: %d, revenue: %d, dest: %d, cost: %d, profit:%d", id,revenue, dest,cost, profit);
		}
	}
	
	static Product WORST_PRODUCT = new Product(30001, -1, -1, -1);
	
	
	static class Node implements Comparable<Node>{
		int id, cost;
		public Node(int id, int cost) {
			this.id = id;
			this.cost = cost;
		}
		
		public int compareTo(Node n) {
			return cost - n.cost;
		}
		
		public String toString() {
			return id+" "+cost;
		}
	}
	
	static ArrayList<Node> adjList[];
	
//	static Map<Integer, Product> managed = new HashMap<>();
	static Set<Integer> managed = new HashSet<>();
	static Set<Integer> removed = new HashSet<>();
	
	static PriorityQueue<Product> ppq;
	
	static void init() {
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		adj = new int[n][n];
		dist = new int[n];
		ppq = new PriorityQueue<>();
		for(int i = 0; i <n;i++) {
			Arrays.fill(adj[i], INF);
		}
		
		for(int i = 0; i < m; i++) {
			int v = Integer.parseInt(st.nextToken());
			int u = Integer.parseInt(st.nextToken());
			int weight = Integer.parseInt(st.nextToken());
			if(u == v)
				continue;
			adj[u][v] = Math.min(adj[u][v], weight);
			adj[v][u] = Math.min(adj[v][u], weight);
		}
		
		adjList = new ArrayList[n];
		for(int i = 0; i <n;i++) {
			adjList[i] = new ArrayList<>();
		}
		
		for(int y= 0; y<n; y++) {
			for(int x= 0; x<n; x++) {
				if(adj[y][x] == INF)
					continue;
				adjList[y].add(new Node(x,adj[y][x]));
			}
		}
		
//		for(int i = 0; i < n; i++) {
//			System.out.println(i+": "+adjList[i]);
//		}
		
		dijkstra(start);
	}
	
	static void createProduct(int id, int revenue, int dest) {
		managed.add(id);
		ppq.add(new Product(id,revenue, dest, dist[dest]));
	}
	
	static void cancelProduct() {
		int id = Integer.parseInt(st.nextToken());
		if(managed.contains(id))
			removed.add(id);
	}
	
	static void printDist(int dist[]) {
		for(int i = 0; i < dist.length;i++) {
			System.out.print((dist[i] == INF ? "INF" : dist[i])+" ");
		}
		System.out.println();
	}
	
	static void dijkstra(int start) {
		Arrays.fill(dist, INF);
		PriorityQueue<Node> pq = new PriorityQueue<>();
		dist[start] = 0;
		pq.add(new Node(start, dist[start]));
		
		while(!pq.isEmpty()) {
			Node cur = pq.poll();
			
			if(dist[cur.id] < cur.cost)
				continue;
			
			for(Node nxt : adjList[cur.id]) {
				if(dist[nxt.id] < dist[cur.id] + nxt.cost)
					continue;
				dist[nxt.id] = dist[cur.id] + nxt.cost;
				pq.add(new Node(nxt.id, dist[nxt.id]));
			}
		}
		
		
		
	}
	
	/*
	 * 최적의 여행상품 판매 
	 * 1. cost 구하기 -> 다익스트라 
	 */
	static void sellBestProduct() {
		
		while(!ppq.isEmpty()) {
			Product product = ppq.peek();
			
			if(product.profit < 0)
				break;
			
			ppq.poll();
			if(!removed.contains(product.id)) {
				System.out.println(product.id);
				return;
			}
		}
		
		System.out.println(-1);
		
	}
	
	static void changeStart() {
		start = Integer.parseInt(st.nextToken());
		dijkstra(start);
		
		ArrayList<Product> temp = new ArrayList<>();
		while(!ppq.isEmpty()) {
			temp.add(ppq.poll());
		}
		
		for(Product p : temp) {
			ppq.add(new Product(p.id, p.revenue, p.dest,dist[p.dest]));
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		q = Integer.parseInt(br.readLine());
		
		for(int turn=1 ; turn<=q; turn++) {
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());
			if(cmd == 100) {
				init();
			}else if(cmd == 200) {
				int id = Integer.parseInt(st.nextToken());
				int revenue = Integer.parseInt(st.nextToken());
				int dest = Integer.parseInt(st.nextToken());
				createProduct(id, revenue, dest);
			}else if(cmd == 300) {
				cancelProduct();
			}else if(cmd == 400) {
				sellBestProduct();
			}else if(cmd == 500) {
				changeStart();
			}
		}
	}
}