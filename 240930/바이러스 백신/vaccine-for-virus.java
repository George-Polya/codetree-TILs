import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        board = new int[n+1][n+1];
        dist = new int[n+1][n+1];
        for(int y=1; y<=n;y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x=1; x<=n; x++) {
        		board[y][x] = Integer.parseInt(st.nextToken());
        		if(board[y][x] == 2) {
        			hospitals.add(new Pair(y,x));
        		}
        	}
        }
        
        selected = new Pair[m];
        solve(0,0);
        System.out.println(ans == INT_MAX ? -1 : ans);
    }
    
    static int bfs() {
    	for(int y=1; y<=n; y++) {
    		Arrays.fill(dist[y], INT_MAX);
    	}
    	
    	Queue<Pair> q = new ArrayDeque<>();
    	for(Pair p : selected) {
    		dist[p.y][p.x] = 0;
    		q.add(p);
    	}
    	
    	while(!q.isEmpty()) {
    		Pair cur = q.poll();
    		
    		for(int dir = 0; dir < 4; dir++) {
    			int ny = cur.y + dy[dir];
    			int nx = cur.x + dx[dir];
    			
    			if(OOB(ny,nx) || dist[ny][nx] != INT_MAX || board[ny][nx] == WALL)
    				continue;
    			
    			dist[ny][nx] = dist[cur.y][cur.x] + 1;
    			q.add(new Pair(ny,nx));
    		}
    	}
    	
    	int ret = 0; 
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(board[y][x] == VIRUS) {
    				ret = Math.max(ret,  dist[y][x]);
    			}
    		}
    	}
    	
    	return ret;
    	
    	
    }
    
    static int VIRUS = 0;
    static int WALL = 1;
    
    static int dy[] = {-1,1,0,0};
    static int dx[] = {0,0,-1,1};
    static boolean OOB(int y,int x) {
    	return y<=0 || y>n || x<=0 || x>n;
    }
    
    static void solve(int cur,int cnt) {
    	if(cnt == m) {
    		ans = Math.min(ans,  bfs());
    		return;
    	}
    	
    	
//    	if(cur == hospitals.size())
//    		return;
//    	
//    	// 현재 병원을 고를 경우  경우 
//    	selected[cnt] = hospitals.get(cur);
//    	solve(cur + 1, cnt + 1);
////    	selected[cnt] = null;
//    	
//    	solve(cur + 1, cnt);
    	
    	for(int i = cur; i < hospitals.size(); i++) {
    		selected[cnt] = hospitals.get(i);
    		solve(i + 1, cnt + 1);
    	}
    	
    }
    
    static int n,m;
    static int INT_MAX = Integer.MAX_VALUE;
    static int ans = INT_MAX;
    static int dist[][];
    static ArrayList<Pair> hospitals = new ArrayList<>();
    static Pair selected[];
    static StringTokenizer st;
    static int board[][];
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
}