import java.io.*;
import java.util.*;
public class Main {
	static final int BLACK = -1;
	static final int RED = 0;
	static final int EMPTY = -2;
	static int n,m;
	static int board[][];
	static int nxtBoard[][];
	static StringTokenizer st;
	
	static class Group{
		int size, redCnt, y, x;
		public Group(int size, int redCnt, int y,int x) {
			this.size = size;
			this.redCnt = redCnt;
			this.y = y;
			this.x = x;
		}
		
		public boolean isHigher(Group g) {
			if(size != g.size)
				return size > g.size;
			if(redCnt != g.redCnt)
				return redCnt < g.redCnt;
			
			if(y != g.y)
				return y > g.y;
			return x < g.x;
		}
		
		public String toString() {
			return y+" "+x+"|";
		}
	}
	
	static class Pair{
		int first,second;
		public Pair(int first,int second) {
			this.first = first;
			this.second = second;
		}
		public String toString() {
			return first+" "+second+"|";
		}
	}
	
	static int INT_MAX = Integer.MAX_VALUE;
	static boolean visited[][];
	static void initialize() {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++)
				visited[y][x] = false;
		}
	}
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	static Pair NO_PAIR = new Pair(0,INT_MAX);
	static Pair bfs(int y,int x,int color) {
		int cnt = 1;
		int size = 0;
		int redCnt = 0;
		Queue<Pair> q = new ArrayDeque<>();
		q.add(new Pair(y,x));
		visited[y][x] = true;
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			size++;
			if(board[cur.first][cur.second] == RED) {
				cnt++;
				redCnt++;
			}
			
			for(int dir = 0; dir< 4; dir++) {
				int ny = cur.first + dy[dir];
				int nx = cur.second + dx[dir];
				if(OOB(ny,nx) || visited[ny][nx] || board[ny][nx] == EMPTY)
					continue;
				if(board[ny][nx] == color || board[ny][nx] == RED) {
					visited[ny][nx] = true;
					q.add(new Pair(ny,nx));
				}
			}
		}
		
		if(size <= 1)
			return NO_PAIR;
		
		return new Pair(size, redCnt);
	}
	
	static Group NO_GROUP = new Group(0,INT_MAX, 0, INT_MAX);
	static Group findBestGroup() {
		Group best = NO_GROUP;
		for(int y=1; y<=n ;y++) {
			for(int x=1; x<=n; x++) {
				int color = board[y][x];
				if(color == RED || color  == BLACK || color == EMPTY)
					continue;
				initialize();
				Pair pair = bfs(y,x, color);
//				System.out.println(pair);
				if(pair == NO_PAIR )
					continue;
				Group cur = new Group(pair.first, pair.second, y, x);
				if(cur.isHigher(best)) {
					best = cur;
				}
			}
		}
		return best;
	}
	
	static void explode(Group g) {
		initialize();
		Queue<Pair> q = new ArrayDeque<>();
		q.add(new Pair(g.y, g.x));
		int color = board[g.y][g.x];
		visited[g.y][g.x] = true;
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			
			if(board[cur.first][cur.second] == color || board[cur.first][cur.second] == RED)
				board[cur.first][cur.second] = EMPTY;
			for(int dir = 0; dir<4;dir++) {
				int ny = cur.first + dy[dir];
				int nx = cur.second + dx[dir];
				if(OOB(ny,nx) || visited[ny][nx] || board[ny][nx] == EMPTY)
					continue;
				if(board[ny][nx] == color || board[ny][nx] == RED) {
					visited[ny][nx] = true;
					q.add(new Pair(ny,nx));
				}
			}
		}
	}
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.print(board[y][x] +" ");
			}
			System.out.println();
			
		}
	}
	
	static void drop() {
		nxtBoard = new int[n+1][n+1];
		for(int y=1; y<=n; y++) {
			Arrays.fill(nxtBoard[y], -2);
		}
		for(int x=1; x<=n; x++) {
			int idx = n;
			
			for(int y=n; y>= 1; y--) {
				if(board[y][x] == EMPTY)
					continue;
				if(board[y][x] == BLACK)
					idx = y;
				nxtBoard[idx][x] = board[y][x];
				idx--;
			}
		}
		board = nxtBoard;
	}
	
	static void rotate() {
		nxtBoard = new int[n+1][n+1];
		
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++)
				nxtBoard[n+1-x][y] = board[y][x];
		}
		board = nxtBoard;
	}
	
	
	
	public static void main(String[] args) throws IOException{
//		System.setIn(new FileInputStream("./input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		board = new int[n+1][n+1];
		nxtBoard = new int[n+1][n+1];
		visited = new boolean[n+1][n+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++)
				board[y][x] = Integer.parseInt(st.nextToken());
		}
		int ans = 0;
		while(true) {
			Group best = findBestGroup();
//			System.out.println(best);
			if(best == NO_GROUP)
				break;
			explode(best);
			drop();
			rotate();
			drop();
			ans += (best.size) * (best.size);
//			printBoard(board);
//			System.out.println("----");
		}
		System.out.println(ans);
		
		
		
	}
}