import java.util.*;
import java.io.*;
public class Main {
	static int n,m,c;
	static int board[][];
	static StringTokenizer st;
	static int WALL = 1;
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public String toString() {
			return y+" "+x;
		}
	}
	
	static Pair taxi;
	static int battery;
	
	static Pair starts[], ends[];
	static boolean arrived[];
	static boolean end() {
		for(int i = 1; i <= m; i++) {
			if(!arrived[i])
				return false;
		}
		return true;
	}
	
	static class Tuple{
		int y,x,dist;
		public Tuple(int y,int x, int dist) {
			this.y = y;
			this.x = x;
			this.dist = dist;
		}
		
		public boolean isHigher(Tuple t) {
			if(dist != t.dist)
				return dist < t.dist;
			if(y != t.y)
				return y < t.y;
			return x < t.x;
		}
		
		public String toString() {
			return y+" "+x+" "+dist;
		}
	}
	static int INF = Integer.MAX_VALUE;
	static Tuple NO_CUSTOMER = new Tuple(22,22,INF);
	
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	
	static int dist[][];
	static int findBestCustomer() {
		int idx = -1;
		for(int y=1; y<=n; y++) {
			Arrays.fill(dist[y], INF);
		}
		
		Queue<Pair> q = new LinkedList<>();
		q.add(new Pair(taxi.y,taxi.x));
		
		dist[taxi.y][taxi.x] = 0;
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			
			for(int dir = 0; dir< 4; dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				
				if(OOB(ny,nx) || dist[ny][nx] != INF  || board[ny][nx] == WALL)
					continue;
				
				dist[ny][nx] = dist[cur.y][cur.x] + 1;
				q.add(new Pair(ny,nx));
			}
		}
		
		Tuple best = NO_CUSTOMER;
		
		for(int i = 1; i <=m ;i++) {
			if(arrived[i]) // 이미 목적지에 데려다 준 승객은 스킵 
				continue;
			int y = starts[i].y;
			int x = starts[i].x;
			
			Tuple start = new Tuple(y,x, dist[y][x]);
			if(start.isHigher(best)) {
				best = start;
				idx = i;
			}
			
		}
//		System.out.println(best);
		if(best.dist == INF)
			return -1;
		
		return idx;
	}
	
	static Tuple goToEnd(int idx) {
		Pair end = ends[idx];
		for(int y=1; y<=n; y++) {
			Arrays.fill(dist[y], INF);
		}
		
		Queue<Pair> q = new LinkedList<>();
		q.add(new Pair(taxi.y, taxi.x));
		dist[taxi.y][taxi.x] = 0;
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			
			for(int dir = 0; dir<4;dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				if(OOB(ny,nx) || dist[ny][nx] != INF || board[ny][nx] == WALL)
					continue;
				dist[ny][nx] = dist[cur.y][cur.x] + 1;
				q.add(new Pair(ny,nx));
			}
		}
		
		return new Tuple(end.y, end.x, dist[end.y][end.x]);
	}
	
	static void simulate() {
		while(!end()) {
			int idx = findBestCustomer();
//			System.out.println("idx: "+idx);
			if(idx == -1)
				break;
			int y = starts[idx].y;
			int x = starts[idx].x;
			Tuple start = new Tuple(y,x, dist[y][x]);
//			System.out.println("start: "+start);
			if(start.dist > battery || start.dist == INF)
				break;
			
			taxi.y = start.y;
			taxi.x = start.x;
			battery -= start.dist;
			
			Tuple end = goToEnd(idx);
//			System.out.println("end: "+end);
			if(end.dist > battery || end.dist == INF)
				break;
			battery -= end.dist;
			taxi.y = end.y;
			taxi.x = end.x;
			battery +=  end.dist * 2;
			arrived[idx] = true;
			
//			System.out.printf("ty: %d tx: %x battery: %d\n", taxi.y, taxi.x, battery);
//			System.out.println("----");
		}
		System.out.println(end() ? battery : -1);
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		battery = c;
		
		board = new int[n+1][n+1];
		for(int  y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		st = new StringTokenizer(br.readLine());
		int ty = Integer.parseInt(st.nextToken());
		int tx = Integer.parseInt(st.nextToken());
		taxi = new Pair(ty,tx);
		
		starts = new Pair[m+1];
		ends = new Pair[m+1];
		arrived = new boolean[m+1];
		dist = new int[n+1][n+1];
		for(int i = 1;i<=m;i++ ) {
			st = new StringTokenizer(br.readLine());
			int sy = Integer.parseInt(st.nextToken());
			int sx = Integer.parseInt(st.nextToken());
			int ey = Integer.parseInt(st.nextToken());
			int ex = Integer.parseInt(st.nextToken());
			starts[i] = new Pair(sy,sx);
			ends[i] = new Pair(ey,ex);
		}
		
		simulate();
	}
}