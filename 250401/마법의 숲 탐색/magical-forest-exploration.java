import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) throws IOException {
//    	System.setIn(new FileInputStream("./input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		R = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		board = new int[R + 1 + 3][C + 1];
		exit = new int[R + 1 + 3][C + 1];

		int ans = 0;
		for (int i = 1; i <= K; i++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			Robot robot = new Robot(i, x, d);
			int score = robot.move();
			score = score == 0 ? 0 : score - 3;
			ans += score;
		}

		System.out.println(ans);
	}

	static void printBoard(int board[][]) {
		for (int y = 4; y <= R + 3; y++) {
			for (int x = 1; x <= C; x++) {
				System.out.printf("%3d", board[y][x]);
			}
			System.out.println();
		}
	}

	static int R, C, K;
	static int board[][];
	static int exit[][];
	static StringTokenizer st;
	static int dy[] = { -1, 0, 1, 0 };
	static int dx[] = { 0, 1, 0, -1 };

	static class Robot {
		int id, y, x, d;

		public Robot(int id, int x, int d) {
			this.id = id;
			this.y = 2;
			this.x = x;
			this.d = d;
		}

		public int move() {
			Tuple nxt = findNxtPos(y, x, d);
//    		System.out.println("nxt: "+nxt);
			if (nxt == null) {// 최대한 남쪽으로 내려갔는데 숲을 벗어났음.
				board = new int[R + 1 + 3][C + 1];
				exit = new int[R + 1 + 3][C + 1];
				return 0;
			}

			this.y = nxt.y;
			this.x = nxt.x;
			this.d = nxt.d;
			board[y][x] = this.id;
			for (int dir = 0; dir < 4; dir++) {
				int ny = this.y + dy[dir];
				int nx = this.x + dx[dir];
				board[ny][nx] = this.id;
			}

			int ey = this.y + dy[this.d];
			int ex = this.x + dx[this.d];
			exit[ey][ex] = this.id;

			// 요정의 이동
			return fariyMove(y, x);
		}

		private int fariyMove(int cy, int cx) {
			boolean visited[][] = new boolean[R + 1 + 3][C + 1];
			Queue<Pair> q = new ArrayDeque<>();
			q.add(new Pair(cy, cx, id));
			visited[cy][cx] = true;
			int ret = 0;
			while (!q.isEmpty()) {
				Pair cur = q.poll();
//    			System.out.println("cur: "+cur);
				ret = Math.max(ret, cur.y);
				for (int dir = 0; dir < 4; dir++) {
					int ny = cur.y + dy[dir];
					int nx = cur.x + dx[dir];

					if (OOB(ny, nx, 4, R + 3) || visited[ny][nx] || board[ny][nx] == 0)
						continue;

					if(board[ny][nx] == board[cur.y][cur.x] || exit[cur.y][cur.x] != 0) {
						visited[ny][nx] = true;
						q.add(new Pair(ny,nx, board[ny][nx]));
					}

				}
			}

			return ret;
		}

		private Tuple findNxtPos(int y, int x, int d) {
			if (canMove(y, x, 2)) { // 남쪽으로 이동가능
				int ny = y + dy[2];
				int nx = x + dx[2];
				return findNxtPos(ny, nx, d);
			} else if (canMove(y, x, 3)) { // 서쪽으로 이동가능
				int ny = y + dy[3];
				int nx = x + dx[3];
				int nd = (d + 3) % 4;
				return findNxtPos(ny, nx, nd);
			} else if (canMove(y, x, 1)) { // 동쪽으로 이동가능
				int ny = y + dy[1];
				int nx = x + dx[1];
				int nd = (d + 1) % 4;
				return findNxtPos(ny, nx, nd);
			}

			// 숲을 벗어나는지 체크
			if (OOR(y, x))
				return null;

			return new Tuple(y, x, d);

		}

		private boolean OOR(int y, int x) {
			// 5칸 중 하나라도 OOB면 true
			if (OOB(y, x, 4, R + 3))
				return true;

			for (int dir = 0; dir < 4; dir++) {
				int ny = y + dy[dir];
				int nx = x + dx[dir];
				if (OOB(ny, nx, 4, R + 3))
					return true;
			}

			return false;
		}

		private boolean canMove(int cy, int cx, int moveDir) {
			int y = cy + dy[moveDir];
			int x = cx + dx[moveDir];
			/*
			 * 남쪽으로 이동 가능한가? 남쪽 세칸이 모두 OOB가 아니고, 모두 1이 아닌가?
			 */
			if (moveDir == 2) {
				for(int dir : new int[] {1,2,3}) {
					int ny = y + dy[dir];
					int nx = x + dx[dir];
					if(OOB(ny,nx, 0, R+3) || board[ny][nx] != 0)
						return false;
				}
				
				return true;
			} else if (moveDir == 3) {
				for(int dir : new int[] {0,2,3}) {
					int ny = y + dy[dir];
					int nx = x + dx[dir];
					if(OOB(ny,nx, 0,R+3) || board[ny][nx] != 0)
						return false;
						
				}
 
				return canMove(y, x, 2);
			} else {
				for(int dir : new int[] {0,1,2}) {
					int ny = y + dy[dir];
					int nx = x + dx[dir];
					if(OOB(ny,nx, 0,R+3) || board[ny][nx] != 0)
						return false;
						
				}
				return canMove(y, x, 2);
			}

		}
	}

	static boolean OOB(int y, int x, int minY, int maxY) {
		return y < minY || y > maxY || x <= 0 || x > C;
	}

	static class Pair {
		int y, x, id;

		public Pair(int y, int x, int id) {
			this.y = y;
			this.x = x;
			this.id = id;
		}

		public String toString() {
			return String.format("(%d, %d) | id: %d", y, x, id);
		}
	}

	static class Tuple {
		int y, x, d;

		public Tuple(int y, int x, int d) {
			this.y = y;
			this.x = x;
			this.d = d;
		}

		public String toString() {
			return String.format("(%d, %d), d: %d", y, x, d);
		}
	}
}