import java.io.*;
import java.util.*;

public class Main{
	static int n,m,k;
	static class Player{
		int id,y,x;
		int power, dir, gun;
		public Player(int id, int y, int x, int dir, int power, int gun) {
			this.id = id;
			this.y = y;
			this.x = x;
			this.dir = dir;
			this.power = power;
			this.gun = gun;
		}
		
		public String toString() {
			return String.format("id: %d, y: %d, x: %d, dir: %d, power: %d, gun: %d", id,y,x,dir,power, gun);
		}
	}
	static PriorityQueue<Integer> board[][];
	static StringTokenizer st;
	static int dy[] = {-1,0,1,0};
	static int dx[] = {0,1,0,-1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static Player players[];
	static int playerBoard[][];
	
	static void printBoard(int board[][]) {
		for(int y=1;y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.print(board[y][x]+" ");
			}
			System.out.println();
		}
			
	}
	static class Tuple{
		int y,x,dir;
		public Tuple(int y,int x, int dir) {
			this.y = y;
			this.x = x;
			this.dir = dir;
		}
	}
	
	static Tuple getNxt(int y,int x, int dir) {
		int ny = y + dy[dir];
		int nx = x + dx[dir];
		if(OOB(ny,nx)) {
			dir = (dir + 2) % 4;
			ny = y + dy[dir];
			nx = x + dx[dir];
		}
		return new Tuple(ny,nx,dir);
	}
	
	
	static void loserMove(Player loser) {
		int y = loser.y;
		int x = loser.x;
		int dir = loser.dir;
		int power = loser.power;
		int gun = loser.gun;
		int id = loser.id;

		
		// 총 내려놓기 
		board[y][x].add(gun);
		gun = 0;
		
		for(int i =0; i<4;i++) {
			int moveDir =(dir +i) % 4;
			int ny = y + dy[moveDir];
			int nx = x + dx[moveDir];
			if(!(OOB(ny,nx)||playerBoard[ny][nx]!=0)) {
				board[ny][nx].add(gun);
				gun = board[ny][nx].poll();
				
				loser = new Player(id,ny,nx,moveDir,power,gun);
				playerBoard[ny][nx] = id;
				players[id] = loser;
				break;
			}
		}
		
	}
	
	
	
	static void fight(Player player1, Player player2) {
		
		int y = player1.y;
		int x = player1.x;
		
		int power1 = player1.power;
		int gun1 = player1.gun;
		int sum1 = power1 + gun1;
		
		int power2 = player2.power;
		int gun2 = player2.gun;
		int sum2 = power2 + gun2;
		
		if(sum1 > sum2 || (sum1 == sum2) && (power1 > power2)) {
//			System.out.println("winner: "+player1);
//			System.out.println("loser: "+player2);
			
			playerBoard[y][x] = player1.id;
			scores[player1.id] += sum1 - sum2;
			
			loserMove(player2);
			
			board[y][x].add(gun1);
			player1.gun = board[y][x].poll();
			
		}else {
//			System.out.println("winner: "+player2);
//			System.out.println("loser: "+player1);
			playerBoard[y][x] = player2.id;
			scores[player2.id] += sum2 - sum1;
			loserMove(player1);
			
			board[y][x].add(gun2);
			player2.gun = board[y][x].poll();
		}
	}
	
	
	/*
	 * id에 해당하는 플레이어의 이동
	 * 
	 *  
	 * 1. 이동하려는 칸이 OOB인지 확인 
	 *  1.1 OOB면은 플레이어의 방향을 반대로 바꾸고
	 *  1.2 바뀐 방향으로 이동 
	 * 2. 이동하려는 칸에 플레이어가 있는지 확인
	 *  2.1 플레이어가 없다면 총 획득 프로세스
	 *  2.2 플레이어가 있다면   
	 */
	static void forward(int id) {
		Player cur = players[id];
		int y = cur.y;
		int x = cur.x;
		int dir = cur.dir;
		int power = cur.power;
		int gun = cur.gun;
		playerBoard[y][x] = 0;
//		System.out.println("cur: "+cur);
		
		// 다음 위치 
		Tuple nxt = getNxt(y,x,dir);
		y = nxt.y;
		x = nxt.x;
		dir = nxt.dir;
		
		players[id] = new Player(id,y,x,dir,power,gun);
		cur = players[id];
		
		
		if(playerBoard[y][x] != 0) {
			int prevIdx = playerBoard[y][x];
			Player prev = players[prevIdx];
			
			fight(cur, prev);
		}else {
			playerBoard[y][x] = id;
			
				
			board[y][x].add(cur.gun);
			cur.gun = board[y][x].poll();
		}
		
		
		
	}
	
	/*
	 * 플레이어들의 이동 
	 * 첫번째 플레이어부터 본인이 향하고 있는 방향대로 한 칸 이동 
	 * 
	 * 
	 */
	static void simulate() {
		for(int id = 1; id<=m; id++) {
			forward(id);
		}
	}
	
	static int scores[];
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		board = new PriorityQueue[n+1][n+1];
		
		
		for(int y=1;y<=n;y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1;x<=n; x++) {
				if(board[y][x] == null)
					board[y][x] = new PriorityQueue<>(Collections.reverseOrder());
				int value = Integer.parseInt(st.nextToken());
				board[y][x].add(value);
			}
		}
		
		players = new Player[m+1];
		playerBoard = new int[n+1][n+1];
		scores = new int[m+1];
		for(int id = 1; id<=m; id++) {
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			int power = Integer.parseInt(st.nextToken());
			
			players[id] = new Player(id,y,x,dir,power, 0);
			playerBoard[y][x] = id;
		}
		
		// 1. 플레이어들의 이동
		for(int turn = 1; turn <=k ;turn++) {
//			System.out.println("before");
//			for(int id=1;id<=m;id++) {
//				System.out.println(players[id]);
//			}
			simulate();
			
//			System.out.println("after");
//			for(int id=1;id<=m;id++) {
//				System.out.println(players[id]);
//			}
//			printBoard(playerBoard);
//			
//			System.out.println("------");
		}
		
		for(int id=1; id<=m;id++) {
			System.out.print(scores[id]+" ");
		}
		
	}
}