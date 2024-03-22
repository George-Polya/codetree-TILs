import java.io.*;
import java.util.*;

public class Main{
	static int n,m;
	static int board[][];
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public boolean isSame(Pair p) {
			return y == p.y && x == p.x;
		}
		public String toString() {
			return y+" "+x;
		}
		
		public boolean isHigher(Pair p) {
			if(y != p.y)
				return y < p.y;
			return x < p.x;
		}
	}
	static StringTokenizer st;
	static int BLOCK = -1;
	static Pair stores[];
	static int t = 0;
	
	/*
	 *  종료조건
	 *  모든 사람이 편의점에 도착하면 종료 
	 *  편의점 위치가 모두 BLOCK이면 종료 
	 *  
	 *  하나라도 BLOCK이 아니면 계속함 
	 */
	static boolean end() {
		for(int id = 1; id<=m; id++) {
			Pair dest = stores[id];
			if(board[dest.y][dest.x] != -1)
				return false;
		}
		return true;
	}
	
	static Pair NO_PAIR = new Pair(16,16);
	static Pair ARRIVED = new Pair(17,17);
	static Pair people[];
	static Pair nxtPeople[];
	static int dy[] = {1,0,0,-1};
	static int dx[] = {0,1,-1,0};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0|| x>n;
	}
	static int dist[][];
	static void init(int dist[][]) {
		for(int y=1;y<=n;y++)
			Arrays.fill(dist[y], -1);
	}
	
	static void printBoard(int board[][]) {
		for(int y=1;y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.print(board[y][x]+" ");
			}
			System.out.println();
		}
	}
	
	static int getDir(int y,int x, int id) {
		/*
		 * 편의점에서 현재위치까지 bfs를 했을 때, 현재위치에서 네 방향의 위치 중에서 visited인 곳의 방향 
		 * 편의점에서 bfs할때는 하우좌상의 순서로 해야함 
		 * 편의점에서 현재위치까지의 최단거리는 베이스캠프나 도착한 편의점을 고려할때마다 바뀐다 
		 * 따라서 매번 bfs해줘야한다. 
		 */
		Pair dest = stores[id];
		init(dist);
		Queue<Pair> q= new ArrayDeque<>();
		q.add(new Pair(dest.y,dest.x));
		dist[dest.y][dest.x] = 0;
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			
			for(int dir = 0;dir<4;dir++) {
				int ny = cur.y +dy[dir];
				int nx = cur.x +dx[dir];
				
				if(OOB(ny,nx) || dist[ny][nx] != -1 || board[ny][nx] == BLOCK)
					continue;
				
				dist[ny][nx] = dist[cur.y][cur.x] + 1;
				q.add(new Pair(ny,nx));
			}
		}
		
//		printBoard(dist);
		
		int ret = -1;
		int minDist = INT_MAX;
		for(int dir =3 ; dir>=0; dir-- ) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			if(OOB(ny,nx) || board[ny][nx] == BLOCK)
				continue;
			
			// 현재 위치에서 
			if(dist[ny][nx] != -1) {
				int distance = dist[ny][nx];
				if(minDist > distance) {
					minDist = distance;
					ret = dir;
				}
				
			}
		}
		return ret;
	}
	
	static void move(int id) {
		Pair person = people[id];
		int y = person.y;
		int x = person.x;
		
		int dir = getDir(y,x,id); // 이동할 방향 정하기 
//		System.out.printf("id: %d, dir: %d\n", id, dir);

		
		// 이동하기 
		int ny = y + dy[dir];
		int nx = x + dx[dir];
		nxtPeople[id] = new Pair(ny,nx);
		
	}
	
	static void moveAll() {
		
		for(int id = 1; id<=m;id++) {
			if(people[id].isSame(stores[id]))
				nxtPeople[id] = people[id];
			else
				nxtPeople[id] = NO_PAIR;
		}
	
		// 격자 안에 있는 사람만 이동 
		// 격자 안에 있지만 이미 도착한 사람은 이동하지 않는다 
		for(int id = 1; id<=m; id++) {
			if(people[id] == NO_PAIR || people[id].isSame(stores[id]))
				continue;
			move(id);
		}
		
		
		// 격자 사람들이 모두 이동
		System.arraycopy(nxtPeople,1,people,1,m);
	}
	
	/*
	 * 1. 편의점에 도착했으면 해당 편의점에서 멈춘다 
	 * 2. 다른 사람들은 해당 편의점을 지나갈 수 없게 된다
	 * 3. 격자의 사람들이 모두 이동한 뒤에 지나갈 수 없게 된다 
	 */
	static void arrive() {
		for(int id = 1; id<=m; id++) {
			if(people[id] == NO_PAIR)
				continue;
			Pair person = people[id];
			if(person.isSame(stores[id])) {
				board[person.y][person.x] = BLOCK;
			}
		}
	}
	
	
	static int INT_MAX = Integer.MAX_VALUE;
	
	/*
	 * 편의점에서 가장 가까운 베이스캠프 찾기 
	 * 최단거리가 같다면 행이 작은 캠프, 행도 같다면 열이 작은 캠
	 */
	static Pair findBasecamp(int id) {
		Pair store = stores[id];
		int minDist = INT_MAX;
		init(dist);
		Pair ret = NO_PAIR;
		Queue<Pair> q = new LinkedList<>();
		q.add(new Pair(store.y,store.x));
		dist[store.y][store.x] = 0;
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			
			if(board[cur.y][cur.x] == 1) {
				int distance = dist[cur.y][cur.x];
				
				// 최단거리,최단거리가 같다면 행이 작은거, 행도 작다면 
				if(minDist > distance || (minDist == distance && cur.isHigher(ret))) {
					minDist = distance;
					ret = cur;
				}
			}
			
			for(int dir = 0; dir < 4;dir++) {
				int ny = cur.y+ dy[dir];
				int nx = cur.x+ dx[dir];
				
				if(OOB(ny,nx) || dist[ny][nx] != -1 || board[ny][nx] == BLOCK)
					continue;
				dist[ny][nx] = dist[cur.y][cur.x] + 1;
				q.add(new Pair(ny,nx));
			}
		}
		return ret;
		
	}
	static void gotoBasecamp(int id) {
		// id에 해당하는 편의점에서 가장 가까운 베이스 캠프 찾기 
		Pair basecamp = findBasecamp(id);
//		System.out.println("basecamp: "+basecamp);
		
		
		// id에 해당하는 사람이 베이스캠프로 이동 
		people[id] = basecamp;
		
		// 이 베이스캠프는 더 이상 지나갈 수 없음 
		board[basecamp.y][basecamp.x] = BLOCK;
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		board = new int[n+1][n+1];
		dist = new int[n+1][n+1];
		
		for(int y=1;y<=n;y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1;x<=n;x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		
		nxtPeople = new Pair[m+1];
		stores = new Pair[m+1];
		people = new Pair[m+1];
		for(int id = 1; id<=m; id++) {
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			stores[id] = new Pair(y,x);
			people[id] = NO_PAIR;
		}
		
		while(!end()) {
			t++; 
//			System.out.println("turn: "+t);
			moveAll(); // 격자 안 사람들의 이동 
			arrive();  // 편의점 도착 판단 
			if(t <=m)
				gotoBasecamp(t); //t번 사람의 베이스캠프로의 이동
//			System.out.println(Arrays.toString(people));
//			printBoard(board);
//			System.out.println("---------------");
		}
		System.out.println(t);
	}
}