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
	
	static class Product{
		int revenue, dest;
		public Product(int revenue, int dest) {
			this.revenue = revenue;
			this.dest = dest;
		}
		
		public String toString() {
			return revenue+" "+dest;
		}
	}
	
	static List<Pair> adj[];
	static void init() {
//		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		costs = new int[n];
		
		adj = new ArrayList[n];
		for(int i = 0; i <n;i++) {
			adj[i] = new ArrayList<>();
		}
		
		for(int i = 0; i < m;i++) {
			int v = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int weight = Integer.parseInt(st.nextToken());
			adj[v].add(new Pair(w,weight));
			adj[w].add(new Pair(v,weight));
		}
		
		
		// 출발지가 0일때 cost 배열 초기화 
		dijkstra();
	}
	
	static HashMap<Integer, Product> products = new HashMap<>();
	
	static void createProduct() {
		int id = Integer.parseInt(st.nextToken());
		int revenue = Integer.parseInt(st.nextToken());
		int dest = Integer.parseInt(st.nextToken());
		products.put(id, new Product(revenue, dest));
	}
	
	static void cancelProduct() {
		int id = Integer.parseInt(st.nextToken());
		if(products.containsKey(id)) {
			products.remove(id);
		}
	}
	
	static int start;
	
	static void changeStart() {
//		출발지가 변경되면 변경된 출발지를 기준으로 cost 배 초기화 
		start = Integer.parseInt(st.nextToken());
		dijkstra();
//		System.out.println(Arrays.toString(cost));
	}
	static int costs[];
	static int INF = Integer.MAX_VALUE;
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
	
	static void sellBestProduct() {
		int idx = INF;
		int bestProfit = -1;
//		System.out.println("products: "+products);
//		System.out.println(products.isEmpty());
		
		for(int id : products.keySet()) {
			Product product = products.get(id);
			int revenue = product.revenue;
			int dest = product.dest;
			int cost = costs[dest];
//			System.out.printf("id: %d cost: %d\n", id, cost);
			if(cost == INF || cost > revenue)
				continue;
			
			int profit = revenue - cost; 
//			System.out.println("profit: "+profit);
			if(bestProfit <  profit) {
				bestProfit = profit;
				idx = id;
			}else if(bestProfit == profit) {
				idx = Math.min(idx, id);
			}
		}
		
		if(idx == INF) {
			System.out.println(-1);
		}else {
			products.remove(idx);
			System.out.println(idx);
		}
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
    		case 200:
    			createProduct();
    			break;
    		case 300:
    			cancelProduct();
    			break;
    		case 400:
    			sellBestProduct();
    			break;
    		case 500:
    			changeStart();
    			break;
    		}
    	}
    }
}