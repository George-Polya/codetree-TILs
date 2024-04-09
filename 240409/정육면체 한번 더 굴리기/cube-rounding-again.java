import java.io.*;
import java.util.*;

public class Main{
	static int n,m;
	static StringTokenizer st;
	static int board[][];
	
	static int dy[] = {0,1,0,-1};
	static int dx[] = {1,0,-1,0};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
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
	
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
	}
	
	static class Dice{
		int y,x,moveDir;
		int up = 1;
		int down = 6;
		int front = 2;
		int back = 5;
		int right = 3;
		int left = 4;
		
		boolean visited[][];
		
		
		public Dice(int y,int x,int dir) {
			this.y = y;
			this.x = x;
			this.moveDir = dir;
			visited = new boolean[n+1][n+1];
		}
		
		void init() {
			for(int y=1; y<=n;y++) {
				Arrays.fill(visited[y], false);
			}
		}
		
		public Tuple getNxtPos() {
			int ny = y + dy[moveDir];
			int nx = x + dx[moveDir];
			
			if(OOB(ny,nx)) {
				moveDir = (moveDir +2 )% 4;
				ny = y + dy[moveDir];
				nx = x + dx[moveDir];
			}
			
			return new Tuple(ny,nx,moveDir);
			
		}
		
		public void move() {
			/*
			 * 1. 주사위의 이동 
			 *  1.1 주사위의 다음 위치와 방향 구하기 (OOB인 경우, 방향도 바뀜)
			 *  1.2 주사위의 위치와 방향 업데이트 
			 *  1.3 주사위 굴리기 
			 */
			Tuple nxt = getNxtPos(); // 다음 위치와 방향 구하기 
//			System.out.println("nxt: "+nxt);
			// 주사위 위치와 방향 업데이트 
			this.y = nxt.y;
			this.x = nxt.x;
			this.moveDir = nxt.dir;
			roll(); // 주사위 굴리기 
			
		}
		
		
		
		public void roll() {
			/*
			 * 주사위 굴리기 
			 * 현재 방향에 따라서 면이 달라짐 
			 */
			int temp = up;
			if(moveDir == 0) {
				up = left;
				left = down;
				down = right;
				right = temp;
			}else if(moveDir == 1) {
				up = back;
				back = down;
				down = front;
				front = temp;
			}else if(moveDir == 2) {
				up = right;
				right = down;
				down = left;
				left = temp;
			}else {
				up = front;
				front = down;
				down = back;
				back = temp;
			}
		}
		
		public void print() {
			System.out.printf("up: %d down: %d left: %d right: %d front: %d back: %d\n", up,down,left,right,front,back);
		}
		
		public int calcScore() {
			int ret= 0;
			init();
			
			int value = board[y][x];
			Queue<Pair> q = new LinkedList<>();
			q.add(new Pair(y,x));
			visited[y][x] = true;
			
			while(!q.isEmpty()) {
				Pair cur = q.poll();
//				ret += value;
				ret += board[cur.y][cur.x];
				
				// 상하좌우 인접하며 같은 숫자가 적혀 있는 모든 칸의 합만큼 점수 획득 
				for(int dir = 0; dir<4;dir++) {
					int ny = cur.y + dy[dir];
					int nx = cur.x + dx[dir];
					
					if(OOB(ny,nx) || visited[ny][nx] || board[ny][nx] != value)
						continue;
					
					visited[ny][nx] = true;
					q.add(new Pair(ny,nx));
				}
			}
			
			return ret;
		}
		
		
		public int getNxtMoveDir() {
			if(down > board[y][x]) {
				// 아랫면이 보드 칸보다 크면 
				// 90도 시계방향 회전 
				return (moveDir + 1) % 4;
			}else if(down < board[y][x]) {
				// 아랫면이 보드 칸보다 작으면
				// 90도 반시계 방향 회전 
				return (moveDir + 3) % 4;
			}else {
				// 아랫면과 보드 칸이 같으면
				// 동일한 방향 
				return moveDir;
			}
		}
	}
	
	static Dice dice;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		board = new int[n+1][n+1];
		for(int y=1; y<=n;y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		int sum = 0;
		dice = new Dice(1,1,0);
//		dice.move();
//		score += dice.calcScore();
//		System.out.println(score);
//		dice.moveDir = dice.getNxtMoveDir();
//		System.out.println(dice.moveDir);
		
		for(int turn=1; turn<=m; turn++) {
//			System.out.println("------");
//			System.out.println("turn: " + turn);
			
			dice.move();
			int score = dice.calcScore();
//			dice.print();
//			System.out.println("score: "+score);
			sum += score;
			dice.moveDir = dice.getNxtMoveDir();
//			System.out.println("moveDir: "+dice.moveDir);
			
		}
		System.out.println(sum);
		
	}
}