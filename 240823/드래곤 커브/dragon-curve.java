import java.io.*;
import java.util.*;

public class Main{
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		for(int i = 1; i<=n; i++) {
//			System.out.println("-----");
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			int g = Integer.parseInt(st.nextToken());
			makeDragonCurve(y,x,d,g);
		}
		
		int ans = 0;
		for(int y= 0; y<=MAX_R;y++) {
			for(int x= 0; x<=MAX_R;x++) {
				if(check(y,x))
					ans++;
			}
		}
		System.out.println(ans);
	}
	static int n;
	static StringTokenizer st;
	static int MAX_R = 100;
	static boolean board[][] = new boolean[MAX_R+1][MAX_R+1];
	static int dy[] = {0,-1,0,1};
	static int dx[] = {1,0,-1,0};
	static void makeDragonCurve(int y,int x,int d, int g) {
		board[y][x] = true;
		int ny = y + dy[d];
		int nx = x + dx[d];
		board[ny][nx] = true;
		if(g == 0) 
			return;
		y = ny;
		x = nx;
		Deque<Integer> deq = new ArrayDeque<>();
		deq.add(d);
		Queue<Integer> q = new LinkedList<>();
		for(int turn = 1; turn<=g; turn++) {
//			System.out.println("=====");
//			System.out.println("turn: "+turn);
			int size = deq.size();
//			System.out.println("before: "+deq);
			for(int i = 0; i < size; i++) {
				int dir = deq.pollFirst();
				int nxtDir = (dir + 1) % 4;
				deq.addLast(dir);
				q.add(nxtDir);
			}
			
//			System.out.println("q: "+q);
			
			while(!q.isEmpty()) {
				int dir = q.poll();
				y = y + dy[dir];
				x = x + dx[dir];
				board[y][x] = true;
				deq.addFirst(dir);
			}
			
//			System.out.println("after: "+deq);
		}
//		System.out.printf("(%d, %d)\n", y,x);
	}
	
	static boolean check(int y,int x) {
		return board[y][x] && board[y+1][x] && board[y][x+1] && board[y+1][x+1];
	}
}