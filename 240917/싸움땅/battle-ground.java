import java.io.*;
import java.util.*;

public class Main {
	static int n,m,k;
	static PriorityQueue<Integer> board[][];
	static int idBoard[][];
	static StringTokenizer st;
	
	static int dy[] = {-1,0,1,0};
	static int dx[] = {0,1,0,-1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	static int scores[];
	
	static void printPlayers() {
		for(int id=1;id<=m;id++)
			System.out.println(players[id]);
	}
	
	static class Player{
		int y,x,dir,s, gun, id;
		public Player(int y,int x, int dir, int s, int id) {
			this.y = y;
			this.x = x;
			this.dir = dir;
			this.s = s;
			this.id = id;
		}
		
		public String toString() {
			return String.format("id: %d | (%d, %d) | %d + %d = %d | dir: %d", id,y,x, s,gun, s+gun,dir);
		}
		
		private Tuple getNxtPos() {
			int dir = this.dir;
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			if(OOB(ny,nx)) {
				dir = (dir + 2) % 4;
				ny = y + dy[dir];
				nx = x + dx[dir];
			}
			return new Tuple(ny,nx,dir);
		}
		
		private boolean isStronger(Player o) {
			if((s + gun) != (o.s + o.gun))
				return (s + gun) > (o.s + o.gun);
			return s > o.s;
		}
		

		/*
		 * 패자는 총을 내려놓고 이동해야함 
		 * 패자의 idBoard 업데이트 먼저 해야함 
		 */
		private void loserMove() {
			idBoard[y][x] = 0; // idBoard 업데이트
			board[y][x].add(gun); // 총을 내려놓음 
			gun = 0; // 총을 내려놓았으니 0이 됨 
			
			for(int i = 0; i < 4; i++) {
				int ndir = (dir + i) % 4;
				int ny = y + dy[ndir];
				int nx = x + dx[ndir];
				
				if(OOB(ny,nx) || idBoard[ny][nx] != 0) // 다음 위치가 격자밖이거나 다른 플레이어가 있으면 
					continue; // 오른쪽으로 90도 회전
				// 가능한 순간 이동하고 반복문 break 
				y = ny;
				x = nx;
				dir = ndir;
				break;
			}
			
			idBoard[y][x] = id;
			// 가장 쎈 총을 가져감 
//			board[y][x].add(gun);
			if(!board[y][x].isEmpty())
				gun = board[y][x].poll();
		}
		
		public void move() {
			// 다음 칸 (방향이 바뀔 수 있음)
			Tuple nxt = getNxtPos();
			idBoard[y][x] = 0; // 이전 위치에서 id 제거 
			
			y = nxt.y;
			x = nxt.x; 
			dir = nxt.dir;
			
			if(idBoard[nxt.y][nxt.x] == 0) { // 다음 칸에 다른 플레이어가 없음
				// 이동 
				idBoard[y][x] = id;
				board[y][x].add(gun); // 자신의 총을 내려놓고(총이 없으면 0을 내려놓음)  
				gun = board[y][x].poll(); // 가장 쎈 총 가져감 
			}else { // 다음 칸에 다른 플레이어가 있음 
				// 싸워야함 

				Player other = players[idBoard[y][x]]; // 다른 플레이어 
				if(this.isStronger(other)) { // 이긴 경우 
					
//					System.out.println("winner: "+this.toString());
//					System.out.println("loser: "+other.toString());
					int score = calc(this, other);
//					System.out.println("score: "+score);
					scores[id] += score;
					
					
					// 패자 
					other.loserMove();
					
					// 승자 
					idBoard[y][x] = id;
					
					// 기존 총을 내려놓고 가장 쎈 총을 가져감 
					board[y][x].add(gun);
					gun = board[y][x].poll();
					
				}else { // 진 경우
					
//					System.out.println("winner: "+other.toString());
//					System.out.println("loser: "+this.toString());
					// 점수 계산 
					int score = calc(this, other);
//					System.out.println("score: "+score);
					scores[other.id] += score;
					
					
					// 패자 
					this.loserMove();
					
					// 승자 
					idBoard[other.y][other.x] = other.id;
					
					board[other.y][other.x].add(other.gun);
					other.gun = board[other.y][other.x].poll();
					
				}
				
			}
		}
	}
	
	static int calc(Player p1, Player p2) {
		return Math.abs( (p1.s + p1.gun) - (p2.s + p2.gun) );
	}
	
	
	static class Tuple{
		int y,x,dir;
		public Tuple(int y,int x, int dir) {
			this.y = y;
			this.x = x;
			this.dir = dir;
		}
		
		public String toString() {
			return y+" "+x+" "+dir;
		}
	}
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++)
				System.out.printf("%3d", board[y][x]);
			System.out.println();
		}
	}
	static Player players[];
	
	static void moveAll() {
		for(int id=1; id<=m;id++) { // 첫번째 플레이어부터 이동 
//			System.out.println("=====");
//			System.out.println("id: "+id);
			players[id].move();
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());

		board = new PriorityQueue[n+1][n+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				if(board[y][x] == null)
					board[y][x] = new PriorityQueue<>(Collections.reverseOrder());
				board[y][x].add(Integer.parseInt(st.nextToken()));
			}
		}
		players = new Player[m+1];
		idBoard = new int[n+1][n+1];
		scores = new int[m+1];
		for(int id = 1; id<=m; id++) {
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			players[id] = new Player(y,x,dir,s, id);
			idBoard[y][x] = id;
		}
		
		
		for(int turn = 1; turn<=k; turn++) {
//			System.out.println("-----");
//			System.out.println("turn: "+turn);
			
			moveAll();
//			System.out.println("idBoard");
//			printBoard(idBoard);
//			System.out.println("players");
//			printPlayers();
		}
		
		StringBuilder sb = new StringBuilder();
		for(int id = 1; id<=m; id++) {
			sb.append(scores[id]).append(' ');
		}
		System.out.println(sb);
	}
}