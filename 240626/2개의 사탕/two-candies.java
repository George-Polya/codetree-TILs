import java.util.*;
import java.io.*;
public class Main {
	static StringTokenizer st;
	static char board[][];
	static int n,m;
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public String toString() {
			return y+" "+x;
		}
		
		public boolean isSame(int y,int x) {
			return this.y == y && this.x == x;
		}
	}
	
	static Pair red;
	static Pair blue;
	static Pair exit;
	
	static int INT_MAX = Integer.MAX_VALUE;
	static int ans = INT_MAX;
	static Pair NO_POS = new Pair(-1,-1);
	
	
	static boolean redFirst(int dir) {
		int ry = red.y;
		int rx = red.x;
		
		int by = blue.y;
		int bx = blue.x;
		
		if(dir == 0) {
			return rx == bx && ry < by;
		}else if(dir == 1) {
			return rx == bx && ry > by;
		}else if(dir == 2) {
			return rx < bx && ry == by;
		}
		
		return rx > bx && ry == by;
	}
	
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	static boolean isWall(int y,int x) {
		return board[y][x] == '#';
	}
	static Pair move(Pair target, Pair other, int dir) {
		int y = target.y;
		int x = target.x;
		
		int ny = y + dy[dir];
		int nx = x + dx[dir];
		
		if(exit.isSame(ny,nx))
			return NO_POS;
		
		if(isWall(ny,nx) || other.isSame(ny, nx))
			return target;
		
		return move(new Pair(ny,nx), other, dir);
		
	}
	
	static void tilt(int dir) {
		boolean flag = redFirst(dir);
		
		if(flag) {
			red = move(red, blue, dir);
			blue = move(blue, red, dir);
		}else {
			blue = move(blue, red, dir);
			red = move(red, blue, dir);
		}
//		System.out.printf("blue: %s red: %s\n", blue, red);
	}
	
	static boolean blueEscape() {
		return blue == NO_POS;
	}
	
	static boolean redEscape() {
		return red == NO_POS;
	}
	
	static void solve(int cnt) {
		if(blueEscape())
			return;
		
		if(redEscape()) {
			ans = Math.min(ans, cnt);
			return;
		}
		
		if(cnt > 10)
			return;
		
		for(int dir = 0; dir<4;dir++) {
			Pair tempRed = red;
			Pair tempBlue = blue;
			tilt(dir);
			solve(cnt+1);
			
			red = tempRed;
			blue = tempBlue;
		}
	}
	
	public static void main(String[] args) throws IOException{
//		System.setIn(new FileInputStream("./input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		board = new char[n+1][m+1];
		for(int y=1; y<=n; y++) {
			String str = br.readLine();
			for(int x = 1; x<=m; x++) {
				board[y][x] = str.charAt(x-1);
				
				
				if(board[y][x] == 'R') {
					red = new Pair(y,x);
				}else if(board[y][x] == 'B') {
					blue = new Pair(y,x);
				}else if(board[y][x] == 'O') {
					exit = new Pair(y,x);
				}
				
			}
		}
		
		
		solve(0);
		System.out.println(ans >=11 ? -1 : ans);
		
		
	}
}