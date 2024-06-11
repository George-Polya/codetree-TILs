import java.util.*;
import java.io.*;
public class Main {
	static int r,c,k;
    static StringTokenizer st;
    static int board[][];
    static boolean isExit[][];
    
    static boolean OOB(int y,int x) {
    	return y <= 3 || y > r+3 || x <= 0 || x>c;
    }
    
    static int dy[] = {-1,0,1,0};
    static int dx[] = {0,1,0,-1};
    static int ans;
    
    static boolean canGo(int y, int x) {
    	boolean flag = 1<=x-1 && x+1<=c && y + 1 <= r + 3;
    	
    	flag = flag && (board[y-1][x-1] ==0);
    	flag = flag && (board[y-1][x] == 0);
    	flag = flag && (board[y-1][x+1] == 0);
    	
    	flag = flag && (board[y][x-1] == 0);
    	flag = flag && (board[y][x] == 0);
    	flag = flag && (board[y][x+1] == 0);
    	
    	flag = flag && (board[y+1][x] == 0);
    	return flag;
    }
    
    static class Pair{
    	int y,x;
    	public Pair(int y,int x) {
    		this.y = y;
    		this.x = x;
    	}
    }
    
    static int bfs(int y, int x) {
    	Queue<Pair> q = new LinkedList<>();
    	boolean visited[][] = new boolean[r+1+3][c+1];
    	visited[y][x] = true;
    	q.add(new Pair(y,x));
    	int ret = y;
    	
    	while(!q.isEmpty()) {
    		Pair cur = q.poll();
    		ret = Math.max(ret, cur.y);
    		
    		for(int dir = 0; dir< 4;dir++) {
    			int ny = cur.y + dy[dir];
    			int nx = cur.x + dx[dir];
    			if(OOB(ny,nx) || visited[ny][nx] || board[ny][nx] == 0)
    				continue;
    			
    			if(board[ny][nx] == board[cur.y][cur.x] || isExit[cur.y][cur.x]) {
    				visited[ny][nx] = true;
    				q.add(new Pair(ny,nx));
    			}
    		}
    	}
    	
    	
    	return ret;
    }
    
    static void down(int y,int x, int d, int id) {
    	if(canGo(y+1,x)) {
    		down(y+1,x,d,id);
    	}else if(canGo(y+1,x-1)) {
    		down(y+1,x-1,(d+3)%4, id);
    	}else if(canGo(y+1,x+1)) {
    		down(y+1,x+1, (d+1)%4, id);
    	}else {
    		if(OOB(y-1,x-1) || OOB(y+1,x+1)) {
    			for(int i=1; i<=r+3;i++) {
    				Arrays.fill(board[i], 0);
    				Arrays.fill(isExit[i], false);
    			}
    		}else {
    			board[y][x] = id;
    			for(int dir = 0; dir < 4; dir++) {
    				board[y + dy[dir]][x+dx[dir]] = id;
    			}
    			
    			isExit[y + dy[d]][x + dx[d]] = true;
    			ans += bfs(y,x) - 3;
    		}
    	}
    }
    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	r = Integer.parseInt(st.nextToken());
    	c = Integer.parseInt(st.nextToken());
    	k = Integer.parseInt(st.nextToken());
    	
    	board = new int[r+1+3][c+1];
    	isExit = new boolean[r+1+3][c+1];
    	
    	for(int id=1;id<=k;id++) {
    		st = new StringTokenizer(br.readLine());
    		int x = Integer.parseInt(st.nextToken());
    		int d = Integer.parseInt(st.nextToken());
    		down(2,x,d,id);
    	}
    	System.out.println(ans);
    }
}