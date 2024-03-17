import java.io.*;
import java.util.*;
public class Main {
	static int n,m,p,c,d;
	
	static StringTokenizer st;
	
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
	static Pair rudolf;
	static int board[][];
	static class Santa{
		int id, y,x,score,stun;
		public Santa(int id, int y, int x) {
			this.id = id;
			this.y = y;
			this.x = x;
		}
		
		public boolean isHigher(Santa s) {
			if(y != s.y)
				return y > s.y;
			return x > s.x;
		}
		
		public String toString() {
			return String.format("id: %d, y: %d, x: %d, score: %d, stun: %d", id,y,x,score, stun);
		}
	}
	static Santa santas[];
	static int INT_MAX = Integer.MAX_VALUE;
	static Santa NO_SANTA = new Santa(-1,-1,-1);
	
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static int getDistance(int y1,int x1, int y2, int x2) {
		return (y1-y2)*(y1-y2) + (x1-x2)*(x1-x2);
	}
	
	static int dy[] = {-1,0,1,0,-1,1,1,-1};
	static int dx[] = {0,1,0,-1,1,1,-1,-1};
	
	static int getNxtDir(Pair rudolf, Santa selected) {
		int y = rudolf.y;
		int x = rudolf.x;
		int minDistance = getDistance(y,x,selected.y, selected.x);
		int ret = -1;
		for(int dir = 0; dir<8;dir++) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			if(OOB(ny,nx))
				continue;
			int distance = getDistance(ny,nx, selected.y, selected.x);
			if(minDistance > distance) {
				minDistance = distance;
				ret = dir;
			}
			
		}
		return ret;
	}
	
	static void push(Santa santa, int dir, int dist) {
		
		int y = santa.y;
		int x = santa.x;
		board[y][x] = 0;
		int ny = y + dy[dir] * dist;
		int nx = x + dx[dir] * dist;
		if(OOB(ny,nx)) {
			santa.y = -1;
			santa.x = -1;
			return;
		}
			
		
		if(board[ny][nx] != 0) {
			int id = board[ny][nx];
//			System.out.println("next id: "+id);
			push(santas[id], dir, 1);
		}
		santa.y = ny;
		santa.x = nx;
		board[ny][nx] = santa.id;
	}
	
	static void rudolfMove() {
		
		// 1. 가장 가까운 격자 안의 산타 정하기 
		
		int minDistance = INT_MAX;
		Santa selected = NO_SANTA;
		
		for(int id = 1; id<=p; id++) {
			Santa santa = santas[id];
			if(OOB(santa.y, santa.x)) // 격자 밖의 산타는 선택하지 않는다. 
				continue;
			
			int distance = getDistance(rudolf.y, rudolf.x, santa.y, santa.x);
			
			if(minDistance > distance) {
				minDistance = distance;
				selected = santa;
			}else if(minDistance == distance && santa.isHigher(selected)) {
				selected = santa;
			}
		}
		if(selected == NO_SANTA)
			return;
		
		
		// 2. 가장 가까운 산타로 이동 
		int dir = getNxtDir(rudolf, selected);
		
		rudolf.y = rudolf.y + dy[dir];
		rudolf.x = rudolf.x + dx[dir];
//		System.out.println("selected: "+ selected);
//		System.out.println("dir: "+ dir);
		
		// 3. 충돌 
		
		if(board[rudolf.y][rudolf.x] != 0) {
			// 기존에 있던 산타는 c만큼의 점수를 얻게됨 
			int id = board[rudolf.y][rudolf.x];
			santas[id].score += c;
			push(santas[id], dir, c); // dir방향으로 c만큼 밀려남  
			
			// 기절 
			santas[id].stun = 2;
		}
		
	}
	
	static void printSantas() {
		for(int id = 1; id<=p; id++) {
			System.out.println(santas[id]);
		}
	}
	
	static void printBoard(int board[][]) {
		for(int y=1;y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.print(board[y][x]+" ");
			}
			System.out.println();
		}
	}
	
	static int getSantaNxtDir(int y,int x) {
		int ret = -1;
		
		// 움직일 수 있는 칸이 있더라도 루돌프로부터 멀어지면 움직이지 않는
		int minDistance = getDistance(rudolf.y, rudolf.x, y,x);
		
		
		for(int dir = 0; dir < 4; dir++) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			
			if(OOB(ny,nx) || board[ny][nx] != 0) // 다른 산타가 있는 칸이나 격자 밖으로는 움직이지 않는다.
				continue;
			int distance = getDistance(rudolf.y, rudolf.x , ny, nx);
			if(minDistance > distance) {
				minDistance = distance;
				ret = dir;
			}
		}
		
		
		
		return ret;
	}
	
	static void move(Santa santa) {
		int y = santa.y;
		int x = santa.x;
		
		// 방향 정하기 
		int dir = getSantaNxtDir(y,x);
//		System.out.println("Santa: "+santa);
//		System.out.println("nxtDir: "+dir);
		if(dir == -1) // 움직일수 있는 칸이 없다면 움직이지 않는다.
			return;
		
		board[y][x] = 0;
		// 이동
		santa.y = y + dy[dir];
		santa.x = x + dx[dir];
		// 루돌프와 충돌 
		if(santa.y == rudolf.y && santa.x == rudolf.x) {
			santa.score += d;
			push(santa, (dir+2)%4, d);
			santa.stun += 2;
			return;
		}
		board[santa.y][santa.x] = santa.id;
		
		
	}
	
	static void moveSantas() {
		for(int id = 1; id<=p;id++) {
			Santa santa = santas[id];
			if(OOB(santa.y, santa.x) || santa.stun > 0) // 탈락했거나, 기절한 산타는 움직일 수 없다.
				continue;
			move(santa);
		}
	}
	
	static void minusStun() {
		for(int id = 1; id<=p; id++) {
			Santa santa = santas[id];
			if(OOB(santa.y, santa.x))
				continue;
			if(santa.stun > 0)
				santa.stun--;
		}
	}
	
	static void plusScore() {
		for(int id = 1; id<=p; id++) {
			Santa santa = santas[id];
			if(OOB(santa.y, santa.x))
				continue;
			
			santa.score += 1;
		}
	}
	
	static boolean allDead() {
		for(int id = 1; id<=p;id++) {
			Santa santa = santas[id];
			if(!OOB(santa.y, santa.x))
				return false;
		}
		return true;
	}
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		p = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		d = Integer.parseInt(st.nextToken());
		
		st = new StringTokenizer(br.readLine());
		int ry = Integer.parseInt(st.nextToken());
		int rx = Integer.parseInt(st.nextToken());
		rudolf = new Pair(ry,rx);
		
		board = new int[n+1][n+1];
		santas = new Santa[p+1];
		
		for(int i = 0; i<p;i++) {
			st = new StringTokenizer(br.readLine());
			int id = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			board[y][x] = id;
			santas[id] = new Santa(id, y,x);
		}
		
//		System.out.println("rudolf: "+rudolf);
//		printSantas();
//		printBoard(board);
//		System.out.println("-----");
		
		
		for(int turn = 1; turn <= m;turn++) {
			
			//모든 산타 탈락 여부 확인 
			if(allDead())
				break;
			
			// 루돌프의 이동 
			rudolfMove();
			// 산타들의 이동
			
			moveSantas();
			
			// 기절턴진행 
			minusStun();
			
			
			// 점수 계산 
			plusScore();
			
			
//			System.out.println("turn: "+(turn));
//			System.out.println("rudolf: "+rudolf);
//			printSantas();
//			printBoard(board);
//			System.out.println("-----");
			
		}
		
		for(int id =1; id<=p;id++) {
			System.out.print(santas[id].score +" ");
		}
	}
	
}