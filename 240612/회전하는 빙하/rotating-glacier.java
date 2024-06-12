import java.util.*;
import java.io.*;
public class Main {
	static int n,q,size;
	static int board[][];
	static StringTokenizer st;
	
	static void copy(int src[][], int dst[][], int sy, int sx, int size) {
		for(int y = 0; y< size; y++) {
			for(int x=0 ;x < size; x++) {
				dst[y][x] = src[y+sy][x+sx];
			}
		}
	}
	
	static void printBoard(int board[][]) {
		int n = board.length;
		int m = board[0].length;
		
		for(int y = 0; y<n; y++) {
			for(int x= 0; x<m; x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	static int dy[] = {1,0,-1,0};
	static int dx[] = {0,1,0,-1};
	static boolean OOB(int y, int x) {
		return y<0 || y>=size || x<0 || x>=size; 
	}
	
	static void update(int ny,int nx, int sy,int sx,int size) {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size;j++) {
				board[sy + i][sx + j] = board[ny+i][nx+j];
			}
		}
	}
	
	
	static void rotate(int sy,int sx, int level) {
//		System.out.println(level);
		if(level  < 1) 
			return;
			
		int newSize = 1 << (level-1);
		int temp[][] = new int[newSize][newSize];
		copy(board, temp, sy,sx, newSize);
//		printBoard(temp);
//		System.out.println();
		
		int y = sy;
		int x = sx;
		for(int dir = 0; dir< 3; dir++) {
			int ny = y + dy[dir] * newSize;
			int nx = x + dx[dir] * newSize;
			
			update(ny,nx, y,x,newSize);
			y = ny;
			x = nx;
		}
//		System.out.printf("y: %d, x: %d\n", y,x);
		
		for(int i = 0; i < newSize;i++) {
			for(int j =0 ; j<newSize;j++) {
				board[y + i][x + j] = temp[i][j];
			}
		}
		
		
	}
	
	static void rotateAll(int level) {
		int newSize = 1 << level;
		
		for(int y=0; y<size;y+=newSize) {
			for(int x=0; x<size; x+=newSize) {
//				System.out.println(y+" "+x);
				rotate(y,x,level);
			}
		}
	}
	
	static int count[][];
	
	static void melt() {
		for(int y= 0; y<size;y++) {
			Arrays.fill(count[y], 0);
		}
		
		for(int y=0; y<size;y++) {
			for(int x=0; x<size;x++) {
				for(int dir = 0; dir<4;dir++) {
					int ny = y + dy[dir];
					int nx = x + dx[dir];
					
					if(OOB(ny,nx) || board[ny][nx] == 0)
						continue;
					count[y][x]++;
				}
			}
		}
		
		for(int y =0 ;y<size;y++) {
			for(int x= 0; x<size;x++) {
				if(count[y][x] < 3 && board[y][x] > 0)
					board[y][x]--;
			}
		}
	}
	
	static boolean visited[][];
	static class Pair{
		int y,x;
		
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
	}
	static int bfs(int y,int x) {
		Queue<Pair> q = new LinkedList<>();
		q.add(new Pair(y,x));
		visited[y][x] = true;
		int ret=0;
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			ret++;
			
			for(int dir = 0; dir<4;dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				
				if(OOB(ny,nx) || visited[ny][nx] || board[ny][nx] == 0)
					continue;
				visited[ny][nx] = true;
				q.add(new Pair(ny,nx));
			}
			
		}
//		System.out.println(ret);
		return ret;
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());
		size = 1 << n;
		board = new int[size][size];
		count = new int[size][size];
		
		for(int y=0; y<size;y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=0;x<size;x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		st= new StringTokenizer(br.readLine());
		for(int turn=1; turn<=q;turn++) {
			int level = Integer.parseInt(st.nextToken());
			rotateAll(level);
			melt();
			
		}
		
//		printBoard(board);
//		System.out.println("----");
		visited = new boolean[size][size];
		int sum = 0;
		int _max = 0;
		for(int y=0; y<size;y++) {
			for(int x= 0; x<size;x++) {
				sum += board[y][x];
				if(visited[y][x] || board[y][x] == 0)
					continue;
				_max = Math.max(_max, bfs(y,x));
			}
		}
		
		System.out.println(sum);
		System.out.println(_max);
		
		
	}
}