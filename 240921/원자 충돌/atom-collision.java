import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        
        board = new ArrayList[n+1][n+1];
        nxtBoard = new ArrayList[n+1][n+1];
        for(int y=1; y<=n; y++) {
        	for(int x=1; x<=n; x++) {
        		board[y][x] = new ArrayList<>();
        		nxtBoard[y][x] = new ArrayList<>();
        	}
        }
        for(int i = 0; i<m;i++) {
        	st = new StringTokenizer(br.readLine());
        	int y = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken());
        	int mass = Integer.parseInt(st.nextToken());
        	int speed = Integer.parseInt(st.nextToken());
        	int dir = Integer.parseInt(st.nextToken());
        	Atom atom = new Atom(mass,speed, dir);
        	board[y][x].add(atom);
        }
        
        for(int turn = 1; turn<=k; turn++) {
//        	System.out.println("-----");
//        	System.out.println("turn: "+turn);
        	moveAll();
        	
        	fusionAll();
        	
        }
        
        int ans = 0;
        for(int y=1; y<=n; y++) {
        	for(int x=1; x<=n; x++) {
        		for(Atom atom : board[y][x]) {
        			ans += atom.mass;
        		}
        	}
        }
        System.out.println(ans);
    }
    
    static void fusion(int y,int x) {
		int massSum = 0;
		int speedSum = 0;
		
		boolean flag = check(board[y][x]);
		int size = board[y][x].size();
		for(Atom atom : board[y][x]) {
			massSum += atom.mass;
			speedSum += atom.speed;
		}
		
		int mass = massSum / 5;
		board[y][x].clear();
		if(mass == 0) {
			return;
		}
		
		int speed = speedSum / size;
		for(int dir = flag ? 0 : 1; dir <8; dir+=2) {
			board[y][x].add(new Atom(mass,speed, dir));
		}
		
    }
    
    static boolean check(List<Atom> atoms) {
    	int dir = atoms.get(0).dir;
    	
		for(int i = 1; i< atoms.size(); i++) {
			int d = atoms.get(i).dir;
			if((dir % 2 == 0 && d % 2 == 1) || (dir %2 ==1 && d % 2 ==0))
				return false;
		}
		return true;
    }
    
    
    
    static void fusionAll() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(board[y][x].size() >= 2) {
    				fusion(y,x);
    			}
    		}
    	}
    	
//    	System.out.println("after fusion");
//    	printBoard(board);
    	
    }
    static int n,m,k;
    static StringTokenizer st;
    static ArrayList<Atom> board[][], nxtBoard[][];
    static int dy[] = {-1,-1,0,1,1,1,0,-1};
    static int dx[] = {0,1,1,1,0,-1,-1,-1};
    static boolean OOB(int y,int x) {
    	return y<=0 || y>n || x<=0 || x>n;
    }
    
    
    static class Atom{
    	int mass, speed, dir;
    	public Atom(int mass, int speed, int dir) {
    		this.mass = mass;
    		this.speed = speed;
    		this.dir = dir;
    	}
    }
    
    static void move(int y,int x) {
    	for(Atom atom : board[y][x]) {
    		int dir = atom.dir;
    		int speed = atom.speed;
    		int ny = y + dy[dir] * (speed % n);
    		int nx = x + dx[dir] * (speed % n);
    		
    		
    		ny = (ny - 1 + n) % n + 1;
    		nx = (nx - 1 + n) % n + 1;
    		nxtBoard[ny][nx].add(atom);
    	}
    }
    
    static void moveAll() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			nxtBoard[y][x].clear();
    		}
    	}
    	for(int y= 1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			
    			if(!board[y][x].isEmpty())
    				move(y,x);
    		}
    	}
    	
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			board[y][x] = (ArrayList)nxtBoard[y][x].clone();
    		}
    	}
    	
//    	System.out.println("after move");
//    	printBoard(board);
    	
    }
    
    static void printBoard(List<Atom> board[][]) {
    	for(int y= 1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			System.out.printf("%3d", board[y][x].size());
    		}
    		System.out.println();
    	}
    }
}