import java.io.*;
import java.util.*;


public class Main {
	static int n,m;
	static int board[][];
	static StringTokenizer st;
	static int dy[] = {0,-1,-1,-1,0,1,1,1};
	static int dx[] = {1,1,0,-1,-1,-1,0,1};
	static boolean OOB(int y, int x) {
		return y<=0 || y>n || x<=0 || x>n;
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
	static int total;
	static Deque<Pair> fertils = new ArrayDeque<>();
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	static void move(Pair pair, int dir, int dist) {
		int y = pair.y;
		int x = pair.x;

		int ny = y + dy[dir] * (dist % n);
		int nx = x + dx[dir] * (dist % n);
		
//		while(OOB(ny,nx)) {
//			if(ny <= 0)
//				ny += n;
//			else if(ny > n)
//				ny -= n;
//			
//			if(nx <= 0)
//				nx += n;
//			else if(nx > n)
//				nx -= n;
//				
//		}
		
		
		ny = ((ny - 1 + n) % n) + 1;
		nx = ((nx - 1 + n) % n) + 1;
		
//		pair.y = ny;
//		pair.x = nx;
		
		fertils.addLast(new Pair(ny,nx));
		
	}
	
	static void grow(int y, int x) {
		total++;
		board[y][x] += 1;
	}
	static void extraGrow(int y, int x) {
		int cnt = 0;
		for(int dir = 1; dir <8; dir+=2) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			if(OOB(ny,nx))
				continue;
			if(board[ny][nx] >= 1)
				cnt++;
		}
		total+=cnt;
		board[y][x] += cnt;
	}
	
	static boolean fertilized[][];
	
	static void simulate(int dir, int dist) {
		
		// 영양제의 이동과 나무의 성장 
//		for(int i = 0; i < fertils.size(); i++) {
//			
//			// 이동 
//			Pair pair = fertils.get(i);
//			move(pair, dir, dist);
//			fertilized[pair.y][pair.x] = true;
//			// 투입해서 성장 
//			grow(pair.y, pair.x);
//			
//		}
		
		int size = fertils.size();
		for(int i = 0; i< size;i++) {
			Pair pair = fertils.pollFirst();
			move(pair, dir, dist);
			grow(pair.y, pair.x);
			fertils.addFirst(pair);
		}
		
		
		// 추가 성장
		for(int i = 0 ; i < size;i++) {
			Pair pair = fertils.pollFirst();
			extraGrow(pair.y,pair.x);
		}
		
//		fertils.clear(); // 영양제 소멸 
		
		// 영양제를 투입한 위치를 제외하고 높이 2이상인 리브로수 제거 후 특수영양제 추가 
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				if(fertilized[y][x]) {
					fertilized[y][x] = false;
					continue;
				}
				if(board[y][x] >= 2) {
					board[y][x] -= 2;
					fertils.add(new Pair(y,x));
					total -= 2;
				}
			}
		}
	
		
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
				total += board[y][x];
			}
		}
		
		
		for(int y = n-1; y<=n; y++) {
			for(int x=1; x<=2;x++)
				fertils.add(new Pair(y,x));
		}
		
		for(int turn = 1; turn<=m; turn++) {
			st = new StringTokenizer(br.readLine());
			int d = Integer.parseInt(st.nextToken()) - 1;
			int p = Integer.parseInt(st.nextToken());
			simulate(d,p);
		}
		
		System.out.println(total);
	}
}