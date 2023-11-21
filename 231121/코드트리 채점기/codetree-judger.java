import java.io.*;
import java.util.*;

public class Main {
	static int q,n;
	static int MAX_D = 300;
	static StringTokenizer st;
	static int cnt = 0;
	static HashMap<String, Integer> domain2Code = new HashMap<>();
	static PriorityQueue<Integer> restMachine = new PriorityQueue<>();
	static HashSet<Integer> isInReadyQ[] = new HashSet[MAX_D+1];
	static PriorityQueue<Task> pq[] = new PriorityQueue[MAX_D+1];
	static int INF = Integer.MAX_VALUE;
	static int end[] = new int[MAX_D+1];
	static int start[] = new int[MAX_D+1];
	static int judging[];
	static class Task implements Comparable<Task>{
		int p, time, id;
		public Task(int time, int p, int id) {
			this.p = p;
			this.time = time;
			this.id = id;
		}
		
		public int compareTo(Task task) {
			if(p != task.p)
				return p - task.p;
			return time - task.time;
		}
		public String toString() {
			return String.format("%d %d %d", time, p, id);
		}
	}
	static int ans;
	static Task NO_TASK;
	static void init(StringTokenizer st) {
		
		for(int i = 1;i <= MAX_D; i++) {
			isInReadyQ[i] = new HashSet<Integer>();
			pq[i] = new PriorityQueue<Task>();
		}
		
		n = Integer.parseInt(st.nextToken());
		judging = new int[n+1];
		
		for(int i = 1; i<=n; i++)
			restMachine.add(i);
		NO_TASK = new Task((int)1e6 + 1, n+1, -1);
		String url = st.nextToken();
		StringTokenizer st2 = new StringTokenizer(url, "/");
		String domain = st2.nextToken();
		int id = Integer.parseInt(st2.nextToken());
//		System.out.printf("%d %s\n", n, url);
		
		if(!domain2Code.containsKey(domain)) {
			cnt++;
			domain2Code.put(domain, cnt);
		}
		int domainCode = domain2Code.get(domain);
		
		isInReadyQ[domainCode].add(id);
		
		Task task = new Task(0, 1, id);
		pq[domainCode].add(task);
		
		ans++;
		
		
	}
	
	static void add(StringTokenizer st) {
		int t = Integer.parseInt(st.nextToken());
		int p = Integer.parseInt(st.nextToken());
		String url = st.nextToken();
		StringTokenizer st2 = new StringTokenizer(url,"/");
		String domain = st2.nextToken();
		int id = Integer.parseInt(st2.nextToken());
//		System.out.printf("%d %d %s\n", t, p, url);
		
		if(!domain2Code.containsKey(domain)) {
			cnt++;
			domain2Code.put(domain, cnt);
		}
		int domainCode = domain2Code.get(domain);
		
		
		// 채점 대기큐에 있는 task 중 정확히 u와 일치하는 url이 단 하나라도 존재한다면 추가하지 않음 
		if(isInReadyQ[domainCode].contains(id))
			return;
		isInReadyQ[domainCode].add(id);
		Task task = new Task(t, p, id);
		pq[domainCode].add(task);
		
		ans++;
		
	}
	
	/**
	 * 1. 채점 대기큐에서 채점가능한 task 찾기 
	 *  1.1 현재 채점 중인 도메인이면 안됨 
	 *  1.2 t < start + 3 * gap이면 안됨 
	 * 2. 채점 가능한 task 중에서 우선순위가 가장 높은 task 찾기 
	 *  2.1 각 도메인의 pq에서 가장 우선순위가 높은 task
	 * 3. 우선순위가 가장 높은 task 처리 
	 *  3.1 pq에서 해당 task제거하고
	 *  3.2 isInReadyQ에서도 해당 task 제거하고 
	 *  3.2 start 배열과 end 배열갱신(채점 중임을 표시)
	 *  3.3 judging처리 
	 * @param st
	 */
	
	static void score(StringTokenizer st) {
		int t = Integer.parseInt(st.nextToken());
//		System.out.printf("%d\n", t);
		if(restMachine.isEmpty())
			return;
		
		int domainCode = -1;
		Task best = NO_TASK;
		
		for(int code = 1; code <= cnt; code++) {
			
			if(t < end[code])
				continue;
			
			if(pq[code].isEmpty())
				continue;
			
			Task task = pq[code].peek();
			
			if( (best.p > task.p) || (best.p == task.p && best.time > task.time) ) {
				domainCode = code;
				best = task;
			}
			
		}
		
		if(domainCode != -1) {
			pq[domainCode].poll(); // pq에서 해당 테스크 제거 
			isInReadyQ[domainCode].remove(best.id); // 채점 대기큐에서 해당 태스크 제거 
			
			start[domainCode] = t; // 채점 시작 시간 표시 
			end[domainCode] = INF; // 언제가 limit인지 표시. 아직 끝나지 않았으므로 INF 
			
			
			int machineIdx = restMachine.poll(); 
			judging[machineIdx] = domainCode;
			
			
			
			ans--; // 채점 대기큐에서 task가 제거되었으므로 ans도 줄어
			
		}
	}
	
	/**
	 * 채점 종료 
	 * 1. jid 채점기가 진행하던 채점이 없었다면 이 명령은 무시 
	 * 2. domainCode가져오기 
	 * 3. 다시 쉬는 상태가 됨 : judging[jid] = 0, restMachine.add(jid)
	 * 4. start와 t로 end 계산  
	 * @param st
	 */
	
	static void terminate(StringTokenizer st) {
		int t = Integer.parseInt(st.nextToken());
		int jid = Integer.parseInt(st.nextToken());
//		System.out.printf("%d %d\n", t, jid);
		
		// jid채점기가 진행하던 채점이 없으면 이 명령 무시 
		if(judging[jid] == 0)
			return;
		
		int domainCode = judging[jid];
		judging[jid] = 0;
		restMachine.add(jid);
		
		int startTime = start[domainCode];
		int gap = t - startTime;
		end[domainCode] = startTime + 3 * gap;
		
		
		
		
		
	}
	
	static StringBuilder sb = new StringBuilder();
	static void print(StringTokenizer st) {
//		System.out.printf("%d\n", ans);
		sb.append(ans).append('\n');
	}
	
	public static void main(String[] args) throws IOException{
//		System.setIn(new FileInputStream("./input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		q = Integer.parseInt(br.readLine());
		for(int i = 0; i < q; i++) {
			st = new StringTokenizer(br.readLine());
			int op = Integer.parseInt(st.nextToken());
//			System.out.print(op+" ");
			switch(op) {
			case 100:
				init(st);
				break;
			case 200:
				add(st);
				break;
			case 300:
				score(st);
				break;
			case 400:
				terminate(st);
				break;
			case 500:
				print(st);
				break;
			}
//			System.out.println("-----");
		}
		System.out.println(sb);
	}
}