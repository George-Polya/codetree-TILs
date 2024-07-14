import java.io.*;
import java.util.*;

public class Main{
	static int n,m,k,c;
	static StringTokenizer st;
	static int tree[][], nxtTree[][], pesticide[][];
	static int WALL = -1;
	static int dy[] = {-1,1,0,0,-1,1,1,-1};
	static int dx[] = {0,0,-1,1,1,1,-1,-1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static void printBoard(int board[][]) {
		for(int y=1;y<=n;y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%-3d", board[y][x]);
			}
			System.out.println();
		}
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
	
	
	/*
	 * (y,x)에 위치한 나무의 성장
	 * 네 개의 칸 중 나무가 있는 칸의 수만큼 성 
	 */
	static void grow(int y,int x) {
		int cnt = 0;
		for(int dir = 0; dir < 4;dir++) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			if(OOB(ny,nx))
				continue;
			if(tree[ny][nx] > 0)
				cnt++;
		}
		tree[y][x] += cnt;
	}
	
	/*
	 * 나무 전의 성장 
	 * 1.1 인접한 네 개의 칸 중 나무가 있는 칸의 수만큼 성장
	 * 1.2 성장은 모두 동시에 일어남 
	 */
	static void growAll() {
		for(int y=1; y<=n;y++) {
			for(int x=1;x<=n;x++) {
				if(tree[y][x] > 0) {
					grow(y,x);
				}
				
				if(pesticide[y][x] > 0)
					pesticide[y][x]--;
			}
		}
	}
	
	
	static void copy(int src[][], int dst[][]) {
		for(int y=1; y<=n; y++) {
			System.arraycopy(src[y], 1, dst[y], 1, n);
		}
	}
	
	/*
	 * 나무 하나에 대한 번식 
	 *  2.2 인접한 4개의 칸 중 벽, 다른 나무, 제초제가 모두 없는 칸에 번식 진행 
	 *  2.3 각 칸의 나무수/번식가능한 칸 수 만큼 번식
	 */
	static void reproduce(int y,int x) {
		List<Pair> results = new ArrayList<>();
		
		for(int dir = 0; dir < 4; dir++) {
			int ny = y + dy[dir];
			int nx = x + dx[dir];
			
			if(OOB(ny,nx) || tree[ny][nx] != 0 || pesticide[ny][nx] != 0)
				continue;
			results.add(new Pair(ny,nx));
		}
		if(results.isEmpty())
			return;
		int cnt = tree[y][x] / results.size();
		for(Pair pair : results) {
			nxtTree[pair.y][pair.x] += cnt;
		}
	}
	
	/*
	 * 2. 번식
	 *  2.1 nxtTree 초기화 
	 *  2.2 인접한 4개의 칸 중 벽, 다른 나무, 제초제가 모두 없는 칸에 번식 진행 
	 *  2.3 각 칸의 나무수/번식가능한 칸 수 만큼 번식
	 *  2.4 모두 동일하게 일어
	 */
	static void reproduceAll() {
		copy(tree,nxtTree);
		
		for(int y=1; y<=n; y++) {
			for(int x=1;x<=n;x++) {
				if(tree[y][x] > 0)
					reproduce(y,x);
			}
		}
		
		copy(nxtTree,tree);
	}
	
	static class Tuple{
		int y,x,cnt;
		public Tuple(int y,int x, int cnt) {
			this.y = y;
			this.x = x;
			this.cnt = cnt;
		}
		
		public boolean isHigher(Tuple t) {
			if(cnt != t.cnt)
				return cnt  > t.cnt;
			if(y != t.y)
				return y < t.y;
			return x < t.x;
		}
		
		public String toString() {
			return y+" "+x+" "+cnt;
		}
	}
	static Tuple NO_TARGET = new Tuple(22,22,-1);
	
	static Tuple findTarget(int y,int x) {
		int cnt = tree[y][x];
		for(int dir = 4; dir< 8;dir++) {
			
			for(int dist = 1; dist<=k; dist++) {
				int ny = y + dy[dir] * dist;
				int nx = x + dx[dir] * dist;
				
				if(OOB(ny,nx) || tree[ny][nx] <= 0)
					break;
				cnt += tree[ny][nx];
			}
		}
		
		return new Tuple(y,x,cnt);
	}
	
	static Tuple findBestTarget() {
		Tuple best = NO_TARGET;
		for(int y=1; y<=n; y++) {
			for(int x=1; x <= n; x++) {
				if(tree[y][x] <= 0) // 나무가 없는 칸에 뿌리면 박멸되는 나무가 전혀 없다 
					continue;
				// 현재 칸에서 k칸 전파시 박멸되는 나무 수 
				Tuple target = findTarget(y,x);
				if(target.isHigher(best)) {
					best = target;
				}
			}
		}
		return best;
	}
	
	static int score;
	/*
	 * 3. 박멸
	 *  3.1 나무가 가장 많이 박멸되는 칸 찾기(대각선으로 k칸 전파하고 벽이 있거나 나무가 아예 없으면 그 칸까지만 살포했을 때 가장 많이 박멸되는칸) 
	 *  3.2 제초제 살포 -> 4개의 대각선 방향으로 k칸 전파. 벽이 있거나 나무가 없으면 그칸까지만 살포 -> tree는 0이 되고 pesticide는 c+1
	 *  3.3 제초제는 c년 동안 남아있음 
	 */
	static void exterminate() {
		// 나무가 가장 많이 박멸되는 칸 찾기 
		Tuple bestTarget = findBestTarget();
//		System.out.println("best: "+bestTarget);
		if(bestTarget == NO_TARGET)
			return;
		
		int y = bestTarget.y;
		int x = bestTarget.x;
		score += tree[y][x];
		tree[y][x] = 0;
		pesticide[y][x] = c + 1;
		// 제초제 살포 
		for(int dir = 4; dir < 8; dir++) {
			for(int dist = 1; dist<=k; dist++) {
				int ny = y + dy[dir] * dist;
				int nx = x + dx[dir] * dist;
				if(OOB(ny,nx))
					break;
				if(tree[ny][nx] <= 0) {
					pesticide[ny][nx] = c + 1;
					break;
				}
				
				score += tree[ny][nx];
				tree[ny][nx] = 0;
				pesticide[ny][nx] = c + 1;
			}
		}
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		
		tree = new int[n+1][n+1];
		nxtTree = new int[n+1][n+1];
		pesticide = new int[n+1][n+1];
		for(int y=1;y<=n;y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1;x<=n;x++) {
				tree[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
//		// 성장 
//		growAll();
//		// 번식 
//		reproduceAll();
//		// 박멸 
//		exterminate();
//		// 제초제의 사라짐 
		
		for(int turn = 1; turn <=m; turn++) {
//			System.out.println("------");
//			System.out.println("turn: "+turn);
			growAll();
//			System.out.println("\nafter grow");
//			printBoard(tree);
			reproduceAll();
//			System.out.println("\nafter reproduce");
//			printBoard(tree);
			exterminate();
//			System.out.println("\npesticide");
//			printBoard(pesticide);
//			System.out.println("\ntree");
//			printBoard(tree);
//			System.out.println("score: "+score);
		}
		System.out.println(score);
		
	}
}