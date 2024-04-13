import java.io.*;
import java.util.*;

public class Main{
	static int n,m;
	static int dy[] = {0,-1,-1,-1,0,1,1,1};
	static int dx[] = {1,1,0,-1,-1,-1,0,1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<= 0 || x>n;
	}
	static StringTokenizer st;
	static boolean fertilized[][];
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
	static void printBoard() {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.print(board[y][x] +" ");
			}
			System.out.println();
		}
	}
	
	static int board[][];
	
	static List<Pair> fertilizers = new ArrayList<>(); 
	
	/*
	 * 영양제의 다음 위치 구하기 
	 * 1. (ny,nx)를 구한다 
	 * 2. OOB(ny,nx)면 반대편으로 
	 * 3. OOB가 아니면 원래 그 위
	 */
	static Pair getNxtPos(int y,int x, int d, int p) {

		// 여기는 좀 바꿔보자 
		int ny = y + dy[d] * (p % n);
		int nx = x + dx[d] * (p % n);

		// 방법 1. 격자 밖을 벗어난 경우 처리 
//		if(OOB(ny,nx)) {
//			if (ny > n)
//				ny -= n;
//			if(ny <= 0)
//				ny += n;
//			
//			if(nx > n )
//				nx -= n;
//			if(nx<=0)
//				nx += n;
//		}
//		
		
		
		// 방법 2. 모든 경우에 대해서 처리 
		ny = ((ny - 1 + n) % n) + 1;
		nx = ((nx - 1 + n) % n) + 1;
		
		return new Pair(ny,nx);
	}
	
	/*
	 * 1.1 idx에 해당하는 영양제가 주어진 방향과 거리만큼 이동한다 
	 *  1.1.1 영양제의 다음 위치를 구한다 
	 *  1.1.2 fertilizers[idx]를 해당 위치로 교체한다 
	 */
	
	static void move(int idx, int d, int p) {
		Pair fertilizer = fertilizers.get(idx);
		
		int y = fertilizer.y;
		int x = fertilizer.x;
		
		Pair nxt = getNxtPos(y,x, d, p);
//		System.out.println("nxt: "+nxt);
		fertilizers.set(idx, nxt);
	}
	
	/*
	 * 1. 영양제 이동 
	 *  1.1 각 영양제가 주어진 방향과 거리만큼 이동한다 
	 */
	
	static void moveAll(int d, int p) {
		for(int i = 0; i < fertilizers.size();i++) {
			move(i, d, p);
		}
	}
	
	/*
	 * 2.1 (y,x)의 나무 성장 
	 */
	static void grow(int y,int x) {
		board[y][x] += 1;
	}
	
	/*
	 * 2. 나무 성장 
	 *  2.1 영양제가 위치한 곳의 나무가 성장함
	 *  2.2 대각선 나무가 성장함  
	 *  2.3 영양제 소멸 
	 */
	static void growAll() {
		// 2.1
		for(Pair fertilizer : fertilizers) {
			grow(fertilizer.y, fertilizer.x);
		}
		
		
		List<Integer> counts = new ArrayList<>();
		// 2.2 
		for(int idx = 0; idx < fertilizers.size();idx++) {
			Pair fertilizer = fertilizers.get(idx);
			int y = fertilizer.y;
			int x = fertilizer.x;
			fertilized[y][x] = true;
			int cnt = 0;
			for(int dir : new int[] {1,3,5,7}) {
				int ny = y + dy[dir];
				int nx = x + dx[dir];
				if(OOB(ny,nx) || board[ny][nx] <= 0)
					continue;
				cnt++;
			}
			counts.add(cnt);
		}
		
		
		for(int idx = 0; idx < fertilizers.size();idx++) {
			Pair fertilizer = fertilizers.get(idx);
			int y = fertilizer.y;
			int x = fertilizer.x;
			board[y][x] += counts.get(idx);
		}
		
		fertilizers.clear();
		
	}
	
	static void cut() {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				if(fertilized[y][x]) { // 영양제를 투입한 나무는 제외한다 
					fertilized[y][x] = false;
					continue;
				}
				
				// 높이 2이상인 나무는 베어내고 영양제를 올려둔
				if(board[y][x] >= 2) {
					board[y][x] -= 2;
					fertilizers.add(new Pair(y,x));
				}
			}
		}
	}
	
	static void simulate(int d, int p) {
		// 1. 영양제 이동 
		moveAll(d,p);
		// 2. 영양제 투입 (나무 성장)  
		growAll();
//		System.out.println("after grow");
//		printBoard();
		
		// 3. 영양제 구매
		cut();
//		System.out.println("after cut");
//		printBoard();
//		System.out.println(fertilizers);
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		board = new int[n+1][n+1];
		fertilized = new boolean[n+1][n+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		fertilizers.add(new Pair(n-1,1));
		fertilizers.add(new Pair(n-1,2));
		fertilizers.add(new Pair(n,1));
		fertilizers.add(new Pair(n,2));
		
		
		for(int turn=1;turn<=m;turn++) {
//			System.out.println("turn: "+turn);
			st = new StringTokenizer(br.readLine());
			int d = Integer.parseInt(st.nextToken()) - 1;
			int p = Integer.parseInt(st.nextToken());
			simulate(d,p);
//			System.out.println("-----");
		}
		
		int sum = 0;
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				sum += board[y][x];
			}
		}
		System.out.println(sum);
		
		
	}
}