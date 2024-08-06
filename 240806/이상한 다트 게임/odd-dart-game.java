import java.util.*;
import java.io.*;

public class Main {
	static int n,m,q;
	static StringTokenizer st;
	static int board[][],nxtBoard[][];
	static int total;
	static int size;
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=m; x++) {
				System.out.printf("%-3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	static void copy(int src[][], int dst[][]) {
		for(int y=1; y<=n; y++) {
			System.arraycopy(src[y], 1, dst[y], 1,m);
		}
	}
	
	static void rotate(int id, int dir ,int k) {
		int temp[] = new int[m+1];
		int arr[] = board[id];
		for(int i = 1; i<=m; i++) {
			int nxt = 0;
			if(dir == 0)
				nxt = (i + k) % m == 0 ? (i + k) : (i + k ) % m;
			else
				nxt = (i + m - k) % m == 0 ? (i + m - k) : (i + m - k) % m;
			temp[nxt] = arr[i];
		}
		
		board[id] = temp;
	}
	
	
	/*
	 * 인접한 수 지우기.
	 * 타겟이 되는 수 board[y][x]는 0이 아니어야 한다. 
	 * 상하좌우 중 어느 방향으로든 한번이라도 인접한게 존재하면 한번만 지운다. 
	 * 
	 */
	
	static int remove() {
		copy(board, nxtBoard);
		int ret = 0;
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=m; x++) {
				int lx = (x-1) <= 0 ? m : x-1;
				int rx = (x+1) > m ? 1 : x+1;
//				System.out.printf("(y,x): (%d,%d), lx: %d, rx: %d\n", y,x,lx,rx);
				boolean flag = board[y][x] != 0;
				boolean left = board[y][x] == board[y][lx];
				boolean right = board[y][x] == board[y][rx];
				boolean down = y > 1 && board[y][x] == board[y-1][x];
				boolean up = y < n && board[y][x] == board[y+1][x];
				
				if(flag && (left || right || up || down)) {
					total -= board[y][x];
					size--;
					nxtBoard[y][x] = 0;
					ret++;
				}
				
			}
		}
		
		copy(nxtBoard,board);
		return ret;
	}
	
	/*
	 * 정규화
	 * 이미 지워진 곳은 정규화하지 않는다.
	 * 정규화할때마다 total도 업데이트 되어야한다. 
	 */
	static void normalize() {
		int avg = total/size;
		for(int y=1; y<=n; y++) {
			for(int x=1;x<=m;x++) {
				if(board[y][x] > avg) {
					board[y][x] -= 1;
					total -= 1;
				}else if(board[y][x] != 0 && board[y][x] < avg) {
					board[y][x] += 1;
					total += 1;
				}
			}
		}
	}
	
//	static int calcTotal() {
//		int ret = 0;
//		for(int y=1; y<=n; y++) {
//			for(int x=1; x<=m; x++)
//				ret += board[y][x];
//		}
//		return ret;
//	}
	
	
	static void simulate(int x, int dir, int k) {
		// 1. 회전
		for(int id = 1; id<=n; id++) {
			if(id % x == 0)
				rotate(id, dir, k);
		}
//		System.out.println("after rotate");
//		printBoard(board);
		
		// 2. 원판에 수가 남아있으면 지운다. 
		if(total != 0) {
			int removeCnt = remove();
//			System.out.println("after remove");
//			printBoard(board);
			
			/*
			 * 3. 지워지는 수가 없으면 정규화한다.
			 * 만약 2에서 원판에 수가 남아있지 않아서 지우지 않으면? 역시 정규화한다. 
			 * 다만, 원판에 수가 남아있지 않다는건 전부 0, 즉, total = 0이라는 건데, 평균보다 큰 수와 작은 수가 없을 것이다. 
			 */
			if(removeCnt == 0) {
//				System.out.println("after normalize");
//				System.out.printf("total: %d, size: %d, avg: %d\n", total, size, total/size);
				normalize();
//				printBoard(board);
//				System.out.printf("total: %d, calc: %d\n", total, calcTotal());
			}
		}
		
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());
		board = new int[n+1][m+1];
		nxtBoard = new int[n+1][m+1];
		
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=m; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
				total += board[y][x];
			}
		}
		
		size = n * m;
		
		for(int turn=1;turn<=q;turn++) {
//			System.out.println("------");
//			System.out.println("turn: "+turn);
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			simulate(x,d,k);
		}
		System.out.println(total);
	}
}