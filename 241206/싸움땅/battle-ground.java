import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException{
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	
    	n = Integer.parseInt(st.nextToken());
    	m = Integer.parseInt(st.nextToken());
    	k = Integer.parseInt(st.nextToken());
    	
    	board = new PriorityQueue[n+1][n+1];
    	idBoard = new int[n+1][n+1];
    	for(int y=1; y<=n; y++) {
    		st = new StringTokenizer(br.readLine());
    		for(int x=1; x<=n; x++) {
    			if(board[y][x] == null)
    				board[y][x] = new PriorityQueue<>(Collections.reverseOrder());
    			
    			int value = Integer.parseInt(st.nextToken());
    			board[y][x].add(value);
    		}
    	}
    	
    	players = new Player[m+1];
    	for(int id = 1; id<=m; id++) {
    		st = new StringTokenizer(br.readLine());
    		int y= Integer.parseInt(st.nextToken());
    		int x= Integer.parseInt(st.nextToken());
    		int dir= Integer.parseInt(st.nextToken());
    		int s= Integer.parseInt(st.nextToken());
    		players[id] = new Player(y,x,dir,s,id);
    		idBoard[y][x] = id;
    	}
    	scores = new int[m+1];
    	for(int turn = 1; turn<=k; turn++) {
//    		System.out.println("-----");
//    		System.out.println("turn: "+turn);
    		
    		simulate();
//    		printPlayer();
//    		printBoard(idBoard);
    	}
    	
    	
    	StringBuilder sb = new StringBuilder();
    	for(int id = 1; id<= m; id++)
    		sb.append(scores[id]).append(' ');
    	System.out.println(sb);
    }

    static void printPlayer() {
    	for(int i = 1; i<=m; i++) {
    		System.out.println(players[i]);
    	}
    }
    
    static int scores[];
    
    static void simulate() {
    	// 1. 첫번째 플레이어부터 순서대로 이동
    	moveAll();
    }
    
    static void moveAll() {
    	for(int id = 1; id<=m; id++) {
    		move(id);
    	}
    }
    
    static void move(int id) {
    	Player player = players[id];
    	
    	int y = player.pos.y;
    	int x = player.pos.x;
    	int dir = player.pos.dir;
    	idBoard[y][x] = 0;
    	Pos nxtPos = getNxtPos(y,x,dir);
    	
    	player.pos = nxtPos;
    	y = nxtPos.y;
    	x = nxtPos.x;
    	
    	if(idBoard[y][x] == 0) {
    		idBoard[y][x] = id;
    		board[y][x].add(player.gun);
    		player.gun = board[y][x].poll();
    	}else {
    		Player other = players[idBoard[y][x]];
    		
    		if(player.isStronger(other)) {
    			scores[id] += (player.gun + player.stat - (other.gun + other.stat));
    			
    			board[y][x].add(other.gun);
    			other.gun = 0;
    			
    			loserMove(other);
    			
    			board[y][x].add(player.gun);
    			player.gun = board[y][x].poll();
    			idBoard[y][x] = id;
    		}else {
    			scores[other.id] += (other.gun + other.stat - (player.gun + player.stat));
    			
    			board[y][x].add(player.gun);
    			player.gun = 0;
    			
    			loserMove(player);
    			
    			board[y][x].add(other.gun);
    			other.gun = board[y][x].poll();
    		}
    		
    	}
    }
    
    static void loserMove(Player player) {
    	int y = player.pos.y;
    	int x = player.pos.x;
    	int dir = player.pos.dir;
    	
    	for(int i = 0; i < 4; i++) {
    		int moveDir = (dir + i) % 4;
    		int ny = y + dy[moveDir];
    		int nx = x + dx[moveDir];
    		if(OOB(ny,nx) || idBoard[ny][nx] != 0)
    			continue;
    		y = ny;
    		x = nx;
    		dir = moveDir;
    		break;
    	}
    	player.pos.y = y;
    	player.pos.x = x;
    	player.pos.dir = dir;
    	
    	idBoard[y][x] = player.id;
    	board[y][x].add(player.gun);
    	player.gun = board[y][x].poll();
    }
    
    static Pos getNxtPos(int y,int x, int dir) {
    	int ny = y + dy[dir];
		int nx = x + dx[dir];
		if(OOB(ny,nx)) {
			dir = (dir + 2) % 4;
			ny = y + dy[dir];
			nx = x + dx[dir];
		}
		
		return new Pos(ny,nx,dir);
    }
    
    static void printBoard(int board[][]) {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			System.out.printf("%3d", board[y][x]);
    		}
    		System.out.println();
    	}
    }
    
    static StringTokenizer st;
    static int n,m,k;
    static PriorityQueue<Integer> board[][];
    static int idBoard[][];
    static Player players[];
    static class Pos{
    	int y,x,dir;
    	public Pos(int y,int x,int dir) {
    		this.y = y;
    		this.x = x;
    		this.dir = dir;
    	}
    	
    	public String toString() {
    		return y+" "+x+" "+dir;
    	}
    }
    
    static class Player{
    	Pos pos;
    	int stat, gun, id;
    	public Player(int y,int x, int dir, int s, int id) {
    		this.pos = new Pos(y,x,dir);
    		this.stat = s;
    		this.id = id;
    		this.gun = 0;
    	}
    	
    	public String toString() {
    		return String.format("id: %d | pos: %s | stat: %d | gun: %d", id, pos, stat,gun);
    	}
    	
    	public boolean isStronger(Player o) {
    		int s1 = this.stat;
    		int g1 = this.gun;
    		
    		int s2 = o.stat;
    		int g2 = o.gun;
    		
    		if((s1 + g1 > s2 + g2) || (s1+g1==s2+g2 && s1 > s2))
    			return true;
    		else
    			return false;
    				
    	}
    }
    
    static int dy[] = {-1,0,1,0};
    static int dx[] = {0,1,0,-1};
    static boolean OOB(int y,int x) {
    	return y<=0 || y>n || x<=0 || x>n;
    }
}