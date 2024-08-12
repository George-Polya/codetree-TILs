import java.util.*;
import java.io.*;

public class Main {
	static StringTokenizer st;
	static Turret board[][];
	static int n,m,k;
	
	
	static class Turret implements Comparable<Turret>{
		int power, time, y,x;
		
		public Turret(int power, int time, int y, int x) {
			this.time = time;
			this.power = power;
			this.y = y;
			this.x = x;
		}
		
		public int compareTo(Turret t) {
			if(power != t.power)
				return power - t.power;
			if(time != t.time)
				return t.time - time;
			if( (y+x) != (t.y + t.x))
				return (t.y + t.x) - (y + x);
			return t.x - x;
		}
		
		public String toString() {
			return String.format("(%d, %d), power: %d, time: %d", y,x,power,time);
		}
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
		
		public boolean isSame(int y,int x) {
			return this.y == y && this.x == x;
		}
	}
	static Pair NO_POS = new Pair(-1,-1);
	
	static void printBoard(Turret board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=m; x++) {
				System.out.printf("%3d", board[y][x].power);
			}
			System.out.println();
		}
	}
	
	static Turret BROKEN = new Turret(0,-1,-1,-1);
	static ArrayList<Turret> turrets;
	static Turret attacker, target;
	static void init() {
		turrets = new ArrayList<>();

		for(int y=1; y<=n; y++) {
			for(int x=1; x<=m; x++) {
				involved[y][x] = false;
				if(board[y][x] == BROKEN)
					continue;
				turrets.add(board[y][x]);
			}
		}
		
		Collections.sort(turrets);
		attacker = turrets.get(0);
		target = turrets.get(turrets.size() - 1);
		
	}
	
	static boolean involved[][];
	static int dy[] = {0,1,0,-1,-1,1,1,-1};
	static int dx[] = {1,0,-1,0,1,1,-1,-1};
	static boolean OOB(int y,int x) {
		return y<= 0 || y>n || x<=0 || x>m;
	}
	
	static void attack() {
		
		// 최단 경로 존재 판단  
		boolean canLaser = false;
		Queue<Pair> q = new LinkedList<>();
		q.add(new Pair(attacker.y, attacker.x));
		Pair prev[][] = new Pair[n+1][m+1];
		prev[attacker.y][attacker.x] = NO_POS;
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			
			if(cur.isSame(target.y, target.x)) {
				canLaser = true;
				break;
			}
			
			for(int dir = 0; dir< 4; dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				ny = ((ny - 1 + n) % n) + 1;
				nx = ((nx - 1 + m) % m) + 1;
				
				if(prev[ny][nx] != null || board[ny][nx] == BROKEN)
					continue;
				prev[ny][nx] = cur;
//				visited[ny][nx] = true;
				q.add(new Pair(ny,nx));
			}
		}
		
		// 최단경로로 이동가능하다면 레이저 공격 
		int power = attacker.power + (n + m);
		if(canLaser) {
			Pair pair = prev[target.y][target.x];
			
			// 레이저 경로에 있는 포탑들은 증가된 공격력의 절반만큼의 공격을 받음 
			while(pair != NO_POS) {
//				System.out.println(pair);
				involved[pair.y][pair.x] = true;
				board[pair.y][pair.x].power -= power / 2;
				pair = prev[pair.y][pair.x];
			}
			
			// 공격대상은 증가된 공격력만큼의 공격을 받음 
			target.power -= power;
			involved[target.y][target.x] = true;
			
			attacker.power = power;
			involved[attacker.y][attacker.x] = true;
			attacker.time = turn;
			return;
		}
		
		
		/*
		 * 공격대상 주위 8개의 방향에 있는 포탑도 피해를 받는다 
		 * 공격자는 공격에 영향을 받지 않는다
		 * 격자 밖을 벗어나면  
		 */
		int y = target.y;
		int x = target.x;
		
		involved[y][x] = true;
		
		for(int dir = 0; dir < 8; dir++) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			ny = ((ny - 1 + n) % n ) + 1;
			nx = ((nx - 1 + m) % m ) + 1;
			
			// 공격자는 공격에 영향받지 않는다 
			if(board[ny][nx] == BROKEN || (ny == attacker.y && nx == attacker.x))
				continue;
			involved[ny][nx] = true;
			board[ny][nx].power -= power / 2;
		}
		
		involved[attacker.y][attacker.x] = true;
		attacker.power = power;
		attacker.time = turn;
		
	}
	
	static void broke() {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=m; x++) {
				if(board[y][x] == BROKEN)
					continue;
				if(board[y][x].power <= 0)
					board[y][x] = BROKEN;
			}
		}
	}
	
	static void simulate() {
		init();
		if(turrets.size() == 1) { // 부서지지 않은 포탑이 1개라면 그 즉시 중지 
			System.out.println(target.power);
			System.exit(0);
		}
		
//		System.out.println("attacker: "+ attacker);
//		System.out.println("target: " + target);
		
		
		// 공격 
		attack();
		
		// 포탑 부서짐
		broke();
		
		// 포탑 정비 
		for(int y = 1; y<=n; y++) {
			for(int x=1; x<=m; x++) {
				if(board[y][x] == BROKEN || involved[y][x])
					continue;
				
				board[y][x].power++;
			}
		}
//		printBoard(board);
		
	}
	static int turn;
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        board = new Turret[n+1][m+1];
        involved = new boolean[n+1][m+1]; // 포탑이 공격에 연관되었는지를 판단
        for(int y=1; y<=n; y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x=1; x<=m; x++) {
        		int power = Integer.parseInt(st.nextToken());
        		if(power != 0)
        			board[y][x] = new Turret(power, 0, y,x);
        		else
        			board[y][x] = BROKEN;
        	}
        }
        
        for(turn=1;turn<=k;turn++) {
//        	System.out.println("-----");
//        	System.out.println("turn: "+turn);
        	simulate();
        	
        }
        
        init();
        System.out.println(target.power);
    }
}