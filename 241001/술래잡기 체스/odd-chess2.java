import java.util.*;
import java.io.*;
public class Main {
    static int n;
    static class Pair{
        int id, dir;
        public Pair(int first, int second) {
            this.id = first;
            this.dir = second;
        }
        
        public String toString() {
            return id +" "+dir+"|";
        }
    }
    
    static int board[][];
    static Thief POLICE = new Thief(-1,-1,-1,-1);
    static Thief EMPTY = new Thief(-2,-2,-2,-2);
    static int dy[] = {-1,-1,0,1,1,1,0,-1};
    static int dx[] = {0,-1,-1,-1,0,1,1,1};
    static boolean OOB(int y,int x) {
        return y<0 || y>=4 ||x <0 || x>=4;
    }
    static StringTokenizer st;
    

    static void moveAll(Thief police) {
        for(int id=1; id<=16;id++) {
        	if(thiefs[id].isSame(POLICE) || thiefs[id].isSame(EMPTY))
        		continue;
        	thiefs[id].move(police);
        }
    }
    
    static void printBoard() {
    	for(int y= 0; y<4; y++) {
    		for(int x=0; x<4; x++) {
    			System.out.print(board[y][x]+" ");
    		}
    		System.out.println();
    	}
    }
    static int ans;
    
    static boolean policeCanGo(int y,int x) {
    	return !OOB(y,x) && board[y][x] != -2;
    }
    
    static class Pos{
    	int y,x;
    	public Pos(int y,int x) {
    		this.y = y;
    		this.x = x;
    	}
    	
    	public String toString() {
    		return y+" "+x;
    	}
    }
    
    static ArrayList<Pos> getNxtPositions(Thief police){
    	int y = police.y;
    	int x = police.x;
    	int dir = police.dir;
    	ArrayList<Pos> ret = new ArrayList<>();
    	for(int dist = 1; dist<=4; dist++) {
    		int ny = y + dy[dir] * dist;
    		int nx = x + dx[dir] * dist;
    		if(policeCanGo(ny,nx))
    			ret.add(new Pos(ny,nx));
    	}
    	return ret;
    }
    
    /*
     * (py,px) : 술래의 현재 위치 
     */
    static void solve(Thief p, int sum) {
    	
    	ArrayList<Pos> positions = getNxtPositions(p);
    	if(positions.isEmpty()) {
    		ans = Math.max(ans, sum);
    		return;
    		
    	}
    	
//    	System.out.println("positions: "+positions);
    	for(Pos nxt : positions) {
    		int ny = nxt.y;
    		int nx = nxt.x;
    		
    		
    		// backup
    		
    		int temp[][] = new int[4][4];
    		copy(board, temp);
    		Thief tempThiefs[] = new Thief[17];
    		for(int i = 1; i<=16; i++) {
    			tempThiefs[i] = new Thief(thiefs[i]);
    		}
    		
    		// 현재 술래가 있던 위치는 EMPTY로 변경 
    		board[p.y][p.x] = -2;
    		thiefs[p.id] = EMPTY;
    		
    		
    		
    		/*
    		 * 술래의 이동 
    		 */
    		int id = board[ny][nx];
    		int dir = thiefs[id].dir;
    		Thief nxtPolice = thiefs[id]; 
    		thiefs[id] = POLICE;
    		board[ny][nx] = -1;
    		
    		// 도둑의 이동
    		moveAll(nxtPolice);
    		
    		
    		solve(nxtPolice, sum + id);
    		
    		
    		// restore
    		copy(temp, board);
    		for(int i = 1; i<=16; i++) {
    			thiefs[i] = tempThiefs[i];
    		}
    		
    		
    	}
    }
    
    static void copy(int src[][], int dst[][]) {
    	for(int y=0; y<4; y++) {
    		System.arraycopy(src[y], 0, dst[y], 0, 4);
    	}
    }
    
    static void printThiefs() {
    	for(int i = 1;i<=16;i++) {
    		System.out.println(thiefs[i]);
    	}
    }
    
    static class Thief{
    	int y,x,id, dir;
    	public Thief(int y,int x,int id,int dir) {
    		this.y = y;
    		this.x = x;
    		this.id = id;
    		this.dir = dir;
    	}
    	
    	public Thief(Thief o) {
    		this(o.y,o.x,o.id,o.dir);
    	}
    	
    	public String toString() {
    		return String.format("id: %d | (%d %d) | dir: %d", id,y,x,dir);
    	}
    	
    	public void move(Thief police) {
//    		System.out.println(this);
    		Tuple nxt = getNxt(police);
    		
    		int id = board[nxt.y][nxt.x];
    		
    		if(id > 0) {
    			Thief other = thiefs[id];
    			
    			board[other.y][other.x] = this.id;
    			board[y][x] = id;
    			
    			other.y = y;
    			other.x = x;
    			
    			y = nxt.y;
    			x = nxt.x;
    			dir = nxt.dir;
    			
    		}else if(id == -2) {
    			board[y][x] = id;
    			y = nxt.y;
    			x = nxt.x;
    			dir = nxt.dir;
    		}
    		
    	}
    	
    	private Tuple getNxt(Thief police) {
    		for(int i = 0; i < 8;i++) {
    			int moveDir = (dir + i) % 8;
    			int ny = y + dy[moveDir];
    			int nx = x + dx[moveDir];
    			
    			if(canGo(ny,nx))
    				return new Tuple(ny,nx,moveDir);
    		}
    		
    		return new Tuple(y,x,dir);
    	}
    	
    	private boolean canGo(int y,int x) {
    		return !OOB(y,x) && board[y][x] != -1;
    	}
    	
    	public boolean isSame(Thief o) {
    		return this.y == o.y && this.x == o.x;
    	}
    	
    }
    
    static Thief thiefs[];
    static class Tuple{
    	int y,x,dir;
    	public Tuple(int y,int x,int dir) {
    		this.y = y;
    		this.x = x;
    		this.dir = dir;
    	}
    	
    }
    
    static void printBoard(int board[][]) {
    	for(int y=0; y<4; y++) {
    		for(int x=0; x< 4; x++) {
    			System.out.printf("%3d", board[y][x]);
    		}
    		System.out.println();
    	}
    }
    
    public static void main(String[] args) throws IOException{
    	// System.setIn(new FileInputStream("./input.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        board = new int[4][4];
        thiefs = new Thief[17];
        for(int y= 0; y<4;y++) {
            st = new StringTokenizer(br.readLine());
            for(int x= 0; x<4;x++) {
                int id = Integer.parseInt(st.nextToken());
                int dir = Integer.parseInt(st.nextToken()) - 1;
//                board[y][x] = new Pair(id,dir);
                board[y][x] = id;
                thiefs[id] = new Thief(y,x,id,dir);
            }
        }
        
        // 술래의 이동 
        int id = board[0][0];
        Thief police = thiefs[id];
        board[0][0] = -1;
        thiefs[id] = POLICE;
        
        // 도둑의 이동 
        moveAll(police);
        
        
//        printBoard(board);
//        printThiefs();
        solve(police,id);
        System.out.println(ans);
    }
}