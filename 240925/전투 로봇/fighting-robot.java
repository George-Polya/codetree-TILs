import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        board = new int[n+1][n+1];
        
        for(int y=1; y<=n;y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x=1; x<=n; x++) {
        		int value = Integer.parseInt(st.nextToken());
        		if(value == 9) {
        			robot = new Robot(y,x);
        		}else if(value > 0) {
        			board[y][x] = value;
        		}
        	}
        }
        
//        printBoard(board);
//    	System.out.println("robot: "+robot);
    	
//        while(!robot.end()) {
//        	robot.move();
//        }
        
        while(true) {
//        	System.out.println("------");
        	if(robot.end())
        		break;
        	robot.move();
//        	System.out.println("after move");
//        	printBoard(board);
//        	System.out.println("robot: "+robot);
        }
        
        System.out.println(ans);
        
    }
    static void printBoard(int board[][]) {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			System.out.printf("%3d", board[y][x]);
    		}
    		System.out.println();
    	}
    }
    
    static Robot robot;
    
    static StringTokenizer st;
    static int n;
    static int board[][];
    static int EMPTY = 0;
    static boolean visited[][];
    static int dy[] = {-1,1,0,0};
    static int dx[] = {0,0,-1,1};
    static boolean OOB(int y,int x) {
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
    
    static class Tuple{
    	int y,x,distance;
    	public Tuple(int y,int x, int distance) {
    		this.y = y;
    		this.x = x;
    		this.distance = distance;
    	}
    	
    	public boolean isHigher(Tuple o) {
    		if(distance != o.distance)
    			return distance < o.distance;
    		if(y != o.y)
    			return y < o.y;
    		return x < o.x;
    	}
    	
    	public String toString() {
    		return String.format("(%d, %d) | dist: %d", y,x,distance);
    	}
    }
    static int ans;
    static int INT_MAX = Integer.MAX_VALUE;
    static Tuple WORST = new Tuple(22,22, INT_MAX);
    
    static class Robot{
    	int y,x, level, cnt;
    	Tuple best;
    	public Robot(int y,int x) {
    		this.y = y;
    		this.x = x;
    		this.level = 2; // 초기 레벨은 2이다. 
    	}
    	
    	public String toString() {
    		return String.format("(%d,%d) | level: %d", y,x,level);
    	}
    	
    	public void move() {
    		// 이동 
    		y = best.y;
    		x = best.x;
    		
    		// 없앰 
    		board[y][x] = 0;  // 몬스터를 없애면 해당 칸은 빈칸이 됨 
    		cnt++; 
    		ans += best.distance;
    		
    		if(cnt == level) { // 레벨과 같은 수의 몬스터를 없앴으면 
    			level++; // 레벨 상승 
    			cnt = 0; // 없앤 몬스터 수 초기화 
    		}
    	}
    	
    	public boolean end() {
    		best = bfs(); // 없앨 수 있는 몬스터가 있는지 확인 
//    		System.out.println("best: "+best);
    		return best == WORST; // WORST라면 없앨 수 있는 몬스터가 없다는 뜻 
    		
    	}
    	
    	/*
    	 * 없앨 수 있는 몬스터 중 가장 가까운 몬스터 찾기 
    	 */
    	private Tuple bfs() {
    		Tuple ret = WORST;
    		visited = new boolean[n+1][n+1];
    		
    		Queue<Tuple> q = new ArrayDeque<>();
    		visited[y][x] = true;
    		q.add(new Tuple(y,x, 0));
    		
    		while(!q.isEmpty()) {
    			Tuple cur = q.poll();
    			
    			// 빈칸이 아니고 로봇보다 레벨이 낮아야 한다
    			if(0 < board[cur.y][cur.x] && board[cur.y][cur.x] < level && cur.isHigher(ret)) {
    				ret = cur;
    			}
    			
    			for(int dir = 0;dir < 4; dir++) {
    				int ny = cur.y + dy[dir];
    				int nx = cur.x + dx[dir];
    				
    				
    				if(OOB(ny,nx) || visited[ny][nx])
    					continue;
    				
    				if(board[ny][nx] > level) // 로봇보다 레벨이 큰 몬스터는 지나가지 못한다. 
    					continue;
    				
    				visited[ny][nx] = true;
    				q.add(new Tuple(ny,nx,cur.distance + 1));
    			}
    		}
    		
    		return ret;
    	}
    }
}