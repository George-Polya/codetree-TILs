import java.util.*;
import java.io.*;

public class Main {
	static int n,k;
	static StringTokenizer st;
	static int color[][];
	static int WHITE = 0, RED = 1, BLUE = 2;
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static int dy[] = {0,0,-1,1};
	static int dx[] = {1,-1,0,0};
	
	static int turn ;
	
	static void printBoard() {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%3d", board[y][x].size());
			}
			System.out.println();
		}
	}
	
	static class Horse{
		int y,x,dir;
		
		public Horse(int y,int x,int dir) {
			this.y = y;
			this.x = x;
			this.dir = dir;
		}
		
		public String toString() {
			return y+" "+x+" "+dir;
		}
	}
	
	static Stack<Integer> board[][];
	
	static Horse horses[];
	
	static Horse getNxtPos(int y,int x,int dir) {
		int ny = y + dy[dir];
		int nx = x + dx[dir];
		if(OOB(ny,nx) || color[ny][nx] == BLUE) {
//			System.out.printf("(ny,nx): (%d,%d)\n", ny,nx);
			dir = dir % 2 == 0 ? dir + 1 : dir - 1;
			ny = y + dy[dir];
			nx = x + dx[dir];
//			System.out.printf("(ny,nx): (%d,%d)\n", ny,nx);
			if(OOB(ny,nx) || color[ny][nx] == BLUE) {
				return new Horse(y,x,dir);
			}else {
				return new Horse(ny,nx,dir);
			}
		}
		
		return new Horse(ny,nx,dir);
		
	}
	
	static String printStack(Stack<Integer> stk) {
		List<Integer> list = new ArrayList<>(stk);
		StringBuilder sb = new StringBuilder();
		for(int id : list) {
			sb.append(String.format("id: %d, dir: %d|", id, horses[id].dir));
		}
		return sb.toString();
	}
	
	static boolean move(int id) {
		Horse horse = horses[id];
		int y = horse.y;
		int x = horse.x;
		int dir = horse.dir;
	
		
		int ny = y + dy[dir];
		int nx = x + dx[dir];
		
//		System.out.printf("board[%d][%d] : %s\n", y,x, printStack(board[y][x]));
		Stack<Integer> temp = new Stack<>();
		while(board[y][x].peek() != id) {
			temp.push(board[y][x].pop());
		}
		
		int target = board[y][x].pop();
		
		if(OOB(ny,nx) || color[ny][nx] == BLUE) {
			dir = (dir %2 == 0) ? dir + 1 : dir - 1;
			ny = y + dy[dir];
			nx = x + dx[dir];
			horses[id].dir = dir;
			if(OOB(ny,nx) || color[ny][nx] == BLUE) {
				temp.push(id);
				
				while(!temp.isEmpty()) {
					int cur = temp.pop();
					board[y][x].push(cur);
				}
//				System.out.printf("board[%d][%d] : %s\n", y,x, printStack(board[y][x]));
				return false;
			}
		}
		
		temp.push(target);
//		System.out.printf("board[%d][%d] : %s\n", ny,nx, printStack(temp));
		if(color[ny][nx] == RED) {
			Collections.reverse(temp);
		}
		
		while(!temp.isEmpty()) {
			int idx = temp.pop();
			Horse cur = horses[idx];
			cur.y = ny;
			cur.x = nx;
			board[cur.y][cur.x].push(idx);
			if(board[ny][nx].size() >= 4)
				return true;
		}
		
		
		return false;
		
	}
	
	static boolean moveAll() {
//		System.out.println("-----");
//		System.out.println("turn: "+turn);
		for(int id = 1; id<=k; id++) {
//			System.out.println("==========");
//			System.out.println("id: "+id);
			boolean moved = move(id);
			if(moved)
				return false;
		}
		return true;
	}
	
 	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		color = new int[n+1][n+1];
		board = new Stack[n+1][n+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				color[y][x] = Integer.parseInt(st.nextToken());
				board[y][x] = new Stack<>();
			}
		}
		
		horses = new Horse[k+1];
		
		for(int id =1;id<=k;id++) {
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken()) - 1;
			Horse horse = new Horse(y,x,dir);
			horses[id] = horse;
			board[y][x].add(id);
		}
		
		
		int ans = -1;
		for(turn = 1; turn<=1000;turn++) {
			if(!moveAll()) {
				System.out.println(turn);
				System.exit(0);
//				ans = turn;
			}
		}
//		moveAll();
		
		System.out.println(ans);
		
	}
    
}