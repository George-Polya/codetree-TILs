import java.util.*;
import java.io.*;
public class Main {
	static int l,n,q;
	static StringTokenizer st;
	static int BLANK = 0;
	static int TRAP = 1;
	static int WALL = 2;
	static int board[][];
	static class Knight{
		int id;
		int cy,cx; // 좌상단 좌표 
		int h,w; // 높이와 너비 
		int hp; // 초기 체력 
		public Knight(int id,int cy,int cx, int h, int w, int hp) {
			this.id = id;
			this.cy = cy;
			this.cx = cx;
			this.h = h;
			this.w = w;
			this.hp = hp;
		}
		
		public String toString() {
			return String.format("id: %d | %d %d", id, cy,cx);
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
	
	static int dy[] = {-1,0,1,0};
	static int dx[] = {0,1,0,-1};
	static boolean OOB(int y,int x) {
		return y<= 0 || y>l || x<=0 || x>l;
	}
	
	static void addQ(int id, Queue<Pair> q, boolean visited[][], int dir) {
		Knight knight = knights[id];
		int cy = knight.cy;
		int cx = knight.cx;
		int h = knight.h;
		int w = knight.w;
		int sy = cy;
		int sx = cx;
		if(dir == 1 || dir == 2) {
			sy += (dy[dir] == 0 ? 0 : dy[dir] * h - 1);
			sx += (dx[dir] == 0 ? 0 : dx[dir] * w - 1);
		}
		
		int ey = cy + h - 1;
		int ex = cx + w - 1;
		
//		System.out.println("knight: "+knight);
//		System.out.printf("y: %d x: %d\n", y,x);
//		System.out.printf("sy: %d, sx: %d, ey: %d, ex: %d\n", sy,sx,ey,ex);
		
		for(int y= sy; y<=ey; y++) {
			for(int x=sx; x<=ex; x++) {
				q.add(new Pair(y,x));
				visited[y][x] = true;
			}
		}
	}
	
	static boolean isWall(int y,int x) {
		return OOB(y,x) || board[y][x] == WALL;
	}
	
	static boolean inRange(Knight knight, int y,int x) {
		int sy = knight.cy;
		int sx = knight.cx;
		int h = knight.h;
		int w = knight.w;
		
		int ey = sy + h - 1;
		int ex = sx + w - 1;
		
//		System.out.println("knight: "+knight);
//		System.out.printf("y: %d x: %d\n", y,x);
//		System.out.printf("sy: %d, sx: %d, ey: %d, ex: %d\n", sy,sx,ey,ex);
		return sy <= y && y <= ey && sx <= x && x<=ex;
	}
	
	/*
	 * 현재 위치에 존재하는 다른 기사의 id를 찾는 메소드
	 * 만약 현재위치가 기사의 범위내에 있으면 그 기사의 id를 리턴  
	 * O(n)
	 */
	static int findNxtKnight(int y, int x, Set<Integer> set) {
		int ret = -1;
		for(int id = 1; id<=n; id++) {
			if(isDead[id] || set.contains(id))
				continue;
			Knight knight = knights[id];
			if(inRange(knight, y,x)) {
				return id;
			}
		}
		
		return ret;
	}
	
	static void update(int id,int dir) {
		Knight knight = knights[id];
		int cy = knight.cy;
		int cx = knight.cx;
		
		int ny = cy + dy[dir];
		int nx = cx + dx[dir];
		knight.cy = ny;
		knight.cx = nx;
	}
	
	static void updateAll(Set<Integer> set, int dir) {
		for(int id : set) {
			if(isDead[id])
				continue;
			update(id, dir);
		}
	}
	
	static Set<Integer> set = new HashSet<>();
	static boolean move(int id, int dir) {
//		Knight knight = knights[id];
		boolean canMove = true;
		Queue<Pair> q = new LinkedList<>();
		boolean visited[][] = new boolean[l+1][l+1];
		addQ(id, q, visited, dir);
		set.clear();
		
		set.add(id);
		
//		System.out.println("q: "+q);
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			int ny = cur.y + dy[dir];
			int nx = cur.x + dx[dir];
			
			if(isWall(ny,nx)) {
				canMove = false;
				break;
			}
			if(visited[ny][nx])
				continue;
			
			int nxtId = findNxtKnight(ny,nx,set);
//			System.out.println("nxtId: "+nxtId);
			if(nxtId != -1) {
				addQ(nxtId, q,visited,dir); //  연쇄적으로 밀려날 수 있는지 확인 
				set.add(nxtId);
			}
		}
		
		
		// 이동 가능하다면 이동 
//		System.out.println("canMove: "+ canMove);
		if(canMove) {
			updateAll(set, dir);
		}
		
		return canMove;
		
	}
	
	static Knight DEAD = new Knight(-1,-1,-1,-1,-1,-1);
	static int damages[]; 
	static Knight knights[];
	static boolean isDead[];
	
	static int getTrapCnt(int id) {
		Knight knight = knights[id];
		int cy = knight.cy;
		int cx = knight.cx;
		int h = knight.h;
		int w = knight.w;
		
		
		int ey = cy + h - 1;
		int ex = cx + w - 1;
		
		int ret = 0;
		for(int y = cy; y<=ey;y++) {
			for(int x= cx; x<=ex;x++) {
				ret += (board[y][x] == TRAP ? 1 : 0); 
			}
		}
		return ret;
	}
	
	/*
	 * 이번에 명령받은 기사는 피해를 입지 않는다. 
	 * h x w 의 함정수만큼 피해를 입는다.  
	 */
	static void fight(int idx) {
		for(int id : set) {
			if(id == idx || isDead[id])
				continue;
			Knight knight = knights[id];
			int cnt = getTrapCnt(id); // 함정 
			damages[id] += cnt; // id인 기사가 입은 피해 누적량 
			if(knight.hp <= damages[id]) { // 피해 누적이 체력보다 같거나 커지면 사망 
				isDead[id] = true;
				knights[id] = DEAD;
			}
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		l = Integer.parseInt(st.nextToken());
		n = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());
		board = new int[l+1][l+1];
		
		damages = new int[n+1];
		knights = new Knight[n+1];
		isDead = new boolean[n+1];
		
		for(int y=1; y<=l; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=l;x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		for(int id = 1; id<=n; id++) {
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int hp = Integer.parseInt(st.nextToken());
			knights[id] = new Knight(id,y,x,h,w,hp);
		}
		
		for(int turn=1; turn<=q; turn++) {
//			System.out.println("------");
//			System.out.println("turn: "+turn);
//			System.out.println(Arrays.toString(isDead));
			st = new StringTokenizer(br.readLine());
			int id = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			if(isDead[id] || knights[id] == DEAD)
				continue;
			
			boolean moved = move(id, dir);
//			System.out.println("knights: "+Arrays.toString(knights));
			
			// 대결 
			if(moved)
				fight(id);
			
//			System.out.println(Arrays.toString(damages));
		}
//		System.out.println("knights: "+Arrays.toString(knights));
		int sum = 0;
		for(int id = 1; id<=n; id++) {
			if(isDead[id] || knights[id] == DEAD)
				continue;
			sum += damages[id];
		}
		System.out.println(sum);
	}
}