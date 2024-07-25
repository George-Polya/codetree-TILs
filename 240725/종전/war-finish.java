import java.io.*;
import java.util.*;

/*
 * 1. 1번 부족영역 정하기 
 *  1.1 y<=3, 2<=x<=(n-1)인 (y,x)에 대해서 
 *  1.2 각 방향으로 움직일 횟수를 정해서 움직임 
 *  1.3 1번의 경계 설정. 
 *  
 * 2. 2,3,4,5부족의 영역 점유 
 * 3. 남은 모든 곳은 1번이 점유 
 */

public class Main {
	static StringTokenizer st;
	static int n;
	static int board[][];
	static boolean map[][];
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static int total;
	
	static int dy[] = {-1,-1,1,1};
	static int dx[] = {1,-1,-1,1};
	
	
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
	
	static void init() {
		for(int y=1; y<=n;y++) {
			Arrays.fill(map[y], false);
		}
	}
	
	/*
	 * 기울어진 직사각형 만들기
	 * 1. 1,2,3,4 방향순으로 반시계 순회해야함 
	 * 2. 이동 도중 격자밖으로 나가서는 안됨. 
	 */
	static Pair[] getEdges(int sy, int sx, int moveNums[]) {
//		List<Pair> ret = new ArrayList<>();
		int y = sy;
		int x = sx;
		Pair ret[] = new Pair[4];
		int idx = 0;
		for(int dir = 0; dir < 4; dir++) {
			int moveNum = moveNums[dir];
			
			ret[idx] = new Pair(y,x);
			idx++;
			while(moveNum-->0) {
				map[y][x] = true;
				int ny = y + dy[dir];
				int nx = x + dx[dir];
				if(OOB(ny,nx))
					return null;
				y = ny;
				x = nx;
			}
		}
		
		return ret; // 1번 부족의 꼭짓점 
		
	}
	
	static int ans = Integer.MAX_VALUE;
	
	
	static void occupy(Pair[] edges) {
		
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		
		// 5번 
		int scores[] = new int[5+1];
		for(int y= n; y>=1; y--) {
			if(y == edges[1].y)
				break;
			for(int x=n; x>=1; x--) {
				if(x < edges[0].x || map[y][x])
					break;
				map[y][x] = true;
				scores[5] += board[y][x];
			}
		}
		
		// 3번 
		for(int x = n ; x>=1; x--) {
			if(x == edges[2].x)
				break;
			
			for(int y=1; y<=n; y++) {
				if(y > edges[1].y || map[y][x])
					break;
				map[y][x] = true;
				scores[3] += board[y][x];
			}
		}
		
		// 2번
		for(int y = 1; y<=n; y++) {
			if(y == edges[3].y)
				break;
			
			for(int x=1;x<=n; x++) {
				if(x > edges[2].x || map[y][x])
					break;
				map[y][x] = true;
				scores[2] += board[y][x];
			}
		}
		
		// 4번 
		for(int x=1; x<=n; x++) {
			if(x == edges[0].x)
				break;
			
			for(int y=n;y>=1; y--) {
				if(y < edges[3].y || map[y][x])
					break;
				map[y][x] = true;
				scores[4] += board[y][x];
			}
		}
		
		
		// 나머지는 1번 
		scores[1] = total - (scores[2] + scores[3] + scores[4] + scores[5]);
		
		for(int i = 1; i<=5;i++) {
			max = Math.max(max, scores[i]);
			min = Math.min(min, scores[i]);
		}
		
		ans = Math.min(ans,  max-min);
		
	}
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	static void simulate(int y,int x) {
		for(int a = 1; a<=n-2; a++) { // 1,3번 방향의 움직일 횟수 
			for(int b=1; b<=n-2; b++) { // 2,4번 방향의 움직일 횟수 
				int moveNums[] = new int[]{a,b,a,b};
				init();
				
				Pair[] edges = getEdges(y,x,moveNums); // 1번 부족의 경계 구하기 
				
				if(edges == null) { // 격자밖을 나갔음 
					continue;
				}
				
				
				// 부족의 영역 점유
				occupy(edges);
			}
		}
		
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		board = new int[n+1][n+1];
		map = new boolean[n+1][n+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
				total += board[y][x];
			}
		}
		
		for(int sy = 3; sy<=n; sy++) {
			for(int sx=2; sx<=n-1;sx++) {
				simulate(sy,sx);
			}
		}
		System.out.println(ans);
	}
}