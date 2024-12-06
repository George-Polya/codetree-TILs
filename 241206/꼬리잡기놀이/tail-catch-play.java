import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException{
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	n = Integer.parseInt(st.nextToken());
    	m = Integer.parseInt(st.nextToken());
    	k = Integer.parseInt(st.nextToken());
    	
    	board = new int[n+1][n+1];
    	order = new int[n+1][n+1];
    	idBoard = new int[n+1][n+1];
    	teams = new Team[m+1];
    	int id = 1;
    	for(int y=1; y<=n; y++) {
    		st = new StringTokenizer(br.readLine());
    		for(int x=1;x<=n; x++) {
    			board[y][x] = Integer.parseInt(st.nextToken());
    			if(board[y][x] == 1) {
    				teams[id] = new Team();
    				Pair head = new Pair(y,x);
    				teams[id].head = head;
    				id++;
    			}
    		}
    	}
    	
    	for(int turn = 1; turn<=k; turn++) {
//    		System.out.println("-----");
//    		System.out.println("turn: "+turn);
    		
    		
    		moveAll();
    		
    		Pos pitchPos = findPitchPos(turn);
//    		System.out.println(pitchPos);
    		
    		pitch(pitchPos);
    		
//    		System.out.println("board");
//        	printBoard(board);
//        	System.out.println("idBoard");
//        	printBoard(idBoard);
//        	System.out.println(Arrays.toString(teams));
    	}
    	
    	System.out.println(ans);
    	
    }
    
    static int ans;
    static void pitch(Pos pos) {
    	int y = pos.pair.y;
    	int x = pos.pair.x;
    	int dir = pos.dir;
    	for(int dist = 1; dist<=n; dist++) {
    		int ny = y + dy[dir] * dist;
    		int nx = x + dx[dir] * dist;
//    		System.out.println(ny+" "+nx);
    		
    		// k가 0이 아니다. 즉, 누군가가 공을 만났다. 
    		int k = order[ny][nx];
    		if(k != 0 ) {
    			
    			ans += k * k; // 점수 획득 
    			
    			int id = idBoard[ny][nx]; // 그 공을 잡은 사람의 팀
    			
    			// 공을 잡은 사람이 속한 팀의 head와 tail을 바꾼다.
    			Pair head = teams[id].head;
    			teams[id].head = teams[id].tail;
    			teams[id].tail = head;

    			
    			head = teams[id].head;
    			Pair tail = teams[id].tail;
    			board[head.y][head.x] = 1;
    			board[tail.y][tail.x] = 3;
    			
    			break;
    		}
    	}
    	
    }
    
    static Pos findPitchPos(int turn) {
    	int idx = ((turn - 1) % (4*n) + 1) % n;
    	idx = idx == 0 ? n : idx;
//    	System.out.println("idx: "+idx);
    	int dir = ((turn - 1) / n) % 4;
    	if(dir == 0)
    		return new Pos(idx, 0, dir);
    	if(dir == 1)
    		return new Pos(n + 1, idx, dir);
    	if(dir == 2)
    		return new Pos(n + 1 - idx, n + 1, dir);
    	
    	return new Pos(0, n + 1 - idx, dir);
    	
    }
    
    static class Pos{
    	Pair pair;
    	int dir;
    	
    	public Pos(int y, int x, int dir) {
    		this.pair = new Pair(y,x);
    		this.dir = dir;
    	}
    	
    	public String toString() {
    		return pair+" "+dir;
    	}
    }
    
    static void moveAll() {
    	for(int idx = 1; idx<=m; idx++) {
    		Pair head = teams[idx].head;
    		dfs(idx, head.y, head.x, -1,-1,1);
    	}
    	
//    	System.out.println("order");
//    	printBoard(order);
    	
    }
    
    static void printBoard(int board[][]) {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			System.out.printf("%3d", board[y][x]);
    		}
    		System.out.println();
    	}
    }
    
    /*
     * head에서 dfs
     * 1. cur = 3에 도달하면 board, order, id, tail업데이트
     * 2. 자신보다 1크거나 같은 쪽으로 이동해야함. 
     * 3. 다음 재귀가 끝났으면 현재 위치 업데이트 해야함 
     * 4. 모든게 끝나고 cur == 1일때 head가 4로 이동해야  
     */
    
    static void dfs(int id, int y,int x, int py, int px, int depth) {
    	int cur = board[y][x];
    	if(cur == 3) {
    		board[y][x] = 4;
    		order[y][x] = 0;
    		teams[id].tail = new Pair(py,px);
    		idBoard[py][px] = id;
    		return;
    	}
    	
    	for(int dir = 0; dir < 4; dir++) {
    		int ny = y + dy[dir];
    		int nx = x + dx[dir];
    		if(OOB(ny,nx) || (ny == py && nx == px) || board[ny][nx] == 0)
    			continue;
    		int nxt = board[ny][nx];
    		if(nxt - cur <= 1) {
//    			System.out.printf("cur: %d, nxt: %d\n", cur, nxt);
    			dfs(id,ny,nx,y,x,depth + 1);
    			board[y][x] = nxt;
    			idBoard[y][x] = id;
    			order[y][x] = depth + 1;
    		}
    	}
    	
    	if(cur == 1) {
    		for(int dir = 0; dir < 4; dir++) {
    			int ny = y + dy[dir];
    			int nx = x + dx[dir];
    			if(OOB(ny,nx) || board[ny][nx] == 0)
        			continue;
    			
    			if(board[ny][nx] == 4) {
    				board[ny][nx] = 1;
    				order[ny][nx] = 1;
    				teams[id].head = new Pair(ny,nx);
    				idBoard[ny][nx] = id;
    				break;
    			}
    			
    		}
    	}
    }
    
    static int idBoard[][];
    
    static int dy[] = {0,-1,0,1};
    static int dx[] = {1,0,-1,0};
    static boolean OOB(int y,int x) {
    	return y<=0 || y>n || x<=0 || x>n;
    }
    
    static class Pair{
    	int y,x;
    	public Pair(int y, int x) {
    		this.y = y;
    		this.x = x;
    	}
    	public String toString() {
    		return y+" "+x;
    	}
    }

    static Team teams[];
    static class Team{
    	Pair head, tail;
    	
    	public String toString() {
    		return String.format("head: %s | tail: %s", head, tail);
    	}
    }
    
    
    static StringTokenizer st;
    static int n,m,k;
    static int board[][], order[][];
}