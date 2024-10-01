import java.util.*;
import java.io.*;
public class Main {
    static int n;
    static class Pair{
        int first, second;
        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
        
        public String toString() {
            return first +" "+second+"|";
        }
    }
    
    static Pair board[][];
    static Pair POLICE = new Pair(-1,-1);
    static Pair EMPTY = new Pair(-2,-2);
    static int policeDir = -1;
    static int dy[] = {-1,-1,0,1,1,1,0,-1};
    static int dx[] = {0,-1,-1,-1,0,1,1,1};
    static boolean OOB(int y,int x) {
        return y<0 || y>=4 ||x <0 || x>=4;
    }
    static StringTokenizer st;
    
    static class Tuple{
        int first, second, third;
        public Tuple(int first,int second ,int third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
        
        public String toString() {
            return first+" "+second+" "+third+"|";
        }
    }
    
    static boolean thiefCanGo(int y,int x) {
    	return !OOB(y,x) && board[y][x] != POLICE;
    }
    
    static Tuple getNxt(int y,int x,int dir) {
        for(int i = 0; i<8;i++) {
        	int moveDir = (dir + i) % 8;
        	int ny = y + dy[moveDir];
        	int nx = x + dx[moveDir];
        	if(thiefCanGo(ny,nx))
        		return new Tuple(ny,nx,moveDir);
        }
        return new Tuple(y,x,dir);
    }
    
    static void swap(int y1,int x1, int y2, int x2) {
    	Pair temp = board[y1][x1];
    	board[y1][x1] = board[y2][x2];
    	board[y2][x2] = temp;
    }
    
    static void move(int target) {
        for(int y=0; y<4;y++) {
            for(int x=0; x<4;x++) {
                if(board[y][x] == POLICE || board[y][x] == EMPTY)
                    continue;
                int id = board[y][x].first;
                int dir = board[y][x].second;
                if(target == id) {
                    Tuple nxt = getNxt(y,x,dir);
//                    board[y][x].second = nxt.third;
                    board[y][x] = new Pair(id,nxt.third);
                    swap(nxt.first, nxt.second, y,x);
                    return;
                }
            }
        }
    }
    
    static void moveAll() {
        for(int id=1; id<=16;id++) {
            move(id);
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
    	return !OOB(y,x) && board[y][x] != EMPTY;
    }
    
    static boolean end(int y,int x) {
    	for(int dist = 1; dist<=4; dist++) {
    		int ny = y + dy[policeDir] * dist;
    		int nx = x + dx[policeDir] * dist;
    		if(policeCanGo(ny,nx))
    			return false;
    	}
    	return true;
    }
    
    static ArrayList<Pair> getNxtPositions(int y, int x){
    	ArrayList<Pair> ret = new ArrayList<>();
    	for(int dist = 1; dist<=4; dist++) {
    		int ny = y + dy[policeDir] * dist;
    		int nx = x + dx[policeDir] * dist;
    		if(policeCanGo(ny,nx))
    			ret.add(new Pair(ny,nx));
    	}
    	return ret;
    }
    
    /*
     * (py,px) : 술래의 현재 위치 
     */
    static void solve(int py,int px, int sum) {
//    	System.out.println("-------");
    	
    	ArrayList<Pair> positions = getNxtPositions(py,px);
    	if(positions.isEmpty()) {
    		ans = Math.max(ans, sum);
    		return;
    		
    	}
    	
    	for(Pair nxt : positions) {
    		int ny = nxt.first;
    		int nx = nxt.second;
    		
    		int temp = policeDir;
    		Pair tempBoard[][] = new Pair[4][4];
    		for(int y = 0; y<4 ; y++) {
    			for(int x = 0; x<4; x++) {
    				tempBoard[y][x] = board[y][x];
    			}
    		}
    		
    		board[py][px] = EMPTY;
    		policeDir = board[ny][nx].second;
    		int nxtScore = board[ny][nx].first;
    		board[ny][nx] = POLICE;
    		moveAll();
    		solve(ny,nx, sum + nxtScore);
    		
    		
    		// restore
    		for(int i = 0; i<4;i++) {
    			for(int j = 0; j<4;j++)
    				board[i][j] = tempBoard[i][j];
    		}
    		policeDir = temp;
    		
    		
    	}
    	
//    	for(int dist = 1; dist<=4;dist++) {
//    		int ny = py + dy[policeDir] * dist;
//    		int nx = px + dx[policeDir] * dist;
//    		if(!policeCanGo(ny,nx))
//    			continue;
//    		
//    		
//    		//backup
//    		int temp = policeDir;
//    		Pair tempBoard[][] = new Pair[4][4];
//    		for(int y = 0; y<4 ; y++) {
//    			for(int x = 0; x<4; x++) {
//    				tempBoard[y][x] = board[y][x];
//    			}
//    		}
//    		
//    		board[py][px] = EMPTY;
//    		policeDir = board[ny][nx].second;
//    		int nxtScore = board[ny][nx].first;
//    		board[ny][nx] = POLICE;
//    		moveAll();
//    		solve(ny,nx, sum + nxtScore);
//    		
//    		
//    		// restore
//    		for(int i = 0; i<4;i++) {
//    			for(int j = 0; j<4;j++)
//    				board[i][j] = tempBoard[i][j];
//    		}
//    		policeDir = temp;
////    		System.out.println("----");
//    	}
    }
    
    public static void main(String[] args) throws IOException{
    	// System.setIn(new FileInputStream("./input.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        board = new Pair[4][4];
        for(int y= 0; y<4;y++) {
            st = new StringTokenizer(br.readLine());
            for(int x= 0; x<4;x++) {
                int id = Integer.parseInt(st.nextToken());
                int dir = Integer.parseInt(st.nextToken()) - 1;
                board[y][x] = new Pair(id,dir);
            }
        }
        policeDir = board[0][0].second;
        int score = board[0][0].first;
        board[0][0] = POLICE;
        moveAll();
        solve(0,0,score);
        System.out.println(ans);
    }
}