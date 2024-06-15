import java.util.*;
import java.io.*;
public class Main {
	static StringTokenizer st;
	static int n,m,q,p;
	
	static class Rabbit{
		int jumpCnt; // 총 점프횟수
		int y,x; // 현재 서있는 좌표 
		int pid; // 고유 번호 
		int dist; // 이동해야하는 거리 
		
		public Rabbit(int pid, int y, int x, int dist) {
			this.pid = pid;
			this.y = y;
			this.x = x;
			this.dist = dist;
		}
		
		public String toString() {
			return "id: "+pid+" "+y+" "+x;
		}
	}
	
	static Map<Integer, Integer> pid2Idx = new HashMap<>();
	static Rabbit rabbits[];
	static PriorityQueue<Rabbit> pq1 = new PriorityQueue<>((p1,p2)->{
		if(p1.jumpCnt != p2.jumpCnt)
			return p1.jumpCnt - p2.jumpCnt;
		if((p1.y+p1.x) != (p2.y+p2.x))
			return (p1.y+p1.x)- (p2.y+p2.x);
		if(p1.y != p2.y)
			return p1.y - p2.y;
		if(p1.x != p2.x)
			return p1.x - p2.x;
		return p1.pid - p2.pid;
	});
	
	static void init() {
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		p = Integer.parseInt(st.nextToken());
		rabbits = new Rabbit[p];
		scores = new int[p];
		for(int idx = 0; idx<p; idx++) {
			int pid = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			rabbits[idx] = new Rabbit(pid, 1,1,d);
			pid2Idx.put(pid, idx);
			pq1.add(rabbits[idx]);
		}
	}
	
	static int totalSum=0;
	static int scores[];
	
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public boolean isHigher(Pair p) {
			if((y+x) != (p.y + p.x))
				return (y+x) > (p.y + p.x);
			if(y != p.y)
				return y > p.y;
			return x > p.x;
		}
		
		public String toString() {
			return y+" "+x;
		}
	}
	
	static PriorityQueue<Rabbit> pq2 = new PriorityQueue<>((r1,r2)->{
		if((r1.y + r1.x) != (r2.y + r2.x))
			return (r2.y + r2.x) - (r1.y + r1.x);
		if(r1.y != r2.y)
			return r2.y - r1.y;
		if(r1.x != r2.x)
			return r2.x - r1.x;
		return r2.pid - r1.pid;
	});
	
	static Pair NO_POS = new Pair(-1,-1);
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	
	static boolean OOB(int y,int x) {
		return y<= 0|| y>n || x<=0 || x>m;
	}
	
	static Pair getNxtPos(int y,int x,int dist,int dir) {
		int yDist = dist % (2 * (n-1));
		int xDist = dist % (2 * (m-1));
		
		int ny = y + dy[dir] * yDist;
		int nx = x + dx[dir] * xDist;
		
//			
//		System.out.println("~~~~~");
//		System.out.printf("ny: %d, nx:%d\n", ny,nx);
		while(OOB(ny,nx)) {
			if(ny <= 0)
				ny = 2 - ny;
			else if(ny > n)
				ny = 2 * n - ny;
			
			if(nx <= 0)
				nx = 2 - nx;
			else if(nx > m)
				nx = 2 * m - nx;
		}
		
//		System.out.printf("ny: %d, nx:%d\n", ny,nx);
		return new Pair(ny,nx);
		
	}
	
	static Pair findBestNxtPos(Rabbit rabbit) {
		int y = rabbit.y;
		int x = rabbit.x;
		int dist = rabbit.dist;
		
		Pair ret = NO_POS;
		
		for(int dir = 0; dir < 4; dir++) {
			Pair nxt = getNxtPos(y,x,dist, dir);
			if(nxt.isHigher(ret))
				ret = nxt;
		}
		
		return ret;
	}
	
	
	// 경기 시작 
	static void race(int k, int s) {
		// k턴 동안 
		// 우선 순위가 가장 높은 토끼(best) 선택 
		// 그 토끼의 다음 위치 구해서 이동 
		// 선택된 토끼외에 다른 토끼들이 점수 획득 
		// k턴 진행 후, 새로 우선순위를 골라서 S더함. 이 때 best로 선택된 적이 있는 토끼들에 대해서만 수  
		for(int turn = 1; turn<=k; turn++) {
//			System.out.println("------");
//			System.out.println("turn: "+turn);
			Rabbit bestRabbit = pq1.poll(); // 우선순위가 가장 높은 토끼 찾기
//			System.out.println("bestRabbit: "+bestRabbit);
			int idx = pid2Idx.get(bestRabbit.pid);
			Pair nxtPos = findBestNxtPos(bestRabbit);
//			System.out.println("nxtPos: " +nxtPos);
			
			// 실제 이동 
			bestRabbit.y = nxtPos.y;
			bestRabbit.x = nxtPos.x;
			bestRabbit.jumpCnt++;
			
			totalSum += (bestRabbit.y + bestRabbit.x);
			scores[idx] -= (bestRabbit.y + bestRabbit.x);
			pq1.add(bestRabbit); // 동일한 토끼가 여러번 선택될 수 있음 
			pq2.add(bestRabbit); // 
		}
		
		// k 턴 모두 진행 
		Rabbit rabbit = pq2.poll();
		int idx = pid2Idx.get(rabbit.pid);
		scores[idx] += s;
	}
	
	
	static void changeDistance(int pid, int l) {
		int idx = pid2Idx.get(pid);
		rabbits[idx].dist *= l;
	}
	
	static int maxScore() {
		int ret = 0;
		for(int i = 0; i<p;i++) {
			ret = Math.max(ret, scores[i] + totalSum);
		}
		return ret;
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		q = Integer.parseInt(br.readLine());
		for(int query=1; query<=q; query++) {
//			System.out.println("=======");
			st = new StringTokenizer(br.readLine());
			int cmd=  Integer.parseInt(st.nextToken());
			if(cmd == 100) {
				init();
			}else if(cmd==200) {
				int k = Integer.parseInt(st.nextToken());
				int s = Integer.parseInt(st.nextToken());
				race(k,s);
			}else if(cmd ==300) {
				int pid  = Integer.parseInt(st.nextToken());
				int l = Integer.parseInt(st.nextToken());
				changeDistance(pid, l);
			}else if(cmd == 400) {
				System.out.println(maxScore());
			}
		}
	}
}