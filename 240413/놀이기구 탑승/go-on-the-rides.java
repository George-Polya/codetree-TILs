import java.io.*;
import java.util.*;

public class Main{
	static int n;
	static int board[][];
	static StringTokenizer st;
	static Set<Integer> table[];
	static int arr[];
	
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static class Cell{
		int y,x;
		int friendCnt, emptyCnt;
		
		public Cell(int y,int x, int friendCnt, int emptyCnt) {
			this.y = y;
			this.x = x;
			this.friendCnt = friendCnt;
			this.emptyCnt = emptyCnt;
		}
		
		public boolean isHigher(Cell c) {
			if(friendCnt != c.friendCnt)
				return friendCnt > c.friendCnt;
			if(emptyCnt != c.emptyCnt)
				return emptyCnt > c.emptyCnt;
				
			if(y != c.y)
				return y < c.y;
			
			return x < c.x;
		}
		
		public String toString() {
			return String.format("y: %d x: %d friendCnt: %d emptyCnt: %d", y,x,friendCnt, emptyCnt);
		}
	}
	
	static Cell NO_CELL = new Cell(22,22,-1,-1);
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.print(board[y][x]+" ");
			}
			System.out.println();
		}
	}
	
	
	static Cell makeCell(int y,int x, int id) {
		int friendCnt = 0;
		
		int emptyCnt = 0;
		for(int dir = 0; dir<4; dir++) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			if(OOB(ny,nx))
				continue;
			if(table[id].contains(board[ny][nx]))
				friendCnt++;
			if(board[ny][nx] == 0)
				emptyCnt++;
		}
		
		return new Cell(y,x,friendCnt, emptyCnt);
		
	}
	
	/*
	 * 우선순위가 가장 높은 칸 찾기 
	 * 1. 좋아하는 친구의 수가 가장 많은 위치 찾기 
	 * 2. 그 중 인접한 비어있는 칸의 수가 가장 많은 위치 
	 * 3. y작은거 먼저 x작은거 먼저  
	 */
	static Cell getBestCell(int id) {
		Cell bestCell = NO_CELL;
		
		for(int y=1;y<=n; y++) {
			for(int x=1; x<=n;x++) {
				if(board[y][x] != 0)
					continue;
				Cell cell = makeCell(y,x,id);
				if(cell.isHigher(bestCell)) {
					bestCell = cell;
				}
				
			}
		}
		return bestCell;
	}
	
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
	
	static Pair id2pair[];
	
	static void ride(int id) {
		Cell best = getBestCell(id);
//		System.out.println("best: "+best);
		board[best.y][best.x] = id;
		id2pair[id] = new Pair(best.y,best.x);
	}
	
	static int scores[] = {0,1,10,100,1000};
	
	static int getFriendCnt(int id) {
		Pair pair = id2pair[id];
		int y = pair.y;
		int x = pair.x;
		
		int friendCnt = 0;
		for(int dir = 0; dir< 4; dir++) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			if(OOB(ny,nx))
				continue;
			
			if(table[id].contains(board[ny][nx]))
				friendCnt++;
		}
		return friendCnt;
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		table = new Set[n*n+1];
		arr = new int[n*n+1];
		board = new int[n+1][n+1];
		id2pair = new Pair[n*n+1];
		
		for(int i = 1; i <=n * n;i++) {
			st = new StringTokenizer(br.readLine());
			int id = Integer.parseInt(st.nextToken());
			table[id] = new HashSet<>();
			arr[i] = id;
			for(int j = 0; j < 4; j++) {
				table[id].add(Integer.parseInt(st.nextToken()));
			}
		}
		
		for(int i = 1; i<=n*n;i++) {
			int id = arr[i];
//			System.out.println("id: "+id);
			
			ride(id);
//			printBoard(board);
//			System.out.println("-----");
			
		}
		
		
		int sum = 0;
		for(int i=1; i<=n*n;i++) {
			int id = arr[i];
			int cnt = getFriendCnt(id);
			sum += scores[cnt];
		}
		System.out.println(sum);
		
		
	}
}