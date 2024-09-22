import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	n = Integer.parseInt(st.nextToken());
    	m = Integer.parseInt(st.nextToken());
    	k = Integer.parseInt(st.nextToken());
    	c = Integer.parseInt(st.nextToken());
    	
    	board = new int[n+1][n+1];
    	nxtBoard = new int[n+1][n+1];
    	drug = new int[n+1][n+1];
    	for(int y=1; y<=n; y++) {
    		st = new StringTokenizer(br.readLine());
    		for(int x=1; x<=n; x++) {
    			board[y][x] = Integer.parseInt(st.nextToken());
    		}
    	}
    	
    	for(int turn=1; turn<=m; turn++) {
//    		System.out.println("------");
//    		System.out.println("turn: "+turn);
    		
    		// 나무 성장 
    		growAll();
    		
    		// 나무 번식 
    		breedAll();
    		
    		// 제초제 뿌리기 
    		spray();
    		
    		// 제초제 사라짐 
    		vanish();
    	}
    	System.out.println(ans);
    }
    
    static int ans = 0;
    static void vanish() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(drug[y][x] > 0)
    				drug[y][x]--;
    		}
    	}
    	
//    	System.out.println("after spray");
//    	printBoard(drug);
    }
    
    
    static void spray() {
    	// 1. 나무가 가장 많이 박멸되는 칸 찾기 
    	Tuple best = findBestPos();
//    	System.out.println("best: "+best);
    	if(best == WORST)
    		return;
    	
    	ans += best.cnt;
    	board[best.y][best.x] = 0;
    	drug[best.y][best.x] = c + 1;
    	
    	for(int dir = 1; dir < 8; dir+=2) {
    		int dist = k;
    		int y = best.y;
    		int x = best.x;
    		while(dist-- >0) {
    			int ny = y + dy[dir];
    			int nx = x + dx[dir];
    			if(OOB(ny,nx))
    				break;
    			
    			y = ny;
    			x = nx;
    			drug[y][x] = c + 1; // 이 칸까지는 제초제가 뿌려짐. 새로 뿌려진 경우 c년 동안 제초제 유지  
    			if(board[y][x] <= 0) // 벽이나 나무가 없을 경우 이후로는 전파되지 않음 
    				break;
    			
    			board[y][x] = 0; // 나무가 있는 곳이라면 나무가 제거됨 
    		}
    	}
    	
    	
    }
    
    static Tuple findBestPos() {
    	Tuple ret = WORST;
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(board[y][x] <= 0) // 나무가 없는 칸에 뿌리면 박멸되는 나무가 없다 
    				continue;
    			Tuple pos = makePos(y,x);
    			if(ret.compareTo(pos) > 0)
    				ret = pos;
    		}
    	}
    	return ret;
    }
    
    /*
     * (y,x)에서 대각선 방향으로 k칸 뿌렸을 때 제거되는 나무 수 구하기 
     * 단 전파도중 벽이 있거나 나무가 없는 칸이 있는 경우 그 칸까지만 제초제가 뿌려지며 
     * 그 이후로는 전파되지 않음 
     */
    static Tuple makePos(int sy,int sx) {
    	int cnt = board[sy][sx]; // 현재 위치는 일단 제거됨 
    	for(int dir = 1; dir< 8; dir+=2) {
    		int dist = k;
    		int y = sy;
    		int x = sx;
    		
    		// dir 방향으로 쭉 나아감 
    		while(dist-- > 0) {
    			int ny = y + dy[dir];
    			int nx = x + dx[dir];
    			
    			if(OOB(ny, nx))
    				break;
    			
    			y = ny;
    			x = nx;
    			if(board[y][x] <=0 ) // 벽이 있거나 나무가 아예 없으면 멈춤
    				break;
    			cnt += board[y][x]; // 벽 아니고 나무 있는 경우만 카운트 
    		}
    	}
    	return new Tuple(sy,sx,cnt);
    }
    
    static Tuple WORST = new Tuple(22, 22, -1);
    
    static class Tuple implements Comparable<Tuple>{
    	int y,x,cnt;
    	public Tuple(int y,int x,int cnt) {
    		this.y = y;
    		this.x = x;
    		this.cnt = cnt;
    	}
    	
    	public String toString() {
    		return y+" "+x+" "+cnt;
    	}
    	
    	public int compareTo(Tuple o) {
    		if(cnt != o.cnt)
    			return o.cnt - cnt;
    		if(y != o.y)
    			return y - o.y;
    		return x - o.x;
    	}
    }
    
    static void breedAll() {
    	copy(nxtBoard, board);
    	
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(board[y][x] > 0)
    				breed(y,x);
    		}
    	}
    	
    	copy(board,nxtBoard);
//    	System.out.println("after breed");
//    	printBoard(board);
    }
    
    
    
    static void breed(int cy,int cx) {
    	List<int[]> blanks = new ArrayList<>();
    	for(int dir = 0; dir < 8; dir += 2) {
    		int ny = cy + dy[dir];
    		int nx = cx + dx[dir];
    		
    		// 벽이거나 다른나무가 있거나( 빈칸이 아님) 또는 제초제가 남아있으면 스킵 
    		if(OOB(ny,nx) || board[ny][nx] != BLANK || drug[ny][nx] >0 )
    			continue;
    		blanks.add(new int[] {ny,nx});
    	}
    	
    	int size = blanks.size();
    	if(size == 0)
    		return;
    	
    	/*
    	 * 번식은 모든 나무에서 동시에 일어남 
    	 * -> 현재 나무가 번식한 결과가 다음 나무의 번식에 영향을 줘선 안됨
    	 * 따라서 board와 nxtBoard를 분리 
    	 */
    	int treeCnt = board[cy][cx] / size;
    	for(int[] blank : blanks) {
    		int y = blank[0];
    		int x = blank[1];
    		
    		nxtBoard[y][x] += treeCnt;
    	}
    }
    
    static void copy(int dst[][], int src[][]) {
    	for(int y=1; y<=n; y++) {
    		System.arraycopy(src[y],1,dst[y],1,n);
    	}
    }
    
    
    static void grow(int y,int x) {
    	int cnt = 0;
    	for(int dir = 0; dir < 8; dir += 2) {
    		int ny = y + dy[dir];
    		int nx = x + dx[dir];
    		if(OOB(ny,nx) || board[ny][nx] <=0)
    			continue;
    		cnt++;
    	}
    	board[y][x] += cnt;
    }
    
    /*
     * 모든 나무의 성장 
     */
    static void growAll() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(board[y][x] > 0) {
    				grow(y,x);
    			}
    		}
    	}
    	
//    	System.out.println("after grow");
//    	printBoard(board);
    }
    static int dy[] = {-1,-1,0,1,1,1,0,-1};
    static int dx[] = {0,1,1,1,0,-1,-1,-1};
    static boolean OOB(int y,int x) {
    	return y<=0 || y>n || x<=0 || x>n;
    }
    static int n,m,k,c;
    static StringTokenizer st;
    static int board[][], nxtBoard[][], drug[][];
    static int BLANK = 0, WALL = -1;
    
    static void printBoard(int board[][]) {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			System.out.printf("%3d", board[y][x]);
    		}
    		System.out.println();
    	}
    }
}