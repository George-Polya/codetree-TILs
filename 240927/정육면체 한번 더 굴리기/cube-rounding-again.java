import java.io.*;
import java.util.Arrays;
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
        
        dice = new Dice();
        int ans = 0;
        for(int turn = 1; turn<=m; turn++) {
//        	System.out.println("------");
//        	System.out.println("turn: "+turn);
        	
        	dice.move();
//        	System.out.println("after move");
//        	System.out.println(dice);
//        	dice.printState();
        	
        	
        	// 점수 획득 
        	int score = bfs(dice.y, dice.x);
//        	System.out.println("score: "+score);
        	ans += score;
        	
        	// 방향 변경 
        	dice.rotate();
//        	System.out.println("after rotate: "+dice.dir);
        	
        }
        System.out.println(ans);
    }
    static int n,m;
    static StringTokenizer st;
    static int board[][];
    
    static Dice dice;
    
    static class Pair{
    	int y,x;
    	public Pair(int y,int x) {
    		this.y = y;
    		this.x = x;
    	}
    }
    
    static int bfs(int y,int x) {
    	int value = board[y][x];
    	boolean visited[][] = new boolean[n+1][n+1];
    	Queue<Pair> q = new ArrayDeque<>();
    	int ret = 0;
    	q.add(new Pair(y,x));
    	visited[y][x] = true;
    	
    	while(!q.isEmpty()) {
    		Pair cur = q.poll();
    		
    		ret += board[cur.y][cur.x];
    		
    		for(int dir = 0; dir < 4; dir++) {
    			int ny = cur.y + dy[dir];
    			int nx = cur.x + dx[dir];
    			
    			if(OOB(ny,nx) || visited[ny][nx] || board[ny][nx] != value)
    				continue;
    			
    			visited[ny][nx] = true;
    			q.add(new Pair(ny,nx));
    		}
    	}
    	return ret;
    }
    
    static class Dice{
    	int y,x,dir;
    	int top, bottom, left, right, front, back;
    	
    	public Dice() {
    		this.y = 1;
    		this.x = 1;
    		this.dir = 0;
    		this.top = 1;
    		this.bottom = 7 - top;
    		this.front = 2;
    		this.back = 7 - front;
    		this.right = 3;
    		this.left = 7 - right;
    	}
    	
    	public void move() {
    		Tuple nxt = getNxtPos(y,x,dir);
    		
    		y = nxt.y;
    		x = nxt.x;
    		dir = nxt.dir;
			roll(dir);
    	}
    	
    	private void roll(int dir) {
    		switch(dir) {
    		case 0:{
    			bottom = right;
    			right = top;
    			top = 7 - bottom;
    			left = 7 - right;
    			break;
    		}
    		case 1:{
    			bottom = front;
    			front = top;
    			top = 7 - bottom;
    			back = 7 - front;
    			break;
    		}
    		case 2:{
    			bottom = left;
    			left = top;
    			top = 7 -bottom;
    			right = 7 - left;
    			break;
    		}
    		case 3:
    			bottom = back;
    			back = top;
    			top = 7 - bottom;
    			front = 7 - back;
    			break;
    		}
    	}
    	
    	public void rotate() {
    		int value = board[y][x];
    		if(bottom > value) {
    			dir = (dir + 1) % 4;
    		}else if(bottom < value) {
    			dir = (dir + 3) % 4;
    		}
    	}
    	
    	public void printState() {
    		System.out.printf("top: %d, bottom: %d\n", top, bottom);
    		System.out.printf("front: %d, back: %d\n", front, back);
    		System.out.printf("right: %d, left: %d\n", right, left);
    	}
    	public String toString() {
    		return y+" "+x+" "+dir;
    	}
    }
    
    static Tuple getNxtPos(int y,int x,int dir) {
    	int ny = y + dy[dir];
    	int nx = x + dx[dir];
    	
    	if(OOB(ny,nx)) {
    		dir = (dir + 2) % 4;
    		ny = y + dy[dir];
    		nx = x + dx[dir];
    	}
    	
    	return new Tuple(ny,nx,dir);
    }
    
    static int dy[] = {0,1,0,-1};
    static int dx[] = {1,0,-1,0};
    static boolean OOB(int y,int x) {
    	return y<=0 || y>n || x<=0 || x>n;
    }
    
    static class Tuple{
    	int y,x,dir;
    	public Tuple(int y,int x, int dir) {
    		this.y = y;
    		this.x = x;
    		this.dir = dir;
    	}
    }
}