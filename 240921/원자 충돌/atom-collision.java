import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        
        board = new int[n+1][n+1];
        for(int i = 0; i<m;i++) {
        	st = new StringTokenizer(br.readLine());
        	int y = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken());
        	int mass = Integer.parseInt(st.nextToken());
        	int speed = Integer.parseInt(st.nextToken());
        	int dir = Integer.parseInt(st.nextToken());
        	Atom atom = new Atom(y,x,mass,speed, dir);
        	atoms.add(atom);
        	board[y][x] += 1;
        }
        
//        printBoard(board);
        for(int turn = 1; turn<=k; turn++) {
//        	System.out.println("-----");
//        	System.out.println("turn: "+turn);
        	
        	moveAll();
        	
        	fusionAll();
        	
        }
        
        int ans = 0;
        for(Atom atom : atoms) {
        	ans += atom.mass;
        }
        		
        System.out.println(ans);
    }
    
    static void fusion(int y,int x) {
		int massSum = 0;
		int speedSum = 0;
		
		int size = atoms.size();
		List<Integer> dirs = new ArrayList<>();
		for(int i = size - 1; i>=0;i--) {
			Atom atom = atoms.get(i);
			if(atom.isSame(y,x)) {
				massSum += atom.mass;
				speedSum += atom.speed;
				dirs.add(atom.dir);
				atoms.remove(i);
			}
		}
		
		if(dirs.isEmpty())
			return;
    	boolean flag = check(dirs);
    	int mass = massSum / 5;
    	if(mass == 0) {
    		board[y][x] = 0;
    		return;
    	}
    	int speed = speedSum / dirs.size();
    	board[y][x] = 4;
    	for(int d = flag ? 0 : 1 ; d<8; d+=2) {
    		Atom atom = new Atom(y,x,mass,speed, d);
    		atoms.add(atom);
    	}
    }
    
    static boolean check(List<Integer> dirs) {
    	int dir = dirs.get(0);
    	
		for(int i = 1; i< dirs.size(); i++) {
			int d = dirs.get(i);
			if((dir % 2 == 0 && d % 2 == 1) || (dir %2 ==1 && d % 2 ==0))
				return false;
		}
		return true;
    }
    
    
    
    static void fusionAll() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(board[y][x] >= 2) {
    				fusion(y,x);
    			}
    		}
    	}
    }
    static int n,m,k;
    static StringTokenizer st;
//    static Atom atoms[];
    static List<Atom> atoms = new ArrayList<>();
    static int board[][], nxtBoard[][];
    static int dy[] = {-1,-1,0,1,1,1,0,-1};
    static int dx[] = {0,1,1,1,0,-1,-1,-1};
    static boolean OOB(int y,int x) {
    	return y<=0 || y>n || x<=0 || x>n;
    }
    
    
    static class Atom{
    	int y,x,mass, speed, dir;
    	public Atom(int y,int x, int mass, int speed, int dir) {
    		this.y = y;
    		this.x = x;
    		this.mass = mass;
    		this.speed = speed;
    		this.dir = dir;
    	}
    	
    	public String toString() {
    		return y+" "+x;
    	}
    	
    	public boolean isSame(int y,int x) {
    		return this.y == y && this.x == x;
    	}
    	
    	public void move() {
    		int s = speed % n;
    		int ny = y + dy[dir] * s;
    		int nx = x + dx[dir] * s;
//    		System.out.printf("/(%d %d), (%d %d)\n",y,x,ny,nx);
    		ny = (ny - 1 + n) % n + 1;
    		nx = (nx - 1 + n) % n + 1;
//    		System.out.printf("(%d %d), (%d %d)\n",y,x,ny,nx);
    		board[y][x]--;
    		y = ny;
    		x = nx;
    		board[y][x]++;
    	}
    }
    
    static void moveAll() {
    	for(int i = 0; i < atoms.size();i++) {
    		Atom atom = atoms.get(i);
//    		System.out.println("atom: "+atom);
    		atom.move();
    	}
//    	System.out.println("after move");
//    	System.out.println(atoms);
//    	printBoard(board);
    }
    
    static void printBoard(int board[][]) {
    	for(int y= 1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			System.out.printf("%3d", board[y][x]);
    		}
    		System.out.println();
    	}
    }
}