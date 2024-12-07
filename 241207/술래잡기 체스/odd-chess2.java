import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException{
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	for(int y= 0; y<n; y++) {
    		st = new StringTokenizer(br.readLine());
    		for(int x=0; x<n; x++) {
    			int p = Integer.parseInt(st.nextToken());
    			int d = Integer.parseInt(st.nextToken()) - 1;
    			board[y][x] = new Thief(p,d);
    		}
    	}
    	policeDir = board[0][0].dir;
    	int score = board[0][0].id;
    	board[0][0] = POLICE;
    	moveAll();
    	
    	
    	solve(0,0, score,0);
    	
//    	printBoard(board);
    	System.out.println(ans);
    }
    
    static int policeDir;
    
    static ArrayList<Pair> getNxtPositions(int y,int x){
    	ArrayList<Pair> ret = new ArrayList<>();
    	
    	for(int dist = 1; dist<= n; dist++) {
    		int ny = y + dy[policeDir] * dist;
    		int nx = x + dx[policeDir] * dist;
    		
    		if(!OOB(ny,nx) && board[ny][nx] != EMPTY)
    			ret.add(new Pair(ny,nx));
    	}
    	
    	return ret;
    }
    static class Pair{
    	int y, x;
    	public Pair(int y,int x) {
    		this.y = y;
    		this.x = x;
    	}
    }
    
    static void solve(int py, int px, int sum, int depth) {
    	ArrayList<Pair> nxtPositions = getNxtPositions(py,px);
    	if(nxtPositions.isEmpty()) {
    		ans = Math.max(ans, sum);
    		return;
    	}
    	
    	/*
    	 * 술래의 이동
    	 * 1. 이전 위치는 EMPTY로 변경
    	 *  
    	 * 2. 다음 위치는 POLICE로 변경 
    	 * 3. 위치 이동 
    	 * 4. 다음 재귀 수행 
    	 * 5. 복원  
    	 */
    	
    	// 경찰 위치 백업 
    	for(Pair nxt : nxtPositions) {
    		int ny = nxt.y;
    		int nx = nxt.x;
    		
    		
    		// backup
    		Thief temp[][] = new Thief[n][n];
    		for(int y = 0; y<n; y++) {
    			for(int x =0; x<n; x++) {
    				temp[y][x] = board[y][x];
    			}
    		}
    		int tmpDir = policeDir;
    		
    		board[py][px] = EMPTY;
    		policeDir = board[ny][nx].dir;
    		int score = board[ny][nx].id;
    		board[ny][nx] = POLICE;

    		// 도둑말의 이동
    		moveAll();
    		
    		solve(ny,nx, sum + score, depth + 1);
    		
    		for(int y= 0; y<n; y++) {
    			for(int x = 0; x<n; x++) {
    				board[y][x] = temp[y][x];
    			}
    		}
    		policeDir =  tmpDir;
    				
    	}
    	
    }
    
    
    static void printBoard(Thief board[][]) {
    	for(int y=0; y<n; y++) {
    		for(int x=0; x<n; x++) {
    			System.out.printf("[%3d %3d]\t", board[y][x].id, board[y][x].dir);
    		}
    			
    		System.out.println();
    	}
    }
    
    /*
     * 도둑말의 이동 
     * 1. 작은 id부터 이동. -> 이번에 이동할 idx가 어느곳에 위치하는지 알아야함 
     * 2. 다음 위치와 방향 찾기 
     */
    static void move(int idx) {
    	
    	for(int y= 0; y<n; y++) {
    		for(int x= 0; x<n; x++) {
    			if(board[y][x] == POLICE || board[y][x] == EMPTY)
    				continue;
    			
    			int id  = board[y][x].id;
    			int dir = board[y][x].dir;
    			
    			if(idx == id) {
    				Tuple nxt = getNxtPos(y,x,dir);
    				board[y][x] = new Thief(id, nxt.dir);
    				swap(nxt.y, nxt.x, y,x);
    				return;
    			}
    			
    		}
    	}
    	
    	
    }
    
    static void swap(int y1, int x1, int y2, int x2) {
    	Thief temp = board[y1][x1];
    	
    	board[y1][x1] = board[y2][x2];
    	board[y2][x2] = temp;
    }
    
    
    
    /*
     * 다음 위치와 방향 찾기
     * 1. 술래가 있거나 OOB로는 이동 불가
     * 2. 빈칸이거나 도둑말이 있는 곳은 이동 가능
     * 3. 이동할 수 있는 칸이 나올때까지 45도 반시계 회전 
     * 4. 이동가능한 칸이 없으면 이동x
     *  
     */
    static Tuple getNxtPos(int y,int x, int dir) {
    	
    	for(int i = 0; i < 8; i++) {
    		int moveDir = (dir + i) % 8;
    		int ny = y + dy[moveDir];
    		int nx = x + dx[moveDir];
    		
    		if(!OOB(ny,nx) && board[ny][nx] != POLICE)
    			return new Tuple(ny,nx,moveDir);
    	}
    	return new Tuple(y,x,dir);
    }
    
    static class Tuple{
    	int y,x,dir;
    	public Tuple(int y,int x, int dir) {
    		this.y = y;
    		this.x = x;
    		this.dir = dir;
    	}
    	
    	public boolean isSame(int y,int x) {
    		return this.y == y && this.x == x;
    	}
    	
    	public String toString() {
    		return y+" "+x+" "+dir;
    	}
    }
    
  
    static void moveAll() {
    	for(int i = 1; i<=16; i++) {
//    		System.out.println("------\n id: "+i);
    		
    		move(i);
//    		printBoard(board);
    	}
    }
    
    
    static int dy[] = {-1,-1,0,1,1,1,0,-1};
    static int dx[] = {0,-1,-1,-1,0,1,1,1};
    static boolean OOB(int y,int x) {
    	return y<0 || y>=n || x<0 || x>=n;
    }
    
    static int ans;
    
    static StringTokenizer st;
    static int n = 4;
    static Thief board[][] = new Thief[n][n]; 
    
    static Thief EMPTY = new Thief(-2,-2);
    static Thief POLICE = new Thief(-1,-1);
    
    static class Thief{
    	int id, dir;
    	public Thief(int id, int dir) {
    		this.id = id;
    		this.dir = dir;
    	}
    	
    	public String toString() {
    		return id+" "+dir;
    	}
    }
}