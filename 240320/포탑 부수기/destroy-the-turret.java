import java.io.*;
import java.util.*;



/**
 * k번 1000
 * init 100
 * 최단거리 계산 100
 * 공격 100
 * 1000(100+100+100)
 * O(10^5)
 */


public class Main {
	static int n,m,k;
	static StringTokenizer st;
	
	static class Tower implements Comparable<Tower>{
		int y,x,power,time;
		public Tower(int y,int x, int power ,int time) {
			this.y = y;
			this.x = x;
			this.power = power;
			this.time = time;
		}
		
		@Override
		public int compareTo(Tower t) {
			if(power != t.power)
				return power - t.power;
			if(time != t.time)
				return t.time - time;
			if( y+ x != t.y + t.x)
				return (t.y+t.x) - (y+x);
			return t.x - x;
		}
		
		public String toString() {
			return String.format("y: %d, x: %d, power: %d, time: %d", y,x,power, time);
		}
	}
	
	static Tower NO_TOWER = new Tower(-1,-1,-1,-1);
	
	static Tower board[][];
	
//	static List<Tower> towers = new ArrayList<>();
	static TreeSet<Tower> towers = new TreeSet<>();
	
	static void init() {
		towers.clear();
		
		for(int  y=1;y<=n;y++) {
			for(int x=1;x<=m; x++) {
				visited[y][x] = related[y][x] = false;
				if(board[y][x] != NO_TOWER)
					towers.add(board[y][x]);
			}
		}
		
//		Collections.sort(towers);
	}
	
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
	static Pair NO_PAIR = new Pair(-1,-1);
	
	static Pair prev[][];
	
	static int dy[] = {0,1,0,-1,-1,1,1,-1};
	static int dx[] = {1,0,-1,0,1,1,-1,-1};
	
	static boolean OOB(int y, int x) {
		return y<=0 || y>n || x<=0 || x>m;
	}
	
	static boolean related[][];
	static boolean visited[][];
	
	static void attack() {
		Tower attacker = towers.first();
		Tower target = towers.last();
//		Tower attacker = towers.get(0);  // 공격자 
//		Tower target = towers.get(towers.size() - 1); // 공격 대상 
		
		
		
		// 최단거리 존재 여부 판단 
		boolean canLaser = false;
		Queue<Pair> q = new LinkedList<>();
		prev = new Pair[n+1][m+1];
		prev[attacker.y][attacker.x] = NO_PAIR; // 지나온 경로 
		visited[attacker.y][attacker.x] = true;
		q.add(new Pair(attacker.y, attacker.x));
		
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			if(cur.y == target.y && cur.x == target.x) {
				canLaser = true;
				break;
			}
			
			
			for(int dir = 0; dir < 4; dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				if(OOB(ny,nx)) { // 격자밖을 벗어난 경우 반대편으로 나온다.
					
					// 반대편으로 나온 후의 ny,nx
					if(ny<=0)
						ny += n;
					if(nx<=0)
						nx += m;
					
					if(ny>n)
						ny -=n;
					if(nx>m)
						nx -=m;
					
					
				}
				
				// 반대편으로 나온 후가 부서진 포탑인지 검사
				// 이미 방문한 곳인지 검사 
				if(board[ny][nx] == NO_TOWER || visited[ny][nx])
					continue;
//				System.out.printf("ny: %d, nx: %d\n", ny,nx);
				prev[ny][nx] = cur;
				visited[ny][nx] = true;
				q.add(new Pair(ny,nx));
			}
		}
		
		// 공격 
		attacker.power += n+m;
		attacker.time = turn;
		related[attacker.y][attacker.x] = true; // 공격자는 유관하다 
		
		target.power -= attacker.power;
		related[target.y][target.x] = true; // 공격대상도 유관하다 
		
		if(target.power <= 0) // 포탑 부서짐
			board[target.y][target.x] = NO_TOWER;

		
		if(canLaser) {
//			System.out.println("laserAttack");
			
			Pair cur = prev[target.y][target.x];
			
			// 경로에 있는 포탑들 
			while(!(cur.y == attacker.y && cur.x == attacker.x)) {
				board[cur.y][cur.x].power -= attacker.power / 2;
				
				if(board[cur.y][cur.x].power <= 0) // 포탑 부서짐 
					board[cur.y][cur.x] = NO_TOWER;
				
				related[cur.y][cur.x] = true; // 경로 상의 포탑은 피해를 받았으므로 유관하다 
				
				cur = prev[cur.y][cur.x];
			}
			
		}else {
//			System.out.println("bombAttack");
			
			
			
			int y = target.y;
			int x = target.x;
			
			for(int dir = 0; dir < 8; dir++) {
				int ny = y + dy[dir];
				int nx = x + dx[dir];
				
				
				// 격자를 벗어나면 반대편 격자 
				if(OOB(ny,nx)) {
					// 반대편으로 나온 후의 ny,nx
					if(ny<=0)
						ny += n;
					if(nx<=0)
						nx += m;
					
					if(ny>n)
						ny -=n;
					
					if(nx>m)
						nx -=m;
					
				}
				
				
				// 부서진 포탑이면 스킵 
				if(board[ny][nx] == NO_TOWER)
					continue;
					
				// 공격자는 영향받지 않음 
				if(ny == attacker.y && nx == attacker.x)
					continue;
//				System.out.printf("ny: %d, nx: %d\n", ny,nx);
				
				
				board[ny][nx].power -= attacker.power / 2;
				if(board[ny][nx].power <=0)
					board[ny][nx] = NO_TOWER;
				related[ny][nx] = true;
				
			}
			
			
		}
		
		
		
	}
	
	static void printBoard(Tower board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1;x<=m; x++) {
				System.out.printf("%-3d", board[y][x] == NO_TOWER ? 0 : board[y][x].power);
			}
			System.out.println();
		}
	}
	
	static void fix() {
		for(int y=1;y<=n; y++) {
			for(int x=1;x<=m; x++) {
				if(board[y][x] == NO_TOWER)
					continue;
				if(related[y][x])
					continue;
				board[y][x].power += 1;
			}
		}
	}
	
	static int turn;
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		board = new Tower[n+1][m+1];
		related = new boolean[n+1][m+1];
		visited = new boolean[n+1][m+1];
		
		for(int y=1;y<=n;y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1;x<=m;x++) {
				int power = Integer.parseInt(st.nextToken());
				if(power == 0)
					board[y][x] = NO_TOWER;
				else {
					board[y][x] = new Tower(y,x,power,0);
				}
			}
		}
		
		for(turn = 1; turn<=k; turn++) {
//			System.out.println("turn: "+turn);
			
			
			
			// 공격자 선정을 위한 초기화 및 정렬 
			init();
			
			if(towers.size() == 1)
				break;
			
			// 공격 
			attack();
			
			// 포탑정비 
			fix();
//			printBoard(board);
		}
		
		init();
		System.out.println(towers.last().power);
		
	}
}