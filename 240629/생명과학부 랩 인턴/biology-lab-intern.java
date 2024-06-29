import java.util.*;
import java.io.*;

public class Main {
	static int n,m, k;
	static StringTokenizer st;
	static class Mold{
		int speed, dir, size;
		public Mold(int speed, int dir, int size) {
			this.speed = speed;
			this.dir = dir;
			this.size = size;
		}
		
		public boolean isGreater(Mold m) {
			return size > m.size;
		}
		
		public String toString() {
			return String.format("speed: %d dir: %d size: %d", speed, dir, size);
		}
		
	}
	
	static ArrayList<Mold> board[][], nxtBoard[][];
	
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,1,-1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>m;
	}
	
	static void move(int y,int x) {
		Mold mold = board[y][x].get(0);
		int speed = mold.speed;
		int dir = mold.dir;
		int size = mold.size;
		
		for(int i = 0; i<speed;i++) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			
			if(OOB(ny,nx)) {
				dir = (dir % 2 == 0) ? dir + 1 : dir - 1;
				ny = y + dy[dir];
				nx = x + dx[dir];
			}
			y = ny;
			x = nx;
		}
		nxtBoard[y][x].add(new Mold(speed, dir, size));
	}
	
	static void initialize() {
		for(int  y=1; y<=n; y++) {
			for(int x=1; x<=m; x++)
				nxtBoard[y][x] = new ArrayList<>();
		}
	}
	
	static int ans;
	
	static void moveAll() {
		initialize();
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=m; x++) {
				if(board[y][x].isEmpty())
					continue;
				move(y,x);
			}
		}
		
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=m; x++)
				board[y][x] = (ArrayList<Mold>)nxtBoard[y][x].clone();
		}
	}
	
	static Mold NO_MOLD = new Mold(-1,-1,-1);
	static void compete(int y,int x) {
		Mold biggest = NO_MOLD;
		int lastIdx = board[y][x].size() - 1;
		for(int i = lastIdx; i>=0; i--) {
			Mold mold = board[y][x].get(i);
			
			if(mold.isGreater(biggest)) {
				biggest = mold;
			}
			board[y][x].remove(i);
		}
		
		board[y][x].add(biggest);
	}
	
	static void competeAll() {
		for(int y=1; y<=n; y++) {
			for(int x=1;x<=m; x++) {
				if(board[y][x].size() >= 2)
					compete(y,x);
			}
		}
	}
	
	static void simulate(int x) {
		// 곰팡이 채취 
		for(int  y=1; y<=n; y++) {
			if(!board[y][x].isEmpty()) {
				ans += board[y][x].get(0).size;
				board[y][x].clear();
				break;
			}
		}
//		printBoard();
		
		// 곰팡이 이동 
		moveAll();
//		printBoard();
		// 곰팡이 경쟁
		competeAll();
//		printBoard();
		
	}
	
	static void printBoard() {
		StringBuilder sb = new StringBuilder();
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=m; x++)
				sb.append(board[y][x]).append(' ');
			sb.append('\n');
		}
		System.out.println(sb);
	}
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		board = new ArrayList[n+1][m+1];
		nxtBoard = new ArrayList[n+1][m+1];
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=m; x++) {
				board[y][x] = new ArrayList<>();
			}
		}
		
		for(int i = 0; i<k; i++) {
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int speed = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken()) - 1;
			int size = Integer.parseInt(st.nextToken());
			
			board[y][x].add(new Mold(speed, dir, size));
		}
		
		
		for(int x= 1; x<=m; x++)
			simulate(x);
		
		System.out.println(ans);
		
	}

}