import java.io.*;
import java.util.*;

public class Main {
	static int n,m,k;
	static StringTokenizer st;
	static int board[][];
	static boolean wall[][][];
	static int windBoard[][], tempBoard[][];
	static class Tuple {
		int first, second, third;
		public Tuple(int first, int second, int third) {
			this.first = first;
			this.second = second;
			this.third = third;
		}
		
		public String toString() {
			return first +" "+second+" "+third+"|";
		}
		
	}
	static ArrayList<Tuple> airCons = new ArrayList<>();
	
	static int dy[] = {0,-1,0,1};
	static int dx[] = {-1,0,1,0};
	
	
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static boolean canGo(int y,int x,int dir) {
		return !OOB(y,x) && !wall[y][x][dir];
	}
	
	static void blow(int y,int x,int dir, int cnt, int board[][]) {
		if(cnt == 0)
			return;
		board[y][x] = cnt;
		
		int dir1 = (dir + 3) % 4;
		int ny1 = y + dy[dir1];
		int nx1 = x + dx[dir1];
		
		int dir2 = (dir+1)% 4;
		int ny2 = y + dy[dir2];
		int nx2 = x + dx[dir2];
		
		if(canGo(ny1,nx1,dir2)) {
			ny1 = ny1 + dy[dir];
			nx1 = nx1 + dx[dir];
			if(canGo(ny1,nx1,(dir+2)%4))
				blow(ny1,nx1,dir, cnt - 1,board);
		}
		
		if(canGo(ny2,nx2, dir1)) {
			ny2 = ny2 + dy[dir];
			nx2 = nx2 + dx[dir];
			if(canGo(ny2,nx2,(dir+2)%4))
				blow(ny2,nx2,dir, cnt - 1,board);
		}
		
		int ny = y + dy[dir];
		int nx = x + dx[dir];
//		System.out.println(y+" "+x);
//		System.out.println(ny+" "+nx);
//		System.out.println("------");
		if(canGo(ny,nx,(dir+2)%4))
			blow(ny,nx,dir,cnt-1,board);
		
		
	}
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++)
				System.out.print(board[y][x]+" ");
			System.out.println();
		}
	}
	
	static void initialize(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++)
				board[y][x] = 0;
		}
	}
	
	static void copy(int src[][], int dst[][]) {
		for(int y=1; y<=n; y++) {
			System.arraycopy(src[y], 1, dst[y], 1, n);
		}
	}
	
	static void blowAll() {
		
		for(Tuple airCon : airCons) {
			initialize(tempBoard);
			int cy = airCon.first;
			int cx = airCon.second;
			int dir = airCon.third;
			int ny = cy + dy[dir];
			int nx = cx + dx[dir];
			blow(ny,nx, dir, 5, tempBoard);
			for(int y=1; y<=n; y++) {
				for(int x=1; x<=n; x++)
					windBoard[y][x] += tempBoard[y][x];
			}
		}
	}
	
	static void airMix() {
		copy(windBoard, tempBoard);
		
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				for(int dir = 0; dir< 4; dir++) {
					int ny = y + dy[dir];
					int nx = x + dx[dir];
					if(OOB(ny,nx))
						continue;
					if(wall[y][x][dir])
						continue;
					int diff = windBoard[y][x] - windBoard[ny][nx];
					if(diff > 0) {
						tempBoard[ny][nx] += (diff/4);
						tempBoard[y][x] -= (diff/4);
					}
				}
			}
		}
		
		copy(tempBoard, windBoard);
		
	}
	
	static void decrease() {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				if(y == 1 || x == 1 || y == n || x==n) {
					if(windBoard[y][x] >= 1)
						windBoard[y][x] -= 1;
				}
			}
		}
	}
	
	static void simulate() {
		
		// 1. 에어컨에서 바람이 나옴
		blowAll();
		
		
		// 2. 공기들이 섞임
		airMix();
		
		// 3. 외벽과 맞닿은 칸의 시원함 감소
		decrease();
	}
	
	static boolean check() {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				if(board[y][x] == 1 && windBoard[y][x] < k)
					return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) throws IOException{
//		System.setIn(new FileInputStream("./input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		board = new int[n+1][n+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				int value = Integer.parseInt(st.nextToken());
				if(value == 1) {
					board[y][x] = value;
				}else if(value >=2) {
					airCons.add(new Tuple(y,x,value - 2));
				}
			}
		}
		
		wall = new boolean[n+1][n+1][4];
		for(int i = 0; i<m;i++) {
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			if(s == 0) {
				wall[y][x][1] = true;
				if(!OOB(y-1, x))
					wall[y-1][x][3] = true;
				
			}else {
				wall[y][x][0] = true;
				if(!OOB(y,x-1))
					wall[y][x-1][2] = true;
			}	
		}
		
		windBoard = new int[n+1][n+1];
		
		tempBoard = new int[n+1][n+1];
		
		
//		simulate();
			
//		printBoard(windBoard);
//		System.out.println("-----");
//		simulate();
//		printBoard(windBoard);
//		System.out.println("-----");
		
		int turn = 0;
		while(turn <= 100) {
			if(check())
				break;
			turn++;
			simulate();
		}
		if(turn > 100)
			turn = -1;
		System.out.println(turn);
		
	}
	
}