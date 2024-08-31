import java.util.*;
import java.io.*;

public class Main {
    static class Point{
		int r;
		int c;
		public Point(int r, int c) {
			this.r = r;
			this.c = c;
		}
	}
	static int N;
	static int[][][] map;
	static boolean[][] visited;
	static List<Point>[] arr;
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, -1, 0, 1};
	static int groupCnt;
	static int[] comb;
	static boolean[] combVisited;
	static int[] combResult;
	static int roundSum;
	static int answer;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		
		map = new int[N][N][2];
		
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j < N; j++) {
				map[i][j][0] = Integer.parseInt(st.nextToken());
			}
		}
		
		for(int i = 0; i < 4; i++) {
			roundSum = 0;
			zoning();
			initBeforeComb();
			combination(0, comb.length, 2);
			answer += roundSum;
			
			// System.out.println("answer: " + answer);
			
			// 회전
			rotateMap();
			
			// System.out.println("회전 후");
			// printMap();
		}
		
		System.out.println(answer);
    }

    private static void rotateMap() {
		int size = N/2;
		crossRotate();
		rotate(0, 0, size);
		rotate(size+1, 0, size);
		rotate(0, size+1, size);
		rotate(size+1, size+1, size);
	}
	
	private static void crossRotate() {
		int midR = N/2;
		int midC = N/2;
		int temp = 0;
		
		for(int k = 1; k <= midR; k++) {
			temp = map[midR - k][midC][0];
	        
	        // 왼쪽 값을 위쪽으로 이동
	        map[midR - k][midC][0] = map[midR][midC - k][0];
	        
	        // 아래쪽 값을 왼쪽으로 이동
	        map[midR][midC - k][0] = map[midR + k][midC][0];
	        
	        // 오른쪽 값을 아래쪽으로 이동
	        map[midR + k][midC][0] = map[midR][midC + k][0];
	        
	        // temp 값을 오른쪽으로 이동
	        map[midR][midC + k][0] = temp;
		}
	}

    private static void rotate(int r, int c, int size) {
		int[][] tempMap = new int[size][size];
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				int nr = r + i;
				int nc = c + j;
				tempMap[size - 1 - j][i] = map[nr][nc][0];
			}
		}
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				int nr = r + i;
				int nc = c + j;
				map[nr][nc][0] = tempMap[i][j];
			}
		}
		
	}
	
	private static void initBeforeComb() {
		comb = new int[groupCnt];
		combVisited = new boolean[groupCnt];
		for(int i = 0; i < comb.length; i++) {
			comb[i] = i;
		}
	}

    private static void combination(int depth, int n, int r) {
		if(r == 0) {
			combResult = new int[2];
			int k = 0;
			for(int i = 0; i < comb.length; i++) {
				if(combVisited[i]) combResult[k++] = i;
			}
			roundSum += calculateHarmony();
			return;
		}
		
		if(depth == n) {
			return;
		}
		
		combVisited[depth] = true;
		combination(depth+1, n, r-1);
		
		combVisited[depth] = false;
		combination(depth+1, n, r);
	}
	
	private static int calculateHarmony() {
		int harmony = 0;
		
		// System.out.println(Arrays.toString(combResult));
		
		int a = arr[combResult[0]].size();
		int b = arr[combResult[1]].size();
		
		Point aPoint = arr[combResult[0]].get(0);
		Point bPoint = arr[combResult[1]].get(0);
		
		// System.out.println("a: " + a);
		// System.out.println("b: " + b);
		
		int aNum = map[aPoint.r][aPoint.c][0];
		int bNum = map[bPoint.r][bPoint.c][0];
		
		int aGroup = map[aPoint.r][aPoint.c][1];
		int bGroup = map[bPoint.r][bPoint.c][1];
		
		
		
		int wall = 0;
		for(Point p : arr[combResult[0]]) {
			for(int d = 0; d < 4; d++) {
				int nr = p.r + dx[d];
				int nc = p.c + dy[d];
				
				if(isInRange(nr, nc) && map[nr][nc][1] == bGroup) {
					wall++;
				}
			}
		}
		
		// System.out.println("wall: " + wall); 
		
		harmony = (a + b) * aNum * bNum * wall;
		
		// System.out.println("harmony: " + harmony);
		
		return harmony;
	}

    private static void zoning() {
		visited = new boolean[N][N];
		
		// 그룹이 몇개인지 체크하는 변수
		groupCnt = 0;
		
		// 맵 전체를 돌면서 아직 방문하지 않았다면 새롭게 bfs를 한다
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(!visited[i][j]) {
					// System.out.println("bfs 실행 좌표");
					// System.out.println("i: " + i);
					// System.out.println("j: " + j);
					bfs(new Point(i,j));
					// printVisited();
					// bfs를 했으면 무조건 group이 하나 생기는거다.
					groupCnt++;
				}
			}
		}
		
		// System.out.println("groupCnt: " + groupCnt);
		
		arr = new List[groupCnt];
		
		for(int i = 0; i < arr.length; i++) {
			arr[i] = new ArrayList<Point>();
		}
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				arr[map[i][j][1]].add(new Point(i,j));
			}
		}
		
	}

    private static void bfs(Point start) {
		Queue<Point> queue = new LinkedList<>();
		visited[start.r][start.c] = true;
		map[start.r][start.c][1] = groupCnt;
		
		queue.offer(start);
		
		while(!queue.isEmpty()) {
			Point now = queue.poll();
			
			for(int d = 0; d < 4; d++) {
				int nr = now.r + dx[d];
				int nc = now.c + dy[d];
				
				if(isInRange(nr, nc) && !visited[nr][nc] && map[nr][nc][0] == map[now.r][now.c][0]) {
					map[nr][nc][1] = groupCnt;
					visited[nr][nc] = true;
					queue.offer(new Point(nr, nc));
				}
			}
		}
		
	}
	
	
	private static boolean isInRange(int r, int c) {
		return r >= 0  && c >= 0 && r < N && c < N;
	}
	
	private static void printMap() {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				System.out.print(map[i][j][0] + " ");
			}
			System.out.println();
		}
	}

    private static void printVisited() {
		for(int i = 0; i < visited.length; i++) {
			for(int j = 0; j < visited[0].length; j++) {
				System.out.print(visited[i][j] + " ");
			}
			System.out.println();
		}
	}
}