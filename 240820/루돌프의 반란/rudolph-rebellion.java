import java.util.*;
import java.io.*;


public class Main {
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        p = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        d = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        rudolf = new Rudolf(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        
        santas = new Santa[p+1];
        scores = new int[p+1];
        board = new int[n+1][n+1];
        for(int i = 1; i<=p; i++) {
        	st = new StringTokenizer(br.readLine());
        	int id = Integer.parseInt(st.nextToken());
        	int y = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken());
        	santas[id] = new Santa(id, y,x);
        	board[y][x] = id;
        }
        
//        System.out.println("rudolf: "+rudolf);
//    	printBoard(board);
        
        for(int turn = 1; turn<=m; turn++) {
//        	System.out.println("------");
//        	System.out.println("turn: "+turn);
        	if(isEnd())
        		break;
        	
        	// 루돌프의 이동 
        	rudolf.move();
        	
        	
//        	System.out.println("rudolf: "+rudolf);
        	// 산타들의 이동
        	moveAll();
        	
        	plusScore();
//        	printBoard(board);
//        	System.out.println("santas: "+Arrays.toString(santas));
//        	System.out.println("score: "+Arrays.toString(scores));
        }
        StringBuilder sb = new StringBuilder();
        for(int id = 1; id<=p; id++) {
        	sb.append(scores[id]).append(' ');
        }
        System.out.println(sb);
//        rudolf.move();
//        System.out.println("rudolf: "+rudolf);
//        System.out.println("Santas: "+Arrays.toString(santas));
//        printBoard(board);
//        System.out.println("-----");
//        moveAll();
//        System.out.println("rudolf: "+rudolf);
//        System.out.println("Santas: "+Arrays.toString(santas));
//        printBoard(board);
        
    }
    
    
    static void printBoard(int board[][]) {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			System.out.printf("%3d", board[y][x]);
    		}
    		System.out.println();
    	}
    }
    
	static int n,m,p,c,d;
	static StringTokenizer st;
	static int board[][];
	
	static int dy[] = {-1,-1,0,1,1,1,0,-1};
	static int dx[] = {0,1,1,1,0,-1,-1,-1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static class Rudolf{
		int y,x;
		public Rudolf(int y,int x) {
			this.y = y;
			this.x = x;
		}
		public String toString() {
			return y+" "+x;
		}
		
		public boolean isSame(int y,int x) {
			return this.y == y && this.x == x;
		}
		/*
		 * 가장 가까운 산타에게 1칸 돌진.
		 * 게임에서 탈락하지 않은 산타여야함 
		 */
		
		public void move() {
			// 가장 가까운 산타(best)의 위치 찾기 
			Santa bestSanta = findBestSanta();
			if(bestSanta == DROPPED)
				return;
			
			// 8방향 중에서 best로 가는 가장 가까워지는 좌표찾기
			Pos nxtPos = findNxtPos(bestSanta.y, bestSanta.x);
			
			
			// 실제 이동 
			y = nxtPos.y;
			x = nxtPos.x;
			
			
			// 루돌프가 움직여서 충돌 발생 
			if(this.isSame(bestSanta.y, bestSanta.x)) {
				int id = bestSanta.id;
				scores[id] += c; // 해당 산타는 c만큼의 점수 획득 
				santas[id].stun = 2;
				
				// 해당 방향으로 c만큼 밀려남 
				push(id,nxtPos.dir, c);
			}
		}
		
		private Pos findNxtPos(int sy, int sx) {
			int ry = this.y;
			int rx = this.x;
			Pos ret = NO_POS;
			for(int dir = 0; dir < 8; dir++) {
				int ny = ry + dy[dir];
				int nx = rx + dx[dir];
				if(OOB(ny,nx))
					continue;
				int dist = getDistance(ny,nx,sy,sx);
				
				Pos pos = new Pos(ny, nx, dir, dist);
				if(pos.isHigher(ret))
					ret = pos;
				
			}
			return ret;
		}
		
		private Santa findBestSanta() {
			Santa best = DROPPED;
			for(int id = 1; id<=p; id++) {
				Santa santa = santas[id];
				if(santa == DROPPED) // 탈락하지 않은 산타여야함 
					continue;
				int dist = getDistance(this.y, this.x, santa.y, santa.x);
				santa = new Santa(santa.id, santa.y, santa.x, dist);
				if(santa.isHigher(best))
					best = santa;
			}
			
			return best;
		}
	}
	
	static void plusScore() {
		for(int id = 1; id<=p ;id++) {
			if(santas[id] == DROPPED)
				continue;
			scores[id]++;
			if(santas[id].stun > 0)
				santas[id].stun--;
		}
	}
	
	/*
	 * id에 해당하는 산타가
	 * dir 방향으로 
	 * dist만큼 밀려남 
	 */
	
	static void push(int id, int dir, int dist) {
		Santa santa = santas[id];
		if(santa == DROPPED)
			return;
		int y = santa.y;
		int x = santa.x;
		int ny = y + dy[dir] * dist;
		int nx = x + dx[dir] * dist;
		if(OOB(ny,nx)) { // 게임판 밖으로 밀려나면 탈락 
			board[y][x] = 0;
			santas[id] = DROPPED;
			return;
		}
		
		// 다음 위치에 다른 산타가 있다면 상호작용 
		if(board[ny][nx] != 0) {
			int nxt = board[ny][nx]; 
			push(nxt, dir, 1); // 다른 산타는 1칸씩 해당방향으로 밀려남 
		}
		
		// 실제 이동 
		santa.y = ny;
		santa.x = nx;
		board[y][x] = 0;
		board[ny][nx] = id;
	}
	
	static int getDistance(int y1,int x1, int y2, int x2) {
		return (y1-y2) * (y1-y2) + (x1-x2) * (x1-x2);
	}
	
	static class Pos{
		int y,x, dir, dist;
		public Pos(int y,int x,int dir, int dist) {
			this.y = y;
			this.x = x;
			this.dir = dir;
			this.dist = dist;
		}
		public boolean isHigher(Pos o) {
			return dist < o.dist;
		}
		
		public boolean isSame(int y,int x) {
			return this.y == y && this.x == x;
		}
	}
	
	static int INT_MAX = Integer.MAX_VALUE;
	static Pos NO_POS = new Pos(-1,-1, -1 , INT_MAX);
	
	static void moveAll() {
		for(int id = 1; id<= p; id++) {
			Santa santa = santas[id];
			if(santa == DROPPED || santa.stun != 0) // 탈락했거나 기절한 산타는 움직일 수 없다 
				continue;
			
//			System.out.println("id: "+id);
			santa.move();
		}
	}
	

	
	static class Santa{
		int id,y,x,dir, stun, dist;
		public Santa(int id, int y,int x) {
			this.id = id;
			this.y = y;
			this.x = x;
		}
		
		public Santa(int id, int y, int x, int dist) {
			this.id = id;
			this.y = y;
			this.x = x;
			this.dist = dist;
		}
		
		
		public String toString() {
			return String.format("[%d: (%d, %d, %d)]", id,y,x, stun);
		}
		
		public boolean isHigher(Santa o) {
			if(dist != o.dist) 
				return dist < o.dist;
			if(y != o.y)
				return y > o.y;
			return x > o.x;
		}
		
		public void move() {
			Pos nxtPos = findBestPos();
			
			// 이동 
			board[y][x] = 0;
			y = nxtPos.y;
			x = nxtPos.x;
			
			// 루돌프와 충돌이 났다 
			if(this.isSame(rudolf.y, rudolf.x)) {
				scores[id] += d;
				
				santas[id].stun = 2;
				// 자신이 이동해온 반대 방향으로 D칸 밀려난다 
				push(id, (nxtPos.dir + 4) % 8, d);
				return;
			}
			board[nxtPos.y][nxtPos.x] = id;
			
		}
		
		private boolean isSame(int y,int x) {
			return this.y == y && this.x == x;
		}
		
		private Pos findBestPos() {
			int ry = rudolf.y;
			int rx = rudolf.x;
			
			Pos ret = new Pos(this.y, this.x ,-1, getDistance(this.y,this.x, ry,rx));
			for(int dir = 0 ; dir < 8; dir+=2) {
				int ny = this.y + dy[dir];
				int nx = this.x + dx[dir];
				
				// 게임판 밖이나 다른 산타가 있는쪽으로는 움직이지 않는다 
				if(OOB(ny,nx) || board[ny][nx] != 0) 
					continue;
				int dist = getDistance(ny,nx, ry,rx);
				Pos pos = new Pos(ny,nx,dir, dist);
				/*
				 * ret의 dist가 원래 위치에서 루돌프까지의 거리이므로 
				 * pos가 ret보다 우선순위가 높다는건 루돌프로 가까워지는 방향이 있다는 것이다 
				 */
				if(pos.isHigher(ret)) 
					ret = pos;
			}
			
			return ret;
		}
	}
	
	static Santa DROPPED = new Santa(-1,-1,-1, INT_MAX); // 탈락한 산타 
	
	static int scores[];
	static Rudolf rudolf;
	static Santa santas[];
	
	static boolean isEnd() {
		for(int id = 1; id<= p; id++) {
			if(santas[id] != DROPPED)
				return false;
		}
		return true;
	}
}