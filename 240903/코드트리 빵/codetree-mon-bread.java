import java.io.*;
import java.util.*;

public class Main {
    static int n,m;
    static int board[][], dist[][];
    static int INT_MAX = Integer.MAX_VALUE;
    static StringTokenizer st;
    static int t;
    static Pair stores[], people[];
    static List<Pair> basecamps = new ArrayList<>();
    
    static int dy[] = {-1,0,0,1};
    static int dx[] = {0,-1,1,0};
    
    static boolean OOB(int y,int x) {
    	return y<=0 || y>n || x<=0 || x>n;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        board = new int[n+1][n+1];
        dist = new int[n+1][n+1];
        
        for(int y=1; y<=n; y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x=1; x<=n; x++) {
        		int value = Integer.parseInt(st.nextToken());
        		if(value == 1)
        			basecamps.add(new Pair(y,x));
        	}
        }
        
        stores = new Pair[m+1];
        people = new Pair[m+1];
        
        for(int i = 1; i<=m; i++) {
        	st = new StringTokenizer(br.readLine());
        	int y = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken());
        	stores[i] = new Pair(y,x);
        	people[i] = NO_POS;
        }
        
        while(!end()) {
        	t++;
        	
        	// 격자안의 모든 사람이 1칸 씩 이동 
        	moveAll();
        	
        	// 이동이 끝났으면 편의점 비활성화 
        	disableStores();
        	
        	
        	if(t > m )
        		continue;
        	
        	// t번 사람이 베이스캠프로 이동 
        	gotoBasecamp(t);
        }
        System.out.println(t);
    }
    
    static Tuple findBestCamp(int idx) {
    	Pair store = stores[idx];
    	
    	bfs(store.y, store.x);
    	
    	Tuple best = NO_CAMP;
    	for(Pair pair : basecamps) {
    		if(board[pair.y][pair.x] == 1) // 이미 점유된 캠프는 선택하지 않음 
    			continue;
    		Tuple camp = new Tuple(pair.y, pair.x, dist[pair.y][pair.x]);
    		if(camp.isHigher(best))
    			best = camp;
    	}
    	
    	return best;
    }
    
    static void gotoBasecamp(int idx) {
    	Tuple bestCamp = findBestCamp(idx);
    	
    	people[idx] = new Pair(bestCamp.y, bestCamp.x);
    	board[bestCamp.y][bestCamp.x] = 1;
    }
    
    static class Tuple{
    	int y, x, dist;
    	public Tuple(int y,int x, int dist) {
    		this.y = y;
    		this.x = x;
    		this.dist = dist;
    	}
    	
    	public boolean isHigher(Tuple o) {
    		if(dist != o.dist)
    			return dist < o.dist;
    		if(y != o.y)
    			return y < o.y;
    		return x < o.x;
    	}
    }
    
    static Tuple NO_CAMP = new Tuple(16,16,INT_MAX);
    
    static void disableStores() {
    	for(int i = 1; i<=m; i++) {
//    		Pair person = people[i];
    		Pair store = stores[i];
    		
    		if(board[store.y][store.x] == 0 && store.isSame(people[i]))
    			board[store.y][store.x] = 1;
    	}
    }
    
    
    /*
     * 격자 안의 모두가 가고 자신이 가고 싶어하는 편의점을 향해 최단거리로 이동 
     */
    static void moveAll() {
    	for(int i = 1; i<=m; i++) {
    		// 격자 밖에 있는 사람은 이동하지 않는다
    		//  편의점에 도착한 사람도 이동하지 않는다 
    		if(people[i] == NO_POS || people[i].isSame(stores[i]))  
    			continue;
    		move(i);
    	}
    }
    
    static void move(int idx) {
    	Pair nxtPos = getNxtPos(idx);
    	
    	// nxtPos == null 체크는 안해도 됨. 
    	// nxtPos가 null인 경우는 지금 사람이 이동하려는 위치 4곳 모두 막혀있다는 뜻
    	// 즉, 이러면 지금 사람이 이동을 못하게 되므로 결국 편의점으로 도달하지 못하게 됨. 
    	// 문제에서 이런 경우는 절대 발생하지 않는다 하였음 
    	people[idx] = nxtPos;
    }
    
    static void bfs(int sy,int sx) {
    	for(int y = 1; y<=n; y++) {
    		Arrays.fill(dist[y], INT_MAX);
    	}
    	Queue<Pair> q = new LinkedList<>();
    	q.add(new Pair(sy,sx));
    	dist[sy][sx] = 0;
    	
    	while(!q.isEmpty()) {
    		Pair cur = q.poll();
    		
    		for(int dir = 0; dir< 4; dir++) {
    			int ny = cur.y + dy[dir];
    			int nx = cur.x + dx[dir];
    			
    			if(OOB(ny,nx) || dist[ny][nx] != INT_MAX || board[ny][nx] != 0)
    				continue;
    			dist[ny][nx] = dist[cur.y][cur.x] + 1;
    			q.add(new Pair(ny,nx));
    		}
    	}
    }
    
    static Pair getNxtPos(int idx) {
    	
    	Pair store = stores[idx];
    	
    	bfs(store.y, store.x);
    	
    	Pair ret = null;
    	int distance = INT_MAX;
    	for(int dir = 0; dir < 4; dir++) {
    		int ny = people[idx].y + dy[dir];
    		int nx = people[idx].x + dx[dir];
    		if(OOB(ny,nx) || board[ny][nx] != 0)
    			continue;
    		if(distance > dist[ny][nx]) {
    			distance = dist[ny][nx];
    			ret = new Pair(ny,nx);
    		}
    	}
    	
    	return ret;
    }
    
    
    static boolean end() {
    	for(int i = 1; i<=m; i++) {
    		if(!people[i].isSame(stores[i]))
    			return false;
    	}
    	return true;
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
    	
    	public boolean isSame(Pair o) {
    		return y == o.y && x == o.x;
    	}
    }
    
    static Pair NO_POS = new Pair(-1,-1);
}