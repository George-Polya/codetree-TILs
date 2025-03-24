import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        sy = Integer.parseInt(st.nextToken());
        sx = Integer.parseInt(st.nextToken());
        ey = Integer.parseInt(st.nextToken());
        ex = Integer.parseInt(st.nextToken());
        
        medusa = new Medusa(sy,sx);
        
        st = new StringTokenizer(br.readLine());
        warriors = new Warrior[M];
        wBoard = new int[N][N];
        for(int i = 0; i < M; i++) {
        	int y = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken());
        	warriors[i] = new Warrior(i, y,x);
        	wBoard[y][x]++;
        }
        
        board = new int[N][N];
        dist = new int[N][N];
        for(int y=0; y<N; y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x = 0; x<N; x++) {
        		board[y][x] = Integer.parseInt(st.nextToken());
        		dist[y][x] = INF;
        	}
        }
        
        init();
        
        if(dist[sy][sx] == INF) {
        	System.out.println(-1);
        	return;
        }
        
//        medusa.move();
        
        
        StringBuilder sb = new StringBuilder();
        while(true) {
        	medusa.move();
        	if(medusa.end()) {
        		sb.append(0).append('\n');
        		break;
        	}
        	Sight bestSight = medusa.findBestSight();
        	
        	int[] moved = moveAll(bestSight);
        	sb.append(moved[0]+" "+bestSight.cnt+" "+moved[1]).append('\n');
        }
        System.out.println(sb);
    }
    
    static int[] moveAll(Sight bestSight){
    	int sighted[][] = bestSight.sighted;
    	List<Warrior> notFreezed = bestSight.notFreezed;
    	
    	int distSum = 0;
    	int attackerCnt = 0;
    	
    	for(Warrior w : notFreezed) {
    		Move move = w.move(sighted);
    		if(move.attack)
    			attackerCnt++;
    		distSum += move.dist;
    	}
    	
    	
    	
    	return new int[] {distSum, attackerCnt};
    }
    
    
    static int N,M;
    static StringTokenizer st;
    static Medusa medusa;
    static int dist[][];
    static Warrior warriors[];
    static int sy,sx,ey,ex;
    
    static class Pair{
    	int y,x;
    	public Pair(int y,int x) {
    		this.y = y;
    		this.x = x;
    	}
    	
    	public String toString() {
    		return String.format("(%d, %d)", y,x);
    	}
    	
    }
    static Pair NO_PAIR = new Pair(-1,-1);
    
    
    static class Medusa{
    	int y,x;
    	public Medusa(int y,int x) {
    		this.y = y;
    		this.x = x;
    	}
    	
    	public boolean isSame(int y,int x) {
    		return this.y == y && this.x == x;
    	}
    	
    	public boolean end() {
    		return isSame(ey,ex);
    	}
    	
    	public String toString() {
    		return String.format("(%d, %d)", y,x);
    	}
    	
    	/*
    	 * 메두사의 이동 
    	 */
    	public void move() {
    		Pair nxt = findNxtPos();
    		y = nxt.y;
    		x = nxt.x;
    		
    		for(int i = 0; i < M;i++) {
    			if(warriors[i] == NO_WARRIOR)
    				continue;
    			int wy = warriors[i].y;
    			int wx = warriors[i].x;
    			if(isSame(wy,wx)) { // 전사가 있으면 공격받고 사라짐. 
    				wBoard[wy][wx]--;
    				warriors[i] = NO_WARRIOR;
    			}
    		}
    	}
    	
    	/*
    	 * 다음 위치 정하기 
    	 */
    	private Pair findNxtPos() {
    		int curDist = dist[y][x];
    		Pair ret = NO_PAIR;
    		for(int dir = 0; dir < 4; dir++) { // 상하좌우 우선순위 
    			int ny = y + dy[dir];
    			int nx = x + dx[dir];
    			
    			if(OOB(ny,nx) || board[ny][nx] == 1)
    				continue;
    			
    			int nDist = dist[ny][nx];
    			if(curDist > nDist) {
    				curDist = nDist;
    				ret = new Pair(ny,nx);
    			}
    		}
    		
    		return ret;
    	}
    	
    	/*
    	 * 메두사의 시선 
    	 */
    	public Sight findBestSight() {
    		Sight ret = NO_SIGHT;
    		
    		/*
    		 * sDir : 시선 방향 
    		 */
    		for(int sDir = 0; sDir < 4; sDir++) { 
    			Sight sight = new Sight(y,x,sDir);
    			if(sight.isHigher(ret))
    				ret = sight;
    		}
    		
    		return ret;
    	}
    }
    
    static int dy2[] = {-1,-1,0,1,1,1,0,-1};
    static int dx2[] = {0,1,1,1,0,-1,-1,-1};
    static class Tuple{
    	int y,x,dir;
    	public Tuple(int y,int x,int dir) {
    		this.y = y;
    		this.x = x;
    		this.dir = dir;
    	}
    }
    static Sight NO_SIGHT = new Sight();
    static class Sight{
    	int cnt, sDir;
    	int sighted[][];
    	List<Warrior> notFreezed;
    	
    	public Sight() {
    		this.cnt = -1;
    		this.sDir = 5;
    	}
    	/*
    	 * (N * N) / 4
    	 */
    	public Sight(int my, int mx, int sDir) {
    		this.sDir = sDir;
    		sighted = new int[N][N];
    		Queue<Tuple> q = new ArrayDeque<>();
    		int dir = sDir;
			if(sDir == 1)
    			dir = 4;
    		else if(sDir == 2)
    			dir = 6;
    		else if(sDir == 3)
    			dir = 2;
			
			List<Tuple> freezeds = new ArrayList<>();
			for(int nDir : getDirs(dir)) {
				int ny = my + dy2[nDir];
				int nx = mx + dx2[nDir];
				if(OOB(ny,nx))
					continue;
				sighted[ny][nx] = 1;
				q.add(new Tuple(ny,nx,nDir));
				if(wBoard[ny][nx] != 0) {
					freezeds.add(new Tuple(ny,nx, nDir));
				}
			}
			
			while(!q.isEmpty()) {
				Tuple cur = q.poll();
				
				for(int nDir : getDirs(cur.dir, sDir)) {
					int ny = cur.y + dy2[nDir];
					int nx = cur.x + dx2[nDir];
					
					if(OOB(ny,nx) || sighted[ny][nx] == 1)
						continue;
					if(wBoard[ny][nx] != 0)
						freezeds.add(new Tuple(ny,nx, nDir));
					
					q.add(new Tuple(ny,nx,nDir));
					sighted[ny][nx] = 1;
				}
			}
			
			for(Tuple freezed : freezeds) {
				if(sighted[freezed.y][freezed.x] == 2)
					continue;
				shadow(freezed);
			}
			
			
			notFreezed = new ArrayList<>();
			for(int i = 0; i < M; i++) {
				if(warriors[i] == NO_WARRIOR)
					continue;
				int wy = warriors[i].y;
				int wx = warriors[i].x;
				if(sighted[wy][wx] == 1) {
					cnt++;
					continue;
				}
				notFreezed.add(warriors[i]);
			}
			
			
    	}
    	
    	private void shadow(Tuple freezed) {
    		Queue<Tuple> q = new ArrayDeque<>();
    		for(int nDir : getDirs(freezed.dir, this.sDir)) {
    			int ny = freezed.y + dy2[nDir];
    			int nx = freezed.x + dx2[nDir];
    			if(OOB(ny,nx) || sighted[ny][nx] == 2)
    				continue;
    			q.add(new Tuple(ny,nx,nDir));
    			sighted[ny][nx] = 2;
    		}
    		
    		while(!q.isEmpty()) {
    			Tuple cur = q.poll();
    			
    			for(int nDir : getDirs(cur.dir, this.sDir)) {
    				int ny = cur.y + dy2[nDir];
    				int nx = cur.x + dx2[nDir];
    				
    				if(OOB(ny,nx) || sighted[ny][nx] == 2)
    					continue;
    				q.add(new Tuple(ny,nx,nDir));
    				sighted[ny][nx] = 2;
    			}
    		}
    		
    	}
    	
    	public boolean isHigher(Sight s) {
    		if(cnt != s.cnt)
    			return cnt > s.cnt;
    		return sDir < s.sDir;
    	}
    	
    	
    	private int[] getDirs(int dir) {
    		if(dir %2 == 0) {
    			return new int[] {(dir+7) % 8, dir, (dir + 1) % 8};
    		}
    		return new int[] {dir};
    	}
    	
    	private int[] getDirs(int cDir, int sDir) {
    		if(sDir == 0) {
    			if(cDir == 7) {
    				return new int[] {7, 0};
    			}else if(cDir == 0) {
    				return new int[] {0};
    			}else {
    				return new int[] {0, 1};
    			}
    			
    		}else if(sDir == 1) {
    			if(cDir == 3) {
    				return new int[] {3,4};
    			}else if(cDir == 4) {
    				return new int[] {4};
    			}else {
    				return new int[] {4,5};
    			}
    			
    		}else if(sDir == 2) {
    			if(cDir == 5) {
    				return new int[] {5,6};
    			}else if(cDir == 6) {
    				return new int[] {6};
    			}else {
    				return new int[] {6,7};
    			}
    		}else {
    			if(cDir == 1) {
    				return new int[] {1,2};
    			}else if(cDir == 2) {
    				return new int[] {2};
    			}else {
    				return new int[] {2,3};
    			}
    		}
    	}
    	

		
		public String toString() {
			return String.format("cnt: %d, sDir: %d", cnt, sDir);
		}
    }
    
    static Warrior NO_WARRIOR = new Warrior(-1,-1,-1);
    static class Warrior{
    	int id, y,x;
    	public Warrior(int id, int y,int x) {
    		this.id = id;
    		this.y = y;
    		this.x = x;
    	}
    	
    	public String toString() {
    		return String.format("id: %d | (%d, %d)", id, y,x);
    	}
    	
    	public Move move(int sighted[][]) {
    		int distance = getDistance(y,x, medusa.y, medusa.x);
    		int direction = -1;

    		
    		for(int dir = 0; dir < 4; dir++) {
    			int ny = y + dy[dir];
    			int nx = x + dx[dir];
    			
    			if(OOB(ny,nx) || sighted[ny][nx] == 1)
    				continue;
    			int D = getDistance(ny,nx, medusa.y, medusa.x);
    			if(distance > D) {
    				distance =  D;
    				direction = dir;
    			}
    		}
    		
    		if(direction == -1) {
    			return new Move(0, false);
    		}
    		
    		wBoard[y][x]--;
    		y = y + dy[direction];
    		x = x + dx[direction];
    		
    		
    		if(medusa.isSame(y,x)) {
    			warriors[id] = NO_WARRIOR;
    			return new Move(1,true);
    		}
    		wBoard[y][x]++;
    		
    		direction = -1;
    		for(int dir = 2; dir < 6; dir++) {
    			int nDir = dir % 4;
    			int ny = y + dy[nDir];
    			int nx = x + dx[nDir];
    			
    			if(OOB(ny,nx) || sighted[ny][nx] == 1)
    				continue;
    			int D = getDistance(ny,nx, medusa.y, medusa.x);
    			if(distance > D) {
    				distance = D;
    				direction = nDir;
    			}
    		}
    		
    		if(direction == -1)
    			return new Move(1, false);
    		
    		wBoard[y][x]--;
    		y = y + dy[direction];
    		x = x + dx[direction];
    		if(medusa.isSame(y,x)) {
    			warriors[id] = NO_WARRIOR;
    			return new Move(2, true);
    		}
    		wBoard[y][x]++;
    		return new Move(2,false);
    	}
    	
    }
    
    static class Move{
    	int dist;
    	boolean attack;
    	public Move(int dist, boolean attack) {
    		this.dist = dist;
    		this.attack = attack;
    	}
    }
    
    static int getDistance(int y1,int x1, int y2, int x2) {
    	return Math.abs(y1-y2) + Math.abs(x1-x2);
    }
    
    static int board[][];
    static int wBoard[][];
    
    static int dy[] = {-1,1,0,0};
    static int dx[] = {0,0,-1,1};
    
    static boolean OOB(int y,int x) {
    	return y<0 || y>=N || x < 0 || x>=N;
    }
    
    static void printBoard(int board[][]) {
    	for(int y=0; y<N; y++) {
    		for(int x= 0; x<N; x++) {
    			System.out.printf("%3d", board[y][x] == INF ? -1 : board[y][x]);
    		}
    		System.out.println();
    	}
    }
    
    /*
     * 메두사의 이동을 위한 초기 
     * 도착점을 기준으로 BFS
     * 그러면 각 칸에 공원으로의 최단거리가 새겨짐 
     */
	static void init() {
    	Queue<Pair> q = new ArrayDeque<>();
    	q.add(new Pair(ey,ex));
    	dist[ey][ex] = 0;
    	
    	while(!q.isEmpty()) {
    		Pair cur = q.poll();
    		
    		if(medusa.isSame(cur.y, cur.x))
    			break;
    		
    		for(int dir = 0; dir < 4; dir++) {
    			int ny = cur.y + dy[dir];
    			int nx = cur.x + dx[dir];
    			
    			if(OOB(ny,nx) || dist[ny][nx] != INF || board[ny][nx] == 1)
    				continue;
    			q.add(new Pair(ny,nx));
    			dist[ny][nx] = dist[cur.y][cur.x] + 1;
    		}
    	}
	}
	static int INF = Integer.MAX_VALUE / 2;
}