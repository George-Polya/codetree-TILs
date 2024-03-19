import java.io.*;
import java.util.*;
public class Main {
	static int n,m,k;
	static StringTokenizer st;
	static int board[][];
	static int idBoard[][], nxtIdBoard[][];
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public String toString() {
			return String.format("y: %d, x: %d|",y,x);
		}
	}
	
	static Pair people[];
	
	static void printBoard(int board[][]) {
		int n = board.length - 1;
		int m = board[1].length - 1;
		for(int y=1; y<=n; y++) {
			for(int x=1;x<=m; x++) {
				System.out.printf("%-3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	static Pair exit;
	static Pair EXIT_PERSON = new Pair(-1,-1);
	
	static void init(int board[][]) {
		for(int y=1; y<=n; y++)
			Arrays.fill(board[y], 0);
		board[exit.y][exit.x] = 1<<0;
	}
	
	static int ans;
	
	// 상하로 움직이는걸 우선한다.
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static int getDistance(int y1,int x1, int y2, int x2) {
		return Math.abs(y1-y2) + Math.abs(x1-x2);
	}
	
	static int getDir(int y,int x) {
		int ret = -1;
		int dist1 = getDistance(y,x,exit.y,exit.x); // 현재 위치에서 출구까지의 거리 
		
		for(int dir = 0; dir < 4; dir++) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			
			if(OOB(ny,nx) || board[ny][nx] != 0) // 격자를 벗어나거나 벽이 있는 방향은 고려하지 않는다. 
				continue;
			int dist2 = getDistance(ny,nx, exit.y ,exit.x); // 다음 위치에서 출구까지의 거리 
			if(dist1 > dist2) { // 기존위치에서의 거리가 더 멀면 기존위치를 업데이트한다. 
				dist1 = dist2;
				ret = dir;
			}
		}
		
		return ret;
		
	}
	
	
	static void move(int id) {
		Pair person = people[id];
		
		int y = person.y;
		int x = person.x;
		
		int dir = getDir(y,x); // 이동할 방향 
		if(dir == -1) {
			// dir == -1 이라는건 어느 방향으로 이동해도 출구와 가까워질 수 없음 
			// 이동을 안함 
			nxtIdBoard[y][x] += (1<< id);
			return;
		}
			
		
		// 이
		int ny = y + dy[dir];
		int nx = x + dx[dir];
		ans++;
		
		// 이동한 위치에 출구가 존재하면 즉시 탈출 
		if(ny == exit.y && nx == exit.x) {
			people[id] = EXIT_PERSON;
			return;
		}
		// 사람이 이동한 위치를 새롭게 바꿔준다. 
		people[id] = new Pair(ny,nx);
		
		// 참가자들의 위치를 기록 
		nxtIdBoard[ny][nx] += (1<<id);
	}
	
	static void moveAll() {
		init(nxtIdBoard); // 참가자들의 다음 위치를 기록할 보드 초기화 
		
		for(int id=1;id<=m;id++) {
			if(people[id] == EXIT_PERSON) // 탈출하지 않은 사람은 이동한다. 
				continue;
			
			move(id);
		}
		
		
		// 참가자들의 위치 업데이트 
		for(int y=1;y<=n; y++)
			System.arraycopy(nxtIdBoard[y], 1, idBoard[y], 1, n);
		
	}
	
	static boolean allEscape() {
		for(int id=1;id<=m;id++) {
			if(people[id] != EXIT_PERSON)
				return false;
		}
		return true;
			
	}
	
	static class Square{
		int sy,sx; // 좌상단의 y,x좌표 
		int r; //한 변의 길이
		
		public Square(int sy, int sx, int r) {
			this.sy = sy;
			this.sx = sx;
			this.r = r;
		}
		
		public boolean isHigher(Square s) { // 우선순위가 높은 것
			if(r != s.r)
				return r < s.r; // 변의 길이가 작거나 
			else if(sy != s.sy)
				return sy < s.sy; // 변의 길이가 같다면 좌상단의 y좌표가 작거나 
			
			return sx < s.sx; // 변의 길이도 같고, 좌상단의 y좌표도 같다면 좌상단의 x좌표가 작거나 
		}
		
		public String toString() {
			return String.format("sy: %d, sx: %d, r: %d", sy,sx,r);
		}
	}
	
	static Square MAX_SQUARE = new Square(11,11,11);
	
	static Square getSquare(Pair person) {
		int minY = Math.min(exit.y, person.y);
		int minX = Math.min(exit.x, person.x);
		
		int maxY = Math.max(exit.y, person.y);
		int maxX = Math.max(exit.x, person.x);
		Square ret = MAX_SQUARE;
		
		if(maxY - minY > maxX - minX) {
			int r = maxY - minY + 1;
			for(int x = maxX - r+1; x<= minX; x++) {
				if(OOB(minY, x)) // 정사각형의 좌상단이 격자를 벗어나면 안된다. 
					continue;
				Square square = new Square(minY, x, r);
				if(square.isHigher(ret))
					ret = square;
			}
		}else if(maxY - minY < maxX - minX) {
			int r = maxX - minX + 1;
			
			for(int y=maxY - r + 1; y<=minY; y++) {
				if(OOB(y, minX)) // 정사각형의 좌상단이 격자를 벗어나면 안된다. 
					continue;
				
				Square square = new Square(y, minX, r);
				if(square.isHigher(ret))
					ret = square;
			}
			
		}else {
			ret = new Square(minY, minX, maxY - minY + 1);
		}
		
		return ret;
	}
	
	static Square findMinSquare() {
		Square minSquare = MAX_SQUARE;
		
		
		//  가장 작은 정사각형 찾기 
		for(int id=1;id<=m;id++) {
			if(people[id] == EXIT_PERSON) // 이미 탈출한 사람은 제외 
				continue;
			Square square = getSquare(people[id]);// person과 출구를 포함하는 정사각형
//			System.out.println("square: "+square);
			if(square.isHigher(minSquare))
				minSquare = square;
		}
//		System.out.println("minSquare: "+minSquare);
		return minSquare;
	}
	
	static void rotateSquare(Square square) {
		int sy = square.sy;
		int sx = square.sx;
		int r = square.r;
		
//		System.out.printf("sy: %d, sx: %d, r: %d\n", sy,sx,r);
		
		int tempBoard[][] = new int[r+1][r+1];
		int tempIdBoard[][] = new int[r+1][r+1];
		
		for(int y = sy; y<sy+r; y++) {
			for(int x = sx; x< sx+r; x++) {
				tempBoard[x-sx+1][r - y + sy]=board[y][x] != 0 ? board[y][x] -1 : 0;
				tempIdBoard[x-sx+1][r - y + sy] = idBoard[y][x];
				board[y][x] = 0; // 기존 위치는 지운다.
				idBoard[y][x] = 0;
			}
		}
		
		
		// board의 square부분에 tempBoard 복사 
		
		// idBoard의 square부분에 tempIdBoard복
		
		
		for(int  y= sy; y<sy+r;y++) {
			for(int x = sx; x<sx+r;x++) {
				board[y][x] = tempBoard[y-sy+1][x-sx+1];
				idBoard[y][x] = tempIdBoard[y-sy+1][x-sx+1];
				if(idBoard[y][x] == 1) // idBoard에 출 
					exit = new Pair(y,x); // 출구 위치 업데이트 
				else {
					// idBoard에 참가자들 >> 참가자들 위치 업데이트 
					for(int bit = 1; bit<=m;bit++) {
						if( (idBoard[y][x] & (1<<bit)) !=0 ) {
							people[bit] = new Pair(y,x);
						}
					}
				}
			}
		}
		
		
	}
	
	/*
	 * 1. 한명 이상의 참가자와 출구를 포함하는 가장 작은 정사각형 찾기 
	 *  1.1 여러개라면 좌상단의 y좌표가 작은거, x좌표가 작은 거 
	 * 2. 그 정사각형을 90도 회전하기
	 *  2.1 board의 회전 => 벽의 내구도 하락
	 *  2.2 idBoard의 회전 => people[id]와 exit의 위치 변경 
	 */
	static void rotate() {
		// 가장 작은 정사각형 찾기
		Square minSquare = findMinSquare();
		
		
		
		// 정사각형을 오른쪽으로 90도 회전하기 
		
		rotateSquare(minSquare);
		
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		people = new Pair[m+1];
		board = new int[n+1][n+1];
		idBoard = new int[n+1][n+1];
		nxtIdBoard = new int[n+1][n+1];
		
		for(int y=1; y<=n;y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		for(int id = 1; id<=m; id++) {
			st = new StringTokenizer(br.readLine());
			int y= Integer.parseInt(st.nextToken());
			int x= Integer.parseInt(st.nextToken());
			people[id] = new Pair(y,x);
			idBoard[y][x] += (1<<id);
		}
		st = new StringTokenizer(br.readLine());
		int y= Integer.parseInt(st.nextToken());
		int x= Integer.parseInt(st.nextToken());
		exit=new Pair(y,x);
		idBoard[y][x] += (1<<0);
//		printBoard(board);
//		System.out.println();
//		printBoard(idBoard);
//		System.out.println("-----");
		
		for(int turn=1; turn<=k; turn++) {
//			System.out.println("turn: "+turn);
			
			// 참가자들의 이동
			moveAll();
			
			// 모든 참가자들의 탈출 확인 
			if(allEscape()) {
//				printBoard(board);
//				System.out.println();
//				printBoard(idBoard);
//				
//				System.out.println("exit: "+exit);
//				System.out.println(Arrays.toString(people));
//				System.out.println("ans: "+ans);
//				System.out.println("-----");
				break;
			}
//			System.out.println("after move");
//			printBoard(board);
//			System.out.println();
//			printBoard(idBoard);
			
			// 회전 
			rotate();
			
//			System.out.println("after rotate");
//			printBoard(board);
//			System.out.println();
//			printBoard(idBoard);
			
//			System.out.println("exit: "+exit);
//			System.out.println(Arrays.toString(people));
//			System.out.println("ans: "+ans);
//			System.out.println("-----");
		}
		System.out.println(ans);
		System.out.println(exit.y+" "+exit.x);
	}
}