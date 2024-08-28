import java.io.*;
import java.util.*;

public class Main{
	static int n;
	static int board[][], tempBoard[][];
	static StringTokenizer st;
//	static boolean visited[][];
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
	
	static class Group{
		int size; // 그룹에 속한 칸의 수 
		int value; // 그룹을 이루고 있는 숫자 값 
		int id; // 그룹 id 
		List<Pair> pairs; // 그룹을 이루고 있는 칸들의 좌표 
		public Group(int size, int value, int id, List<Pair> pairs) {
			this.size = size;
			this.value = value;
			this.id = id;
			this.pairs = pairs;
		}
		
		public String toString() {
			return id+"";
		}
	}
	
	static List<Group> groups = new ArrayList<>();
	static int groupId;
	static void init() {
		for(int y=1; y<=n; y++) {
//			Arrays.fill(visited[y], false);
			Arrays.fill(tempBoard[y], 0);
		}
		groups.clear();
		groupId = 1;
	}
	
	static int dy[] = {-1,0,1,0};
	static int dx[] = {0,-1,0,1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.print(board[y][x]+" ");
			}
			System.out.println();
		}
	}
	
	
	static Group bfs(int y, int x) {
		int value = board[y][x];
		
		Queue<Pair> q = new LinkedList<>();
//		visited[y][x] = true;
		tempBoard[y][x] = groupId;
		q.add(new Pair(y,x));
		List<Pair> pairs = new ArrayList<>();
		
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			pairs.add(cur);
//			tempBoard[cur.y][cur.x] = groupId;
			
			for(int dir = 0; dir<4;dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				
				if(OOB(ny,nx) || tempBoard[ny][nx] != 0 || board[ny][nx] != value)
					continue;
				
//				visited[ny][nx] = true;
				tempBoard[ny][nx] = groupId;
				q.add(new Pair(ny,nx));
			}
		}
		
		return new Group(pairs.size(), value, groupId++, pairs);
	}
	
	static void makeGroups() {
		
		init();
		for(int y=1;y<=n;y++) {
			for(int x=1; x<=n; x++) {
				if(tempBoard[y][x] != 0)
					continue;
//				if(visited[y][x])
//					continue;
				groups.add(bfs(y,x));
			}
		}
		
//		printBoard(idBoard);
//		System.out.println(groups);
	}
	
	
	/*
	 * 두 그룹의 조화로움 계
	 */
	static int getScore(Group g1, Group g2) {
		// 맞닿아 있는 변의 수 계산하기 
		int cnt = 0;
		for(Pair pair : g1.pairs) {
			int y = pair.y;
			int x = pair.x;
			
			for(int dir = 0; dir<4;dir++) {
				int ny = y + dy[dir];
				int nx = x + dx[dir];
				
				if(OOB(ny, nx))
					continue;
				
				
				
				if(tempBoard[ny][nx] == g2.id)
					cnt++;
			}
		}
		
		int score = (g1.size+g2.size)*g1.value *g2.value * cnt;
		return score;
	}
	
	/*
	 * 예술점수 계산 
	 * 1. 그룹 나누기  
	 *  1.1 상하좌우 동일 숫자 인접하면 한 그룹 
	 *  1.2 value는 같지만 그룹은 다를 수 있음 
	 *  1.3 따라서 그룹 나눌때 idBoard를 채운다면 맞닿아 있는 변의 수 구할때 이용할 수 있지 않을까 
	 * 
	 * 2. 그룹으로 예술점수 계산하기 
	 *  2.1 맞닿아 있는 변의 수 구하는게...
	 */
	static int calc() {
		// 그룹 나누기 
		makeGroups();
		
		
		// 예술 점수 계산
		int sum = 0;
		for(int i = 0; i<groups.size();i++ ) {
			for(int j = i+1; j<groups.size(); j++) {
				Group g1 = groups.get(i);
				Group g2 = groups.get(j);
				
				sum += getScore(g1,g2); // 두 그룹 간의 조화로움 계산 
			}
		}
		return sum;
	}
	
	static void rotateCross() {
		int cy = n / 2 + 1;
		int cx = n / 2 + 1;
		
//		nxtBoard[cy][cx] = board[cy][cx];
		tempBoard[cy][cx] = board[cy][cx];
		
		for(int size = 1; size <= n/2;size++) {
			int dir = 0;
			int y = cy + dy[dir] * size;
			int x = cx + dx[dir] * size;
			
			int temp = board[y][x];
			for(int i = 0; i<4;i++) {
				dir = (dir + 3) % 4;
				int ny = cy + dy[dir] * size;
				int nx = cx + dx[dir] * size;
//				System.out.printf("y: %d, x: %d\n", y,x);
//				System.out.printf("ny: %d, nx: %d\n", ny,nx);
				tempBoard[y][x] = board[ny][nx];
				y = ny;
				x = nx;
			}
//			System.out.printf("y: %d, x: %d, dir: %d\n", y,x, dir);
			dir = (dir + 1) % 4;
			int ny = cy + dy[dir] * size;
			int nx = cx + dx[dir] * size;
//			System.out.printf("ny: %d, nx: %d\n", ny,nx);
			tempBoard[ny][nx] = temp;
//			System.out.println("-----");
		}
	}
	
	static void rotateSquare(int sy, int sx, int size) {
		
		for(int y = sy; y<sy+size; y++) {
			for(int x= sx; x<sx+size; x++) {
				int ny = x + sy - sx;
				int nx = sy + sx + size - 1 - y;
				
//				System.out.printf("y: %d, x: %d\n", y,x);
//				System.out.printf("ny: %d, nx: %d\n", ny,nx );
//				nxtBoard[x][size + 1 - y] = board[y][x];
				tempBoard[ny][nx] = board[y][x];
			}
		}
	}
	
	static void rotate4Square() {
		int size = n / 2;
		for(int sy=1; sy <=n; sy += size + 1) {
			for(int sx=1; sx<=n; sx+= size + 1) {
				rotateSquare(sy,sx,size);
//				System.out.println("======");
			}
		}
	}
	
	/*
	 * 1. 십자 모양 회전
	 * 
	 * 2. 십자 제외 4개의 정사각형의 회전 
	 */
	static void rotate() {
		
		rotateCross();
//		printBoard(nxtBoard);
//		System.out.println("------");
		
		rotate4Square();
//		printBoard(nxtBoard);
//		System.out.println("------");
		
		for(int y=1; y<=n; y++) {
			System.arraycopy(tempBoard[y], 1, board[y], 1, n);
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		board = new int[n+1][n+1];
		tempBoard = new int[n+1][n+1];
//		visited = new boolean[n+1][n+1];
		
		for(int y=1;y<=n;y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1;x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		int ans = calc();
//		System.out.println(scores[0]);
//		rotate();
		for(int i = 1; i<=3;i ++) {
			rotate();
			ans += calc();
		}
		
		System.out.println(ans);
		
	}
	
}