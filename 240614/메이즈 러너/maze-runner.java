import java.util.*;
import java.io.*;
public class Main {
	
	static int n,m,k;
	static StringTokenizer st;
	static int moveDistances[]; // 참가자들의 이동 거리 
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		public String toString() {
			return y+" "+x;
		}
		
		public boolean isSame(Pair p) {
			return y == p.y && x == p.x;
		}
	}
	
	static int maze[][],nxtMaze[][];
	static Pair NO_POS = new Pair(-1,-1);
	static Pair players[];
	static Pair exit = new Pair(0,0);
	static boolean exited[];
	
	static boolean allExit() {
		for(int id=1;id<=m;id++) {
			if(!exited[id])
				return false;
		}
		return true;
	}
	
	static int getDist(int y1,int x1, int y2,int x2) {
		return Math.abs(y1-y2) + Math.abs(x1-x2);
	}
	
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static Pair getNxtPos(int y,int x) {
//		Pair ret = NO_POS;
		int curDist = getDist(y,x, exit.y, exit.x);
		
		for(int dir = 0; dir< 4; dir++) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			
			if(OOB(ny,nx) || maze[ny][nx] != 0) 
				continue;
			int nxtDist = getDist(ny,nx,exit.y, exit.x);
			
			if(nxtDist < curDist) {
				return new Pair(ny,nx);
			}
		}
		
		return NO_POS;
	}
	
	static int totalMove;
	static void move(int id) {
		// 이미 탈출한 참가자는 이동하지 않는다. 
		if(exited[id])
			return;
		Pair player = players[id];
		Pair nxtPos = getNxtPos(player.y, player.x);
//		System.out.println("nxt: "+nxtPos);
		if(nxtPos == NO_POS) // 움직일 수 없는 상황이면 움직이지 않는다. 
			return;
		
//		idBoard[player.y][player.x] -= 1<<id; // 이전 위치 삭제 
		
		// 실제 이동 
		player.y = nxtPos.y;
		player.x = nxtPos.x;
		totalMove++;
		
		
		// 탈출 가능? 
		if(exit.isSame(player)) {
			players[id] = NO_POS;
			exited[id] = true;
			return;
		}
//		idBoard[player.y][player.x] += 1 << id;
	}
	
	
	static void moveAll() {
		for(int id = 1; id<=m;id++) {
			if(exited[id])
				continue;
			move(id);
		}
	}
	static Square NO_SQUARE = new Square(15, 15 ,15);
	static class Square{
		int y,x; // 좌상단 좌표
		int size; // 정사각형의 한 변의 길이 
		
		public Square(int y, int x, int size) {
			this.size = size;
			this.y = y;
			this.x = x;
		}
		
		public boolean isHigher(Square s) {
			if(size != s.size)
				return size < s.size;
			if( y != s.y)
				return y < s.y;
			return x < s.x;
		}
		
		public String toString() {
			return y+" "+x+" "+size;
		}
	}
	
	static Square makeSquare(int id) {
		if(exited[id])
			return NO_SQUARE;
		
		int y1 = exit.y;
		int x1 = exit.x;
		
		int y2 = players[id].y;
		int x2 = players[id].x; 
		
		int size = Math.max(Math.abs(y1-y2) + 1, Math.abs(x1-x2)+1);
		
		int y = Math.max(y1, y2) - size + 1;
		if(y <= 0)
			y = 1;
		
		int x = Math.max(x1, x2) - size + 1;
		if(x <= 0)
			x = 1;
		return new Square(y, x,size);
		
	}
	
	static Square findBestSquare() {
		Square ret = NO_SQUARE;
		for(int id = 1; id<=m;id++) {
			if(exited[id]) // 이미 탈출한 참가자는 스킵 
				continue;
			
			Square square = makeSquare(id);
			if(square.isHigher(ret))
				ret = square;
		}
		return ret;
	}
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	static void copy(int src[][], int dst[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				dst[y][x] = src[y][x];
			}
		}
	}
	
	static void rotateSquare(Square square) {
		copy(maze, nxtMaze);
		
		
		int sy = square.y;
		int sx = square.x;
		int size = square.size;
		
		int ey = exit.y;
		int ex = exit.x;
		
		//출구의 회전 
		exit.y = ex - sx + sy;
		exit.x = size -1 - ey + sy + sx;
		
		
		// 벽과 참가자들의 회전 
		for(int y= sy; y<sy + size; y++) {
			for(int x= sx; x < sx + size; x++) {
				int _y = x - sx + sy;
				int _x = size - 1 - y + sy + sx;
				nxtMaze[_y][_x] = maze[y][x];
				
				if(nxtMaze[_y][_x] > 0) // 내구도 감소 
					nxtMaze[_y][_x]--;
//				
//				if(idBoard[y][x] != 0) {
//					for(int id = 1; id <= m; id++) {
//						if( (idBoard[y][x] & (1<<id)) !=0 ) {
//							players[id].y = _y;
//							players[id].x = _x;
//						}
//					}
//				}
			}
		}
		
		
//		for(int y = 1; y<=n; y++) {
//			Arrays.fill(idBoard[y], 0);
//		}
//		for(int id = 1; id<=m; id++) {
//			if(exited[id])
//				continue;
//			Pair player = players[id];
//			idBoard[player.y][player.x] += (1<<id);
//		}
		
		for(int id = 1; id<=m;id++) {
			if(exited[id])
				continue;
			int y = players[id].y;
			int x = players[id].x;
			
			if(sy <= y && y < sy+ size && sx <= x && x < sx + size) {
				int _y = x - sx + sy;
				int _x = size - 1 - y + sy + sx;
				players[id].y = _y;
				players[id].x = _x;
			}
		}
		
		copy(nxtMaze, maze);
	}
	
	static void mazeRotate() {
		Square best = findBestSquare();
		rotateSquare(best);
	}
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		maze = new int[n+1][n+1];
//		idBoard = new int[n+1][n+1];
		nxtMaze = new int[n+1][n+1];
		moveDistances = new int[m+1];
//		nxtIdBoard = new int[n+1][n+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				maze[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		players = new Pair[m+1];
		exited = new boolean[m+1];
		for(int id=1;id<=m;id++) {
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			players[id] = new Pair(y,x); 
//			idBoard[y][x] += 1 << id;
		}
		
		
		st = new StringTokenizer(br.readLine());
		exit.y = Integer.parseInt(st.nextToken());
		exit.x = Integer.parseInt(st.nextToken());
		
		
		for(int turn =1; turn<=k;turn++) {
//			System.out.println("------");
//			System.out.println("turn: " + turn);
			
			// 참가자들의 이동 
			moveAll();
			if(allExit()) {
				break;
			}
			// 미로의 회전
			mazeRotate();
		
		}
		System.out.println(totalMove);
		System.out.println(exit.y+" "+exit.x);
	}
	
}