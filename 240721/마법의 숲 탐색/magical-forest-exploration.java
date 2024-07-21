import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
	static int r,c,k;
	static StringTokenizer st;
	static int board[][];
	static int dy[] = {-1,0,1,0};
	static int dx[] = {0,1,0,-1};
	static boolean OOB(int y, int x) {
		return y<=0 || y> r || x<=0 || x>c;
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
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=r; y++) {
			for(int x=1; x<=c; x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	static class Tuple{
		int y,x,dir;
		public Tuple(int y,int x, int dir) {
			this.y = y;
			this.x = x;
			this.dir = dir;
		}
		
		public String toString() {
			return y+" "+x+" "+dir;
		}
	}
	
	static class Fairy{
		int y,x,id;
		public Fairy(int y,int x, int id) {
			this.y = y;
			this.x = x;
			this.id = id;
		}
		
		public boolean isSame(int y, int x) {
			return this.y == y && this.x == x;
		}
	}
	
	static Tuple NO_POS = new Tuple(-3,-3,-3);
	static Tuple END = new Tuple(-4,-4,-4);
	static class Golem{
		int id;
		int cy,cx,exitDir; // 골렘 중앙과 출구의 방향 
		Fairy fairy;
		Pair exit;
		public Golem(int id, int cy, int cx, int exitDir) {
			this.id = id;
			this.cy = cy;
			this.cx = cx;
			this.exitDir = exitDir;
			exit = new Pair(cy+dy[exitDir], cx+dx[exitDir]);
			fairy = new Fairy(cy,cx,id);
		}
		
		private Tuple getNxtPos(int cy, int cx, int exitDir, int moveDirs[], int tempDir) {
			int y = cy + dy[tempDir];
			int x = cx + dx[tempDir];
			
			for(int dir : moveDirs) {
				int ny = y + dy[dir];
				int nx = x + dx[dir];
				if(OOB(ny,nx)) {
					if(ny<=0)
						continue;
					else
						return NO_POS;
				}
				
				if(board[ny][nx]!=0)
					return NO_POS;
			}
			
			if(tempDir == 2) {
				return new Tuple(y,x,exitDir);
			}
			
			int moveDir = (exitDir + tempDir) % 4;
			return getNxtPos(y,x,moveDir,new int[]{1,2,3}, 2);
			
		}
		
		
		private void update(Tuple nxt) {
			cy = nxt.y;
			cx = nxt.x;
			exitDir = nxt.dir;
			exit.y = cy + dy[exitDir];
			exit.x = cx + dx[exitDir];
			fairy.y = cy;
			fairy.x = cx;
		}
		
		private boolean OOR() {
			if(OOB(cy,cx))
				return true;
			
			for(int dir = 0; dir < 4;dir++) {
				int ny = cy + dy[dir];
				int nx = cx + dx[dir];
				if(OOB(ny,nx))
					return true;
			}
			return false;
		}
		
		public boolean move() {
			Tuple nxt = null;
			while(true) {
				nxt = getNxtPos(cy,cx,exitDir, new int[] {1,2,3}, 2);
				if(nxt != NO_POS) { // 남쪽 가능?  
					update(nxt);
				}else if((nxt = getNxtPos(cy,cx,exitDir, new int[]{0,2,3},3)) != NO_POS) { // 서쪽 가능? 
					update(nxt);
				}else if((nxt = getNxtPos(cy,cx,exitDir, new int[]{0,1,2},1)) != NO_POS) { // 동쪽 가능? 
					update(nxt);
				}
				else
					break;
			}
			if(OOR()) {
				return true;
			}

			board[cy][cx] = id;
			for(int dir = 0; dir<4;dir++) {
				int y = cy + dy[dir];
				int x = cx + dx[dir];
				board[y][x] = id;
			}
			return false;
		}
		
		public int fairyMove() {
			Queue<Fairy> q = new LinkedList<>();
			boolean visited[][]= new boolean[r+1][c+1];
			q.add(fairy);
			visited[fairy.y][fairy.x] = true;
			int ret = fairy.y;
			Pair useExit = exit;
			
			while(!q.isEmpty()) {
				Fairy cur = q.poll();
				ret = Math.max(ret,  cur.y);
				useExit = golems.get(cur.id-1).exit;
				
				for(int dir = 0; dir<4;dir++) {
					int ny = cur.y + dy[dir];
					int nx = cur.x + dx[dir];
					if(OOB(ny,nx) || board[ny][nx] == 0 || visited[ny][nx])
						continue;
					
					// 현재위치와 다음 위치가 같은 골렘의 내부이거나 
					// 현재위치가 출구라면 다른 골렘으로 갈 수 있다. 
					if(board[ny][nx] == cur.id || cur.isSame(useExit.y, useExit.x)) {
						visited[ny][nx] = true;
						q.add(new Fairy(ny,nx, board[ny][nx]));
					}
				}
			}
			
			return ret;
		}
		
		public String toString() {
			return cy+" "+cx+" "+exitDir;
		}
	}
	
	static List<Golem> golems = new ArrayList<>();
	static int ans;
	static void reset() {
		
		for(int y=1;y<=r;y++) {
			Arrays.fill(board[y],0);
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		r = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		board = new int[r+1][c+1];
		for(int id = 1; id <= k ;id++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			
			Golem golem = new Golem(id, -1,x, dir);
			boolean oor = golem.move(); 
			golems.add(golem);
			if(oor) { // 이동 후 골렘의 일부가 숲을 벗어난 경우 
				reset();
				continue;
			}
			
			
			int score = golem.fairyMove();
			ans += score;
		}
		System.out.println(ans);
	}
}