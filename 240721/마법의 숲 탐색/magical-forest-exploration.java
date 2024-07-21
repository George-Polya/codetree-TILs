import java.io.*;
import java.util.*;

public class Main {
	static StringTokenizer st;
	static int r,c,k;
	static int board[][];
	static boolean isExit[][];
	
	static int dy[] = {-1,0,1,0};
	static int dx[] = {0,1,0,-1};
	
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=r+3;y++) {
			for(int x=1; x<=c;x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	static int ans;
	
	static class Golem{
		int id, cy,cx,dir;
		
		public Golem(int id, int cy, int cx, int dir) {
			this.id = id;
			this.cy = cy;
			this.cx = cx;
			this.dir = dir;
		}
		
		public String toString() {
			return String.format("id: %d, (cy,cx): (%d,%d), dir: %d", id,cy,cx,dir);
		}
		
		/*
		 * (y,x)가 격자 밖을 벗어나는지 체크 
		 */
		boolean OOB(int y, int x) {
			return  y<=0 || y>r+3 || x<=0 || x>c;
		}
		
		/*
		 * 골렘의 일부가 숲을 벗어나는지 체크 
		 */
		boolean OOB() {
			return cx - 1<=0 || cx+1 >c || cy-1<=3;
		}
		
		/*
		 * cy, cx : 중심 좌표 
		 * moveDir : 이동가능한지 확인하려는 방향 
		 * dirs : 그 방향으로의 칸에서 주변 3칸을 가리키는 방향 
		 */
		
		boolean canGo(int cy, int cx, int moveDir, int dirs[]) {
			int y = cy + dy[moveDir];
			int x = cx + dx[moveDir];
			
			for(int nDir : dirs) {
				int ny = y + dy[nDir];
				int nx = x + dx[nDir];
				if(OOB(ny,nx) || board[ny][nx] != 0)
					return false;
			}
			
			// 남쪽으로 이동하는 경우라면 그냥 true 
			if(moveDir == 2)
				return true;
			
			// 동쪽, 서쪽인 경우, 그 방향으로 이동 후 남쪽으로 이동가능한지 한번 더 체크 
			return canGo(y,x,2,new int[] {1,2,3});
		}
		
		/*
		 * 모든 골렘이 숲을 빠져나감 
		 * 골렘이 점유하던 숲을 0으로 
		 * 출구를 false로  
		 */
		void reset() {
			for(int y=1; y<=r+3;y++) {
				Arrays.fill(board[y], 0);
				Arrays.fill(isExit[y], false);
			}
		}
		
		public void move() {
			// 남쪽으로 이동가능한 경우 
			if(canGo(cy,cx,2, new int[] {1,2,3})) {
				cy += dy[2];
				cx += dx[2];
				this.move();
			}else if(canGo(cy,cx,3, new int[] {2,3})) { // 남쪽으로 이동불가능하고 서쪽으로 회전해서 이동가능한 경우 
				cy += dy[3];
				cx += dx[3];
				dir = (dir + 3) % 4; // 회전 
				this.move();
			}else if(canGo(cy,cx,1,new int[] {1,2})) { // 남쪽, 서쪽 불가능하고 동쪽으로 회전해서 이동가능한 경우 
				cy += dy[1];
				cx += dx[1];
				dir = (dir + 1) % 4; // 회전 
				this.move();
			}else { 
				// 어떤 방향으로도 이동 불가능한 경우 
				if(OOB()) { // 골렘의 일부가 숲을 벗어난 경우 
					reset();
				}else {
					// 골렘이 숲을 점유하고 출구위치를 지정 
					board[cy][cx] = id;
					for(int moveDir =0 ;moveDir<4;moveDir++) {
						int ny = cy + dy[moveDir];
						int nx = cx + dx[moveDir];
						board[ny][nx] = id;
					}
					isExit[cy + dy[dir]][cx+dx[dir]] = true;
					
					
					// 요정의 이동 
					ans += fairyMove() - 3;
				}
			}
		}
		
		int fairyMove() {
			Queue<Pair> q = new LinkedList();
			boolean visited[][] = new boolean[r+3+1][c+1];
			q.add(new Pair(cy,cx));
			visited[cy][cx] = true;
			int value = board[cy][cx];
			
			int ret = 0;
			
			while(!q.isEmpty()) {
				Pair cur = q.poll();
				ret = Math.max(ret,  cur.y);
				
				for(int moveDir = 0; moveDir<4;moveDir++) {
					int ny = cur.y + dy[moveDir];
					int nx = cur.x + dx[moveDir];
					
					if(OOB(ny,nx) || visited[ny][nx] || board[ny][nx] == 0)
						continue;
					
					// 현재위치와 다음 위치가 같은 골렘의 내부이거나 
					// 현재위치가 출구라면 다른 골렘으로 갈 수 있다. 
					if(board[cur.y][cur.x] == board[ny][nx] || isExit[cur.y][cur.x]) {
						q.add(new Pair(ny,nx));
						visited[ny][nx] = true;
					}
					
				}
			}
			
			return ret;
			
		}
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
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		r = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		board = new int[r+3+1][c+1];
		isExit = new boolean[r+3+1][c+1];
		
		for(int id = 1; id<=k; id++) {
			st = new StringTokenizer(br.readLine());
			int cx = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			
			Golem golem = new Golem(id,2,cx,dir);
			
			golem.move();
			
		}
		System.out.println(ans);
	}
}