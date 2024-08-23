//  package samsung;
import java.util.*;
import java.io.*;

public class Main {
	static int n;
	static int MAX_R = 100;
	static boolean board[][] = new boolean[MAX_R+1][MAX_R+1];
	
	
	static int dy[] = {0,-1,0,1};
	static int dx[] = {1,0,-1,0};
	
	
	static void makeDragonCurve(int y,int x, int d, int level) {
		ArrayDeque<Integer> deq = new ArrayDeque<>();
		board[y][x] = true;
		y = y + dy[d];
		x = x + dx[d];
		board[y][x] = true;
		deq.addFirst(d);
		
		Queue<Integer> q = new LinkedList<>();
		for(int l = 1; l<=level; l++) {
			q.addAll(deq);
			while(!q.isEmpty()) {
				int dir = q.poll();
				dir = (dir + 1) % 4;
				y = y + dy[dir];
				x = x + dx[dir];
				board[y][x] = true;
				deq.addFirst(dir);
			}
		}
		
	}
	
	static StringTokenizer st;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		for(int i = 0; i<n;i++) {
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			int g = Integer.parseInt(st.nextToken());
			makeDragonCurve(y,x,d,g);
		}
		int ans = 0;
		for(int y = 1; y<=MAX_R; y++) {
			for(int x=1; x<=MAX_R; x++ ) {
				if(board[y][x] && board[y-1][x] && board[y][x-1] && board[y-1][x-1])
					ans++;
			}
		}
		
		System.out.println(ans);
	}
}