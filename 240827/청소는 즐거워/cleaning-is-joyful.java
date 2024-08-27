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
		
		/*
		 * 이동 로직 
		 * 처음 시작은 중심좌표 
		 * 처음 이동방향은 왼쪽(dir = 0)
		 * 처음 이동횟수는 1
		 * 현재 이동방향이 1이거나 3일때 다음 이동의 이동횟수가 증가함 
		 * 
		 * 이동횟수만큼 이동이 끝난 후 dir 업데이트 
		 * 
		 * 좌측 최상단에 도달할때까지 반복 
		 */
		int y = n/2;
		int x = n/2;
		int moveNum = 1;
		int dir = 0;
		int cnt = 1;
		
		while(true) {
			for(int num = 1; num<= moveNum; num++) {
//				System.out.println("-----");
				
				// 이동 
				y = y + dy[dir];
				x = x + dx[dir];
				
				if(OOB(y,x)) // 좌측 최상단에 도달하면 이동 중지 
					break;
				
				
//				map[y][x] = cnt++;
//				System.out.printf("(%d, %d)\n", y,x);
				
				// 이동한 위치에서 먼지 이동 
				sweep(y,x,dir);
				
//				printBoard(board);
			}
			
			// 좌측 최상단에 도달했으면 중지 
			if(OOB(y,x))
				break;
			
			
			// 현재 이동방향이 1이거나 3일때 다음 이동의 이동횟수가 증가함 
			if(dir == 1 || dir == 3)
				moveNum++;
			
			// 이동방향 업데이트 
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
	
	/*
	 * 먼지의 이동 
	 * 1. 현재위치의 먼지의 양을 바탕으로 5x5에 각각 이동하게 되는 먼지의 양 구하기 
	 * 2. nxn 격자에 5x5먼지의 양 더해주기 
	 *  2.1 a% 계산
	 *  2.2 격자밖을 벗어나면 ans 업데이트 
	 * 3. a%에 해당하는 격자 업데이트 
	 *  3.1 a%에 해당하는 격자가 OOB이면 ans 업데이트 
	 */
	static void sweep(int cy, int cx, int dir) {
		int value = board[cy][cx]; // cur에 해당하는 먼지의 양 
		
		// 이동방향대로 이동했을때 5x5격자에 존재하는 먼지의 양 
//		int swept[][] = makeSwept(value,sweepers[dir]);
//		printBoard(swept);
		int a = value;
		board[cy][cx] = 0; // 현재 격자의 먼지는 모두 없어짐 
		
		for(int y = cy-2; y<= cy+2; y++) {
			for(int x=cx-2; x<=cx+2; x++) {
				int sy = y - (cy-2); 
				int sx = x - (cx-2);
//				value -= swept[sy][sx]; // a%에 해당하는 먼지 양 계산  
				int plus = (value * sweepers[dir][sy][sx]) / 100;
				a -= plus;
				
				if(OOB(y,x)) {
//					ans += swept[sy][sx]; // 격자 밖을 벗어나면 ans 업데이트
					ans += plus;
					continue;
				}
				
//				board[y][x] += swept[sy][sx]; // 이동한 먼지가 더해짐
				board[y][x] += plus;
			}
		}
		
		int ny = cy + dy[dir];
		int nx = cx + dx[dir];
		if(OOB(ny,nx)) {
			ans += a;
			return;
		}
		board[ny][nx] += a;
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