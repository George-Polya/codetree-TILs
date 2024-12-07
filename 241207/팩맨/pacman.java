import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException{
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	m = Integer.parseInt(st.nextToken());
    	t = Integer.parseInt(st.nextToken());
    	dead = new int[n+1][n+1];
    	
    	st = new StringTokenizer(br.readLine());
    	packMan = new Pair(Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()));
    	
    	board = new List[n+1][n+1];
    	nxtBoard = new List[n+1][n+1];
    	egg = new Queue[n+1][n+1];
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			board[y][x] = new ArrayList<>();
    			egg[y][x] = new ArrayDeque<>();
    		}
    	}
    	for(int i = 0; i < m; i++) {
    		st = new StringTokenizer(br.readLine());
    		int y = Integer.parseInt(st.nextToken());
    		int x = Integer.parseInt(st.nextToken());
    		int dir = Integer.parseInt(st.nextToken()) - 1;
    		board[y][x].add(dir);
    	}
    	
//    	printBoard(board);
    	
    	for(int turn = 1; turn<= t; turn++) {
//    		System.out.println("-----\nturn: "+turn);
    		// 복제시도 
    		tryClone();
    		
    		// 몬스터 이동 
    		moveAll();
//    		System.out.println("after monster move");
//        	printBoard(board);
    		
    		// 팩맨 이동 
    		packManMove();
//    		System.out.println("after packMan move");
//    		System.out.println("packMan: "+packMan);
//    		printBoard(board);
    		
    		
    		// 시체 소멸 
    		destroy();
//    		System.out.println("aftter dead destroy");
//    		printBoard(dead);
    		
    		// 복제 완성 
    		completeClone();
//    		System.out.println("after clone");
//    		printBoard(board);
    	}
    	
    	
    	int ans = 0;
    	
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			ans += board[y][x].size();
    		}
    	}
    	System.out.println(ans);
    }
    
    static void completeClone() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			board[y][x].addAll(egg[y][x]);
    			egg[y][x] = new ArrayDeque<>();
    		}
    	}
    }
    
    static void destroy() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(dead[y][x] > 0)
    				dead[y][x]--;
    		}
    	}
    }
    
    /*
     * 팩맨의 이동 
     * 방향 우선순위 direction 초기값 888
     * 64개의 경우의수 모두 확인해서
     * cnt == 3 될때 direction 업데이트 
     */
    static String bestDirection;
    static int eatCnt;
    static boolean visited[][];
    static void packManMove() {
    	bestDirection = "000";
    	eatCnt = 0;
    	visited = new boolean[n+1][n+1];
    	findDirection(packMan.y, packMan.x, 0,0, "");
//    	System.out.printf("eatCnt: %d, bestDirection: %s\n", eatCnt, bestDirection);
    	
    	int y = packMan.y;
    	int x = packMan.x;
    	
    	for(char d : bestDirection.toCharArray()) {
    		int dir = d - '0';
    		int ny = y + dy[dir];
    		int nx = x + dx[dir];
    		if(!board[ny][nx].isEmpty()) {
    			board[ny][nx] = new ArrayList<>();
    			dead[ny][nx] = 3;
    		}
    		y = ny;
    		x = nx;
    	}
    	
    	packMan.y = y;
    	packMan.x = x;
    	
    	
    }
    
    static void findDirection(int y,int x, int depth,int sum, String d) {
    	if(depth == 3) {
//    		System.out.printf("sum: %d, d: %s\n", sum,d);
    		if(eatCnt < sum) {
    			eatCnt = sum;
    			bestDirection = d;
    		}else if(eatCnt == sum && d.compareTo(bestDirection) < 0) {
    			bestDirection = d;
    		}
    		
    		return;
    	}
    	
    	for(int dir = 0; dir< 8; dir+=2) {
    		int ny = y + dy[dir];
    		int nx = x + dx[dir];
    		if(OOB(ny,nx) || visited[ny][nx])
    			continue;
    		visited[ny][nx] = true;
    		findDirection(ny,nx,depth+1, sum + board[ny][nx].size(), d+dir);
    		visited[ny][nx] = false;
    	}
    	
    	
    }
    
    
    
    
    static void moveAll() {
    	// nxtBoard 초기화 
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			nxtBoard[y][x] = new ArrayList<>();
    		}
    	}
    	
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			for(int dir : board[y][x]) {
    				move(y,x,dir);
    			}
    		}
    	}
    	
    	// board = copy(nxtBoard)
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			board[y][x] = new ArrayList<>(nxtBoard[y][x]);
    		}
    	}
    	
    }
    
    /*
     * (y,x)에서 dir 방향을 가지는 몬스터의 이동 
     * 1. 갈 수 있는 위치 선택 
     * 2. 이동 
     */
    static void move(int y,int x, int dir) {
    	Tuple nxtPos = getNxtPos(y,x,dir);
    	nxtBoard[nxtPos.y][nxtPos.x].add(nxtPos.dir);
    }
    
    /*
     * 움직이려는 방향 설정 moveDir
     * 그 방향으로 움직였을 경우, 시체가 있거나, 팩맨이 있거나 OOB면은 회전
     * 갈 수 있을때까지 회전 
     * 갈 수 있는 곳이 없으면 움직이지 않음
     */
    static Tuple getNxtPos(int y,int x, int dir) {
    	
    	for(int i = 0; i < 8; i++) {
    		int moveDir = (dir + i) % 8;
    		int ny = y + dy[moveDir];
    		int nx = x + dx[moveDir];
    		if(OOB(ny,nx) || dead[ny][nx] != 0 || packMan.isSame(ny, nx))
    			continue;
    		return new Tuple(ny,nx,moveDir); // 갈 수 있으면 바로 리턴 
    	}
    	/*
    	 * for문을 벗어났다는 거는 8방향 다 확인해도 갈 수 있는 곳이 없다는 걸 의미
    	 * 움직이지 않아야 하므로 기존위치 리턴
    	 */
    	return new Tuple(y,x,dir);
    }
    
    
    
    /*
     * board[y][x]에 있는 몬스터들을 복제시도함 == 큐에 집어넣음  
     * 나중에 복제완성될때 큐에서 뺴서 board[y][x]에 집어넣으면 됨  
     */
    static void tryClone() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			egg[y][x].addAll(board[y][x]);
    		}
    	}
    }
    
    static Pair packMan;
    static class Pair{
    	int y,x;
    	public Pair(int y,int x) {
    		this.y = y;
    		this.x = x;
    	}
    	
    	public boolean isSame(int y,int x) {
    		return this.y == y && this.x == x;
    	}
    	public String toString() {
    		return y+" "+x;
    	}
    }
    static class Tuple{
    	int y,x,dir;
    	public Tuple(int y,int x, int dir) {
    		this.y = y;
    		this.x = x;
    		this.dir = dir;
    	}
    	
    	public String toString() {
    		return y+" "+x+" "+dir;
    	}
    	
    }
    
    static void printBoard(List[][] board) {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			System.out.printf("%s\t", board[y][x]);
    		}
    		System.out.println();
    	}
    }
    
    static void printBoard(int[][] board) {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			System.out.printf("%3d", board[y][x]);
    		}
    		System.out.println();
    	}
    }
    
    static int dead[][];
    static List<Integer> board[][], nxtBoard[][];
    static Queue<Integer> egg[][];
    static int m,t;
    static int n = 4;
    static StringTokenizer st;
    static int dy[] = {-1,-1,0,1,1,1,0,-1};
    static int dx[] = {0,-1,-1,-1,0,1,1,1};
    static boolean OOB(int y,int x) {
    	return y<=0 || y>n || x<=0 || x>n;
    }
}