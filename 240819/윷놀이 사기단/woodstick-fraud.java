import java.util.*;
import java.io.*;

public class Main {
	static int counts[] = new int[10];
	static int map[] = new int[40];
	static StringTokenizer st;
	
	static int INT_MIN = Integer.MIN_VALUE;
	static int ans = INT_MIN;
	
	static int horses[] = new int[4];
	static void init() {
		for(int i = 1; i<=20;i++) {
			map[i] = i * 2;
		}
		
		map[22] = 13;
		map[23] = 16;
		map[24] = 19;
		
		map[27] = 22;
		map[28] = 24;
		
		map[32] = 28;
		map[33] = 27;
		map[34] = 26;
		
		map[36] = 25;
		map[37] = 30;
		map[38] = 35;
		
	}
	
	static int END = 21;

	// 재귀를 이용한 풀이 
	static int move(int cur, int count, int limit) {
		
		if(cur == END || count == limit)
			return cur;
		if(cur + limit <= 21)
			return cur + limit;
		int nxt = cur + 1;
		if(count == 0 && (cur == 5 || cur == 10 || cur == 15))
			nxt = cur + 17;
		else if(cur == 24 || cur == 28 || cur == 34)
			nxt = 36;
		else if(cur == 38)
			nxt = 20;
		
		return move(nxt, count+1, limit);
		
	}
	
	// 큐를 이용한 풀이 
	static int move(int cur, int limit) {
//		Queue<Integer> q = new LinkedList<>();
//		q.add(cur);
//		
//		int cnt = 0;
//		while(!q.isEmpty()) {
//			cur = q.poll();
//			if(cur == END || cnt == limit) {
//				return cur;
//			}
//			int nxt = cur + 1;
//			if(cnt == 0 && (cur == 5 || cur == 10 || cur == 15))
//				nxt = cur + 17;
//			else if(cur == 24 || cur == 28 || cur == 34)
//				nxt = 36;
//			else if(cur == 38)
//				nxt = 20;
//			cnt++;
//			q.add(nxt);
//		}
//		
//		return cur;
		
		int cnt = 0;
		while(true) {
			if(cur == END || cnt == limit)
				return cur;
			int nxt = cur + 1;
			if(cnt == 0 && (cur == 5 || cur == 10 || cur == 15))
				nxt = cur + 17;
			else if(cur == 24 || cur == 28 || cur == 34)
				nxt = 36;
			else if(cur == 38)
				nxt = 20;
			cnt++;
			cur = nxt;
		}
		
	}
	
	static boolean overlapped(int idx, int nxtPos) {
		if(nxtPos == END)
			return false;
		
		for(int i = 0; i < 4; i++) {
			if(i == idx)
				continue;
			int pos = horses[i];
			if(pos == END)
				continue;
			if(pos == nxtPos)
				return true;
		}
		
		return false;
	}
	
	static void solve(int cur, int sum) {
		if(cur == 10) {
//			System.out.printf("%s: %d\n", Arrays.toString(horses), sum);
			ans = Math.max(ans, sum);
			return;
		}
		
		for(int i = 0; i < 4; i++) {
			int pos = horses[i];
			if(pos == END)
				continue;
			
//			int nxt = move(pos, 0, counts[cur]);
			int nxt = move(pos, counts[cur]);
			if(!overlapped(i, nxt)) {
				horses[i] = nxt;
				solve(cur + 1, sum + map[nxt]);
				horses[i] = pos;
			}
		}
	}
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < 10 ; i++) {
        	counts[i] = Integer.parseInt(st.nextToken());
        }
        
        init();
        
        solve(0,0);
        System.out.println(ans);
    }
}