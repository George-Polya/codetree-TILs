// package samsung;
import java.util.*;
import java.io.*;

class Pair{
	int y,x;
	public Pair(int y,int x) {
		this.y = y;
		this.x = x;
	}
}

public class Main{
	static StringTokenizer st;
	static int n,m;
	static final int VIRUS = 0;
	static final int WALL = 1;
	static final int HOSPITAL = 2;
	
	static int board[][], dist[][];
	
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	
	static ArrayList<Pair> hospitals = new ArrayList<>();
	static ArrayList<Pair> selected = new ArrayList<>();
	static int ans = Integer.MAX_VALUE;
	static Queue<Pair> q;
	
	static void initialize() {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				dist[y][x] = -1;
			}
		}
		
		q = new LinkedList<>(selected);
		for(Pair pos : selected)
			dist[pos.y][pos.x] = 0;
		
	}
	
	static int bfs() {
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			
			
			for(int dir = 0; dir<4;dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				if(OOB(ny,nx) || dist[ny][nx] != -1 || board[ny][nx] == WALL)
					continue;
				dist[ny][nx] = dist[cur.y][cur.x] + 1;
				q.offer(new Pair(ny,nx));
			}
			
		}
		
		int time = 0;
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				if(board[y][x] == VIRUS) {
					if(dist[y][x] == -1)
						return Integer.MAX_VALUE;
					else
						time = Math.max(time, dist[y][x]);
				}
			}
		}
		
		
		return time;
	}
	
	
	static void solve(int cur, int cnt) {
		if(cnt == m) {
			initialize();
			
			int time = bfs();
			
			
			ans = Math.min(ans,  time);
			
			return;
		}
		
		if(cur == hospitals.size())
			return;
		
		selected.add(hospitals.get(cur));
		solve(cur + 1, cnt + 1);
		selected.remove(selected.size() - 1);
		
		solve(cur + 1, cnt);
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		board = new int[n+1][n+1];
		dist = new int[n+1][n+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
				if(board[y][x] == HOSPITAL)
					hospitals.add(new Pair(y,x));
			}
		}
		
		solve(0,0);
		if(ans == Integer.MAX_VALUE)
			ans = -1;
		System.out.println(ans);
		
	}
}