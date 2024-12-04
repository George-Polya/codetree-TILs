import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        f = Integer.parseInt(st.nextToken());
        
        board = new int[n][n];
        for(int y=0; y<n; y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x=0; x<n; x++) {
        		board[y][x] = Integer.parseInt(st.nextToken());
        		if(board[y][x] == 3) {
        			py = Math.min(py, y);
        			px = Math.min(px, x);
        		}
        		
        		if(board[y][x] == 4) {
        			end = new Pair(y,x);
        		}
        	}
        }
        
        plane = new int[3*m][3*m];
        idPlane = new int[3*m][3*m];
        visited1 = new int[3*m][3*m];
        for(int y=0;y<3*m;y++) {
        	for(int x=0; x<3*m;x++) {
        		idPlane[y][x] = visited1[y][x] = -1;
        		plane[y][x] = -1;
        	}
        }
        
        // 동
        for(int x=2*m; x< 3*m; x++) {
        	st = new StringTokenizer(br.readLine());
        	for(int y=2*m-1;y>=m; y--) {
        		plane[y][x] = Integer.parseInt(st.nextToken());
        		idPlane[y][x] = 1;
        	}
        }
        
        // 서
        for(int x = m -1; x>=0; x--) {
        	st = new StringTokenizer(br.readLine());
        	for(int y= m;y< 2*m; y++) {
        		plane[y][x] = Integer.parseInt(st.nextToken());
        		idPlane[y][x] = 3;
        	}
        }
        
        // 남
        for(int y = 2*m; y<3*m; y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x=m;x<2*m; x++) {
        		plane[y][x] = Integer.parseInt(st.nextToken());
        		idPlane[y][x] = 2;
        	}
        }
        
        
        // 북
        for(int y = m - 1; y>=0; y--) {
        	st = new StringTokenizer(br.readLine());
        	for(int x = 2 * m - 1; x>=m; x--) {
        		plane[y][x] = Integer.parseInt(st.nextToken());
        		idPlane[y][x] = 4;
        	}
        }
        // 윗면
        for(int y = m; y<2*m; y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x=m ;x< 2*m; x++) {
        		plane[y][x] = Integer.parseInt(st.nextToken());
        		idPlane[y][x] = 0;
        		if(plane[y][x] == 2)
        			start = new Pair(y,x);
        	}
        }
        
//        printBoard(plane);
        
        anomals = new Anomaly[f];
        for(int i = 0; i < f; i++) {
        	st = new StringTokenizer(br.readLine());
        	int y = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken());
        	int d = Integer.parseInt(st.nextToken());
        	int v = Integer.parseInt(st.nextToken());
        	anomals[i] = new Anomaly(y,x,d,v);
        	board[y][x] = 2;
        }
        
     
        visited2 = new int[n][n];
        for(int row[] : visited2)
        	Arrays.fill(row, -1);
        
        
        
