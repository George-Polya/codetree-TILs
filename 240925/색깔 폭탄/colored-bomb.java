import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        board = new int[n+1][n+1];
        for(int y=1; y<=n; y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x=1; x<=n; x++) {
        		board[y][x] = Integer.parseInt(st.nextToken());
        	}
        }
        
        
        while(true) {
//        	System.out.println("-----");
        	Group best = findBestGroup();
        	if(best == WORST)
        		break;
//        	System.out.println("best: "+best);
	        best.explode();
//	        System.out.println("after explode");
//	        printBoard(board);
	          
	        fall();
//	        System.out.println("after first fall");
//	        printBoard(board);
	          
	        rotate();
//	        System.out.println("after rotate");
//	        printBoard(board);
	          
	        fall();
//	        System.out.println("after second fall");
//	        printBoard(board);
        }
        
        System.out.println(ans);
        
//        System.out.println("-----");
//        Group best = findBestGroup();
//        System.out.println("best: "+ best);
//        best.explode();
//        System.out.println("after explode");
//        printBoard(board);
//        
//        fall();
//        System.out.println("after first fall");
//        printBoard(board);
//        
//        rotate();
//        System.out.println("after rotate");
//        printBoard(board);
//        
//        fall();
//        System.out.println("after second fall");
//        printBoard(board);
    }
    
    
    
    static void fall() {
    	int temp[][] = new int[n+1][n+1];
    	for(int y=1; y<=n; y++) {
    		Arrays.fill(temp[y], EMPTY);
    	}
    	
    	for(int x=1; x<=n; x++) {
    		int idx = n;
    		
    		for(int y=n; y>=1; y--) {
    			if(board[y][x] == -1)
    				idx = y;
    			
    			if(board[y][x] != -2) {
    				temp[idx][x] = board[y][x];
    				idx--;
    			}
    		}
    	}
    	
    	board = temp;
    }
    
    static void rotate() {
    	int temp[][] = new int[n+1][n+1];
    	
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			temp[y][x] = board[x][n + 1 - y];
    		}
    	}
    	board = temp;
    }
    
    static int ans;
    static Group findBestGroup() {
    	Group ret = WORST;
    	visited = new boolean[n+1][n+1];
    	
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			/*
    			 * EMPTY거나 BLACK이거나 RED면 시작점이 될 수 없음
    			 * 특히, RED일때 시작점이 될 수 없어야
    			 * 모두 같은 색깔이거나 RED를 포함해서 정확히 두 색깔로 이루어지게 됨
    			 * 또한, RED만으로 이루어질 수 없게됨
    			 */
    			if(visited[y][x] || board[y][x] <= 0) 
    				continue;
    			Group group = bfs(y,x);
//    			System.out.println("group: "+group);
    			if(group.isHigher(ret))
    				ret = group;
    		}
    	}
    	
    	return ret;
    }
    
    static Group bfs(int y,int x) {
    	int color = board[y][x];
    	Queue<Pair> q = new ArrayDeque<>();
    	visited[y][x] = true;
    	ArrayList<Pair> bombs = new ArrayList<>();
    	ArrayList<Pair> reds = new ArrayList<>();
    	Pair std = new Pair(y,x); // 기준점 
    	q.add(std);
    	
    	
    	while(!q.isEmpty()) {
    		Pair cur = q.poll();
    		
    		bombs.add(cur);
    		if(board[cur.y][cur.x] == RED)
    			reds.add(cur);
    		else if(cur.isHigher(std)) // 기준점은 RED가 아니어야함 
    			std = cur;
    		
    		for(int dir = 0; dir < 4; dir++) {
    			int ny = cur.y + dy[dir];
    			int nx = cur.x + dx[dir];
    			
    			if(OOB(ny,nx) || visited[ny][nx])
    				continue;
    			
    			if(board[ny][nx] <= -1) // BLACK이거나 EMPTY로는 이동못함 
    				continue;
    			
    			if(board[ny][nx] == color || board[ny][nx] == RED) {
    				visited[ny][nx] = true;
    				q.add(new Pair(ny,nx));
    			}
    		}
    		
    	}
    	
    	
    	for(Pair red : reds) {
    		visited[red.y][red.x] = false; // RED는 다음번에도 포함되어야 함 
    	}
    	
    	if(bombs.size() < 2) { // 2개 이상의 폭탄으로 구성되지 않았다면 
    		for(Pair bomb : bombs) {
    			visited[bomb.y][bomb.x] = false; // 복원 
    		}
    		
    		return WORST;
    	}
    	
    	return new Group(std, bombs, reds.size());
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
    	
    	public boolean isHigher(Pair o) {
    		if(y != o.y)
    			return y > o.y;
    		return x < o.x;
    	}
    }
    
    static Group WORST = new Group(new Pair(-1, 22), new ArrayList<Pair>(), 22 * 22);
    static class Group{
    	Pair std;
    	int size, redCnt;
    	ArrayList<Pair> bombs;
    	
    	public String toString() {
    		return String.format("%s | size: %d | redCnt: %d", std, size, redCnt);
    	}
    	
    	public Group(Pair std, ArrayList<Pair> bombs, int redCnt) {
    		this.std = std;
    		this.bombs = bombs;
    		this.size = bombs.size();
    		this.redCnt = redCnt;
    	}
    	
    	public boolean isHigher(Group o) {
    		if(size != o.size)
    			return size > o.size;
    		if(redCnt != o.redCnt)
    			return redCnt < o.redCnt;
    		return std.isHigher(o.std);
    	}
    	
    	public void explode() {
    		ans += size * size;
    		for(Pair bomb : bombs) {
    			board[bomb.y][bomb.x] = EMPTY;
    		}
    	}
    }
    
    static int dy[] = {-1,1,0,0};
    static int dx[] = {0,0,-1,1};
    static boolean OOB(int y,int x) {
    	return y<=0 || y>n || x<=0 || x>n;
    }
    
    static void printBoard(int board[][]) {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++)
    			System.out.printf("%3d", board[y][x] == EMPTY ? 0 : board[y][x]);
    		System.out.println();
    	}
    }
    
    static int n,m;
    static StringTokenizer st;
    static boolean visited[][];
    static int BLACK = -1, RED = 0, EMPTY = -2;
    static int board[][];
}