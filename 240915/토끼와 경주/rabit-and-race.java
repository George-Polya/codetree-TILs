import java.io.*;
import java.util.*;

public class Main {
	static int q,n,m,p;
	static StringTokenizer st;
	static class Rabbit{
		int jumpCnt, y,x,pid, d;
		public Rabbit(int jumpCnt, int y,int x,int pid, int d) {
			this.jumpCnt = jumpCnt;
			this.y = y;
			this.x = x;
			this.pid = pid;
			this.d = d;
		}
		
		public String toString() {
			return String.format(" pid : %d |(%d,%d)| jumpCnt: %d| d: %d", pid, y,x,jumpCnt,d);
		}
	}
	
	static long scores[];
	static ArrayList<Rabbit> rabbits;
	static Map<Integer, Integer> pid2Idx;
	static void init() {
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		p = Integer.parseInt(st.nextToken());
		
		pq1 = new PriorityQueue<>((a,b)->{
			if(a.jumpCnt != b.jumpCnt)
				return a.jumpCnt - b.jumpCnt;
			if((a.y + a.x) != (b.y + b.x))
				return (a.y + a.x) - (b.y+b.x);
			if(a.y != b.y)
				return a.y - b.y;
			if(a.x != b.x)
				return a.x - b.x;
			
			return a.pid - b.pid;
		});
		
		
		rabbits = new ArrayList<>();
		pid2Idx = new HashMap<>();
		scores = new long[p];
		
		for(int idx = 0; idx < p; idx++) {
			int pid = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			Rabbit rabbit = new Rabbit(0,1,1,pid,d);
			rabbits.add(rabbit);
			pq1.add(rabbit);
			pid2Idx.put(pid, idx);
		}
		
	}

	static PriorityQueue<Rabbit> pq1;
	static PriorityQueue<Rabbit> pq2;
	
	static class Pair implements Comparable<Pair>{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		
		public String toString() {
			return y+" "+x;
		}
		
		public int compareTo(Pair o) {
			if((y + x) != (o.y + o.x))
				return (o.y + o.x) - (y+x);
			if(y != o.y)
				return o.y - y;
			return o.x - x;
		}
		
	}
	
	static Pair NO_POS = new Pair(-1,-1);
	
	static int dy[] = {-1,1,0,0};
	static int dx[] = {0,0,-1,1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>m;
	}
	
	static Pair findBestPos(Rabbit rabbit) {
		int y = rabbit.y;
		int x = rabbit.x;
		int d = rabbit.d;
		
		Pair ret = NO_POS;
		
		for(int dir = 0; dir < 4; dir++) {
//			System.out.println("=====");
			int ny = y + dy[dir] * (d % (2 * (n-1)));
			int nx = x + dx[dir] * (d % (2 * (m-1)));
//			System.out.printf("before %d %d\n", ny,nx);
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
//			System.out.printf("after %d %d\n",ny,nx);
			
			Pair pair = new Pair(ny,nx);
			if(ret.compareTo(pair) > 0) {
				ret = pair;
			}
			
		}
		return ret;
	}
	
	static long total;
	
	static void race() {
		int k = Integer.parseInt(st.nextToken());
		int s = Integer.parseInt(st.nextToken());
		
		
		pq2 = new PriorityQueue<>((a,b)->{
			if((a.y + a.x) != (b.y + b.x) )
				return (b.y + b.x) - (a.y + a.x);
			if(a.y != b.y)
				return b.y - a.y;
			if(a.x != b.x)
				return b.x - a.x;
			return b.pid - a.pid;
		});
		
		
		for(int turn =1; turn <=k ;turn++) {
//			System.out.println("-----");
//			System.out.println("turn: "+turn);
//			System.out.println("pq1: "+pq1);
			
			Rabbit best = pq1.poll();
//			System.out.println("bestRabbit: "+best);
			
			Pair bestPos = findBestPos(best);
//			System.out.println("bestPos: "+bestPos);
			best.y = bestPos.y;
			best.x = bestPos.x;
			best.jumpCnt++;
			pq1.add(best);
			pq2.add(best);
			
			int idx = pid2Idx.get(best.pid);
			scores[idx] -= (bestPos.y + bestPos.x);
			total += (bestPos.y + bestPos.x);
		}
		

		int pid = pq2.peek().pid;
		int idx = pid2Idx.get(pid);
		scores[idx] += s;
	}
	
	static void changeDistance() {
		int pid = Integer.parseInt(st.nextToken());
		int l = Integer.parseInt(st.nextToken());
		
		int idx = pid2Idx.get(pid);
		Rabbit rabbit = rabbits.get(idx);
		rabbit.d *= l;
	}
	static long bestScore() {
		long ans = Long.MIN_VALUE;
		for(int i = 0; i < p; i++) {
			ans = Math.max(ans, scores[i]);
		}
		return ans + total;
	}
	
	public static void main(String[] args) throws IOException{
		System.setIn(new FileInputStream("./input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		q = Integer.parseInt(br.readLine());
		
		for(int t = 1; t <= q; t++) {
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());
			
			if(cmd == 100) {
				init();
			}else if(cmd == 200) {
				race();
			}else if(cmd == 300) {
				changeDistance();
			}else if(cmd == 400) {
				System.out.println(bestScore());
			}
		}
	}
}