import java.util.*;
import java.io.*;

public class Main {
	static int n,q;
	static int board[][], nxtBoard[][];
	static int boardSize;
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
	
	static int dy[] = {1,0,-1,0};
	static int dx[] = {0,1,0,-1};
	
	static Pair[] makePoints(int sy,int sx,int size){
//		ArrayList<Pair> ret = new ArrayList<>();
		Pair ret[] = new Pair[4];
		int y = sy;
		int x = sx;
		
		for(int dir = 0; dir<4;dir++) {
//			ret.add(new Pair(y,x));
			ret[dir] = new Pair(y,x);
			y = y + dy[dir] * size;
			x = x + dx[dir] * size;
		}
		
		return ret;
		
	}
	
	static boolean OOB(int y,int x) {
		return y<=0 || y>boardSize || x<=0 || x>boardSize;
	}
	
	
	static void rotate(int sy,int sx, int level) {
		if(level == 0)
			return;
		int size = (1 << (level-1));
		Pair points[] = makePoints(sy,sx,size);
		
		
		int temp[][] = new int[size][size];
		int d = 0;
		for(int y=0;y<size;y++) {
			for(int x=0; x<size;x++) {
				Pair cur = points[d];
				temp[y][x] = board[cur.y+y][cur.x+x];
			}
		}
		
		
		for(d= 0; d<4;d++) {
			for(int y= 0; y<size; y++) {
				for(int x= 0; x<size;x++) {
					Pair cur = points[d];
					Pair nxt = points[(d+1)%4];
					board[cur.y+y][cur.x+x] = board[nxt.y+y][nxt.x+x];
					
				}
			}
			
		}
		
		Pair cur = points[3];
		for(int y= 0; y<size;y++) {
			for(int x=0; x<size; x++) {
				board[cur.y+y][cur.x+x] = temp[y][x];
			}
		}
		
//		printBoard(board);
		
	}
	
	static void rotateAll(int level) {
		int len = 1 << level;
		for(int sy=1; sy<=boardSize; sy += len) {
			for(int sx=1; sx<=boardSize; sx+=len) {
				rotate(sy,sx, level);
			}
		}
	}
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=boardSize;y++) {
			for(int x=1; x<=boardSize; x++)
				System.out.print(board[y][x]+" ");
			System.out.println();
		}
		System.out.println();
	}
	
	static boolean visited[][]; 
	
	static int bfs(int y,int x) {
		Queue<Pair> q = new ArrayDeque<>();
		visited[y][x] = true;
		q.offer(new Pair(y,x));
		int cnt = 0;
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			cnt++;
			for(int dir = 0; dir<4;dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				
				if(OOB(ny,nx) || board[ny][nx] == 0 || visited[ny][nx])
					continue;
				visited[ny][nx] = true;
				q.offer(new Pair(ny,nx));
			}
			
		}
		return cnt;
	}
	
	public static void main(String[] args) throws IOException{
		// System.setIn(new FileInputStream("./input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());
		boardSize = 1 << n;
		board = new int[boardSize+1][boardSize+1];
		for(int y=1; y<=boardSize; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x = 1; x<=boardSize; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i<q;i++) {
			int level = Integer.parseInt(st.nextToken());
//			System.out.println(level);
			rotateAll(level);
			nxtBoard = new int[boardSize+1][boardSize+1];
			for(int y=1; y<=boardSize; y++) {
				for(int x=1; x<=boardSize;x++) {
					int cnt = 0;
					for(int dir = 0; dir<4;dir++) {
						int ny = y + dy[dir];
						int nx = x + dx[dir];
						if(OOB(ny,nx) || board[ny][nx] == 0)
							continue;
						cnt++;
					}
					
					if(cnt <= 2)
						nxtBoard[y][x] = board[y][x] > 0 ? board[y][x] - 1 : board[y][x];
						else
							nxtBoard[y][x] = board[y][x];
				}
			}
			
			board = nxtBoard;
		}
//		printBoard(board);
		
//		printBoard(board);
		
		int sum = 0;
		int maxSize = 0;
		visited = new boolean[boardSize+1][boardSize+1];
		for(int y=1; y<=boardSize;y++) {
			for(int x= 1; x<=boardSize;x++) {
				sum += board[y][x];
				if(visited[y][x] || board[y][x] == 0)
					continue;
				maxSize = Math.max(maxSize, bfs(y,x));
			}
		}
		
		System.out.println(sum+"\n"+maxSize);
	}
	
	
}