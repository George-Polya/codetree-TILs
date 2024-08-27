import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		l = Integer.parseInt(st.nextToken());
		r = Integer.parseInt(st.nextToken());
		board = new int[n+1][n+1];
		nxtBoard = new int[n+1][n+1];
		
		
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		int turn = 0;
		visited = new boolean[n+1][n+1];
		
		while(true) {
//			System.out.println("------");
//			System.out.println("turn: "+ turn);
			boolean moved = move();
//			printBoard(board);
			if(!moved)
				break;
			turn++;
		}
		System.out.println(turn);
	}
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	static int n,l,r;
	static StringTokenizer st;
	static int board[][], nxtBoard[][];
	static boolean visited[][];
	static void init() {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				visited[y][x] = false;
				nxtBoard[y][x] = board[y][x];
			}
		}
	}
	static boolean move() {
		init();
		boolean flag = false;
		
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				if(visited[y][x])
					continue;
				if(bfs(y,x))
					flag = true;
			}
		}
		
		if(flag) {
			copy(nxtBoard, board);
		}
		return flag;
	}
	
	static boolean bfs(int y,int x) {
		Queue<Pair> q = new LinkedList<>();
		visited[y][x] = true;
		q.add(new Pair(y,x));
		boolean flag = false;
		ArrayList<Pair> union = new ArrayList<>();
		int sum = 0;
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			sum += board[cur.y][cur.x];
			union.add(cur);
			
			for(int dir = 0; dir<4 ;dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				if(OOB(ny,nx) || visited[ny][nx])
					continue;
				int diff = Math.abs(board[ny][nx] - board[cur.y][cur.x]);
				if(l<=diff && diff <= r) {
					flag = true;
					q.add(new Pair(ny,nx));
					visited[ny][nx] = true;
				}
			}
		}
		
		if(flag) {
			int size = union.size();
			for(Pair pair : union) {
				nxtBoard[pair.y][pair.x] = sum / size;
			}
		}
		return flag;
	}
	
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
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
	static void copy(int src[][], int dst[][]) {
		for(int y=1; y<=n; y++) {
			System.arraycopy(src[y],1,dst[y],1,n);
		}
	}
}