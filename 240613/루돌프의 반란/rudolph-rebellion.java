import java.util.*;
import java.io.*;
public class Main {
	static StringTokenizer st;
	static int n,m,p,c,d;
	static int dy[] = {-1,0,1,0,-1,1,1,-1};
	static int dx[] = {0,1,0,-1,1,1,-1,-1};
	
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public String toString() {
			return y+" "+x;
		}
		
		
	}
	
	static class Santa{
		int id, y,x,stun,score;
		public Santa(int id,int y,int x) {
			this.id = id;
			this.y = y;
			this.x = x;
		}
		
		public Santa(int y, int x) {
			this.y = y;
			this.x = x;
		}
		
		public boolean isHigher(Santa s) {
			if(y != s.y)
				return y > s.y;
			return x > s.x;
		}
		
		
		public String toString() {
			return "id: "+id+"| "+y+" "+x+" "+stun;
		}
	}
	
	
	static Santa santas[];
	static Pair rudolf;
	static int turn;
	static boolean isDead[];
	static boolean allDead() {
		for(int i=1;i<=p;i++) {
			if(!isDead[i])
				return false;
		}
		return true;
	}
	static int INF = Integer.MAX_VALUE;
	
	static int getDist(int y1,int x1, int y2, int x2) {
		return (y1-y2) * (y1-y2) + (x1-x2) * (x1-x2);
	}
	static int board[][];
	
	static Santa NO_SANTA = new Santa(-1,-1);
	
	static Santa findBestSanta() {
		int bestDist = INF;
		Santa bestSanta = NO_SANTA;
		for(int id =1; id<=p; id++) {
			Santa santa = santas[id];
			if(isDead[id])
				continue;
			
			int dist = getDist(rudolf.y, rudolf.x, santa.y, santa.x);
			if(dist < bestDist) {
				bestDist = dist;
				bestSanta = santa;
			}else if(dist == bestDist && santa.isHigher(bestSanta)) {
				bestSanta = santa;
			}
		}
		return bestSanta;
	}
	
	static boolean collide(int y1, int x1, int y2, int x2) {
		return y1 == y2 && x1 == x2;
	}
	
	static class Tuple{
		int y,x,dir;
		public Tuple(int y,int x, int dir) {
			this.y = y;
			this.x = x;
			this.dir = dir;
		}
		
		public String toString() {
			return y+" "+x+" "+dir;
		}
	}
	
	
	/*
	 * (y1,x1)에서 (y2,x2)로 갈때 다음 위치와 그 방향 
	 */
	static Tuple getNxtPos(int y1, int x1, int y2, int x2) {
		int bestDist = getDist(y1,x1,y2,x2);
		int ret = -1;
		for(int dir = 0; dir<8;dir++) {
			int ny = y1 + dy[dir];
			int nx = x1 + dx[dir];
			if(OOB(ny,nx))
				continue;
			int dist = getDist(ny,nx,y2,x2);
			if(dist < bestDist) {
				bestDist = dist;
				ret = dir;
			}
		}
		
		int ny = y1 + dy[ret];
		int nx = x1 + dx[ret];
		return new Tuple(ny,nx, ret);
		
	}
	
	static void push(Santa santa, int dir, int dist) {
//		System.out.println("id: "+santa.id);
		int y = santa.y;
		int x = santa.x;
		
		board[y][x] = 0; // 이전 위치에서 사라짐 
		int ny = y + dy[dir] * dist;
		int nx = x + dx[dir] * dist;
		
		if(OOB(ny,nx)) {
			isDead[santa.id] = true;
			santas[santa.id].y = -1;
			santas[santa.id].x = -1;
			santas[santa.id].score = santa.score;
			return;
		}
		
		if(board[ny][nx] != 0) { // 이미 다른 산타가 있는 경우
			int id = board[ny][nx];
			push(santas[id], dir, 1); // 한칸 씩 밀림  
		}
		
		
		santa.y = ny;
		santa.x = nx;
		board[ny][nx] = santa.id;
		
	}
	
	static void moveRudolf() {
		//  가장 가까운 산타 선택 
		Santa santa = findBestSanta();
		if(santa == NO_SANTA)
			return;

		
		// 돌진 
		Tuple nxtPos = getNxtPos(rudolf.y, rudolf.x, santa.y, santa.x);
		rudolf.y = nxtPos.y;
		rudolf.x = nxtPos.x;
		
		// 충돌 여부 확인
		if(collide(rudolf.y, rudolf.x, santa.y, santa.x)) {
			// 충돌했으면 충돌 처리 
			
			// C만큼 점수 획득 
			santa.score += c;
			santa.stun = 2;
			
			// 루돌프가 이동해온 방향으로 C만큼 밀려남 
			int dir  = nxtPos.dir;
			push(santa, dir, c);
		}
		
		
		
	}
	
	static Tuple getNxtSantaPos(int sy, int sx) {
		
		int bestDist = getDist(sy,sx, rudolf.y ,rudolf.x);
		Tuple ret = null;
		for(int dir = 0; dir < 4; dir++) {
			int ny = sy + dy[dir];
			int nx = sx + dx[dir];
			
			if(OOB(ny,nx)) { // 격자 밖이거나 다른 산타가 있는 칸으로는 움직일 수 없다 
				continue;
			}
			
//			System.out.println(board[ny][nx]);
			if(board[ny][nx] !=0)
				continue;
			
			int dist = getDist(ny,nx, rudolf.y, rudolf.x);
			if(dist < bestDist) {
				bestDist = dist;
				ret = new Tuple(ny,nx,dir);
			}
			
			
		}
		
		return ret;
	}
	
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	/*
	 * id에 해당하는 산타의 이동 
	 * 1. 이동할 위치와 방향 정하기 
	 * 2. 이동 
	 * 3. 충돌 여부 확인
	 * 4. 충돌했으면 충돌 처리 
	 * 
	 */
	static void moveSanta(int id) {
		Santa santa = santas[id];
		if(isDead[id] || santa.stun != 0) // 탈락했거나 기절한 산타는 움직일 수 없다  
			return;
		
		Tuple nxtPos = getNxtSantaPos(santa.y, santa.x);
//		System.out.println("nxtSanta: "+nxtPos);
		if(nxtPos == null)
			return;
		
		board[santa.y][santa.x] = 0;
		santas[id].y = nxtPos.y;
		santas[id].x = nxtPos.x;
		board[nxtPos.y][nxtPos.x] = santa.id;
		
		// 충돌 여부 확인
		if(collide(rudolf.y, rudolf.x, santas[id].y, santas[id].x)) {
			// 충돌했으면 충돌 처리 
			
			// d만큼 점수 획득 
			santas[id].score += d;
			santas[id].stun = 2;
			
			// 산타가 이동해온 방향의 반대 방향으로 밀려남  
			int dir  = (nxtPos.dir + 2) % 4;
			push(santas[id], dir, d);
		}
		santa = santas[id];
//		System.out.println("santa: "+santa+" score: "+santa.score);
//		printBoard(board);
	}
	
	
	static void moveAllSantas() {
		for(int id = 1; id<=p;id++) {
			moveSanta(id);
		}
	}
	
	static void decrease() {
		for(int id = 1; id<=p;id++) {
			Santa santa = santas[id];
			if(isDead[id]) {
				continue;
			}
			if(santa.stun > 0)
				santa.stun-=1;
			santa.score += 1;
		}
	}
	
	
	static void printScore() {
		StringBuilder sb = new StringBuilder();
		for(int id= 1; id<=p;id++)
			sb.append(String.format("%s score: %d", santas[id], santas[id].score)).append('\n');
		System.out.println(sb);
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		p = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		d = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(br.readLine());
		int ry = Integer.parseInt(st.nextToken());
		int rx = Integer.parseInt(st.nextToken());
		rudolf = new Pair(ry,rx);
		santas = new Santa[p+1];
		board = new int[n+1][n+1];
		isDead = new boolean[p+1];
		for(int i=1; i<=p;i++) {
			st = new StringTokenizer(br.readLine());
			int id = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			santas[id] = new Santa(id,y,x);
			board[y][x] = id;
		}
		
		
		for(turn = 1; turn <=m; turn++) {
//			System.out.println("-----");
//			System.out.println("turn: "+turn);
			if(allDead())
				break;
			
			// 루돌프의 이동 
			moveRudolf();
			// 산타들의 이동 
			moveAllSantas();
			
			// 기절 수치 감소 
			decrease();
//			printScore();
		}
		
		StringBuilder sb = new StringBuilder();
		for(int id = 1; id<=p;id++) {
			sb.append(santas[id].score).append(' ');
		}
		System.out.println(sb);
		
	}
	
}