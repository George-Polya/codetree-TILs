import java.io.*;
import java.util.*;

public class Main {
	static int n,m,k;
	static StringTokenizer st;
	static int walls[][];
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		public String toString() {
			return y+" "+x;
		}
		public boolean isSame(Pair o) {
			return y == o.y && x == o.x;
		}
	}
	
	static Pair EXITED = new Pair(-1,-1);
	
	// 상하로 움직이는걸 우선시 
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static void printBoard(int board[][]) {
		int n = board.length - 1;
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	static Pair people[];
	static Pair exit;
	static int idBoard[][];
	static int exitedCnt;
	
	/*
	 * 모든 참가자들의 이동 
	 * 1. 이미 탈출한 참가자는 이동하지 않는다 
	 */
	static void moveAll() {
		for(int i = 1; i<=m; i++) {
			if(people[i] == EXITED)
				continue;
			move(i);
		}
	}
	
	/*
	 * idx에 해당하는 참가자의 이동 
	 * 1. 지금 위치보다 다음 위치가 출구까지의 최단거리가 가까워야함. 
	 * 2. 이동 후, people 업데이트도 해야하지만 idBoard 업데이트도 해야함 
	 */
	static int moveCnt;
	static void move(int idx) {
		Pair person = people[idx];
		
		int y = person.y;
		int x = person.x;
		idBoard[y][x] -= (1<<idx); // 기존 위치에선 제거 되고 
		
		Pair nxtPos = getNxtPos(y,x); // 다음 위치 
		if(!nxtPos.isSame(person))
			moveCnt++;
		
		if(nxtPos.isSame(exit)) { // 출구에 도달하면 즉시 탈출 
			people[idx] = EXITED;
			exitedCnt++;
			return;
		}
		idBoard[nxtPos.y][nxtPos.x] += (1<<idx); // 새로운 위치엔 더해짐 
		people[idx] = nxtPos;
		
	}
	
	static Pair getNxtPos(int y,int x) {
		Pair ret = new Pair(y,x); // 움직일 수 없는 상황이면 그대로 리턴 
		int best = getDistance(y,x, exit.y, exit.x);
		
		for(int dir = 0; dir < 4; dir++) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			if(OOB(ny,nx) || walls[ny][nx] != 0)
				continue;
			int dist = getDistance(ny,nx, exit.y, exit.x);
			if(best > dist) {
				best = dist;
				ret = new Pair(ny,nx);
			}
		}
		
		return ret;
	}
	
	static int getDistance(int y1, int x1,int y2,int x2) {
		return Math.abs(y1-y2) + Math.abs(x1-x2);
	}

	static class Square{
		int sy,sx; // 좌상단 좌표 
		int size; // 한 변의 길이 
		public Square(int sy,int sx, int size) {
			this.sy = sy;
			this.sx = sx;
			this.size = size;
		}
		
		public String toString() {
			return String.format("(%d,%d) : %d", sy,sx,size);
		}
		
		public boolean isHigher(Square o) {
			if(size != o.size)
				return size < o.size;
			if(sy != o.sy)
				return sy < o.sy;
			return sx < o.sx;
		}
	}
	
	static Square WORST_SQUARE = new Square(12,12,12);
	
	
	static void rotate() {
		Square best = findBestSquare();
		
//		System.out.println("bestSquare: "+best);
		
		int sy = best.sy;
		int sx = best.sx;
		int size = best.size;
		
		
		/*
		 * 회전 
		 * 1. 정사각형 안의 사람, 출구, 벽이 회전됨 
		 *  1.1 idBoard의 회전 
		 *  1.2 walls의 회전 
		 *  1.3 idBoard회전에 따른 people업데이트
		 *  1.4 idBoard회전에 따른 exit업데이트 
		 */
		int idTemp[][] = new int[size + 1][size+1];
		int wallTemp[][] = new int[size + 1][size + 1];
		for(int y=sy; y<=sy+size-1;y++) {
			for(int x=sx; x<=sx+size-1; x++) {
				int y1 = y - sy + 1;
				int x1 = x - sx + 1;
				
				int y2 = x1;
				int x2 = size + 1 - y1;
				
				idTemp[y2][x2] = idBoard[y][x];
				wallTemp[y2][x2] = walls[y][x] > 0 ? walls[y][x] - 1 : 0; // 회전된 벽은 내구도가 깎임 
				
			}
		}
		
		for(int y=1; y<=size; y++) {
			for(int x=1; x<=size;x++) {
				int y3 = y + sy - 1;
				int x3 = x + sx - 1;
				idBoard[y3][x3] = idTemp[y][x];
				walls[y3][x3] = wallTemp[y][x];
				
				int bit = 0;
				if( (idBoard[y3][x3] & (1<<bit) )!= 0 )
					exit = new Pair(y3,x3);
				
				for(bit = 1; bit <= m; bit++) {
					if( (idBoard[y3][x3] & (1<<bit) )!= 0  && people[bit] != EXITED) {
						people[bit] = new Pair(y3,x3);
					}
				}
				
			}
		}
		
//		printBoard(idTemp);
//		printBoard(wallTemp);
		
	}
	
	static Square findBestSquare() {
		Square ret = WORST_SQUARE;
		for(int i = 1; i<=m; i++) {
			if(people[i] == EXITED)
				continue;
			Square square = getSquare(i);
			if(square.isHigher(ret))
				ret = square;
		}
		return ret;
	}
	
	static Square getSquare(int idx) {
		Pair p = people[idx];
		int ey = exit.y;
		int ex = exit.x;
		
		int size = Math.max(Math.abs(p.y-ey), Math.abs(p.x-ex)) + 1;
		
		int y = Math.max(p.y, ey) - size + 1;
		int x = Math.max(p.x, ex) - size + 1;
		if(y <= 0)
			y = 1;
		if(x <= 0)
			x = 1;
		
		return new Square(y,x,size);
		
	}
	
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        walls = new int[n+1][n+1];
        idBoard = new int[n+1][n+1];
        for(int y=1; y<=n; y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x=1; x<=n; x++) {
        		walls[y][x] = Integer.parseInt(st.nextToken());
        	}
        }
        people = new Pair[m+1];
        for(int i = 1; i<=m; i++) {
        	st = new StringTokenizer(br.readLine());
        	int y = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken());
        	people[i] = new Pair(y,x);
        	idBoard[y][x] += (1<<i); // 한 칸에 여러명이 있을 수 있음. 
        }
        
        st = new StringTokenizer(br.readLine());
        exit = new Pair(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        idBoard[exit.y][exit.x] = (1<<0);
//        printBoard(idBoard);
        for(int turn = 1; turn<=k; turn++) {
        	if(exitedCnt == m)
        		break;
//        	System.out.println("-----");
//        	System.out.println("turn: "+turn);
//        	System.out.println("exit: "+exit);
        	// 모든 참가자들의 이동 
        	moveAll();
//        	System.out.println("after move");
//        	printBoard(idBoard);
//        	System.out.println("people: "+Arrays.toString(people));
//        	System.out.println("moveCnt: "+moveCnt);
        	// 미로 회전 
        	rotate();
//        	System.out.println("after rotate");
//        	System.out.println("idBoard");
//        	printBoard(idBoard);
//        	System.out.println("walls");
//        	printBoard(walls);
//        	System.out.println("exit: "+exit);
//        	System.out.println("people: "+Arrays.toString(people));
        }
        System.out.println(moveCnt);
        System.out.println(exit.y+" "+exit.x);
    }
}