import java.util.*;
import java.io.*;

public class Main {
	static int n;
	static int board[][];
	static StringTokenizer st;
	static int MAX_R;
	
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public String toString() {
			return y+" "+x;
		}
		
		public boolean equals(Object o) {
			if(this == o)
				return true;
			if(o == null || getClass() != o.getClass())
				return false;
			Pair p = (Pair)o;
			return y == p.y && x == p.x;
		}
		
		public int hashCode() {
			return Objects.hash(y,x);
		}
		
	}
	
	static int dy[] = {-1,-1,1,1};
	static int dx[] = {1,-1,-1,1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<= 0 || x>n;
	}
	
	static ArrayList<int[]> makeAllMoveNums(int sy, int sx){
		ArrayList<int[]> rects = new ArrayList<>();
		for(int r = 1; r<=MAX_R;r++) {
			A : for(int l = 1; l<=MAX_R; l++) {
				int moveNums[] = {r,l,r,l};
				int y = sy;
				int x = sx;
				ArrayList<Pair> rect = new ArrayList<>();
				int dir = 0;
				
				for(int num : moveNums) {
					rect.add(new Pair(y,x));
					for(int i = 0; i<num; i++) {
						y = y + dy[dir];
						x = x + dx[dir];
						if(OOB(y,x)) {
							continue A;
						}
					}
					dir++;
					
				}
				
				rects.add(moveNums);
				
			}
		}
		
		return rects;
	}
	
	
	
	static int getDiff(int[] moveNums, int sy, int sx) {
		Pair rect[] = new Pair[4];
		HashSet<Pair> points = new HashSet<>();
		int dir = 0;
		
		for(int num : moveNums) {
			rect[dir] = new Pair(sy,sx);
			for(int i = 0; i<num; i++) {
				points.add(new Pair(sy,sx));
				sy = sy + dy[dir];
				sx = sx + dx[dir];
			}
			dir++;
		}
		
//		System.out.println(Arrays.toString(rect));
//		System.out.println(points);
//		System.out.println("-----");
		
		Pair bottom = rect[0];
		Pair right = rect[1];
		Pair top = rect[2];
		Pair left = rect[3];
//		System.out.println(points);
		int sums[] = new int[5];
		int total = 0;
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				total += board[y][x];
			}
		}
		
		for(int y=1; y<=left.y-1; y++) {
			for(int x=1; x<=top.x;x++) {
				if(points.contains(new Pair(y,x)))
					break;
				sums[1] += board[y][x];
			}
		}
		
		for(int x=n; x>= top.x + 1; x--) {
			for(int y=1; y<=right.y;y++) {
				if(points.contains(new Pair(y,x)))
					break;
				sums[2] += board[y][x];
			}
		}
		
		for(int y= n; y >= right.y+1; y--) {
			for(int x = n; x>= bottom.x; x--) {
				if(points.contains(new Pair(y,x)))
					break;
				sums[3] += board[y][x];
			}
		}
		
		for(int x=1; x <= bottom.x-1; x++) {
			for(int y = n ; y>= left.y; y--) {
				if(points.contains(new Pair(y,x)))
					break;
				sums[4] += board[y][x];
			}
		}
		
		
		
//		System.out.println(total);
		sums[0] = total - (sums[1] + sums[2] + sums[3] + sums[4]);
//		System.out.println(Arrays.toString(sums));
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		
		for(int i = 0; i<5;i++) {
			max = Math.max(max, sums[i]);
			min = Math.min(min,  sums[i]);
		}
		
//		System.out.println(Arrays.toString(sums));
//		System.out.println(Arrays.toString(moveNums));
//		System.out.println(Arrays.toString(rect));
//		System.out.println(max+" "+min+" "+(max-min));
//		System.out.println("-----");
		
		
		return max - min;
	}
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		board = new int[n+1][n+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++)
				board[y][x] = Integer.parseInt(st.nextToken());
		}
		MAX_R = n-2;
		int ans = Integer.MAX_VALUE;
		
		for(int y= 1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				ArrayList<int[]> allMoves = makeAllMoveNums(y,x);
				for(int[] moveNums : allMoves) {
					int diff = getDiff(moveNums, y,x);
//					System.out.println(diff);
					ans = Math.min(ans,  diff);
				}
				
			}
		}
		System.out.println(ans);
		
//		int y = 6;
//		int x = 4;
//		ArrayList<int[]> allMoves = makeAllMoveNums(y,x);
//		for(int[] moveNums : allMoves) {
//			System.out.println(Arrays.toString(moveNums));
//			int diff = getDiff(moveNums, y,x);
//			System.out.println(diff);
//		}
		
//		int moveNums[] = {2,2,2,2};
//		int diff = getDiff(moveNums, 6, 4);
		
		
		
		
	}
}