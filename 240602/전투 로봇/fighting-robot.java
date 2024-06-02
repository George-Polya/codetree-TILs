import java.util.*;
import java.io.*;


public class Main {
	static int n,ans;
	static int board[][];
	static StringTokenizer st;
	
	static class Robot{
		int y,x, level, killCnt;
		public Robot(int y,int x,int level, int killCnt) {
			this.y = y;
			this.x = x;
			this.level = level;
			this.killCnt = killCnt;
		}
		
		public String toString() {
			return y+" "+x;
		}
	}
	
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
	}
	
	/**
	 * 거리가 가장 가까움 
	 * 거리가 같다면 가장 위에 존재하는 (y가 작은) 몬스터
	 * 거리도 같고 같은 y라면 x가 작은 몬스터가 없앨 수 있는 몬스터가 된다. 
	 * @author heesukim
	 *
	 */
	static class Monster implements Comparable<Monster>{
		int y,x, dist;
		public Monster(int y,int x,int dist) {
			this.y = y;
			this.x = x;
			this.dist = dist;
		}
		
		public int compareTo(Monster m) {
			if(dist != m.dist)
				return dist - m.dist;
			if(y != m.y)
				return y-m.y;
			return x - m.x;
		}
		
		public String toString() {
			return y+" "+x;
		}
		
	}
	
	static PriorityQueue<Monster> pq;
	
	static void initialize() {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++)
				dist[y][x] = -1;
		}
		
		pq = new PriorityQueue<>();
		
	}
	
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static void bfs() {
		initialize();
		Queue<Pair> q = new ArrayDeque<>();
		q.offer(new Pair(robot.y, robot.x));
		dist[robot.y][robot.x] = 0;
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			
			
			// 몬스터 레벨이 로봇레벨보다 작은 경우에만 없앨 수 있음.
			// 그런 몬스터만 후보 우선순위 큐에 저장  
			int monsterLevel = board[cur.y][cur.x];
			if(monsterLevel != 0 && monsterLevel < robot.level)
				pq.add(new Monster(cur.y,cur.x, dist[cur.y][cur.x]));
			
			for(int dir = 0; dir<4;dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				
				if(OOB(ny,nx) || dist[ny][nx] != -1)
					continue;
				
				
				// 로봇레벫다 큰 몬스터가 있는칸은 지나갈 수 없다. 
				if(board[ny][nx] <= robot.level) {
					dist[ny][nx] = dist[cur.y][cur.x] + 1;
					q.add(new Pair(ny,nx));
				}
				
			}
			
		}
		
	}
	
	static void update(Monster best) {
		ans += best.dist;
		int y = best.y;
		int x = best.x;
		board[y][x] = 0;
		int level = robot.level;
		int killCnt = robot.killCnt + 1; // 현재레벨에서 지금까지 없앤 몬스터의 수  
		
		// 로봇은 본인의 레벨과 같은 수의 몬스터를 없애면 레벨을 올린다.
		if(killCnt == level) {
			level += 1;
			killCnt = 0;
		}
		
		robot = new Robot(y,x,level, killCnt);
	}
	
	static boolean simulate() {
		bfs();
		Monster peek = pq.peek();
//		System.out.println(pq);
		if(!pq.isEmpty() && board[peek.y][peek.x] < robot.level) {
			update(peek);
			return true;
		}else
			return false;
	}
	
	
	static Robot robot;
	static int dist[][];
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		board = new int[n+1][n+1];
		dist = new int[n+1][n+1];
		for(int y=1; y<=n;y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
				if(board[y][x] == 9) {
					board[y][x] = 0;
					robot = new Robot(y,x,2,0);
				}
			}
		}

		
		while(true) {
			boolean moved = simulate();
			if(!moved)
				break;
		}
		System.out.println(ans);
	}
}