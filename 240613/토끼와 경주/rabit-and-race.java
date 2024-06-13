import java.util.*;
import java.io.*;
public class Main {
	
	static int q,n,m,p;
	static class Rabbit{
		int jumpCnt, y,x,pid, score, d, idx;
		
		public Rabbit(int jumpCnt, int y,int x, int pid, int score, int d, int idx) {
			this.jumpCnt = jumpCnt;
			this.y = y;
			this.x = x;
			this.pid = pid;
			this.score = score;
			this.d = d;
			this.idx = idx;
		}
		
		public String toString() {
			return y+" "+x+" "+pid;
		}
	}
	
	static int INT_MAX = Integer.MAX_VALUE;
	static Rabbit NO_RABBIT = new Rabbit(INT_MAX,-1,-1,-1,-1,-1, -1);
	
	static Map<Integer,Integer> pid2Idx = new HashMap<>();
	
	static PriorityQueue<Rabbit> pq1 = new PriorityQueue<>((r1,r2)->{
		if(r1.jumpCnt != r2.jumpCnt)
			return r1.jumpCnt - r2.jumpCnt;
		if((r1.y + r1.x) != (r2.y+r2.x))
			return (r1.y + r1.x) - (r2.y + r2.x);
		if(r1.y != r2.y)
			return r1.y - r2.y;
		if(r1.x != r2.x)
			return r1.x - r2.x;
		return r1.pid - r2.pid;
	});
	
	static PriorityQueue<Rabbit> pq2 = new PriorityQueue<>((r1,r2)->{
		if((r1.y + r1.x) != (r2.y+r2.x))
			return  -((r1.y + r1.x) - (r2.y + r2.x));
		if(r1.y != r2.y)
			return -(r1.y - r2.y);
		if(r1.x != r2.x)
			return -(r1.x - r2.x);
		return -(r1.pid - r2.pid);
	});
	
	static StringTokenizer st;
	static Rabbit rabbits[];
	static void init() {
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		p = Integer.parseInt(st.nextToken());
		rabbits = new Rabbit[p];
		for(int i = 0; i < p; i++) {
			int pid = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			pid2Idx.put(pid,i);
			rabbits[i] = new Rabbit(0,1,1,pid,0,d,i);
			pq1.add(rabbits[i]);
		}
		
	}
	
	static class Pair{
		int y, x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public boolean isHigher(Pair p) {
			if( (y+x) != (p.y+p.x))
				return (p.y+p.x) < (y+x);
			if(y != p.y)
				return p.y < y;
			return p.x < x;
		}
		
		public String toString() {
			return y+" "+x;
		}
	}
	
	static Pair NO_POS = new Pair(-1,-1);
	
	static int dy[] = {-1,0,1,0};
	static int dx[] = {0,-1,0,1};
	
	static Pair getNxtPos(int y,int x, int dir, int dist) {
		int yDist = dist % (2*(n-1));
		int xDist = dist % (2*(m-1));
		
		Pair pair = null;
		
		switch(dir) {
		case 0:
		case 2:
			int ny = y + dy[dir] * yDist;
			while(ny<=0 || ny > n) {
				if(ny >n)
					ny = 2 * n - ny;
				else if(ny<=0)
					ny = 2 * (n-1) + ny;
			}
			pair = new Pair(ny,x);
			break;
		case 1:
		case 3:
			int nx = x + dx[dir] * xDist;
			while(nx <=0 || nx > m) {
				if(nx>m)
					nx = 2 * m - nx;
				else if(nx<=0)
					nx = 2 * (m-1) + nx;
			}
			pair = new Pair(y,nx);
			break;
		}
		return pair;
	}
	
	static Pair getBestPos(Rabbit rabbit) {
		int y = rabbit.y;
		int x = rabbit.x;
		int d = rabbit.d;
		Pair bestPos = NO_POS;
		
		for(int dir = 0; dir < 4; dir++) {
			Pair cur = getNxtPos(y,x,dir, d);
			if(cur.isHigher(bestPos))
				bestPos = cur;
		}
		
//		System.out.println("bestPos: "+bestPos);
		
		return bestPos;
	}
	
	static void update(int idx, Pair nxtPos) {
		rabbits[idx].y = nxtPos.y;
		rabbits[idx].x = nxtPos.x;
		rabbits[idx].jumpCnt+=1;
		pq1.add(rabbits[idx]);
	}
	
	static int totalSum;
	static void scoring(int idx) {
		rabbits[idx].score -= (rabbits[idx].y + rabbits[idx].x);
		totalSum += (rabbits[idx].y + rabbits[idx].x);
	}
	
	static void race(int k, int s) {
		pq2.clear();
		for(int turn=1; turn<=k; turn++) {
			
			// 우선순위가 가장 높은 토끼 
			Rabbit bestRabbit = pq1.poll();
			
			// bestRabbit가 이동할 bestPos
			Pair nxtPos = getBestPos(bestRabbit);
			
			if(nxtPos == NO_POS)
				continue;
			
			// 이동 
			update(bestRabbit.idx, nxtPos);
			
			// 다론 토끼 점수 획득 
			scoring(bestRabbit.idx);
			pq2.add(bestRabbit);
		}
		
		Rabbit rabbit = pq2.peek();
		rabbit.score += s;
		
		
	}
	
	static void changeDist(int pid, int l) {
		int idx = pid2Idx.get(pid);
		rabbits[idx].d *= l;
	}
	
	static void bestPerformance() {
		int _max = 0;
		int idx = -1;
		for(int i = 0; i < p;i++) {
			Rabbit rabbit = rabbits[i];
			if(_max < rabbit.score) {
				_max = rabbit.score;
				idx = i;
			}
		}
		System.out.println(_max+totalSum);
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		q = Integer.parseInt(br.readLine());
		
		for(int query = 1; query<=q; query++) {
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());
			
			switch(cmd) {
			case 100:{
				init();
				break;
			}
			case 200:{
				int k = Integer.parseInt(st.nextToken());
				int s = Integer.parseInt(st.nextToken());
				race(k,s);
				break;
			}
			case 300:{
				int pid = Integer.parseInt(st.nextToken());
				int l = Integer.parseInt(st.nextToken());
				changeDist(pid, l);
				break;
			}
			case 400:{
				bestPerformance();
				break;
			}
				
			}
		}
		
	}
}