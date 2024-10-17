import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException{
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	n = Integer.parseInt(st.nextToken());
    	m = Integer.parseInt(st.nextToken());
    	st = new StringTokenizer(br.readLine());
    	
    	monster = new Pair(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
    	exit = new Pair(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
    	
    	warriors = new Warrior[m+1];
    	st = new StringTokenizer(br.readLine());
    	wBoard = new int[n][n];
    	total = m;
    	for(int id = 1; id<=m; id++) {
    		int y = Integer.parseInt(st.nextToken());
    		int x = Integer.parseInt(st.nextToken());
    		warriors[id] = new Warrior(id,y,x);
    		wBoard[y][x] += (1<<id);
    	}
    	
    	board = new int[n][n];
    	for(int y=0; y<n; y++) {
    		st = new StringTokenizer(br.readLine());
    		for(int x=0; x<n; x++) {
    			board[y][x] = Integer.parseInt(st.nextToken());
    		}
    	}
    	
    	dist = new int[n][n];
    	StringBuilder sb = new StringBuilder();
    	while(true) {
    		monster.move();
    		if(monster.isSame(exit.y, exit.x)) {
    			sb.append(0);
    			break;
    		}
    	
    		Light best = findBestLight();
//    		System.out.println("best: "+best);
//    		sb.append(best.cnt).append(' ');
    		
    		int[] moved = moveAll(best.notStones, best.visited);
//    		sb.append(moved[0]).append(' ').append(moved[1]).append('\n');
    		sb.append(moved[0]+" "+best.cnt+" "+moved[1]).append('\n');
    	}
    	
    	System.out.println(sb);
//    	monster.move();
    	
    	
    }
    
    static int dist[][];
    static Pair NO_POS = new Pair(-1,-1);
    static class Pair{
    	int y,x;
    	public Pair(int y,int x) {
    		this.y = y;
    		this.x = x;
    	}
    	
    	public String toString() {
    		return y+" "+x;
    	}
    	
    	public void move() {
    		for(int y=0; y<n; y++) {
    			Arrays.fill(dist[y], INT_MAX);
    		}
    		
    		bfs();
    		
    		Pair nxt = getNxtPos();
    		if(nxt == NO_POS) {
    			System.out.println(-1);
    			System.exit(0);
    		}
//    		System.out.printf("%d %d\n", y,x);
//    		System.out.println("nxt: "+nxt);
    		y = nxt.y;
    		x = nxt.x;
//    		System.out.printf("%d %d\n", y,x);
    		for(int i = 1; i<=m;i++) {
    			if(warriors[i] == DEAD)
    				continue;
    			if( (wBoard[y][x] & (1<<i) )!=0 ) {
    				wBoard[y][x] -= (1<<i);
    				warriors[i] = DEAD;
    				total--;
    			}
    		}
    	}
    	
    	private Pair getNxtPos() {
    		int y = this.y;
    		int x = this.x;
    		int distance = INT_MAX;
    		Pair ret = NO_POS;
    		for(int dir = 0; dir < 4; dir++) {
    			int ny = y + dy[dir];
    			int nx = x + dx[dir];
    			if(OOB(ny,nx) || board[ny][nx] == 1)
    				continue;
    			if(distance > dist[ny][nx]) {
    				distance = dist[ny][nx];
    				ret = new Pair(ny,nx);
    			}
    		}
    		return ret;
    	}
    	
    	public boolean isSame(int y,int x) {
    		return this.y == y && this.x == x;
    	}
    	
    	private void bfs() {
    		Queue<Pair> q = new ArrayDeque<>();
    		q.add(exit);
    		dist[exit.y][exit.x] = 0;
    		
    		while(!q.isEmpty()) {
    			Pair cur = q.poll();
    			if(cur.isSame(monster.y, monster.x))
    				break;
    			for(int dir = 0; dir < 4; dir++) {
    				int ny = cur.y + dy[dir];
    				int nx = cur.x + dx[dir];
    				
    				if(OOB(ny,nx) || dist[ny][nx] != INT_MAX || board[ny][nx] == 1)
    					continue;
    				dist[ny][nx] = dist[cur.y][cur.x] + 1;
    				q.add(new Pair(ny,nx));
    						
    			}
    		}
    	}
    }
    
    static Light findBestLight() {
    	Light ret = WORST;
    	
    	for(int dir = 0; dir < 8; dir+=2) {
    		Light light = makeLight(dir);
//    		System.out.println("light: " +light);
    		if(light.isHigher(ret))
    			ret = light;
    	}
    	return ret;
    }
    
    static Light makeLight(int dir) {
    	int visited[][] = new int[n][n];
    	
    	
    	int sy = monster.y;
    	int sx = monster.x;
    	Queue<Tuple> q = new ArrayDeque<>();
    	q.add(new Tuple(sy,sx,dir));
    	visited[sy][sx] = 1;
    	ArrayList<Pair> wList = new ArrayList<>();
    	while(!q.isEmpty()) {
    		Tuple cur = q.poll();
    		
    		for(int nDir : getDirs(cur.dir)) {
    			int ny = cur.y + dy2[nDir];
    			int nx = cur.x + dx2[nDir];
    			
    			if(OOB(ny,nx) || visited[ny][nx] != 0)
    				continue;
    			
    			if(wBoard[ny][nx] != 0)
    				wList.add(new Pair(ny,nx));
    			q.add(new Tuple(ny,nx,nDir));
    			visited[ny][nx] = 1;
    		}
    	}
    	
    	visited[sy][sx] = 0;
    	
//    	printBoard(visited);
    	for(Pair w : wList) {
    		int y = w.y;
    		int x = w.x;
    		if(visited[y][x] == 2)
    			continue;
    		shadow(y,x, dir, visited);
    	}
    	
    	
    	int cnt = 0;
    	
    	ArrayList<Warrior> notStones = new ArrayList<>();
    	
    	
    	for(int i = 1; i<=m; i++) {
    		if(warriors[i] == DEAD)
    			continue;
    		int wy = warriors[i].pair.y;
    		int wx = warriors[i].pair.x;
    		
//    		System.out.printf("%d %d\n", wy,wx);
    		if(visited[wy][wx] == 1) {
    			cnt++;
    			continue;
    		}
    		
    		notStones.add(warriors[i]);
    	}
    	
//    	System.out.println("wList: "+wList);
//    	printBoard(visited);
//    	System.out.println("cnt: "+cnt);
    	
    	
    	if(dir == 2) {
    		dir = 3;
    	}else if(dir == 4) {
    		dir = 1;
    	}else if(dir == 6) {
    		dir = 2;
    	}
    	Light ret = new Light(dir, cnt, notStones, visited );
//    	System.out.println(notStones);
    	return ret;
    	
    }
    
    static int[] moveAll(ArrayList<Warrior> warriors, int visited[][]) {
    	int distSum = 0;
    	int attackerCnt = 0;
    	for(Warrior w : warriors) {
    		Move move = w.move(visited);
    		if(move.attack) {
    			attackerCnt++;
    		}
    		distSum += move.dist;
    	}
    	return new int[] {distSum, attackerCnt};
    }
    
   
    
    static void shadow(int y,int x, int dir, int visited[][]) {
    	if(y == monster.y || x == monster.x) {
    		for(int dist = 1; dist<=n; dist++) {
    			int ny = y + dy2[dir] * dist;
    			int nx = x + dx2[dir] * dist;
    			if(OOB(ny,nx))
    				break;
    			visited[ny][nx] = 2;
    		}
    	}else {
    		Queue<Pair> q = new ArrayDeque<>();
    		q.add(new Pair(y,x));
    		visited[y][x] = 2;
    		
    		while(!q.isEmpty()) {
    			Pair cur = q.poll();
    			
    			for(int nDir : getDirs(y, x, dir)) {
    				int ny = cur.y + dy2[nDir];
    				int nx = cur.x + dx2[nDir];
    				
    				if(OOB(ny,nx) || visited[ny][nx] == 2)
    					continue;
    				
    				if(visited[ny][nx] == 1) {
    					q.add(new Pair(ny,nx));
    					visited[ny][nx] = 2;
    				}
    			}
    		}
    		
    		visited[y][x] = 1;
    		
    	}
    }
    
    static int[] getDirs(int wy, int wx, int dir) {
    	if(dir == 0) {
    		if(wy < monster.y && wx < monster.x)
    			return new int[] {7,0};
    		if(wy < monster.y && wx > monster.x)
    			return new int[] {0,1};
    	}else if(dir == 2) {
    		if(wy < monster.y && wx > monster.x)
    			return new int[] {1,2};
    		if(wy > monster.y && wx > monster.x)
    			return new int[] {2,3};
    	}else if(dir == 4) {
    		if(wy > monster.y && wx > monster.x)
    			return new int[] {3,4};
    		if(wy > monster.y && wx < monster.x)
    			return new int[] {4,5};
    	}else if(dir == 6) {
    		if(wy > monster.y && wx < monster.x)
    			return new int[] {5,6};
    		if(wy < monster.y && wx < monster.x)
    			return new int[] {6,7};
    	}
    	return new int[] {dir};
    }
    
  
    
    
    static Light WORST = new Light(5, -1, null, null);
    
    static class Light{
    	int dir, cnt;
    	ArrayList<Warrior> notStones;
    	int visited[][];
    	public Light(int dir, int cnt, ArrayList<Warrior> notStones, int visited[][]) {
    		this.dir = dir;
    		this.cnt = cnt;
    		this.notStones = notStones;
    		this.visited = visited;
    	}
    	
    	public boolean isHigher(Light o) {
    		if(cnt != o.cnt)
    			return cnt > o.cnt;
    		return dir < o.dir;
    	}
    	
    	public String toString() {
    		return String.format("dir= %d, cnt= %d, not= %s", dir, cnt, notStones);
    	}
    }
    
    static class Tuple{
    	int y,x,dir;
    	public Tuple(int y,int x, int dir) {
    		this.y = y;
    		this.x = x;
    		this.dir = dir;
    	}
    }
    
    static void printBoard(int board[][]) {
    	for(int y=0; y<n; y++) {
    		for(int x=0; x<n; x++) {
    			System.out.printf("%3d", board[y][x] == INT_MAX ? 0 : board[y][x]);
    		}
    		System.out.println();
    	}
    }
    
    static int[] getDirs(int dir) {
    	if(dir % 2 == 0) {
    		return new int[] { (dir + 7) % 8, dir, (dir + 1) % 8 };
    	}
    	return new int[] {dir};
    }
    
    static int total;
    
    static Pair monster, exit;
    static StringTokenizer st;
    static int n,m;
    static int board[][], wBoard[][];
    
    static Warrior warriors[];
    static int dy[] = {-1,1,0,0};
    static int dx[] = {0,0,-1,1};
    static int dy2[] = {-1,-1,0,1,1,1,0,-1};
    static int dx2[] = {0,1,1,1,0,-1,-1,-1};
    static boolean OOB(int y,int x) {
    	return y<0 || y>=n || x<0 || x>=n;
    }
    
  
    
    static int INT_MAX = Integer.MAX_VALUE;
    
    static Warrior DEAD = new Warrior(-2,-2,-2);
    
    static class Warrior{
    	int id;
    	Pair pair;
    	public Warrior(int id, int y, int x) {
    		this.pair = new Pair(y,x);
    		this.id = id;
    	}
    	
    	public String toString() {
    		return String.format("%d: (%d,%d)", id,pair.y,pair.x);
    	}
    	
    	public Move move(int visited[][]) {
    		
    		// 첫번째 이동 
    		int distance = getDistance(pair.y,pair.x, monster.y, monster.x);
    		int direction = -1;
    		for(int dir = 0; dir< 4; dir++) {
    			int ny = pair.y + dy[dir];
    			int nx = pair.x + dx[dir];
    			if(OOB(ny,nx) || visited[ny][nx] == 1)
    				continue;
    			int d = getDistance(ny,nx, monster.y, monster.x);
    			if(distance > d) {
    				distance = d;
    				direction = dir;
    			}
    		}
    		if(direction == -1) { //첫번쨰 이동을 못했다  
    			return new Move(0, false);
    		}
    		
    		int value = 1<<id;
    		
    		wBoard[pair.y][pair.x] -= value; // 이동전 
    		
    		pair.y = pair.y + dy[direction];
    		pair.x = pair.x + dx[direction];
    		
    		if(monster.isSame(pair.y, pair.x)) { // 첫번째 이동을 하고 메두사와 같은 칸에 도달했다
    			warriors[id] = DEAD;
    			return new Move(1, true);
    		}
    		wBoard[pair.y][pair.x] += value; // 이동후 
    		
    		
    		// 두번째 이동 
    		direction = -1;
    		for(int dir = 2; dir < 6; dir++) {
    			int nDir = dir % 4;
    			int ny = pair.y + dy[nDir];
    			int nx = pair.x + dx[nDir];
    			if(OOB(ny,nx) || visited[ny][nx] == 1)
    				continue;
    			int d = getDistance(ny,nx, monster.y, monster.x);
    			if(distance > d) {
    				distance = d;
    				direction = nDir;
    			}
    		}
    		
    		if(direction == -1) { // 두번째 이동을 하지 못했다 
    			return new Move(1, false);
    		}
    		wBoard[pair.y][pair.x] -= value; // 이동전 
    		pair.y = pair.y + dy[direction];
    		pair.x = pair.x + dx[direction];
    		
    		if(monster.isSame(pair.y, pair.x)) { // 두번째 이동을 하고 메두사와 같은칸에 도달했다 
    			warriors[id] = DEAD;
    			return new Move(2, true);
    		}
    		
    		wBoard[pair.y][pair.x] += value; // 이동후 
    		
    		
    		// 두번째 이동을 했지만 메두사를 만나지 못했다
    		return new Move(2, false);
    		
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
}