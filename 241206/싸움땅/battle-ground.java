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
    		players[id].move();
    	}
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
    	
    	public void move() {
    		idBoard[pos.y][pos.x] = 0; // 이전 위치
    		Pos nxtPos = getNxtPos(pos.y, pos.x, pos.dir);
//    		System.out.printf("move %d : %s\n", id, nxtPos);
    		this.pos = nxtPos;
    		
    		if(idBoard[pos.y][pos.x] == 0) {
    			idBoard[pos.y][pos.x] = id;
    			
    			board[pos.y][pos.x].add(this.gun);
    			this.gun = board[pos.y][pos.x].poll();
    			
    		}else {
//    			int otherId = idBoard[pos.y][pos.x];
    			Player other = players[idBoard[pos.y][pos.x]];
//    			System.out.println("other: "+other);
    			if(isWin(other)) {
    				/*
    				 * id가 승자 
    				 * 1. 점수 업데이트
    				 * 2. loser(other)가 총을 내려놓고 패배 이동.
    				 * 3. winner(this)는 쎈총 가져감 
    				 */
//    				System.out.println("winner: "+id);
    				
    				// 점수 업데이트 
    				scores[id] += (this.gun + this.stat - (other.gun + other.stat));
    				
    				// loser가 총을 내려놓음 
    				board[pos.y][pos.x].add(other.gun);
    				other.gun = 0;
    				
    				// loser의 패배 이동
    				other.loserMove();
    				
    				// winner가 쎈 총 가져감 
    				board[pos.y][pos.x].add(this.gun);
    				this.gun = board[pos.y][pos.x].poll();
    				idBoard[pos.y][pos.x] = id;
    				
    			}else {
    				/*
    				 * other가 승자
    				 * 1. 점수 업데이트
    				 * 2. loser(this)가 총을 내려놓고 패배 이동
    				 * 3. winner(other)는 쎈총 가져감
    				 */
//    				System.out.println("winner: "+otherId);
    				
    				scores[other.id] += (other.gun + other.stat - (this.gun + this.stat));
    				
    				board[pos.y][pos.x].add(this.gun);
    				this.gun = 0;
    				
    				this.loserMove();
    				
    				board[other.pos.y][other.pos.x].add(other.gun);
    				other.gun = board[other.pos.y][other.pos.x].poll();
//    				idBoard[pos.y][pos.x] = otherId;
    			}
    		}
    	}
    	
    	private Pos getNxtPos(int y,int x, int dir) {
    		int ny = y + dy[dir];
    		int nx = x + dx[dir];
    		if(OOB(ny,nx)) {
    			dir = (dir + 2) % 4;
    			ny = y + dy[dir];
    			nx = x + dx[dir];
    		}
    		
    		return new Pos(ny,nx,dir);
    	}
    	
    	private boolean isWin(Player target) {
    		int s1 = this.stat;
    		int g1 = this.gun;
    		
    		int s2 = target.stat;
    		int g2 = target.gun;
    		
    		if((s1 + g1 > s2 + g2) || (s1+g1==s2+g2 && s1 > s2))
    			return true;
    		else
    			return false;
    	}
    	
    	private void loserMove() {
    		for(int d = 0; d < 4; d++) {
    			int moveDir = (pos.dir + d) % 4;
    			int ny = pos.y + dy[moveDir];
    			int nx = pos.x + dx[moveDir];
    			if(OOB(ny,nx) || idBoard[ny][nx] != 0)
    				continue;
    			pos.y = ny;
    			pos.x = nx;
    			pos.dir = moveDir;
    			break;
    		}
    		
    		idBoard[pos.y][pos.x] = id;
    		board[pos.y][pos.x].add(this.gun);
    		this.gun = board[pos.y][pos.x].poll();
    	}
    }
    
    static int dy[] = {-1,0,1,0};
    static int dx[] = {0,1,0,-1};
    static boolean OOB(int y,int x) {
    	return y<=0 || y>n || x<=0 || x>n;
    }
}