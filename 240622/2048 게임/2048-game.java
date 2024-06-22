import java.util.*;
import java.io.*;
public class Main {
	static StringTokenizer st;
	static int n;
	static int original[][], board[][], nxtBoard[][];
	static int ans;
	static int selected[];
	
	static void copy(int dst[][], int src[][]) {
		for(int y =0; y<n; y++) {
			for(int x=0;x<n;x++) {
				dst[y][x] = src[y][x];
			}
		}
	}
	
	static void printBoard(int board[][]) {
		for(int y=0;y<n;y++) {
			for(int x=0;x<n;x++) {
				System.out.printf("%5d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	static void fall(int board[][], int dir) {
		for(int y= 0; y<n;y++)
			Arrays.fill(nxtBoard[y], 0);
		
		if(dir == 0) {
			for(int x=0; x<n; x++) {
				int idx = n - 1;
				for(int y= n-1; y>=0; y--) {
					if(board[y][x] == 0)
						continue;
					nxtBoard[idx--][x] = board[y][x];
				}
				
				for(int y = n - 1; y>=0; y--) {
					board[y][x] = nxtBoard[y][x];
					nxtBoard[y][x] = 0;
				}
				
				int _y = n - 1;
				
				while(_y >= 0) {
					if(_y - 1 >=0 && board[_y][x] == board[_y-1][x]) {
						board[_y][x] *= 2;
						board[_y-1][x] = 0;
						_y -= 2;
					}else {
						_y--;
					}
				}
				
				idx = n - 1;
				for(int y= n-1; y>=0; y--) {
					if(board[y][x] == 0)
						continue;
					nxtBoard[idx--][x] = board[y][x];
				}
			}
			
		}else if(dir == 1) {
			for(int x = 0; x<n;x++) {
				int idx = 0;
				for(int y = 0; y<n; y++) {
					if(board[y][x] == 0)
						continue;
					nxtBoard[idx++][x] = board[y][x];
				}
				
				for(int y = 0; y<n; y++) {
					board[y][x] = nxtBoard[y][x];
					nxtBoard[y][x] = 0;
				}
				
				int _y = 0;
				while(_y<n) {
					if((_y+1 <n) && board[_y][x] == board[_y+1][x] ) {
						board[_y][x] *= 2;
						board[_y+1][x] = 0;
						_y += 2;
					}else {
						_y++;
					}
				}
				
				
				idx = 0;
				for(int y = 0; y<n; y++) {
					if(board[y][x] == 0)
						continue;
					nxtBoard[idx++][x] = board[y][x];
				}
				
			}
		}else if(dir == 2) {
			for(int y = 0; y<n;y++) {
				int idx = 0;
				for(int x=0;x<n;x++) {
					if(board[y][x] == 0)
						continue;
					nxtBoard[y][idx++] = board[y][x];
				}
				
				for(int x = 0; x <n; x++) {
					board[y][x] = nxtBoard[y][x];
					nxtBoard[y][x] = 0;
				}
				
				int _x = 0;
				while(_x < n) {
					if((_x + 1 < n) && board[y][_x] == board[y][_x+1]) {
						board[y][_x] *= 2;
						board[y][_x+1] = 0;
						_x += 2;
					}else {
						_x++;
					}
				}

				idx = 0;
				for(int x=0;x<n;x++) {
					if(board[y][x] == 0)
						continue;
					nxtBoard[y][idx++] = board[y][x];
				}
				
			}
		}else if(dir == 3) {
			for(int y = 0 ; y<n;y++) {
				int idx = n -1;
				for(int x = n-1; x>=0; x--) {
					if(board[y][x] == 0)
						continue;
					nxtBoard[y][idx--] = board[y][x];
				}
				
				for(int x = n-1; x>=0; x--) {
					board[y][x] = nxtBoard[y][x];
					nxtBoard[y][x] = 0;
				}
				
				
				int _x = n -1;
				while(_x >=0) {
					if(_x - 1 >=0 && board[y][_x] == board[y][_x-1]) {
						board[y][_x] *= 2;
						board[y][_x-1] = 0;
						_x -= 2;
					}else {
						_x--;
					}
				}
				
				
				idx = n -1;
				for(int x = n-1; x>=0; x--) {
					if(board[y][x] == 0)
						continue;
					nxtBoard[y][idx--] = board[y][x];
				}
				
			}
		}
		
		
		copy(board,nxtBoard);
	}
	
	static int getMax(int board[][]) {
		int ret = -1;
		for(int y=0;y<n;y++) {
			for(int x=0;x<n;x++)
				ret = Math.max(ret, board[y][x]);
		}
		return ret;
	}
	
	static void solve(int cnt) {
		if(cnt == 5) {
			copy(board, original);
			
			for(int dir : selected) {
				fall(board, dir);
			}
			
			ans = Math.max(ans, getMax(board));
			
			return;
		}
		
		for(int dir = 0; dir<4;dir++) {
			selected[cnt] = dir;
			solve(cnt + 1);
		}
	}
	
	public static void main(String[] args) throws IOException{
//		System.setIn(new FileInputStream("./input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		n = Integer.parseInt(br.readLine());
		original = new int[n][n];
		board = new int[n][n];
		nxtBoard = new int[n][n];
		
		for(int y= 0; y<n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=0; x<n;x++) {
				original[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		selected = new int[5];
		
		solve(0);
		System.out.println(ans);
	}
}