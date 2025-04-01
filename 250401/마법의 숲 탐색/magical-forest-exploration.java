import java.util.*;
import java.io.*;
public class Main {
	static int r,c,k;
	static int board[][];
	static int dy[] = {-1,0,1,0};
	static int dx[] = {0,1,0,-1};
	static boolean OOB(int y,int x) {
		return y<=3 || y> r+3 || x<=0 || x>c;
	}
	static StringTokenizer st;
	static boolean isExit[][];
	
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
	
	static void printExit() {
		for(int y=1; y<=r+3;y++) {
			for(int x=1; x<=c;x++) {
				System.out.printf("%3d", (isExit[y][x] ? 1 : 0));
			}
			System.out.println();
		}
	}
	
	
	static class Golem{
		int cy,cx, id, dir;
		public Golem(int cy,int cx,int id, int dir) {
			this.cy = cy;
			this.cx = cx;
			this.id = id;
			this.dir = dir;
		}
		
		private boolean canGo(int y,int x) {
			return 1<= x && x <=c && y <= r+3;
		}
		
		private boolean canGo(int cy, int cx, int moveDir) {
			int y = cy + dy[moveDir];
			int x = cx + dx[moveDir];
			if(moveDir == 2) {
				for(int dir : new int[] {1,2,3}) {
					int ny = y + dy[dir];
					int nx = x + dx[dir];
					if(!canGo(ny,nx) || board[ny][nx] != 0) {
						return false;
					}
				}
				return true;
				
			}else if(moveDir == 3) {
				
				for(int dir : new int[] {0,2,3}) {
					int ny = y + dy[dir];
					int nx = x + dx[dir];
					
					if(!canGo(ny,nx) || board[ny][nx] != 0)
						return false;
				}
				return canGo(y,x,2);
				
			}else if(moveDir == 1) {
				for(int dir : new int[] {0,1,2}) {
					int ny = y + dy[dir];
					int nx = x + dx[dir];
					
					if(!canGo(ny,nx) || board[ny][nx] != 0)
						return false;
				}
				return canGo(y,x,2);
			}
			return false;
		}
		
		public void recursiveMove() {
			if(canGo(cy,cx,2)) { // 남쪽으로 이동가능 
				cy += dy[2];
				cx += dx[2];
				this.recursiveMove();
			}else if(canGo(cy,cx,3)) { // 서쪽으로 이동가능 
				cy += dy[3];
				cx += dx[3];
				dir = (dir + 3) % 4;
				this.recursiveMove();
			}else if(canGo(cy,cx,1)) { // 동쪽으로 이동 가능 
				cy += dy[1];
				cx += dx[1];
				dir = (dir + 1) % 4;
				this.recursiveMove();
			}else { // 전부 이동 불가 
//				System.out.println(this);
				if(OOB(cy-1,cx) || OOB(cy+1,cx)) { // 몸의 일부가 숲을 벗어남 
					reset();
				}else {
					board[cy][cx] = id;
					for(int moveDir = 0; moveDir < 4; moveDir++) {
						int ny = cy + dy[moveDir];
						int nx = cx + dx[moveDir];
						if(OOB(ny,nx))
							continue;
						board[ny][nx] = id;
					}
					
					isExit[cy + dy[dir]][cx+dx[dir]] = true;
//					System.out.println("board");
//					printBoard(board);
//					System.out.println("exit");
//					printExit();
//					sum += fairyMove();
					int ret = fairyMove();
//					System.out.println("ret: "+ret);
					sum += ret;
				}
			}
		}
		
		
		public int fairyMove() {
			Queue<Pair> q = new LinkedList<>();
			q.add(new Pair(cy,cx));
			boolean visited[][] = new boolean[r+3+1][c+1];
			visited[cy][cx] = true;
			int ret = cy;
			while(!q.isEmpty()) {
				Pair cur = q.poll();
				ret = Math.max(ret,  cur.y);
				if(ret == r + 3)
					break;
				for(int moveDir=0; moveDir<4;moveDir++) {
					int ny = cur.y + dy[moveDir];
					int nx = cur.x + dx[moveDir];
					
					if(OOB(ny,nx) || board[ny][nx] == 0 || visited[ny][nx])
						continue;
					
					if(board[ny][nx] == board[cur.y][cur.x] || isExit[cur.y][cur.x]) {
						visited[ny][nx] = true;
						q.add(new Pair(ny,nx));
					}
				}
			}
			
			return ret - 3;
			
		}
		
		public String toString() {
			return "id: "+id+"|"+cy+" "+cx+" "+dir;
		}
	}
	
	static int sum;
	
	static void reset() {
		for(int y=1; y<=r+3; y++) {
			Arrays.fill(board[y], 0);
			Arrays.fill(isExit[y], false);
		}
	}
	
	public static void printBoard(int board[][]) {
		for(int y=1; y<=r+3;y++) {
			for(int x=1; x<=c;x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	public static void main(String args[]) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		
		r = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		board = new int[r+3+1][c+1];
		isExit = new boolean[r+3+1][c+1];
		for(int id=1; id<=k; id++) {
//			System.out.println("-----");
//			System.out.println("id: "+id);
			st = new StringTokenizer(br.readLine());
			int cx = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
//			System.out.printf("cx: %d dir: %d\n", cx,dir);
			Golem golem = new Golem(2,cx,id, dir);
			
			golem.recursiveMove(); // 골렘의 이동 
			
			// 정령의 이동 
			
//			int ret = golem.fairyMove();
//			System.out.println("ret: "+ret);
//			sum += ret;
		}
		System.out.println(sum);
	}
}
