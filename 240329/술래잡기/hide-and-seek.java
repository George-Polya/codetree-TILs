import java.io.*;
import java.util.*;

public class Main{
	static int n,m,h,k;
	static StringTokenizer st;
	static class Tuple{
		int y,x,dir;
		public Tuple(int y,int x, int dir) {
			this.y = y;
			this.x = x;
			this.dir = dir;
		}
		
		public String toString() {
			return y+" "+x+" "+dir+"|";
		}
	}
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.print(board[y][x] + " ");
			}
			
			System.out.println();
		}
	}
	
	
	
	static int dy[] = {0,1,0,-1};
	static int dx[] = {1,0,-1,0};
	static boolean OOB(int y, int x) {
		return y<= 0|| y>n || x<=0 || x>n;
	}
	
	static Tuple police;
	static Tuple thieves[];
	static int board[][], nxtBoard[][];
	
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public boolean isSame(int y,int x) {
			return this.y == y && this.x == x;
		}
	}
	
	static Pair trees[];
	
	static void init() {
		for(int y=1; y<=n; y++) {
			Arrays.fill(nxtBoard[y], 0);
		}
		
		for(int i = 0; i < h; i++) {
			Pair tree = trees[i];
			nxtBoard[tree.y][tree.x] += (1 << 0);
		}
	}
	
	static void copy(int src[][], int dst[][]) {
		for(int y=1; y<=n; y++) {
			System.arraycopy(src[y],1, dst[y], 1, n);
		}
	}
	
	static Tuple getNxt(int y, int x, int dir) {
		int ny = y + dy[dir];
		int nx = x + dx[dir];
		
		if(!OOB(ny,nx)) {
			if(ny == police.y && nx == police.x) {
				return new Tuple(y,x,dir);
			}else {
				return new Tuple(ny,nx,dir);
			}
		}else {
			dir = (dir + 2) % 4;
			ny = y + dy[dir];
			nx = x + dx[dir];
			if(ny == police.y && nx == police.x) {
				return new Tuple(y,x,dir);
			}else {
				return new Tuple(ny,nx,dir);
			}
		}
	}
	
	static boolean canMove(int y1,int x1, int y2, int x2) {
		int dist = Math.abs(y1-y2) + Math.abs(x1-x2);
		return dist <= 3;
	}
	
	/*
	 * id에 해당하는 도둑의 이동 
	 */
	static void move(int id) {
		Tuple thief = thieves[id];
		if(thief == null)
			return;
		int y = thief.y;
		int x = thief.x;
		int dir = thief.dir;
		
		if(!canMove(y,x,police.y, police.x)) {
			nxtBoard[thief.y][thief.x] += (1<<id);
			return;
		}
			
		
		Tuple nxt = getNxt(y,x,dir);
		thief.y = nxt.y;
		thief.x = nxt.x;
		thief.dir = nxt.dir;
//		System.out.printf("id: %d, nxt: %s\n", id, nxt);		
		nxtBoard[thief.y][thief.x] += (1<<id);
	}
	
	static void moveAllThieves() {
		init();
		
		for(int id=1;id<=m;id++) {
			
			move(id);
		}
		
		copy(nxtBoard,board);
		
	}
	
	static int cnt = 2;
	static int num = 1;
	static int moveNum = num;
	static boolean flag = true;
	static Pair getNxtPolice(int y,int x,int dir) {
		int ny = y + dy[dir];
		int nx = x + dx[dir];
		return new Pair(ny,nx);
	}
	
	static void policeMove() {
		int y = police.y;
		int x = police.x;
		int dir = police.dir;
		
		Pair nxtPos = getNxtPolice(y,x,dir);
		police.y = nxtPos.y;
		police.x = nxtPos.x;
		
		moveNum -= 1;
		
		if(moveNum == 0) {
			dir = (dir + (flag ? 1 : 3)) % 4;
			police.dir = dir;
			moveNum = num;
			cnt-- ;
		}
		
		if(cnt == 0) {
			cnt = 2;
			num = num + (flag ? 1 : -1);
			moveNum = num;
		}
		
		if(nxtPos.isSame(1,1)) {
			dir = 1;
			police.dir = dir;
			num = num + (flag? -1 : +1);
			moveNum = num;
			flag = !flag;
			cnt += 1;
		}
		
		if(nxtPos.isSame(cy,cx)) {
			dir = 3;
			police.dir = dir;
			num = num + (flag? -1 : +1);
			System.out.println("num: "+num);
			moveNum = num;
			flag = !flag;
		}
	
		
	}
	
	static int cy, cx;
	static int score;
	static int turn = 1;
	static void arrest() {
		int y = police.y;
		int x = police.x;
		int dir = police.dir;
		int cnt = 0;
		for(int dist = 0; dist < 3; dist++) {
			int ny = y + dy[dir] * dist;
			int nx = x + dx[dir] * dist;
			if(OOB(ny,nx))
				continue;
			int value = board[ny][nx];
			
			if((value & 1) == 1) { // 나무가 있는 곳은 검거를 못함 
				continue;
			}
			
			for(int id = 1; id <= m; id++) {
				if((value & (1<<id)) != 0) {
					board[ny][nx] -= (1<<id);
					thieves[id] = null;
					cnt++;
				}
			}
		}
		
		score += turn * cnt;
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		h = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		cy = cx = n / 2 + 1;
		
		police = new Tuple(cy,cx,3);
		thieves = new Tuple[m+1];
		board = new int[n+1][n+1];
		nxtBoard = new int[n+1][n+1];
		for(int i=1;i<=m;i++) {
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			thieves[i] = new Tuple(y,x,dir-1);
			board[y][x] += (1 << i);
		}
		
		trees = new Pair[h];
		for(int i=0; i<h;i++) {
			st = new StringTokenizer(br.readLine());
			int y= Integer.parseInt(st.nextToken());
			int x= Integer.parseInt(st.nextToken());
			trees[i] = new Pair(y,x);
			board[y][x] += (1<<0);
		}
		
//		printBoard(board);
//		System.out.println(Arrays.toString(thieves));
//		System.out.println("------------");
//		moveAllThieves();
//		printBoard(board);
//		System.out.println(Arrays.toString(thieves));
//		System.out.println("------------");
//		policeMove();
//		arrest();
//		printBoard(board);
//		System.out.println(Arrays.toString(thieves));
//		System.out.println("------------");
		
		for(turn = 1; turn<=k; turn++) {
			moveAllThieves();
			policeMove();
			arrest();
//			printBoard(board);
//			System.out.println("police: "+police);
//			System.out.println(Arrays.toString(thieves));
//			System.out.println("------------");
		}
		
		System.out.println(score);
	}
}