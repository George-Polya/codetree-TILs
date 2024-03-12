import java.io.*;
import java.util.*;
public class Main {
	static int l,n,q;
	static StringTokenizer st;
	static int board[][];
	static class Knight{
		int sy,sx,h,w,ey,ex;
		int hp;
		int damage = -1;
		public Knight(int sy, int sx, int h, int w, int hp) {
			this.sy = sy;
			this.sx = sx;
			this.h = h;
			this.w = w;
			
			this.ey = sy + h - 1;
			this.ex = sx + w - 1;
			this.hp = hp;
			this.damage = 0;
		}
		
		public String toString() {
			return sy+" "+sx+" "+damage+"|";
		}
	}
	static Knight knights[];
	static Knight NO_KNIGHT = new Knight(-1,-1,-1,-1,-1);
	static int WALL = 2;
	static int TRAP = 1;
	static int dy[] = {-1,0,1,0};
	static int dx[] = {0,1,0,-1};
	static boolean OOB(int y,int x) {
		return y<= 0|| y>l || x<=0 || x>l;
	}
	
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public String toString() {
			return y+" "+x+"|";
		}
	}
	
	static void addQ(Knight knight, int dir, Queue<Pair> q) {
		if(knight == NO_KNIGHT)
			return;
		
		int sy = knight.sy;
		int sx = knight.sx;
		int ey = knight.ey;
		int ex = knight.ex;
		
		if(dir == 0) {
			for(int x= sx; x<=ex; x++)
				q.add(new Pair(sy,x));
		}else if(dir == 1) {
			for(int y=sy;y<=ey;y++) {
				q.add(new Pair(y,ex));
			}
		}else if(dir == 2) {
			for(int x= sx;x<=ex;x++) {
				q.add(new Pair(ey,x));
			}
		}else if(dir == 3) {
			for(int y = sy; y<=ey; y++)
				q.add(new Pair(y,sx));
		}
	}
	
	static boolean inKnight(Knight knight, int y,int x) {
		int sy = knight.sy;
		int sx = knight.sx;
		int ey = knight.ey;
		int ex = knight.ex;
		
		return sy <= y && y<= ey && sx<=x && x<=ex;
	}
	
	static int getNxtKnight(int y, int x) {
		for(int id =1; id<=n; id++) {
			if(knights[id] == NO_KNIGHT)
				continue;
			
			//생존한 기사들 중에서 
			if(inKnight(knights[id], y, x))
				return id;
		}
		return -1;
	}
	
	/*
	 * id에 해당하는 기사를 dir로 이동시킬 수 있는지 
	 * 이동가능하다면 이동되는 기사들의 id를 리턴
	 * 이동불가능하다면 empty list를 리턴 
	 */
	static List<Integer> canMove(int id, int dir) {
		Knight knight = knights[id];
		
		// 현재 기사가 죽은 기
		if(knight == NO_KNIGHT)
			return new ArrayList<>();
		
		Queue<Pair> q = new LinkedList<>();
		addQ(knight, dir, q); // 
		List<Integer> ret = new ArrayList<>();
		ret.add(id);
		while(!q.isEmpty()) {
			Pair cur = q.poll();
			int ny = cur.y + dy[dir];
			int nx = cur.x + dx[dir];
			
			if(OOB(ny,nx) || board[ny][nx] == WALL) // 벽을 만나게 되면 이동 불
				return new ArrayList<>();
			
			int nxtId = getNxtKnight(ny,nx);
			if(nxtId != -1) {
				ret.add(nxtId);
				addQ(knights[nxtId], dir, q);
			}
		}
		return ret;
	}
	
	static void move(int dir, int id) {
		Knight knight = knights[id];
		if(knight == NO_KNIGHT)
			return;
		int sy = knight.sy;
		int sx = knight.sx;
		int h = knight.h;
		int w = knight.w;
		int hp = knight.hp;
		
		sy = sy + dy[dir];
		sx = sx + dx[dir];
		
		int damage = knight.damage;
		
		knights[id] = new Knight(sy,sx,h,w, hp);
		knights[id].damage = damage;
	}
	
	static void calcDamage(int id) {
		Knight knight = knights[id];
		if(knight == NO_KNIGHT)
			return;
		int sy = knight.sy;
		int sx = knight.sx;
		int ey = knight.ey;
		int ex = knight.ex;
		int hp = knight.hp;
		
		
		for(int y=sy;y<=ey;y++) {
			for(int x=sx; x<=ex;x++) {
				if(board[y][x] == TRAP)
					knight.damage++;
			}
		}
		
		
		if(hp <= knight.damage)
			knights[id] = NO_KNIGHT;
	}
	
	static void moveAll(int cur,int dir, List<Integer> knightList) {
		
		
		// 이동 
		for(int id : knightList)
			move(dir, id);
		
		// 피해량 계산 
		for(int id : knightList) {
			if(id == cur)
				continue;
		    calcDamage(id);
		}
		
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		l = Integer.parseInt(st.nextToken());
		n = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());
		
		board = new int[l+1][l+1];
		for(int y=1; y<=l; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=l; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		knights = new Knight[n+1];
		for(int id=1; id<=n; id++) {
			st = new StringTokenizer(br.readLine());
			int sy = Integer.parseInt(st.nextToken());
			int sx = Integer.parseInt(st.nextToken());
			
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int hp = Integer.parseInt(st.nextToken());
			
			knights[id] = new Knight(sy,sx,h,w,hp);
			
		}
		
		int sum = 0;
		for(int i = 0; i <q ;i++) {
			st = new StringTokenizer(br.readLine());
			int id = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			
			List<Integer> knightList = canMove(id, dir);
			if(!knightList.isEmpty()) { // 현재 기사가 이동할 수 있는 경우 
				// 역순으로 이동한다.
				// 이동하면서 각 기사들은 데미지를 입는다.
				// 단, 명령을 받은 기사는 데미지를 입지 않는다. 
				moveAll(id, dir, knightList);
			}
		}
		
		for(int id = 1; id<=n;id++) {
			Knight knight = knights[id];
			if( knight == NO_KNIGHT)
				continue;
			
			sum += knight.damage;
		}
		System.out.println(sum);
	}

}