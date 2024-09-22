import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	n = Integer.parseInt(st.nextToken());
    	m = Integer.parseInt(st.nextToken());
    	k = Integer.parseInt(st.nextToken());
    	cell = new Cell[n+1][n+1];
    	players = new Player[m+1];
    	board = new List[n+1][n+1];
    	nxtBoard = new List[n+1][n+1];
    	for(int y=1; y<=n; y++) {
    		st = new StringTokenizer(br.readLine());
    		for(int x=1; x<=n; x++) {
    			cell[y][x] = BLANK;
    			if(board[y][x] == null)
    				board[y][x] = new ArrayList<>();
    			
    			int id = Integer.parseInt(st.nextToken());
    			if(id != 0) {
    				cell[y][x] = new Cell(id,k);
    				players[id] = new Player(id);
    				board[y][x].add(players[id]);
    			}
    		}
    	}
    	
    	st = new StringTokenizer(br.readLine());
    	for(int id = 1; id<=m; id++) {
    		int headDir = Integer.parseInt(st.nextToken());
    		players[id].headDir = headDir - 1;
    	}
    	
//    	for(int y=1; y<=n; y++) {
//    		for(int x=1; x<=n; x++) {
//    			System.out.printf("%s\t", board[y][x]);
//    		}
//    		System.out.println();
//    	}
    	
    	priority = new int[m+1][4][4];
    	for(int id = 1; id<=m; id++) {
    		for(int dir = 0; dir < 4; dir++) {
    			st = new StringTokenizer(br.readLine());
    			for(int i = 0; i<4; i++) {
    				int d = Integer.parseInt(st.nextToken());
    				priority[id][dir][i] = d - 1;
    			}
    		}
    		
    	}
    	
    	survived = m;
    	while(true) {
    		turn++;
    		// 이동 
//    		System.out.println("------");
//    		System.out.println("turn: "+turn);
    		simulate();
    		if(end() || turn >= 1000)
    			break;
    	}
    	
    	System.out.println(turn >= 1000  ? -1 : turn);
    	
    	
    }
    
    static boolean end() {
    	int cnt = 0;
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(board[y][x].isEmpty())
    				continue;
    			cnt++;
    		}
    	}
    	return cnt == 1;
    }
    
    static void simulate() {
    	
    	// 플레이어들의 이동 
    	moveAll();
    	
    	// 현재 플레이어들이 계약한 칸의 시간이 지나감 
    	expire();
    	
    	
    	// 모든 플레이어가 이동한 후 한 칸에 여러 플레이어가 있으면 
    	// 가장 작은 번호의 플레이어만 살아남고 나머진 소멸 
    	vanishAll();
    	
    	
    	// 살아남은 플레이어는 현재 칸을 독점계약 
    	contractAll();
    	
    }
    
    static void vanish(int y,int x) {
    	Collections.sort(board[y][x]);
    	Player player = board[y][x].get(0);
    	
    	board[y][x].clear();
    	board[y][x].add(player);
    	
    }
    
    static void vanishAll() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(board[y][x].size() >= 2) {
    				vanish(y,x);
    			}
    		}
    	}
    	
//    	System.out.println("after vanish");
//    	for(int y=1; y<=n; y++) {
//    		for(int x=1; x<=n; x++) {
//    			System.out.printf("%s\t", board[y][x]);
//    		}
//    		System.out.println();
//    	}
    }
    
    static void contract(int y,int x) {
    	int id = board[y][x].get(0).id;
    	cell[y][x] = new Cell(id, k);
    }
    
    static void contractAll() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(board[y][x].isEmpty())
    				continue;
    			contract(y,x);
    		}
    	}
    	
