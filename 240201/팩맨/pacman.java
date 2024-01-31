import java.io.*;
import java.util.*;

public class Main {
	static int n = 4;
	static int dead[][] = new int[n+1][n+1];
	static int count[][] = new int[n+1][n+1];
	static int nxtCount[][];
	static int m,t;
	static int dy[] = {-1,-1,0,1,1,1,0,-1};
	static int dx[] = {0,-1,-1,-1,0,1,1,1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y ;
			this.x = x;
		}
		public String toString() {
			return y+" "+x+"|";
		}
		
		public boolean isSame(int y,int x) {
			return this.y == y && this.x == x;
		}
	}
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++)
				System.out.print(board[y][x]+" ");
			System.out.println();
		}
	}
	
	static StringTokenizer st;
	static Pair packMan;
	
	
	static ArrayList<Integer> board[][] = new ArrayList[n+1][n+1];
	static ArrayList<Integer> nxtBoard[][];
	static ArrayList<Integer> eggBoard[][];
	
	// 복제 
	static void tryClone() {
		eggBoard = new ArrayList[n+1][n+1];
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++)
				eggBoard[y][x] = new ArrayList<>();
		}
		
		
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				if(count[y][x] >= 1) {
					for(int dir : board[y][x]) {
						eggBoard[y][x].add(dir);
					}
					
				}
			}
		}
		
	}
	
	static class Tuple{
		int first,second,third;
		public Tuple(int first,int second, int third) {
			this.first = first;
			this.second = second;
			this.third = third;
		}
	}
	
	static Tuple getNxt(int y,int x,int dir) {
		for(int i = 0; i < 8; i++) {
			int moveDir = (dir + i) % 8;
			int ny = y + dy[moveDir];
			int nx = x + dx[moveDir];
			if(OOB(ny,nx)|| dead[ny][nx] > 0 || packMan.isSame(ny, nx) )
				continue;
			return new Tuple(ny,nx,moveDir);
		}
		return new Tuple(y,x,dir);
	}
	
	static void move(int y,int x, int dir) {
//		int dir = monster.dir;
		
		Tuple nxt = getNxt(y,x,dir);
		nxtBoard[nxt.first][nxt.second].add(nxt.third);
		nxtCount[nxt.first][nxt.second] += 1;
	}
	
	static void initialize() {
		nxtCount = new int[n+1][n+1];
		nxtBoard = new ArrayList[n+1][n+1];
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++)
				nxtBoard[y][x] = new ArrayList<>();
		}
	}
	
	static void moveAll() {
		initialize();
		for(int y=1 ; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				if(count[y][x] >= 1) {
					for(int k = 0; k<board[y][x].size();k++) {
//						Monster monster = board[y][x].get(k);
						int dir = board[y][x].get(k);
//						move(y,x,monster);
						move(y,x,dir);
					}
				}
			}
		}
		
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				board[y][x] = (ArrayList<Integer>)nxtBoard[y][x].clone();
			}
		}
		
		count = nxtCount;
	}
	
	static void copy(int dst[][], int src[][]) {
		for(int y=1; y<=n; y++) {
			System.arraycopy(src[y], 1, dst[y], 1, n);
		}
	}
	
	static String moveDirs="";
	static int maxCnt;
	
	static void decideDirection(int cur, int cnt, String moveDir) {
		if(OOB(packMan.y, packMan.x))
			return;
		
		if(cur == 3) {
			if(maxCnt < cnt) {
				maxCnt = cnt;
				moveDirs = moveDir;
			}else if(maxCnt == cnt){
				if(moveDirs.compareTo(moveDir) > 0)
					moveDirs = moveDir;
			}
		
			return;
		}
		
		for(int dir : new int[] {0,2,4,6}) {
			Pair temp = packMan;
			int tempCount[][] = new int[n+1][n+1];
			int tempBoard[][] = new int[n+1][n+1];
			copy(tempCount, count);
			copy(tempBoard, dead);
			int ny = packMan.y + dy[dir];
			int nx = packMan.x + dx[dir];
			
			int score = 0;
			if(OOB(ny,nx))
				continue;
			packMan = new Pair(ny,nx);
//			System.out.println(packMan);
//			System.out.println("----");
			
			if(count[ny][nx] != 0) {
				score += count[ny][nx];
				count[ny][nx] = 0;
//				board[ny][nx] = 2;
			}
			decideDirection(cur + 1, cnt + score, moveDir + dir);
			
			copy(count, tempCount);
//			copy(board, tempBoard);
			packMan = temp;
		}
	}
	static void packManMove() {
		
		// 팩맨이 이동할 방향 정하
//		System.out.println(packMan);
		moveDirs = "777";
		maxCnt = 0;
		decideDirection(0,0,"");
//		System.out.println("moveDirs : "+ moveDirs);
		int y = packMan.y;
		int x = packMan.x;
		for(int i = 0; i < moveDirs.length();i++) {
			int moveDir = Integer.valueOf(moveDirs.charAt(i)) - 48;
//			System.out.println(moveDir);
			int ny = y + dy[moveDir];
			int nx = x + dx[moveDir];
			if(count[ny][nx] != 0) {
				count[ny][nx] = 0;
				dead[ny][nx] = 3;
				int lastIdx = board[ny][nx].size()- 1;
				for(int k = lastIdx; k>=0 ;k--) {
//					Monster monster = board[ny][nx].get(k);
					board[ny][nx].remove(k);
				}
				
			}
			y = ny;
			x = nx;
		}
		packMan = new Pair(y,x);
		
	}
	
	static void hatchAll() {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				for(int k = 0; k < eggBoard[y][x].size(); k++) {
//					Monster monster = eggBoard[y][x].get(k);
					int dir = eggBoard[y][x].get(k);
//					board[y][x].add(monster);
					board[y][x].add(dir);
					count[y][x] += 1;
				}
			}
		}
		
	}
	
	static void simulate() {
		
		// 몬스터 복제 시도 
		tryClone();
		
		// 몬스터의 이동 
		moveAll();
//		printBoard(count);
//		System.out.println();
		
		// 팩맨 이동 	
		packManMove();
//		printBoard(count);
//		System.out.println("Dead");
//		printBoard(dead);
		
		// 몬스터 부화 
		hatchAll();
//		printBoard(count);
//		System.out.println();
//		 몬스터 시체 소멸
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				if(dead[y][x] > 0)
					dead[y][x] -= 1;
			}
		}
		
//		System.out.println();
//		printBoard(dead);
	}
	
	
	
	
	public static void main(String[] args) throws IOException{
//		System.setIn(new FileInputStream("./input.txt"));
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++)
				board[y][x] = new ArrayList<>();
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		m = Integer.parseInt(st.nextToken());
		t = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(br.readLine());
		int r = Integer.parseInt(st.nextToken());
		int c = Integer.parseInt(st.nextToken());
		packMan = new Pair(r,c);
		
		for(int i = 0; i<m; i++) {
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken()) - 1;
			board[y][x].add(dir);
			count[y][x] += 1;
			
		}
//		System.out.println(packMan);
		for(int i = 0; i< t;i++) {
			simulate();
//			System.out.println("====");
		}
//		System.out.println("002".compareTo("040"));
		int ans = 0;
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++)
				ans += count[y][x];
		}
		
		System.out.println(ans);
	}
}