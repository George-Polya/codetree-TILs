import java.io.*;
import java.util.*;

public class Main {
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
	
	static int dy[] = {0,-1,0,1};
	static int dx[] = {1,0,-1,0};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static class Team{
		Pair head, tail;
		int id;
		
		public Team() {
			
		}
		
		public String toString() {
			return String.format("head: %s, tail: %s", head, tail);
		}
		
		public void init() {
			this.dfs(head.y, head.x, id);
		}
		
		private void dfs(int y,int x, int id) {
			idBoard[y][x] = id;
			
			if(board[y][x] == 3){
				tail = new Pair(y,x);
			}
			
			for(int dir = 0;dir < 4; dir++) {
				int ny = y + dy[dir];
				int nx = x + dx[dir];
				if(OOB(ny,nx) || idBoard[ny][nx] != 0 || board[ny][nx] == 0)
					continue;
				dfs(ny,nx, id);
			}
		}
		
		/*
		 * 팀의 이동 
		 * 꼬리까지 진행 후, 현재 위치에 다음위치의 값 저장 
		 * 즉, 꼬리까지 갔으면 꼬리 이전위치에 꼬리의 값을 저장
		 * 
		 */
		public void move() {
			Pair prev = null;
			
			for(int dir = 0; dir < 4; dir++) {
				int ny = head.y + dy[dir];
				int nx = head.x + dx[dir];
				if(OOB(ny,nx) || board[ny][nx] == 0)
					continue;
				int nxt = board[ny][nx];
				if(nxt == 3 || nxt == 4) {
					prev = new Pair(ny,nx);
					break;
				}
			}
//			System.out.printf("head: %s, prev: %s\n", head, prev);
			// 꼬리까지 진행 
			this.dfs(head.y, head.x, prev.y, prev.x,1);
		}
		
		private void dfs(int y, int x, int py, int px, int depth) {
			int cur = board[y][x];
			board[y][x] = 4; // 현재 위치는 4가 됨 -> 꼬리까지 왔으면 마지막이 4가 됨.
			visited[y][x] = depth;
			for(int dir = 0; dir<4;dir++) {
				int ny = y + dy[dir];
				int nx = x + dx[dir];
				if(OOB(ny,nx) || visited[ny][nx] != 0)
					continue;
				int nxt = board[ny][nx];
				
				if((cur ==1 && nxt == 2) || (cur == 2 && (nxt == 2 || nxt == 3))) {
					dfs(ny,nx,y,x,depth+1);
				}
			}
			
			// 이전 위치에 현재 값이 저장(실제 이동)
			board[py][px] = cur;
			visited[py][px] = depth; // 머리에서 얼마나 떨어져있는지 확인 
			if(cur == 3) {
				visited[y][x] = 0;
				tail = new Pair(py,px);
			}
			if(cur == 1)
				head = new Pair(py,px);
		}
	}
	
	static int visited[][];
	
	static Team teams[];
	static int n,m,k;
	static StringTokenizer st;
	static int board[][];
	static int idBoard[][];
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	static void moveAll() {
		visited = new int[n+1][n+1];
		for(int i =1 ;i<=m; i++) {
			teams[i].move();
//			System.out.printf("teams[%d]: %s\n", i, teams[i]);
		}
	}
	
	static class Ball{
		int y,x,dir;
		
		public Ball(int y,int x,int dir) {
			this.y = y;
			this.x = x;
			this.dir = dir;
		}
		public String toString() {
			return y+" "+x+" "+dir;
		}
		
		public void move() {
			int ny = this.y;
			int nx = this.x;
			for(int i = 0; i < n;i++) {
				ny = ny + dy[dir];
				nx = nx + dx[dir];
//				System.out.printf("%d, %d\n", ny,nx);
				int order = visited[ny][nx]; // 공이 이동하다가 사람을 만남. 
				if(order != 0) { // order != 0 -> 머리에서 얼마나 떨어져있는지에 대한 값 
					int id = idBoard[ny][nx];
					int score = order * order;
//					System.out.printf("id: %d, score: %d\n", id, score);
					total += score;
					
					Pair head = teams[id].head;
					Pair tail = teams[id].tail;
					
					// 머리와 꼬리 위치 바뀜 
					board[head.y][head.x] = 3;
					board[tail.y][tail.x] = 1;
					teams[id].head = tail;
					teams[id].tail = head;
					
					break;
				}
			}
		}
	}
	
	static Ball findPitchPos(int turn) {
		int mod = (turn % n ) == 0 ? n : turn % n;
		int dir = (turn % n ) == 0 ? (turn / n ) % 4 - 1 : (turn / n) % 4;
		if(dir == 0)
			return new Ball(mod,0,dir);
		else if(dir == 1)
			return new Ball(n+1, mod, dir);
		else if(dir == 2)
			return new Ball(n+1-mod, n+1, dir);
		return new Ball(0, n+1-mod, dir);
	}
	
	static void pitch(int turn) {
		Ball ball = findPitchPos(turn);
		
//		System.out.println("ball: "+ball);
		ball.move();
	}
	
	static int total;
	
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        
        board = new int[n+1][n+1];
        idBoard = new int[n+1][n+1];
        teams = new Team[m+1];
        for(int i =1 ;i<=m; i++) {
        	teams[i] = new Team();
        }
        int idx = 1;
        for(int y=1; y<=n; y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x=1; x<=n; x++) {
        		board[y][x] = Integer.parseInt(st.nextToken());
        		if(board[y][x] == 1) {
        			teams[idx].head = new Pair(y,x);
        			teams[idx].id = idx;
        			idx++;
        		}
        	}
        }
        
        
        // 팀 초기화와 각 팀의 이동경로 설정 
        for(int i = 1; i<=m; i++) {
        	teams[i].init();
        }
        
//        printBoard(idBoard);
        
        for(int turn = 1; turn<=k; turn++) {
//        	System.out.println("-----");
//        	System.out.println("turn: "+turn);
//        	System.out.println("teams");
//        	
        	
        	// 팀의 이동 
        	moveAll();
//        	System.out.println("after move");
//        	printBoard(board);
//        	System.out.println(Arrays.toString(teams));
//        	System.out.println("visited");
//        	printBoard(visited);
        	
        	pitch(turn);
//        	System.out.println("after pitch");
//        	printBoard(board);
//        	System.out.println(Arrays.toString(teams));
        	
        }
        System.out.println(total);
   }
}