//    	System.out.println("after contract");
//    	for(int y=1; y<=n; y++) {
//    		for(int x=1; x<=n; x++) {
//    			System.out.printf("%s\t", cell[y][x]);
//    		}
//    		System.out.println();
//    	}
    }
    
    static void move(int y,int x) {
    	Player player = board[y][x].get(0);
    	
    	Tuple nxt = getNxt(y,x, player.id, player.headDir);
    	nxtBoard[nxt.y][nxt.x].add(new Player(player.id, nxt.moveDir));
    }
    

	static Tuple getNxt(int y,int x, int id, int headDir) {
		for(int i = 0; i < 4; i++) {
			int moveDir = priority[id][headDir][i];
			int ny = y + dy[moveDir];
			int nx = x + dx[moveDir];
			if(OOB(ny,nx))
				continue;
			if(cell[ny][nx] == BLANK) // 아무도 계약하지 않은 땅이 있으면 그곳을 리턴 
				return new Tuple(ny,nx,moveDir);
		}
		
		// 4방향 전부 계약된 땅인 경우 
		for(int i = 0; i < 4; i++) {
			int moveDir = priority[id][headDir][i];
			int ny = y + dy[moveDir];
			int nx = x + dx[moveDir];
			if(OOB(ny,nx))
				continue;
			if(cell[ny][nx].owner == id) // 본인이 계약한 땅으로 이동 
				return new Tuple(ny,nx,moveDir);
		}
		
		/*
		 * null인 경우를 리턴하게 되는 경우는 없다.
		 * 왜냐하면 4방향이 모두 계약된 땅이어도 그 중 한방향은 자신이 이전에 위치했던 곳이다
		 * 따라서 반드시 특정 칸과 방향을 리턴하게 된다. 
		 */
		return null;
	}
    
    static void moveAll() {

    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			nxtBoard[y][x] = new ArrayList<>();
    		}
    	}
    	
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(board[y][x].isEmpty())
    				continue;
    			move(y,x);
    		}
    	}
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			board[y][x] = nxtBoard[y][x];
    		}
    	}
    	
    	
//    	System.out.println("after move");
//    	
//    	for(int y=1; y<=n; y++) {
//    		for(int x=1; x<=n; x++) {
//    			System.out.printf("%s\t", board[y][x]);
//    		}
//    		System.out.println();
//    	}
    	
    	
    }
    
    /*
     * 플레이어들이 계약한 칸(=BLANK아닌 칸)의 시간이 지나감
     * 시간이 0이 되면 BLANK가 됨  
     */
    static void expire() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(cell[y][x] != BLANK) {
    				cell[y][x].time--;
    				if(cell[y][x].time == 0)
    					cell[y][x] = BLANK;
    			}
    		}
    	}
    }
    
    
    static int n,m,k;
    static int turn;
    static int survived;
    static StringTokenizer st;
    static Cell cell[][];
    static List<Player> board[][], nxtBoard[][];
    static Player players[];
    static int priority[][][];
    
    static Player NO_PLAYER = new Player(-1,-1);
    static class Player implements Comparable<Player>{
    	int headDir, id;
    	public Player(int id) {
    		this.id = id;
    	}
    	
    	public Player(int id, int headDir) {
    		this.id = id;
    		this.headDir = headDir;
    	}
    	
    	public int compareTo(Player o) {
    		return id - o.id;
    	}
    	
    	
    	public String toString() {
    		return id+"";
    	}
    	
    }
    
    static class Tuple{
    	int y, x, moveDir;
    	public Tuple(int y,int x, int moveDir) {
    		this.y = y;
    		this.x = x;
    		this.moveDir = moveDir;
    	}
    	
    	public String toString() {
    		return y+" "+x+" "+moveDir;
    	}
    }
    
    static int dy[] = {-1,1,0,0};
    static int dx[] = {0,0,-1,1};
    static boolean OOB(int y,int x) {
    	return y<=0 || y>n || x<=0 || x>n;
    }
    
    
    static Cell BLANK = new Cell(0,0);
    static class Cell{
    	int owner, time;
    	public Cell(int owner, int time) {
    		this.owner = owner;
    		this.time = time;
    	}
    	
    	public String toString() {
    		return owner +" "+ time+"|";
    	}
    }
}