import java.io.*;
import java.util.*;

public class Main{
	static int n,m;
	static StringTokenizer st;
	static int board[][], nxtBoard[][];
	static int BLACK = -1;
	static int RED = 0;
	static int BLANK = -2;
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public String toString() {
			return y+" "+x;
		}
		
		public boolean isHigher(Pair p) {
			if(y != p.y)
				return y > p.y;
			return x < p.x;
		}
	}
	
	static Pair NO_PAIR = new Pair(-1, 22);
	
	static class Group implements Comparable<Group>{
		int y,x,size, redCnt;
		List<Pair> bombs;
		
		public Group(int y,int x, int redCnt, List<Pair> bombs) {
			this.y = y;
			this.x = x;
			this.redCnt= redCnt;
			this.bombs = bombs;
			this.size = bombs.size();
		}
		
		public int compareTo(Group g) {
			if(size != g.size)
				return g.size - size;
			if(redCnt != g.redCnt)
				return redCnt - g.redCnt;
			if(y != g.y)
				return g.y - y;
			return x - g.x;
		}
		
		public String toString() {
			return y+" "+x+" "+redCnt+" "+bombs;
		}
		
	}
	
	static List<Group> groups = new ArrayList<>();
	static boolean visited[][];
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%3d",  board[y][x]);
			}
			System.out.println();
		}
	}
	
	
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n|| x<=0 || x>n;
	}
	
	static void init() {
		groups.clear();
		for(int y=1; y<=n; y++) {
			Arrays.fill(visited[y], false);
		}
		
	}
	
	/*
	 * 모든 색깔 묶음생성 
	 * 1. 기준점 : std, bombs, redCnt
	 * 2. 메소드가 종료될때마다 red위치의 visited는 false로 복원 
	 */
	
	static void bfs(int y,int x) {
		int value = board[y][x];
		Queue<Pair> q = new LinkedList<>();
		q.add(new Pair(y,x));
		visited[y][x] = true;
		Pair std = NO_PAIR;
		List<Pair> reds = new ArrayList<>();
		List<Pair> bombs = new ArrayList<>();
		
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			
			// 기준점 
			if(board[cur.y][cur.x] != RED && cur.isHigher(std)) {
				std = cur;
			}
			
			
			// red 개수 
			if(board[cur.y][cur.x] == RED) {
				reds.add(cur);
			}
			
			bombs.add(cur);
			
			
			for(int dir = 0; dir< 4; dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				
				if(OOB(ny,nx)|| board[ny][nx] == BLACK || visited[ny][nx] || board[ny][nx] == BLANK)
					continue;
				
				// 모두 같은 색깔 혹은 RED포함해서 오직 두색깔 
				if(board[ny][nx] == value || board[ny][nx] == RED) {
					q.add(new Pair(ny,nx));
					visited[ny][nx] = true;
				}
			}
		}
		
		int redCnt = reds.size();
		for(Pair red : reds) {
			visited[red.y][red.x] = false;
		}
		if(bombs.size() >= 2) {
			Group group = new Group(std.y, std.x, redCnt, bombs);
			
			groups.add(group);
		}
		
	}
	
	static boolean end() {
		init();
		
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				
				// RED만으로 이루어져서는 안됨 
				if(board[y][x] == RED || board[y][x] == BLACK ||visited[y][x] || board[y][x] == BLANK)
					continue;
				
				bfs(y,x);
			}
		}
		
		Collections.sort(groups);
		return groups.isEmpty();
	}
	static int score;
	static void explode(Group best) {
		List<Pair> bombs = best.bombs;
		int size = best.size;
		score += size * size;
		for(Pair p : bombs) {
			int value = board[p.y][p.x];
			board[p.y][p.x] = BLANK;
		}
	}
	
	static void copy(int src[][], int dst[][]) {
		for(int y=1; y<=n; y++) {
			System.arraycopy(src[y],1, dst[y],1,n);
		}
	}
	
	static void fall() {
		for(int y=1; y<=n; y++) {
			Arrays.fill(nxtBoard[y], BLANK);
		}
		for(int x = 1; x<=n; x++) {
			int idx = n;
			for(int y= n; y>=1; y--) {
				if(board[y][x] == BLANK)
					continue;
				if(board[y][x] == BLACK)
					idx = y;
				nxtBoard[idx--][x] = board[y][x];
			}
		}
		
		copy(nxtBoard,board);
	}
	
	static void rotate() {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				nxtBoard[n + 1 - x][y] = board[y][x];
			}
		}
		copy(nxtBoard,board);
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		board = new int[n+1][n+1];
		nxtBoard = new int[n+1][n+1];
		visited = new boolean[n+1][n+1];
		
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		while(!end()) {
//			System.out.println("turn: "+turn);
//			end();
			Group best = groups.get(0);
//			System.out.println("best: "+best);
			explode(best);
//			System.out.println("after exploded");
//			printBoard(board);
			
			fall();
//			System.out.println("after fall");
//			printBoard(board);
			
			rotate();
//			System.out.println("after rotate");
//			printBoard(board);
			
			fall();
//			System.out.println("after fall");
//			printBoard(board);
//			System.out.println("score: "+score);
//			System.out.println("-----");
		}
		System.out.println(score);
	}
}