//        printBoard(plane);
//        
//        System.out.println("simulate");
        simulate();
        System.out.println(-1);
    }
    
    static void simulate() {
    	Queue<State> q = new ArrayDeque<>();
    	visited1[start.y][start.x] = 0;
    	q.add(new State(start.y, start.x, 0, 0));
    	
    	while(!q.isEmpty()) {
    		State cur = q.poll();
//    		System.out.println("------");
//    		System.out.println("turn: "+cur.turn);
//    		System.out.println("cur: "+cur);
//    		
    		
    		
    		
//    		System.out.println("plane");
//    		printBoard(board);
//    		System.out.println("visited1");
//    		printBoard(visited1);
//    		System.out.println("visited2");
//    		printBoard(visited2);
//    		
    		
    		// 현재 미지의 공간에 위치해있고 탈출구에 도달했다
    		if(cur.plane == 5 && board[cur.pair.y][cur.pair.x] == 4) {
    			System.out.println(cur.turn);
    			System.exit(0);
    		}
    		
    		for(int dir = 0; dir < 4 ;dir++) {
    			Tuple nxt = getNxt(cur.pair.y, cur.pair.x, dir, cur.plane);
    			
    			if(nxt.plane == 5 ) { // 다음 위치가 미지의 공간이라면 
    				if(OOB(nxt.pair.y, nxt.pair.x, 0, n) || visited2[nxt.pair.y][nxt.pair.x] != -1)
    					continue;
    				
    				// 빈 공간 혹은 탈출구의 경우에만 이동할 수 있다
    				// 즉, 1이거나 2인 경우이면 이동 불가 
    				if(board[nxt.pair.y][nxt.pair.x] == 0 || board[nxt.pair.y][nxt.pair.x] == 4 ) {
    					q.add(new State(nxt.pair.y, nxt.pair.x, cur.turn + 1, nxt.plane));
    					visited2[nxt.pair.y][nxt.pair.x] = cur.turn + 1;
    				}
    			}else { // 다음 위치가 시간의 벽 안이라면 
    				if(OOB(nxt.pair.y, nxt.pair.x, 0, 3*m) || visited1[nxt.pair.y][nxt.pair.x] != -1 || plane[nxt.pair.y][nxt.pair.x] != 0)
    					continue;
    				q.add(new State(nxt.pair.y, nxt.pair.x, cur.turn + 1, nxt.plane));
    				visited1[nxt.pair.y][nxt.pair.x] = cur.turn + 1;
    			}
    		}

			anomalizeAll(cur.turn, cur.plane);
    	}
    }
    
    static void anomalizeAll(int turn, int plane) {
    	
    	for(int i = 0; i < f; i++) {
    		anomals[i].diffuse(turn, plane);
    	}
    }
    
    static class State{
    	Pair pair;
    	int turn, plane;
    	
    	public State(int y,int x, int turn, int plane) {
    		this.pair = new Pair(y,x);
    		this.turn = turn;
    		this.plane = plane;
    	}
    	
    	public String toString() {
    		return String.format("%s| turn: %d | plane: %d", pair, turn, plane);
    	}
    }
    
    
    static Tuple getNxt(int y,int x,int dir, int plane) {
    	int ny = y + dy[dir];
    	int nx = x + dx[dir];
    	
    	if(plane == 5) { // 미지의 공간에서의 이동인 경우
    		return new Tuple(ny,nx,dir, plane);
    	}
    	
    	// 시간의 벽에서 미지의 공간으로의 이동인 경우 
    	if(plane < 5 && OOB(ny,nx, 0, 3*m)) {
    		if(ny >= 3*m)
    			ny -= m;
    		if(ny < 0)
    			ny += m;
    		
    		if(nx >= 3*m)
    			nx -= m;
    		
    		if(nx < 0)
    			nx += m;
    		
    		ny -= m;
    		nx -= m;
    		
    		return new Tuple(ny + py, nx + px, dir, 5); 
    	}

    	
    	// 시간의 벽(평면도)에서의 이동 
    	int cur = idPlane[y][x];
    	int nxt = idPlane[ny][nx];
    	
    	if(nxt == -1) { // 동->남, 북->동 처럼 평면의 변화가 발생한 경우 
    		if(cur == 1 && dir == 2)
    			return new Tuple(x,y,1,2);
			if(cur == 2 && dir == 1)
				return new Tuple(3*m-1-x, 3*m-1-y,3,3);
			if(cur == 3 && dir == 3)
				return new Tuple(x,y,4,0);
			if(cur == 4 && dir == 0)
				return new Tuple(3*m-1-x, 3*m-1-y,2,1);
			
			
			if(cur == 1 && dir == 3)
				return new Tuple(3*m-1-x, 3*m-1-y,3,4);
			
			if(cur == 4 && dir == 1)
				return new Tuple(x,y,2,3);
			
			if(cur == 3 && dir == 2)
				return new Tuple(3*m-1-x, 3*m-1-y,0,2);
			
			if(cur == 2 && dir == 0)
				return new Tuple(x,y,3,1);
    		
    	}
    	
    	// 그 외의 경우 
    	return new Tuple(ny,nx,dir,nxt);
    }
    
    static int dy[] = {0,0,1,-1};
    static int dx[] = {1,-1,0,0};
    static boolean OOB(int y,int x,int lower ,int upper) {
    	return y<lower || y >= upper || x<lower || x>=upper;
    }
    
    static class Tuple{
    	Pair pair;
    	int dir, plane;
    	
    	public Tuple(int y,int x, int dir ,int plane) {
    		this.pair = new Pair(y,x);
    		this.dir = dir;
    		this.plane = plane;
    	}
    	
    	public String toString() {
    		return String.format("%s| dir: %d | plane: %d |", pair,dir,plane);
    	}
    }

    static int py = 21,px = 21;
    static Pair start, end;
    
    static Anomaly anomals[];
    
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
    
    static class Anomaly{
    	int d, v;
    	Pair pair;
    	public Anomaly(int y,int x,int d, int v) {
    		this.pair = new Pair(y,x);
    		this.d = d;
    		this.v = v;
    	}
    	
    	public void diffuse(int turn, int plane) {
    		if(turn == 0 || turn % v != 0)
    			return;
    		int ny = pair.y + dy[d];
    		int nx = pair.x + dx[d];
    		if(OOB(ny,nx,0,n)) // 격자밖을 벗어나거나 장애물을 만나면 확산 멈춤
    			return;
    		
    		if(board[ny][nx] == 3) { // 시간의 벽을 빠져나오기 전에 출구가 막히면 4에 도달하지 못함
    			System.out.println(-1);
    			System.exit(0);
    		}
    		// 빈 공간 또는 이미 이상현상이 다른 애에 의한 발생한 곳이라면 
    		if(board[ny][nx] == 0 || board[ny][nx] == 2) {
    			pair.y = ny;
    			pair.x = nx;
    			board[pair.y][pair.x] = 2;
    		}
    	}
    }
    
    static void printBoard(int board[][]) {
    	int n = board.length;
    	int m = board[0].length;
    	for(int y=0; y<n; y++) {
    		for(int x=0;x<m; x++) {
    			System.out.printf("%3d", board[y][x]);
    		}
    		System.out.println();
    	}
    }
    
    static int n,m,f;
    static StringTokenizer st;
    static int board[][], visited1[][], visited2[][];
    static int plane[][], idPlane[][];
}