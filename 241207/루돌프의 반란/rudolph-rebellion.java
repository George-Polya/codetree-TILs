import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException{
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	n = Integer.parseInt(st.nextToken());
    	m = Integer.parseInt(st.nextToken());
    	p = Integer.parseInt(st.nextToken());
    	c = Integer.parseInt(st.nextToken());
    	d = Integer.parseInt(st.nextToken());
    	total = p;
    	
    	st = new StringTokenizer(br.readLine());
    	rudolf = new Rudolf(Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()));
    	
    	board = new int[n+1][n+1];
    	santas = new Santa[p+1];
    	for(int i =1; i<=p; i++) {
    		st = new StringTokenizer(br.readLine());
    		int id = Integer.parseInt(st.nextToken());
    		int y = Integer.parseInt(st.nextToken());
    		int x = Integer.parseInt(st.nextToken());
    		santas[id] = new Santa(y,x);
    		board[y][x] = id;
    	}
    	
    	
    	scores = new int[p + 1];
    	
//    	System.out.println("rudolf: "+rudolf);
//		System.out.println("board");
//		printBoard(board);
//		System.out.println(Arrays.toString(santas));
    	for(int turn=1; turn<=m; turn++) {
//    		System.out.println("-----");
//    		System.out.println("turn: "+turn);
    		/*
    		 * 1. 루돌프의 이동 -> 충돌이 일어날 수 있음 -> 산타 탈락했는지 확인  
    		 *  
    		 */
    		move(rudolf);
//    		System.out.println("after rudolf move");
//    		System.out.println("rudolf: "+rudolf);
//    		System.out.println("board");
//    		printBoard(board);
//    		System.out.println(Arrays.toString(santas));
    		
    		if(total == 0)
    			break;
    		
    		/*
    		 * 2. 산타들의 이동 -> 충돌 일어나서 탈락할 수 있음 
    		 */
    		moveAll();
    		
    		if(total == 0)
    			break;
    		
    		healAll();
//    		System.out.println("after santa move");
//    		System.out.println("board");
//    		printBoard(board);
//    		System.out.println(Arrays.toString(santas));
    		
    	}
    	
    	StringBuilder sb = new StringBuilder();
    	for(int i = 1; i<=p; i++)
    		sb.append(scores[i]).append(' ');
    	System.out.println(sb);
    }
    
    static void healAll() {
    	for(int i = 1; i<=p; i++) {
    		if(santas[i].pair == NO_PAIR)
    			continue;
    		if(santas[i].stun > 0)
    			santas[i].stun--;
    		scores[i]++;
    	}
    }
    
    /*
     * 산타들의 이동 
     */
    static void moveAll() {
    	for(int i = 1; i<=p; i++) {
    		// 탈락한 산타나 기절한 산타는 움직일 수 없음 
    		if(santas[i].pair.isSame(-1, -1) || santas[i].stun > 0)
    			continue;
    		
    		move(i);
    	}
    }
    
    /*
     * 산타의 이동
     * 1. 다음 위치 정하기 
     * 2. 이동 
     * 3. 충돌 확인
     * 4. 상호작용 
     */
    static void move(int idx) {
    	Santa santa = santas[idx];
    	
    	Pos nxt = getNxtSanta(santa.pair.y, santa.pair.x);
    	
    	if(nxt.dir == -1) // 이동하지 않는다 
    		return;
    	
    	board[santa.pair.y][santa.pair.x] = 0;
    	santa.pair.y = nxt.pair.y;
    	santa.pair.x = nxt.pair.x;
    	board[santa.pair.y][santa.pair.x] = idx;
    	
    	if(santa.pair.isSame(rudolf.pair.y, rudolf.pair.x)) {// 루돌프와 충돌 
    		scores[idx] += d; // 점수획득 
    		shift(idx, d, (nxt.dir + 4) % 8);
    		santa.stun = 2; // 기절
    	}
    	
    }
    
    /*
     * 루돌프에게 거리가 가까워지는 방향으로 한 칸 이동 
     * 
     * 다른 산타가 있는 칸이나 게임판 밖으로는 이동 불가
     * 움직일 수 있는 칸이 없다는 움직이지 않음 
     */
    static Pos getNxtSanta(int y,int x) {
    	int D = getDist(y,x, rudolf.pair.y, rudolf.pair.x);
    	Pos ret = new Pos(y,x, -1);
    	for(int dir = 0; dir < 8; dir+=2) {
    		int ny = y + dy[dir];
    		int nx = x + dx[dir];
    		
    		if(OOB(ny,nx) || board[ny][nx] != 0) // 다른 산타가 있거나 밖으로는 이동 불가 
    			continue;
    		int dist = getDist(ny,nx, rudolf.pair.y, rudolf.pair.x);
    		if(D > dist) {
    			D = dist;
    			ret = new Pos(ny,nx,dir);
    		}
    	}
    	
    	/*
    	 * 만약 ret의 업데이트가 이루어지지 않는다면 
    	 * 움직일 수 있는 칸이 없거나 루돌프에 가까워지는 방법이 없다는 뜻이므로 기존 위치 리턴
    	 */
    	return ret;
    }
   
    
    /*
     * 루돌프의 이동
     * 1. 다음 위치와 방향 찾기 
     *  1.1가장 가까운 산타를 향해 한칸 이동
     *  1.2 여러개라면 y큰거, x큰거
     *  1.3 상하좌우 대각 
     * 2. 해당 위치로 이동  
     * 3. 충돌 체크
     *  3.1 밀려나는 산타의 id가져오기 
     *  3.2 밀려나는 산타의 상호작용 
     * 
     */
    static void move(Rudolf rudolf) {
    	Pos nxt = getNxtRudolf(rudolf.pair.y, rudolf.pair.x);
//    	System.out.println("nxt rudolf: "+nxt);
    	if(nxt == null) {
    		return;
    	}
    	
    	rudolf.pair.y = nxt.pair.y;
    	rudolf.pair.x = nxt.pair.x;
    	
    	// 충돌 체크 
    	int santaId = board[rudolf.pair.y][rudolf.pair.x]; 
    	if( santaId == 0) // 충돌 안했음 
    		return;
    	
    	// 산타의 점수 획득 
    	scores[santaId] += c;
    	
    	/*
    	 *  santaId 산타(일단 탈락한 산타는 아니고, 기절한 산타일 수는 있음) 
    	 *  루돌프가 이동해온 방향 nxt.dir로
    	 *  c만큼 밀려남 
    	 */
    	shift(santaId, c, nxt.dir);
    	
    	// 산타 기절 
    	santas[santaId].stun = 2;
    }
    
    /*
     *  id에 해당하는 산타가 dir방향으로 dist만큼 밀려남 
     *  1. 밀려난 위치가 OOB이면 산타는 탈락
     *  2. 밀려난 위치에 다른 산타가 존재하면 상호작용 
     */
    static void shift(int id, int dist, int dir) {
    	Santa santa = santas[id];
//    	System.out.printf("shift santa[%d]: %s\n", id, santa);
    	
    	int y = santa.pair.y;
    	int x = santa.pair.x;
    	
    	board[y][x] = 0; // 기존위치에서 제거 
    	
    	int ny = y + dy[dir] * dist;
    	int nx = x + dx[dir] * dist;
    	
    	// 밀려난 위치가 OOB
    	if(OOB(ny,nx)) {
    		santas[id].pair = NO_PAIR; // 탈락
    		total--;
    		return;
    	}
    	
    	int otherId = board[ny][nx];
    	
    	if(otherId == 0) {  // 밀려난 위치에 다른 산타가 없음
    		board[ny][nx] = id;
    		santas[id].pair.y = ny;
    		santas[id].pair.x = nx;
    		return;
    	}
    	
    	/*
    	 * 밀려난 위치에 다른 산타가 있으면
    	 * dir 방향으로 1칸씩 밀려남 
    	 */
    	shift(otherId, 1, dir);
    	board[ny][nx] = id;
    	santas[id].pair.y = ny;
		santas[id].pair.x = nx;
    	
    }
    
    static int scores[];
    
    
    /*
     * 다음 위치와 방향 찾기
     * 1. 돌진할 산타 찾기
     * 2. 그 산타를 향해 가장 가까워지는 방향으로 한칸 이동한 위치 
     */
    static Pos getNxtRudolf(int y,int x) {
    	Pair best = findBestSanta(y,x);
//    	System.out.println("best Santa: "+best);
    	Pos ret = null;
    	/*
    	 * 산타가 남아있는 이상 NO_PAIR가 될리는 없음.
    	 * 산타가 모두 탈락한 경우는 앞에서 break됨 
    	 */
    	if(best.isSame(-1,-1))  
    		return ret;
    	
    	int D = getDist(y,x, best.y,best.x);
    	for(int dir = 0; dir < 8; dir++) {
    		int ny = y + dy[dir];
    		int nx = x + dx[dir];
    		if(OOB(ny,nx))
    			continue;
    		int dist = getDist(ny,nx, best.y, best.x);
    		if(dist < D) {
    			D = dist;
    			ret = new Pos(ny,nx,dir);
    		}
    	}
    	return ret;
    }
    
    static Pair findBestSanta(int y,int x) {
    	Tuple ret = WORST;
    	for(int i = 1; i<=p; i++) {
    		Pair pair = santas[i].pair;
    		if(pair.isSame(-1, -1))
    			continue;
    		Tuple tuple = makeTuple(y,x, pair);
    		if(tuple.isHigher(ret))
    			ret = tuple;
    	}
    	
    	return ret.pair;
    }
    
    static Tuple makeTuple(int y,int x, Pair pair) {
    	int dist = getDist(y,x,pair.y,pair.x);
    	return new Tuple(pair.y, pair.x, dist);
    }
    
    static int getDist(int y1, int x1, int y2, int x2) {
    	return (y1-y2) * (y1-y2) + (x1-x2)*(x1-x2);
    }
    static int INT_MAX = Integer.MAX_VALUE / 2;
    
    static Tuple WORST = new Tuple(-1,-1, INT_MAX);
    
    static class Tuple{
    	Pair pair;
    	int dist;
    	public Tuple(int y,int x, int dist) {
    		this.pair = new Pair(y,x);
    		this.dist = dist;
    	}
    	
    	public boolean isHigher(Tuple o) {
    		if(dist != o.dist)
    			return dist < o.dist;
    		if(pair.y != o.pair.y)
    			return pair.y > o.pair.y;
    		return pair.x > o.pair.x;
    	}
    }
    
    static class Pos{
    	Pair pair;
    	int dir;
    	public Pos(int y,int x,int dir) {
    		this.pair = new Pair(y,x);
    		this.dir = dir;
    	}
    	public String toString() {
    		return pair+" "+dir;
    	}
    }
    
    static Rudolf rudolf;
    static Santa santas[];
    static int dy[] = {-1,-1,0,1,1,1,0,-1};
    static int dx[] = {0,1,1,1,0,-1,-1,-1};
    static boolean OOB(int y,int x) {
    	return y<=0 || y>n || x<=0 || x>n;
    }
    
    static StringTokenizer st;
    static int n,m,p,c,d;
    static int total;
    static class Pair{
    	int y,x;
    	public Pair(int y,int x) {
    		this.y = y;
    		this.x = x;
    	}
    	
    	public String toString() {
    		return y+" "+x;
    	}
    	
    	public boolean isSame(int y,int x) {
    		return this.y == y && this.x == x;
    	}
    }
    static Pair NO_PAIR = new Pair(-1,-1);
    static class Rudolf{
    	Pair pair;
    	public Rudolf(int y,int x) {
    		this.pair = new Pair(y,x);
    	}
    	
    	public String toString() {
    		return pair.toString();
    	}
    }
    
    static class Santa{
    	Pair pair;
    	int stun;
    	public Santa( int y,int x) {
    		this.pair = new Pair(y,x);
    	}
    	public String toString() {
    		return pair+" "+stun;
    	}
    }
    
    static int board[][];
    static void printBoard(int board[][]) {
    	for(int y=1; y<=n; y++) {
    		for(int x=1; x<=n; x++) {
    			System.out.printf("%3d", board[y][x]);
    		}
    		System.out.println();
    	}
    }
    
}