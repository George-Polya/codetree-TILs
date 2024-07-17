import java.io.*;
import java.util.*;

public class Main {
    static int m,t;
    static int r,c;
    static StringTokenizer st;
    static int dead[][];

    static class Pair{
        int y,x;
        public Pair(int y,int x){
            this.y = y;
            this.x = x;
        }

        public String toString(){
            return y+" "+x;
        }
    }
    
    static int dy[] = {-1,-1,0,1,1,1,0,-1};
    static int dx[] = {0,-1,-1,-1,0,1,1,1};
    static boolean OOB(int y,int x) {
    	return y<=0 || y> 4 || x<=0 || x>4;
    }
    
    static void printBoard(int board[][]) {
    	for(int y=1; y<=4; y++) {
    		for(int x=1; x<=4; x++) {
    			System.out.print(board[y][x]+" ");
    		}
    		System.out.println();
    	}
    }

    static Pair packMan;



    
    static ArrayList<Integer> board[][], nxtBoard[][], egg[][];

    /**
     * 1. 몬스터 복제
     *  1.1 살아있는 몬스터에 대해서 복제 
     *  1.2 몬스터는 자신과 같은 방향을 가진 몬스터를 복제하려 한다 
     *  
     */
    static void tryClone(){

    	init();
    	
    	for(int y=1; y<=4; y++) {
    		for(int x=1; x<=4; x++) {
    			if(board[y][x].isEmpty())
    				continue;
    			egg[y][x].addAll(board[y][x]);
    			
    		}
    	}
    }
    
    static class Tuple{
    	int y,x,dir;
    	public Tuple(int y,int x, int dir) {
    		this.y = y;
    		this.x = x;
    		this.dir = dir;
    	}
    	
    	public String toString() {
    		return y +" "+x+" "+dir;
    	}
    }
    
    static void init() {
    	for(int y=1; y<=4; y++) {
    		for(int x=1; x<=4; x++) {
    			nxtBoard[y][x].clear();
    			egg[y][x].clear();
    		}
    	}
    }
    
    /*
     * 2. 다음 위치 구하기
     *  2.1 OOB, 팩맨이 있거나, 시체가 있으면 반시계방향으로 45도 회전하고 갈 수 있는지 확인
     *  2.2 모두 움직일 수 없으면 움직이지 않음
     */
    
    static Tuple getNxtPos(int y,int x, int dir) {
    	Tuple ret = new Tuple(y,x,dir);
    	
    	for(int i = 0; i < 8; i++) {
    		int moveDir = (dir + i) % 8; // 가려는 방향 
    		int ny = y + dy[moveDir];
    		int nx = x + dx[moveDir];
    		
    		if(OOB(ny,nx) || (ny == packMan.y && nx == packMan.x) || dead[ny][nx] != 0)
    			continue;
    		
    		return new Tuple(ny,nx,moveDir); // 움직일 수 있으면 그 위치와 방향을 리턴
    	}
    	
    	return ret; // 어느 방향으로든 움직일 수 없으면 움직이지 않음 
    }
    
    /*
     * 2.몬스터 하나의 이동
     *  2.1 몬스터가 알에서 깨어나지 않은 몬스터는 이동하지 않는다
     *  2.2 다음 위치 구하기
     *  2.3 몬스터 위치 업데이트 
     */
    static void move(int y, int x) {
    	for(int dir : board[y][x]) {
    		count[y][x] -= 1;
    		Tuple nxt = getNxtPos(y,x,dir);
    		nxtBoard[nxt.y][nxt.x].add(nxt.dir);
    		count[nxt.y][nxt.x] += 1;
    	}
    	
    }
    
    /*
     * 2. 몬스터들의 이동
     *  2.1 살아있는 몬스터가 이동
     *  
     */
    static void moveAll() {
    	
    	
    	for(int y=1; y<=4; y++) {
    		for(int x=1; x<=4; x++) {
    			if(board[y][x].isEmpty())
    				continue;
    			move(y,x);
    		}
    	}
    	
    	for(int y=1; y<=4; y++) {
    		for(int x=1; x<=4; x++) {
    			board[y][x] = (ArrayList<Integer>)nxtBoard[y][x].clone();
    		}
    	}
    	
    }
    
    static int count[][];
    
    
    static String bestDirs="999";
    static int maxSum = 0;
    
