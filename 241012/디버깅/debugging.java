import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException{
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	n = Integer.parseInt(st.nextToken());
    	m = Integer.parseInt(st.nextToken());
    	h = Integer.parseInt(st.nextToken());
    	
    	board = new boolean[h+1][n];
    	for(int i = 0; i < m ;i++) {
    		st = new StringTokenizer(br.readLine());
    		int a = Integer.parseInt(st.nextToken());
    		int b = Integer.parseInt(st.nextToken());
    		board[a][b] = true;
    	}
    	
    	if(m == 0) {
    		System.out.println(0);
    		return;
    	}
    	
    	solve(1, 0);
    	System.out.println(ans == INT_MAX ? -1 : ans);
    }
    
    static int INT_MAX = Integer.MAX_VALUE;
    static int ans = INT_MAX;
    
    static int go(int y, int x) {
//    	System.out.printf("%d %d\n", y,x);
    	if(y == h + 1)
    		return x;

    	if(x == 1) {
    		if(board[y][x])
    			return go(y+1, x + 1);
    	}
    	else if(x == n) {
    		if(board[y][x-1])
    			return go(y+1, x-1);
    	}else {
    		if(board[y][x])
    			return go(y+1, x+1);
    		if(board[y][x-1])
    			return go(y+1, x-1);
    	}
    	return go(y+1,x);
    }
    
    static boolean check() {
    	for(int x = 1; x<= n; x++) {
    		int idx = go(1, x);
//    		System.out.println("idx: "+idx);
    		if(x != idx)
    			return false;
    	}
    	return true;
    }
    
    static void printBoard(boolean board[][]) {
    	for(int y= 1; y<=h; y++) {
    		for(int x=1; x<=(n-1);x++)
    			System.out.printf("%3d", board[y][x] ? 1 : 0);
    		System.out.println();
    	}
    }
    
    static void solve(int cur, int cnt) {
//    	System.out.println("-----");
//    	printBoard(board);
    	
    	if(cnt >= ans)
    		return;
    	
    	if(check()) {
    		ans = Math.min(ans, cnt);
    	}
    	
    	if(cnt == 3)
    		return;
    	
    	for(int i = cur; i <= (n-1) * h; i++) {
    		int y = i % (n-1) == 0 ? i / (n-1) : i / (n-1) + 1;
    		int x = i % (n-1) == 0 ? n - 1 : i % (n-1);
    		if(board[y][x] || !exist(y,x))
    			continue;
//    		System.out.printf("%d %d\n", y,x);
    		board[y][x] = true;
    		solve(cur + 1, cnt + 1);
    		board[y][x] = false;
    	}
    }
    
    static boolean OOB(int y,int x) {
    	return y<=0 || y > n || x <= 0 || x > n-1;
    }
    
    /*
     * (y,x) 왼쪽 혹은 오른쪽에 유실선이 있는지 여부 
     * x == 1이면 오른쪽만 체크하면 되고, x == (n-1)이면 왼쪽만 체크하면 됨 
     */
    static boolean exist(int y,int x) {
//    	System.out.printf("%d %d\n", y,x);
    	if(x == 1 && (OOB(y,x+1) || board[y][x+1]))
    		return false;
    	if(x == n - 1 && (OOB(y,x-1) || board[y][x-1]))
    		return false;
    	
    	if( (1< x && x<(n-1)) && (board[y][x-1] || board[y][x+1]))
    		return false;
    	
    	return true;
    		
    }
    
    static StringTokenizer st;
    static int n,m,h;
    static boolean board[][];
}