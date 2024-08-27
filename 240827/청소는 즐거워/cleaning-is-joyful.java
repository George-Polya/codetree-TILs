import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		board = new int[n][n];
//		map = new int[n][n];
		
		for(int y=0 ; y<n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=0; x<n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		int y = n/2;
		int x = n/2;
		int moveNum = 1;
		int dir = 0;
		int cnt = 1;
		while(true) {
			for(int num = 1; num<= moveNum; num++) {
//				System.out.println("-----");
				y = y + dy[dir];
				x = x + dx[dir];
				
				if(OOB(y,x))
					break;
//				map[y][x] = cnt++;
//				System.out.printf("(%d, %d)\n", y,x);
				sweep(y,x,dir);
//				printBoard(board);
			}
			if(OOB(y,x))
				break;
			
			if(dir == 1 || dir == 3)
				moveNum++;
			dir = (dir + 1) % 4;
		}
		
//		sweep(2,1,0);
//		printBoard(board);
//		printBoard(sweepers[0]);
		System.out.println(ans);
		
	}
	
	static int n;
	static StringTokenizer st;
	static int board[][];
//	static int map[][];
	static int dy[] = {0,1,0,-1};
	static int dx[] = {-1,0,1,0};
	static boolean OOB(int y,int x) {
		return y<0 || y>=n || x<0 || x>=n;
	}
	static void printBoard(int board[][]) {
		
		for(int y=0; y<n; y++) {
			for(int x=0; x<n; x++) {
				System.out.printf("%5d", board[y][x]);
			}
			System.out.println();
		}
	}
	static int a;
	static int sweepers[][][] = {
			{
				{0, 0,2,0,0},
				{0,10,7,1,0},
				{5, 0,0,0,0},
				{0,10,7,1,0},
				{0, 0,2,0,0}
			},
			{
				{0, 0,0, 0,0},
				{0, 1,0, 1,0},
				{2, 7,0, 7,2},
				{0,10,0,10,0},
				{0, 0,5, 0,0}
			},
			{
				{0,0,2, 0,0},
				{0,1,7,10,0},
				{0,0,0, 0,5},
				{0,1,7,10,0},
				{0,0,2, 0,0},
			},
			{
				{0, 0,5, 0,0},
				{0,10,0,10,0},
				{2, 7,0, 7,2},
				{0, 1,0, 1,0},
				{0, 0,0, 0,0}
			}
	};
	
	static int ans;
	static void sweep(int cy, int cx, int dir) {
		int value = board[cy][cx];
		int swept[][] = makeSwept(value,sweepers[dir]);
//		printBoard(swept);
		
		board[cy][cx] = 0;
		
		for(int y = cy-2; y<= cy+2; y++) {
			for(int x=cx-2; x<=cx+2; x++) {
				int sy = y - (cy-2);
				int sx = x - (cx-2);
				value -= swept[sy][sx];

				if(OOB(y,x)) {
					ans += swept[sy][sx];
					continue;
				}
				
				board[y][x] += swept[sy][sx];
			}
		}
		
		int ny = cy + dy[dir];
		int nx = cx + dx[dir];
		if(OOB(ny,nx)) {
			ans += value;
			return;
		}
		board[ny][nx] += value;
	}
	
	static int[][] makeSwept(int value, int sweeper[][]){
		int ret[][] = new int[5][5];
		for(int y = 0; y<5; y++) {
			for(int x = 0; x < 5; x++) {
				ret[y][x] = (value * sweeper[y][x]) / 100;
			}
		}
		
 		return ret;
	}
}