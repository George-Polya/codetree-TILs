import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        h = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        
        thief = new List[n+1][n+1];
        nxtThief = new List[n+1][n+1];
        for(int y=1; y<=n; y++) {
        	for(int x=1; x<=n; x++) {
        		thief[y][x] = new ArrayList<>();
        	}
        }
        for(int i = 0; i < m ;i++) {
        	st = new StringTokenizer(br.readLine());
        	int y = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken());
        	int dir = Integer.parseInt(st.nextToken());
        	thief[y][x].add(dir - 1);
        }
        
        tree = new boolean[n+1][n+1];
        for(int i = 0; i < h ;i++) {
        	st = new StringTokenizer(br.readLine());
        	int y = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken());
        	tree[y][x] = true;
        }
        cy = (n+1)/2;
        cx = (n+1)/2;
        
        police = new Police(cy, cx, 3); // 술래는 정중앙에서 시작 
//        printBoard(thief);
        for(int turn = 1; turn<=k; turn++) {
//        	System.out.println("------");
//        	System.out.println("turn: "+turn);
        	
        	// 도망자들의 이동 
        	moveThief();
        	
        	
        	// 술래의 이동 
        	police.move(turn);
        }
        
        System.out.println(ans);
    }
    
    static Police police;
    
    static class Police{
    	int y, x, dir, moveCnt, moveNum;
    	boolean flag;
    	public Police(int y,int x, int dir) {
    		this.y = y;
    		this.x = x;
    		this.dir = dir;
    		this.flag = true;
    		this.moveCnt = 1;
    		this.moveNum = this.moveCnt;
    	}
    	
    	public String toString() {
    		return String.format("(%d,%d)| dir: %d | moveNum: %d", y,x,dir, moveNum);
    	}
    	
    	public boolean isSame(int y,int x) {
    		return this.y == y && this.x == x;
    	}
    	
    	public void move(int turn) {
    		if(flag) {
    			int ny = y + dy[dir];
    			int nx = x + dx[dir];
    			y = ny;
    			x = nx;
    			moveNum--;
    			
    			if(moveNum == 0) {
    				dir = (dir + 1) % 4;
    				if(dir == 1 || dir == 3) {
    					moveCnt++;
    				}
    				moveNum = moveCnt;
    			}
    			
    			if(y == 1 && x == 1) {
    				flag = !flag;
    				dir = 1;
    				moveCnt = n-1;
    				moveNum = moveCnt;
    			}

    		}else {
    			int ny = y + dy[dir];
    			int nx = x + dx[dir];
    			y = ny;
    			x = nx;
    			moveNum--;
    			
    			if(moveNum == 0) {
    				dir = (dir + 3) % 4;
    				if( y == n && x == 1) {
    					moveCnt = n;
    				}
    				
    				if(dir == 0 || dir == 2)
    					moveCnt--;
    				moveNum = moveCnt;
    			}
    			
    			if(y == cy && x == cx) {
    				flag = !flag;
    				dir = 3;
    				moveCnt = 1;
    				moveNum = moveCnt;
    			}
    		}
    		
    		// 도망자 잡기 
//    		System.out.println("after police move: "+this.toString());
    		int cnt = 0;
    		
    		for(int dist = 0; dist < 3; dist++) {
    			int ny = y + dy[dir] * dist;
    			int nx = x + dx[dir] * dist;
    			if(OOB(ny,nx))
    				break;
    			if(tree[ny][nx]) // 나무가 있으면 잡지 못함 
    				continue;
    			
    			cnt += thief[ny][nx].size();
    			thief[ny][nx].clear(); // 잡힌 도망자는 사라짐 
    		}
    		ans += turn * cnt;
//    		printBoard(thief);
    	}
    }
    
    static int ans;
    
    /*
     * 현재 칸에 있는 모든 도둑들의 이동 
     */
    static void move(int y,int x) {
    	for(int dir : thief[y][x]) {
    		Tuple nxt = getNxtPos(y,x,dir); // 다음 위치 
    		nxtThief[nxt.y][nxt.x].add(nxt.dir);
    	}
    }
    
    /*
     * 다음 위치구하기 
     * 1. 다음 칸이 OOB가 아닌 경우
     *  1.1 술래가 있다면 이동X(위치와 방향 모두 그대로)
     *  1.2 술래가 없다면 이동O(위치 변경, 방향 그대로)
     * 2. 다음칸이 OOB인 경우
     *  2.1 방향 변경
     *  2.2 변경 후의 다음칸에 술래가 있음=>이동X(위치 그대로, 방향 변경)
     *  2.3 변경 후의 다음칸에 술래가 없음=>이동O(위치, 방향 모두 변경)
     */
    static Tuple getNxtPos(int y,int x, int dir) {
    	int ny = y + dy[dir];
    	int nx = x + dx[dir];
    	
    	if(!OOB(ny,nx)) { // 다음 위치가 OOB가 아닌 경우 
    		if(police.isSame(ny,nx)) // 다음 위치에 술래가 있음 
    			return new Tuple(y,x,dir); //이동 X(위치, 방향 그대로) 
    		
    		return new Tuple(ny,nx,dir); // 술래가 없으면 이동O(위치변경, 방향그대로)
    	}
    	
    	dir = (dir + 2) % 4; // 방향 변경 
    	
    	// 방향 변경 후의 다음 칸 
    	ny = y + dy[dir];
    	nx = x + dx[dir];
    	
    	if(police.isSame(ny, nx))
    		return new Tuple(y,x,dir); // 술래 있으면 위치 그대로, 방향 변경
    	
    	return new Tuple(ny,nx,dir);
    }
    
    static boolean canMove(int y,int x) {
    	return getDistance(y,x) <= 3;
    }
    
    static int getDistance(int y,int x) {
    	return Math.abs(police.y - y) + Math.abs(police.x - x);
    }
    
    
    static void moveThief() {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++)
    			nxtThief[y][x] = new ArrayList<>();
    	}
    	
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			if(thief[y][x].isEmpty())
    				continue;
    			
    			// 도망자가 움직일때 술래와의 거리가 3이하인 도망자만 움직임
    			if(canMove(y,x))
    				move(y,x);
    		}
    	}
    	
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++)
    			thief[y][x] = nxtThief[y][x];
    	}
    	
//    	System.out.println("after thief move");
//    	printBoard(thief);
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
    	
    	public String toString() {
    		return y+" "+x +" "+dir;
    	}
    }
    
    
    static void printBoard(List<Integer> board[][]) {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++)
    			System.out.printf("%3s", board[y][x]);
    		System.out.println();
    	}
    }
    
    static int n,m,h,k, cy,cx;
    static StringTokenizer st;
    static boolean tree[][];
    static List<Integer> thief[][], nxtThief[][];
}