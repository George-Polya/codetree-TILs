import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		board = new int[n+1][n+1];
		tempBoard = new int[n+1][n+1];
		
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		makeGroups();
//		System.out.println(sizes);
		
//		printBoard(tempBoard);
		int ans = calc();
//		System.out.println(ans);
		
		for(int turn = 1; turn<=3; turn++) {
//			System.out.println("-----");
//			System.out.println("turn: "+turn);
			rotate();
//			System.out.println("after rotate");
//			printBoard(board);
			makeGroups();
			int score = calc();
//			System.out.println("score: "+score);
			ans += score;
		}
		
		System.out.println(ans);
	}
	
	static void crossRotate(int cy, int cx) {
		tempBoard[cy][cx] = board[cy][cx];
		
		// 십자 회전 
		int limit = n / 2;
		
		for(int l = 1; l <= limit; l++) {
			int dir = 0;
			int y = cy + dy[dir] * l;
			int x = cx + dx[dir] * l;
			int temp = board[y][x];
			
			for(int i = 0; i < 4; i++) {
				dir = (dir + 1) % 4;
				int ny = cy + dy[dir] * l;
				int nx = cx + dx[dir] * l;
				tempBoard[y][x] = board[ny][nx];
				y = ny;
				x = nx;
			}
			
			dir = (dir + 3) % 4;
			y = cy + dy[dir] * l;
			x = cx + dx[dir] * l;
			tempBoard[y][x] = temp;
		}
	}
	
	static void rotate() {
//		for(int y=1; y<=n; y++) {
//			Arrays.fill(tempBoard[y], 0);
//		}
		int cy = (n + 1)/ 2;
		int cx = (n + 1)/ 2;
		
		// 십자 회전 
		crossRotate(cy,cx);
		
		
		// 4개의 정사각형 회전 
		rotate4Squares();
		
		copy(tempBoard,board);
	}
	
	static void rotate4Squares() {
		int size = n / 2;
		for(int sy = 1; sy<=n; sy += size+1) {
			for(int sx=1; sx<=n; sx+= size+1) {
				rotateSquare(sy,sx,size);
			}
		}
	}
	
	static void rotateSquare(int sy, int sx, int size) {
		for(int y = sy; y <= sy + size - 1; y++) {
			for(int x = sx; x <= sx+size - 1; x++) {
				int y1 = y - sy, x1 = x - sx;
				int y2 = size - 1 - x1;
				int x2 = y1;
				int _y = y2 + sy;
				int _x = x2 + sx;
				tempBoard[y][x] = board[_y][_x];
			}
		}
	}
	
	static void copy(int src[][], int dst[][]) {
		for(int y=1; y<=n; y++) {
			System.arraycopy(src[y],1, dst[y],1,n);
		}
	}
	
	static int n;
	static int board[][], tempBoard[][];
	static StringTokenizer st;
	
	static ArrayList<Integer> sizes = new ArrayList<>();
	
	static int calc() {
		int sum = 0;
		
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				for(int dir = 0; dir < 4; dir++) {
					int ny = y + dy[dir];
					int nx = x + dx[dir];
					if(OOB(ny,nx) || tempBoard[ny][nx] == tempBoard[y][x])
						continue;
					
					int size1 = sizes.get(tempBoard[y][x] - 1);
					int size2 = sizes.get(tempBoard[ny][nx] - 1);
					int value1 = board[y][x];
					int value2 = board[ny][nx];
					
					sum += (size1 + size2) * value1 * value2;
				}
			}
		}
		
		return sum / 2;
	}
	
	static void makeGroups() {
		sizes.clear();
		for(int y=1; y<=n; y++) {
			Arrays.fill(tempBoard[y], 0);
		}
		
		int id = 1;
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				if(tempBoard[y][x] != 0)
					continue;
				int size = bfs(y,x,id++);
				sizes.add(size);
			}
		}
	}
	
	static int bfs(int y,int x, int id) {
		Queue<Pair> q = new LinkedList<>();
		int value = board[y][x];
		tempBoard[y][x] = id;
		q.add(new Pair(y,x));
		int size = 0;
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			size++;
			for(int dir = 0; dir<4;dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				if(OOB(ny,nx) || tempBoard[ny][nx] != 0)
					continue;
				if(board[ny][nx] == value) {
					q.add(new Pair(ny,nx));
					tempBoard[ny][nx] = id;
				}
			}
		}
		
		return size;
	}
	
	static int dy[] = {-1,0,1,0};
	static int dx[] = {0,1,0,-1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
	}
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
}