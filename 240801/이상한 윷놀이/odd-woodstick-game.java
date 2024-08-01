import java.util.*;
import java.io.*;

public class Main {
	static int n,k;
	static StringTokenizer st;
	static int board[][], count[][];
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
				System.out.printf("%3d", stks[y][x].size());
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
	
	static Stack<Integer> stks[][];
	
	static Horse horses[];
	
	static boolean end() {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				if(stks[y][x].size() >= 4)
					return true;
			}
		}
		return false;
	}
	
	static Horse getNxtPos(int y,int x,int dir) {
		int ny = y + dy[dir];
		int nx = x + dx[dir];
		if(OOB(ny,nx) || board[ny][nx] == BLUE) {
//			System.out.printf("(ny,nx): (%d,%d)\n", ny,nx);
			dir = dir % 2 == 0 ? dir + 1 : dir - 1;
			ny = y + dy[dir];
			nx = x + dx[dir];
//			System.out.printf("(ny,nx): (%d,%d)\n", ny,nx);
			if(OOB(ny,nx) || board[ny][nx] == BLUE) {
				return new Horse(y,x,dir);
			}else {
				return new Horse(ny,nx,dir);
			}
		}
		
		return new Horse(ny,nx,dir);
		
	}
	
	static void move(int id) {
		Horse horse = horses[id];
		int y = horse.y;
		int x = horse.x;
		int dir = horse.dir;
//		System.out.println("horse: "+horse);
		Horse nxt = getNxtPos(y,x,dir);
//		System.out.println("nxt: "+nxt);
		Stack<Integer> stk = stks[y][x];
		Stack<Integer> temp = new Stack<>();
//		System.out.println("color: " + board[nxt.y][nxt.x]);
//		if(board[nxt.y][nxt.x] == RED) {
//			while(!stk.isEmpty())
//				temp.add(stk.pop());
//			
//			stk = temp;
//		}
//		System.out.printf("stks[%d][%d] : %s\n", y,x, stk);
		temp = new Stack<>();
		int size = stk.size();
		
		for(int i = 0; i < size;i++) {
			int cur = stk.pop();
			horses[cur].y = nxt.y;
			horses[cur].x = nxt.x;
			
			if(cur != id) {
				temp.add(cur);
			}else {
				horses[cur].dir = nxt.dir;
				temp.add(cur);
				break;
			}
		}
		
		stk = new Stack<>();
		if(board[nxt.y][nxt.x] == RED) {
			while(!temp.isEmpty())
				stk.add(temp.pop());
		}else {
			stk = temp;
		}
		
		while(!stk.isEmpty()) {
			stks[nxt.y][nxt.x].add(stk.pop());
		}
//		System.out.printf("stks[%d][%d] : %s\n", nxt.y,nxt.x, stks[nxt.y][nxt.x]);
	}
	
	static boolean moveAll() {
		for(int id = 1; id<=k; id++) {
			if (end())
				return false;
//			System.out.println("-----");
//			System.out.println("id: "+id);
			move(id);
//			printBoard();
		}
		return true;
	}
	
 	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		board = new int[n+1][n+1];
		count = new int[n+1][n+1];
		stks = new Stack[n+1][n+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
				stks[y][x] = new Stack<>();
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
			stks[y][x].add(id);
//			count[y][x]++;
		}
		
		
//		moveAll();
//		while(moveAll()) {
//			turn++;
////			printBoard();
//		}
		
		while(!end()) {
			turn++;
			if(!moveAll())
				break;
		}
		
		System.out.println(turn > 1000 ? -1 : turn);
		
	}
    
}