import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	n = Integer.parseInt(st.nextToken());
    	m = Integer.parseInt(st.nextToken());
    	k = Integer.parseInt(st.nextToken());
    	
    	food = new int[n+1][n+1];
    	plus = new int[n+1][n+1];
    	dead = new int[n+1][n+1];
    	board = new ArrayList[n+1][n+1];
    	nxtBoard = new ArrayList[n+1][n+1];
    	for(int y=1; y<=n; y++) {
    		st = new StringTokenizer(br.readLine());
    		for(int x=1; x<=n; x++) {
    			plus[y][x] = Integer.parseInt(st.nextToken());
    			food[y][x] = 5;
    			board[y][x] = new ArrayList<>();
    		}
    	}
    	
    	for(int i = 0; i < m ;i++) {
    		st = new StringTokenizer(br.readLine());
    		int y = Integer.parseInt(st.nextToken());
    		int x = Integer.parseInt(st.nextToken());
    		int age = Integer.parseInt(st.nextToken());
    		board[y][x].add(new Virus(age));
    	}
    	
    	for(int turn = 1; turn<=k; turn++) {
//    		System.out.println("-----");
//    		System.out.println("turn: "+turn);
    		
    		
    		// 양분 섭취 
    		eatAll();
    		
    		// 죽은 바이러스 처리 
    		nourishAll();
    		
    		breedAll();
    		
    		plus();
//    		System.out.println("food");
//    		printBoard(food);
//    		System.out.println("virus");
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
    
    
    static void plus() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			food[y][x] += plus[y][x];
    		}
    	}
    }
    static int dy[] = {-1,-1,0,1,1,1,0,-1};
    static int dx[] = {0,1,1,1,0,-1,-1,-1};
    static boolean OOB(int y,int x) {
    	return y<=0 || y > n || x<=0 || x>n;
    }
    
    static void breed(int y,int x) {
    	for(Virus v : board[y][x]) {
    		// 5의 배수의 나이를 가진 바이러스에게만 진행됨 
    		if(v.age % 5 != 0)
    			continue;
    		
    		for(int dir = 0; dir < 8; dir++) {
    			int ny = y + dy[dir];
    			int nx = x + dx[dir];
    			if(OOB(ny,nx))
    				continue;
    			
    			board[ny][nx].add(new Virus(1));
    		}
    	}
    }
    
    static void breedAll() {
    	for(int y=1;y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(board[y][x].isEmpty())
    				continue;
    			breed(y,x);
    		}
    	}
    }
    
    static void nourishAll() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			food[y][x] += dead[y][x];
    		}
    	}
    }
    
    
    static void eat(int y,int x) {
    	ArrayList<Virus> list = board[y][x];
    	Collections.sort(list); // 나이가 어린 바이러스부터 양분 섭취 
    	for(Virus v : list) {
    		// 죽은 바이러스가 양분이 되는걸 지금하면 죽어야하는 바이러스가 살게 되는 경우가 생길 수 있음
    		if(food[y][x] < v.age) { 
    			dead[y][x] += v.age/2;
    			continue;
    		}
    		food[y][x] -= v.age;
    		v.age++;
    		nxtBoard[y][x].add(v);
    	}
    }
    
    static void eatAll() {
    	
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			nxtBoard[y][x] = new ArrayList<>();
    			dead[y][x] = 0;
    		}
    	}
    	
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(board[y][x].isEmpty())
    				continue;
    			eat(y,x);
    		}
    	}
    	
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			board[y][x] = nxtBoard[y][x];
    		}
    	}
    	
//    	System.out.println("after eat");
//    	printBoard(food);
//    	printBoard(board);
    	
    }
    
    static int n,m,k;
    static StringTokenizer st;
    static int food[][], plus[][], dead[][];
    static ArrayList<Virus> board[][], nxtBoard[][];
    
    static void printBoard(int board[][]) {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			System.out.printf("%3d", board[y][x]);
    		}
    		System.out.println();
    	}
    }
    
    static void printBoard(ArrayList<Virus> board[][]) {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			System.out.printf("%s", board[y][x]);
    		}
    		System.out.println();
    	}
    }
    static class Virus implements Comparable<Virus>{
    	int age;
    	public Virus(int age) {
    		this.age = age;
    	}
    	
    	public int compareTo(Virus o) {
    		return age - o.age;
    	}
    	public String toString() {
    		return age+"";
    	}
    }
}