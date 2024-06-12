import java.util.*;
import java.io.*;
public class Main {
	static int Q,n,m;
	static StringTokenizer st;
	static class Pair implements Comparable<Pair>{
		int city, weight;
		
		public Pair(int city, int weight) {
			this.city = city;
			this.weight = weight;
		}
		
		public String toString() {
			return city +" "+weight;
		}
		
		public int compareTo(Pair p) {
			return weight - p.weight;
		}
	}
	
	
	static List<Pair> adj[];
	
	static int board[][];
	static void init() {
//		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		board = new int[n][n];
		costs = new int[n];
		
		adj = new ArrayList[n];
		for(int i = 0; i <n;i++) {
			adj[i] = new ArrayList<>();
			Arrays.fill(board[i], INF);
			board[i][i] = 0;
		}
		
		for(int i = 0; i < m;i++) {
			int v = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int weight = Integer.parseInt(st.nextToken());
			board[v][w] = Math.min(board[v][w], weight);

			board[w][v] = Math.min(board[w][v], weight);
		}
		
		for(int y = 0; y < n; y++) {
			for(int x= 0 ;x < n;x++) {
				if(board[y][x] == INF || board[y][x] == 0)
					continue;
				adj[y].add(new Pair(x, board[y][x]));
			}
		}
		
		
		
		
		// 출발지가 0일때 cost 배열 초기화 
		dijkstra();
	}
	
//	static HashMap<Integer, Product> products = new HashMap<>();
	
//	static boolean isMade[], isCancel[];
	static Set<Integer> isMade = new HashSet<>();
	static Set<Integer> isCancel = new HashSet<>();
	
	static class Product implements Comparable<Product>{
		int id, revenue, dest, profit;
		
		public Product(int id, int revenue, int dest, int profit) {
			this.id = id;
			this.revenue = revenue;
			this.dest = dest;
			this.profit = profit;
		}
		
		public int compareTo(Product p) {
			if(profit != p.profit)
				return p.profit - profit;
//				return profit - p.profit;
//				return Integer.compare(p.profit, profit);
//			return Integer.compare(id, p.id);
//			return p.id - id;
			return id - p.id;
		}
		
		public String toString() {
			return id+" "+revenue+" "+dest+" "+profit;
		}
		
	}
	
	static PriorityQueue<Product> productQ = new PriorityQueue<>();
	
	static void createProduct(int id, int revenue, int dest) {
		int profit = revenue - costs[dest];
		
		productQ.add(new Product(id,revenue, dest, profit));
		
		isMade.add(id);
	}
	
	static void cancelProduct(int id) {
		if(isMade.contains(id))
			isCancel.add(id);
	}
	
	static int start;
	
	static void changeStart() {
//		출발지가 변경되면 변경된 출발지를 기준으로 cost 배 초기화 
		start = Integer.parseInt(st.nextToken());
		dijkstra();
		List<Product> temp = new ArrayList<>();
		
		while(!productQ.isEmpty()) {
			temp.add(productQ.poll());
		}
		
		for(Product p : temp) {
			createProduct(p.id, p.revenue, p.dest);
		}
		
	}
	static int costs[];
	static int INF = Integer.MAX_VALUE / 2;
	
	static void dijkstra() {
		PriorityQueue<Pair> pq = new PriorityQueue<>();
		Arrays.fill(costs, INF);
		costs[start] = 0;
		pq.add(new Pair(start, costs[start]));
		
		while(!pq.isEmpty()) {
			Pair cur = pq.poll();
			
			if(costs[cur.city] < cur.weight)
				continue;
			
			for(Pair nxt : adj[cur.city]) {
				if(costs[nxt.city] <= costs[cur.city] + nxt.weight)
					continue;
				costs[nxt.city] = costs[cur.city] + nxt.weight;
				pq.add(new Pair(nxt.city,costs[nxt.city]));
			}
		}
//		System.out.println(Arrays.toString(costs));
	}
	
	static StringBuilder sb = new StringBuilder();
	
	static int  sellBestProduct() {
//		System.out.println("productQ: "+productQ);
		while(!productQ.isEmpty()) {
			Product p = productQ.peek();
			
			if(p.profit < 0)
				break;
			productQ.poll();
			
			if(!isCancel.contains(p.id))
				return p.id;
		}
		return -1;
	}
 	
	public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	Q = Integer.parseInt(br.readLine());
    	
    	for(int q = 1; q<=Q;q++) {
    		st = new StringTokenizer(br.readLine());
    		int cmd = Integer.parseInt(st.nextToken());
    		
    		switch(cmd) {
    		case 100:
    			init();
    			break;
    		case 200:{
    			int id = Integer.parseInt(st.nextToken());
    			int revenue = Integer.parseInt(st.nextToken());
    			int dest = Integer.parseInt(st.nextToken());
    			createProduct(id, revenue, dest);
    			break;
    		}
    		case 300:{
    			int id = Integer.parseInt(st.nextToken());
    			cancelProduct(id);
    			break;    			
    		}
    		case 400:
    			sb.append(sellBestProduct()).append('\n');
    			break;
    		case 500:
    			changeStart();
    			break;
    		}
    	}
    	System.out.println(sb);
    }
}