    /*
     * 팩맨의 최적 이동방향을 정하는 메소드
     * cnt : 이동 횟수
     * sum : 이동을 가정했을 때 먹은 몬스터의 수
     * y,x : 이동을 가정했을 때 위치
     * dirs : 이동 경로 
     */
    static void getBestDirs(int cnt, int sum, int y, int x, String dirs) {
    	if(cnt == 3) {
    		if(maxSum < sum || (maxSum == sum && dirs.compareTo(bestDirs) < 0) ) {
    			maxSum = sum;
    			bestDirs = dirs;
    		}
    		
    		return;
    	}
    	
    	for(int dir =0 ; dir <8; dir += 2) {
    		int ny = y + dy[dir];
    		int nx = x + dx[dir];
    		if(OOB(ny,nx))
    			continue;
    		int plus = count[ny][nx];
    		count[ny][nx] -= plus;
    		getBestDirs(cnt + 1, sum + plus, ny,nx, dirs+dir);
    		count[ny][nx] += plus;
    	}
    }
    
    
    
    /*
     * 3. 팩맨의 이동 
     *  3.1 팩맨의 3칸 이동 방향 정하기
     *   3.1.1 3칸 이동. 각 이동마다 상좌하우의 선택지를 갖게 됨. 
     *   3.1.2 몬스터를 가장 많이 먹을 수 있는 방향으로 움직임 -> 일단 3칸 이동해 봐야함 4개 중에서 1개 선택하는걸 3번 반복하는 중복 순열
     *  3.2 많이 먹을 수 있는 방향이 여러개라면 상좌하우의 우선순위를 가짐.  
     *  3.3 이동하기. 살아있는것만 먹어야한다. 
     */
    static void movePackman() {
    	bestDirs = "999";
    	maxSum = 0;
    	
    	int y = packMan.y;
    	int x = packMan.x;
    	getBestDirs(0,0, y,x, ""); // 최적의 경로 구하기 
    	
    	// 팩맨 이동 
    	for(int i = 0; i < bestDirs.length();i++) {
    		int moveDir = bestDirs.charAt(i) - '0';
    		int ny = y + dy[moveDir];
    		int nx = x + dx[moveDir];
    		if(count[ny][nx] > 0) { // 살아있는 몬스터가 있는 위치라면 
    			count[ny][nx] = 0; // 전부 죽임
    			dead[ny][nx] = 3; // 시체는 2턴 동안만 남는다 
    			
    			board[ny][nx].clear();
    			
    		}
    		y = ny;
    		x = nx;
    	}
    	packMan.y = y;
    	packMan.x = x;
    	
    	
    }
    
    static void destroyDead() {
    	for(int y=1; y<=4; y++) {
    		for(int x=1; x<=4; x++) {
    			if(dead[y][x] > 0)
    				dead[y][x]--;
    		}
    	}
    }
    
    static void completeClone() {
    	for(int y=1; y<=4; y++) {
    		for(int x=1; x<=4; x++) {
    			if(egg[y][x].isEmpty())
    				continue;
    			board[y][x].addAll(egg[y][x]);
    			count[y][x] += egg[y][x].size();
    		}
    	}
    }
    
    static void printMonster(ArrayList<Integer> board[][]) {
    	for(int y=1; y<=4; y++) {
    		for(int x=1; x<=4; x++) {
    			System.out.print(board[y][x].size() +" ");
    		}
    		System.out.println();
    	}
    }
    
    public static void main(String[] args) throws IOException{
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        m = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        dead = new int[4+1][4+1];
        count = new int[4+1][4+1];
        board = new ArrayList[4+1][4+1];
        nxtBoard = new ArrayList[4+1][4+1];
        egg = new ArrayList[4+1][4+1];
        
        for(int y=1; y<=4; y++) {
        	for(int x=1; x<=4; x++) {
        		board[y][x] = new ArrayList<>();
        		nxtBoard[y][x] = new ArrayList<>();
        		egg[y][x] = new ArrayList<>();
        	}
        }
        
        packMan = new Pair(r,c);

        for(int i = 0; i < m; i++){
            st = new StringTokenizer(br.readLine());
            int y = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken()) - 1;
            
            board[y][x].add(dir);
            count[y][x] += 1;
        }
        

        
        for(int turn = 1; turn <= t; turn++) {
        	
        	tryClone();
        	
        	moveAll();
        	
        	movePackman();
        	destroyDead();
        	
        	completeClone();
        }
        
        int ans = 0;
        for(int y=1; y<=4; y++) {
        	for(int x=1; x<=4; x++) {
        		ans += count[y][x];
        	}
        }
        System.out.println(ans);

    }
}