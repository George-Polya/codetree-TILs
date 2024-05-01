//  package samsung;
import java.util.*;
import java.io.*;



public class Main{
	static StringTokenizer st;	
	static int n,m;
	static char board[][];
	static Pair red, blue, EXIT;
	static Pair NO_WHERE = new Pair(-1,-1);
	static int ans = Integer.MAX_VALUE;
	
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public boolean isSame(Pair p) {
			return y == p.y && x == p.x;
		}
		
		public String toString() {
			return y +" "+x;
		}
	}
	
	static int dy[] = {1,-1,0,0};
	static int dx[] = {0,0,1,-1};
	
	static boolean redFirst(int dir) {
		int ry = red.y;
		int rx = red.x;
		int by = blue.y;
		int bx = blue.x;
		
		if(dir == 0) {
			return (rx == bx && ry > by);
		}else if(dir == 1) {
			return (rx == bx && ry < by);
		}else if(dir == 2) {
			return (ry == by && rx > bx);
		}else if(dir == 3) {
			return (ry == by && rx < bx);
		}
		
		return false;
	}
	
	static boolean isWall(int ny, int nx) {
		return board[ny][nx] == '#';
	}
	
	static Pair go(Pair pos, int dir, Pair candy) {
//		System.out.println(pos+" "+candy);
		int y = pos.y;
		int x = pos.x;
		int ny = y + dy[dir];
		int nx = x + dx[dir];
		Pair nxt = new Pair(ny,nx);
		if(isWall(ny,nx) || nxt.isSame(candy))
			return pos;
		if(nxt.isSame(EXIT))
			return NO_WHERE;
		
		
		
		return go(nxt, dir, candy);
	}
	
	static void tilt(int dir) {
		boolean flag = redFirst(dir);
		// System.out.println(flag);
		if(flag) {
			red = go(red, dir, blue);
			blue = go(blue, dir, red);
			
		}else {
			blue = go(blue, dir, red);
			red = go(red, dir, blue);
		}
//		
//		if(red.isSame(EXIT))
//			red = NO_WHERE;
//		if(blue.isSame(EXIT))
//			blue = NO_WHERE;
	}
	
	static boolean blueEscape() {
		return blue == NO_WHERE;
	}
	
	static boolean redEscape() {
		return red == NO_WHERE;
	}
	
	static void solve(int cur) {
		if(blueEscape())
			return;
		if(redEscape()) {
			ans = Math.min(ans, cur);
			return;
		}
		
		if(cur >= 10)
			return;
		
		for(int dir = 0; dir<4;dir++) {
			Pair tempRed = red;
			Pair tempBlue = blue;
			tilt(dir);
			solve(cur+1);
			red = tempRed;
			blue = tempBlue;
		}
	}
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		board = new char[n+1][m+1];
		for(int y=1; y<=n; y++) {
			String str = br.readLine();
			for(int x=1; x<=m; x++) {
				board[y][x] = str.charAt(x-1);
				if(board[y][x] == 'R') {
					board[y][x] = '.';
					red = new Pair(y,x);
				}
				else if(board[y][x] == 'B' ) {
					board[y][x] = '.';
					blue = new Pair(y,x);
				}
				else if(board[y][x] == 'O')
					EXIT = new Pair(y,x);
				
			}
		}
		

		
		solve(0);
//		tilt(3);
		
		if(ans >= 11)
			ans = -1;
		System.out.println(ans);
		
	}
}