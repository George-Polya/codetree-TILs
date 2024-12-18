import java.io.*;
import java.util.*;


public class Main {
	static int L,n,q;
	static StringTokenizer st;
	static int board[][], idBoard[][];
	static class Knight{
		int sy,sx;
		int h,w;
		int k, damage;
		public Knight(int sy, int sx, int h, int w, int k, int damage) {
			this.sy = sy;
			this.sx = sx;
			this.h = h;
			this.w = w;
			this.k = k;
			this.damage = damage;
		}
		
	}
	
	static Knight NO_KNIGHT = new Knight(-1,-1,-1,-1,-1,-1);
	static void draw(int sy,int sx,int h, int w, int idx, int board[][]) {
		for(int y = sy; y < sy + h; y++) {
			for(int x= sx; x < sx + w; x++) {
				board[y][x] = idx;
			}
		}
	}
	
	static Knight knights[];
	static int dy[] = {-1,0,1,0};
	static int dx[] = {0,1,0,-1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>L || x<=0 || x>L;
	}
	static boolean isMoved[];
	
	static boolean canMove(int id, int dir) {
		Knight knight = knights[id];
		int sy = knight.sy;
		int sx = knight.sx;
		int h = knight.h;
		int w = knight.w;
		
		if(dir == 0) {
			int y = sy-1;
			for(int x = sx; x < sx + w; x++) {
				if(OOB(y,x) || board[y][x] == 2)
					return false;
				int nxtId = idBoard[y][x];
				if(nxtId == 0)
					continue;
				if(!canMove(nxtId, dir))
					return false;
			}
			return true;
		}
		
		if(dir == 1) {
			int x = sx + w;
			for(int y = sy; y < sy + h; y++) {
				if(OOB(y,x) || board[y][x] == 2)
					return false;
				int nxtId = idBoard[y][x];
				if(nxtId == 0)
					continue;
				if(!canMove(nxtId, dir))
					return false;
			}
			return true;
		}
		
		if(dir == 2) {
			int y = sy + h;
			for(int x = sx; x < sx + w; x++) {
				if(OOB(y,x) || board[y][x] == 2)
					return false;
				int nxtId = idBoard[y][x];
				if(nxtId == 0)
					continue;
				if(!canMove(nxtId, dir))
					return false;
			}
			return true;
		}
		
		int x = sx - 1;
		for(int y = sy; y < sy + h; y++) {
			if(OOB(y,x) || board[y][x] == 2)
				return false;
			int nxtId = idBoard[y][x];
			if(nxtId == 0)
				continue;
			if(!canMove(nxtId, dir))
				return false;
		}
		return true;
	}
	
	static void move(int id, int dir) {
		if(isMoved[id])
			return;
		
		Knight knight = knights[id];
		int sy = knight.sy;
		int sx = knight.sx;
		int h = knight.h;
		int w = knight.w;
		int k = knight.k;
		int damage = knight.damage;
		
		int ny = sy + dy[dir];
		int nx = sx + dx[dir];
		knights[id] = new Knight(ny,nx,h,w,k,damage);
		isMoved[id] = true;
		
		if(dir == 0) {
			int y = sy-1;
			for(int x= sx; x < sx + w; x++) {
				int nxtId = idBoard[y][x];
				if(nxtId == 0)
					continue;
				move(nxtId, dir);
			}
			return;
		}
		
		if(dir == 1) {
			int x = sx + w;
			for(int y = sy; y < sy + h; y++) {
				int nxtId = idBoard[y][x];
				if(nxtId == 0)
					continue;
				move(nxtId, dir);
			}
			return;
		}
		
		if(dir == 2) {
			int y = sy + h;
			for(int x= sx; x < sx + w; x++) {
				int nxtId = idBoard[y][x];
				if(nxtId == 0)
					continue;
				move(nxtId, dir);
			}
			return;
		}
		
		int x = sx - 1;
		for(int y = sy; y < sy + h; y++) {
			int nxtId = idBoard[y][x];
			if(nxtId == 0)
				continue;
			move(nxtId, dir);
		}
		return;
	}
	
	static void moveAll(int id, int dir) {
		Arrays.fill(isMoved, false);
		if(!canMove(id, dir)) {
			return;
		}
		
		move(id,dir);
		for(int y=1; y<=L; y++)
			Arrays.fill(idBoard[y], 0);
		for(int i=1; i<=n;i++) {
			Knight knight = knights[i];
			int sy = knight.sy;
			int sx = knight.sx;
			int h = knight.h;
			int w = knight.w;
			draw(sy,sx,h,w,i,idBoard);
		}
		
	}
	
	static void update(int id) {
		Knight knight = knights[id];
		int sy = knight.sy;
		int sx = knight.sx;
		int h = knight.h;
		int w = knight.w;
		
		int cnt = 0;
		for(int y= sy; y < sy+h; y++) {
			for(int x= sx; x < sx + w; x++) {
				if(board[y][x] == 1)
					cnt++;
			}
		}
		
		knight.damage += cnt;
		if(knight.k <= knight.damage) {
			knights[id] = NO_KNIGHT;
		}
	}
	
	static void simulate(int id, int dir) {
		moveAll(id, dir);
		
		for(int i =1; i<=n; i++) {
			if(i == id)
				continue;
			if(!isMoved[i])
				continue;
			if(knights[i] == NO_KNIGHT)
				continue;
			update(i);
				
		}
	}
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=L; y++) {
			for(int x=1; x<=L; x++)
				System.out.printf("%3d", board[y][x]);
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws IOException{
//		System.setIn(new FileInputStream("./input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		L = Integer.parseInt(st.nextToken());
		n = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());
		board = new int[L+1][L+1];
		idBoard = new int[L+1][L+1];
		
		for(int y= 1; y<=L; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=L; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		isMoved = new boolean[n+1];
		knights = new Knight[n+1];
		for(int i=1; i<=n; i++) {
			st = new StringTokenizer(br.readLine());
			int y= Integer.parseInt(st.nextToken());
			int x= Integer.parseInt(st.nextToken());
			int h= Integer.parseInt(st.nextToken());
			int c= Integer.parseInt(st.nextToken());
			int k= Integer.parseInt(st.nextToken());
			knights[i] = new Knight(y,x,h,c,k,0);
			draw(y,x,h,c,i,idBoard);
		}
		
//		printBoard(idBoard);
//		System.out.println("----");
		
		for(int i = 1; i <= q; i++) {
			st = new StringTokenizer(br.readLine());
			int id = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			if(knights[id] == NO_KNIGHT)
				continue;
			simulate(id,dir);
		}
		
		int ans = 0;
		for(int i = 1; i<=n; i++) {
			if(knights[i] == NO_KNIGHT)
				continue;
			ans += knights[i].damage;
		}
		
		System.out.println(ans);
	}
}