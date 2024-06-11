import java.util.*;
import java.io.*;
public class Main {
	static int n = 5;
	static int k,m;
	static StringTokenizer st;
	static int pieces[];
	static int board[][];
	static int idx;
	static class Pair implements Comparable<Pair>{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public int compareTo(Pair p) {
			if( x!= p.x)
				return x - p.x;
			return p.y - y;
		}
		
		public String toString() {
			return y+" "+x;
		}
	}
	static class Rotate{
		int cy,cx,angle, sumValue;
		int rotatedBoard[][];
		List<Pair> removes;
		public Rotate(int cy,int cx, List<Pair> removes, int angle) {
			this.cy = cy;
			this.cx = cx;
			this.removes = removes;
			this.sumValue = removes.size();
			this.angle = angle;
		}
		
		public boolean isHigher(Rotate r) {
			if(sumValue != r.sumValue)
				return sumValue > r.sumValue;
			if(angle != r.angle)
				return angle < r.angle;
			if(cx != r.cx)
				return cx < r.cx;
			return cy < r.cy;
		}
		
		public String toString() {
			return cy+" "+cx+" "+angle;
		}
	}
	
	static Rotate WORST_ROTATE = new Rotate(n+1,n+1,new ArrayList<Pair>(), 360);
	static void copy(int src[][], int dst[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1;x<=n;x++) {
				dst[y][x]= src[y][x];
			}
		}
	}
	
	static int[][] rotate(int cy, int cx,int board[][], int angle) {
		int temp[][] = new int[n+1][n+1];
		copy(board,temp);
		
		for(int y = cy-1; y<=cy+1; y++) {
			for(int x=cx-1;x<=cx+1;x++) {
				int _y = y - cy + 1;
				int _x = x - cx + 1;
				if(angle == 90) {
					int tempY = _x;
					int tempX = 2 - _y;
					temp[tempY+ cy - 1][tempX + cx - 1] = board[y][x];
				}else if(angle == 180) {
					int tempY = 2 - _y;
					int tempX = 2 - _x;
					temp[tempY+ cy - 1][tempX + cx - 1] = board[y][x];
				}else if(angle == 270) {
					int tempY = 2 - _x;
					int tempX = _y;
					temp[tempY+ cy - 1][tempX + cx - 1] = board[y][x];
				}
				
			}
		}
		
		return temp;
		
	}
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static List<Pair> bfs(int y,int x, boolean visited[][], int board[][]) {
		List<Pair> ret = new ArrayList<>();
		
		Queue<Pair> q = new LinkedList<>();
		q.add(new Pair(y,x));
		visited[y][x] = true;
		int value = board[y][x];
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			ret.add(cur);
			
			for(int dir = 0; dir<4;dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				
				if(OOB(ny,nx) || visited[ny][nx] || board[ny][nx] != value)
					continue;
				visited[ny][nx] = true;
				q.add(new Pair(ny,nx));
			}
		}
		
		return ret;
	}
	
	static List<Pair> remove(int board[][]){
		boolean visited[][] = new boolean[n+1][n+1];
		
		List<Pair> removes = new ArrayList<>();
		
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				if(visited[y][x])
					continue;
				List<Pair> connected = bfs(y,x,visited, board); // 현재 위치의 조각과 같은 종류인 조각들의 리스트 
//				System.out.println("connected: "+connected);
				if(connected.size() >= 3) // 조각이 3개이상 연결된 경우 
					removes.addAll(connected);
			}
		}
		Collections.sort(removes);
		
		return removes;
	}
	
	static Rotate getRotate(int cy, int cx, int angle) {
		int rotatedBoard[][] = rotate(cy,cx,board,angle); // 중심좌표와 회전각도로 회전한 결과 

		
		List<Pair> removes = remove(rotatedBoard);
		if(removes.size() < 3)
			return WORST_ROTATE;
		
		Rotate ret = new Rotate(cy,cx,removes, angle);
		
		ret.rotatedBoard = rotatedBoard;
		
		return ret;
		
		
	}
	
	static Rotate getBestRotate() {
		Rotate best = WORST_ROTATE;
		
		for(int y=2;y<=4;y++) {
			for(int x=2; x<=4;x++) {
				for(int angle : new int[] {90,180,270}) {
					Rotate rotate = getRotate(y,x,angle);
					if(rotate.isHigher(best))
						best = rotate;
				}
			}
		}
		return best;
	}
	
	static void fill(List<Pair> removes) {
		for(Pair pair : removes) {
			board[pair.y][pair.x] = pieces[idx++];
		}
	}
	
	static int ans;
	static int chain() {
		int ret = 0;
		List<Pair> removes = null;
		while( (removes = remove(board)).size() >= 3) {
			ret += removes.size();
			fill(removes);
		}
		return ret;
	}
	
    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	
    	k = Integer.parseInt(st.nextToken());
    	m = Integer.parseInt(st.nextToken());
    	pieces = new int[m];
    	board = new int[n+1][n+1];
    	
    	for(int y=1; y<=n; y++) {
    		st = new StringTokenizer(br.readLine());
    		for(int x=1; x<=n;x++) {
    			board[y][x] = Integer.parseInt(st.nextToken());
    		}
    	}
    	
    	st = new StringTokenizer(br.readLine());
    	for(int i=0;i<m;i++) {
    		pieces[i] = Integer.parseInt(st.nextToken());
    	}
    	
    	StringBuilder sb = new StringBuilder();
    	for(int turn=1;turn<=k;turn++) {
//    		System.out.println("turn: "+turn);
    		int ans = 0;
    		// 최적의 격자 선택 
    		Rotate best = getBestRotate();
//    		System.out.println("best: "+best);
    		
    		if(best == WORST_ROTATE)
    			break;
    		
    		// 유물 1차 획득 
    		ans += best.sumValue;
    		board = best.rotatedBoard;
    		
    		fill(best.removes);
    		
    		ans += chain();
    		
//    		System.out.println(ans);
    		sb.append(ans).append(' ');
    	}
    	System.out.println(sb);
    	
    }
}