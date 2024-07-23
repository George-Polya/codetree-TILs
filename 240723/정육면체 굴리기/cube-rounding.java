import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
	static StringTokenizer st;
	static int n,m,k;
	
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
	static int board[][];
	static Dice dice = new Dice(0,0);
	
	static boolean OOB(int y,int x) {
		return y<0 || y>=n || x<0 || x>=m;
	}
	static int dy[] = {0,0,-1,1};
	static int dx[] = {1,-1,0,0};
	static StringBuilder sb = new StringBuilder();
	static class Dice{
		int cy,cx;
		int top,bottom, front, back, left,right;
		
		public Dice(int cy,int cx) {
			this.cy = cy;
			this.cx = cx;
			top = bottom = front = back = left = right = 0;
		}
		
		public int getTop() {
			return top;
		}
		
		
		
		/*
		 * 주사위의 이동
		 * 1. 주사위 위치의 변화  
		 * 2. 주사위 각 면의 변화 
		 * 
		 * 다음 위치가 OOB인지 체크하고 OOB면 return
		 * OOB가 아니면 위치를 변화시키고, 면을 변화시킴. 
		 * 
		 * 위치한 칸의 수가 0이면,
		 * 칸에 바닥면의 수를 복사. 주사위의 숫자는 변화x
		 * 
		 * 위치한 칸의 수가 0이 아니면 
		 * 칸의 수를 바닥면에 복사. 칸의 수는 0이 됨 
		 */
		public void move(int dir) {
			Pair nxtPos = getNxtPos(dir); // 다음 위치 가져오기 
			if(OOB(nxtPos.y, nxtPos.x)) // 다음위치가 OOB면 이동하지 않고 무시 
				return;
			
			// 주사위 위치의 변화 
			cy = nxtPos.y;
			cx = nxtPos.x;
			
			// 주사위 면의 변화 
			roll(dir); 
			
			
			if(board[cy][cx] == 0) {
				board[cy][cx] = bottom;
			}else {
				bottom = board[cy][cx];
				board[cy][cx] = 0;
			}
			sb.append(top).append('\n');
			
		}
		
		private Pair getNxtPos(int dir) {
			int ny = cy + dy[dir];
			int nx = cx + dx[dir];
			
			return new Pair(ny,nx);
			
		}
		
		private void roll(int dir) {
			int temp = bottom;
			if(dir == 0) {
				bottom = right;
				right = top;
				top = left;
				left = temp;
			}else if(dir == 1) {
				bottom = left;
				left = top;
				top = right;
				right = temp;
			}else if(dir == 2) {
				bottom = back;
				back = top;
				top = front;
				front = temp;
			}else if(dir == 3) {
				bottom = front;
				front = top;
				top = back;
				back = temp;
			}
		}
		
	}
	
	static void printBoard(int board[][]) {
		for(int y=0;y<n;y++) {
			for(int x= 0; x<m;x++) {
				System.out.printf("%-3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		dice.cy = Integer.parseInt(st.nextToken());
		dice.cx = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		board = new int[n][m];
		for(int y=0; y<n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=0;x<m;x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
 			}
		}
//		printBoard(board);
		st = new StringTokenizer(br.readLine());
		for(int turn=1;turn<=k; turn++) {
//			System.out.println("-----");
//			System.out.println("turn: "+turn);
			int dir = Integer.parseInt(st.nextToken());
			dice.move(dir-1);
//			printBoard(board);
		}
		System.out.println(sb);
		
	}
}