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


	
	static boolean check(int idx, int nxt) {
		if(nxt == END)
			return false;
		
		for(int i = 0; i < 4; i++) {
			if(i == idx)
				continue;
			if(horses[i] == nxt)
				return true;
		}
		return false;
	}
	
	static int move(int cur, int count) {
		
		if(cur == END || count == 0)
			return cur;
		
		int nxt = cur + 1;
		
		if(cur == 24 || cur == 28 || cur == 34)
			nxt = 36;
		else if(cur == 38)
			nxt = 20;
		
		return move(nxt, count-1);
		
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
	
	static void solve(int cnt, int sum) {
		if(cnt == 10) {
//			System.out.printf("%s: %d\n", Arrays.toString(horses), sum);
			ans = Math.max(ans, sum);
			return;
		}
		
		for(int i = 0; i < 4; i++) {
			int cur = horses[i];
			int nxt = -1;
			if(cur == END)
				continue;
			
			if(cur != 0 && (cur == 5 || cur == 10 || cur == 15)) {
				nxt = move(cur + 17, counts[cnt] - 1);
			}else {
				nxt = move(cur, counts[cnt]);
			}
			
			if(!overlapped(i, nxt)) {
				horses[i] = nxt;
				solve(cnt + 1, sum + map[nxt]);
				horses[i] = cur;
			}
			
			horses[i] = cur;
		}
	}
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < 10 ; i++) {
        	counts[i] = Integer.parseInt(st.nextToken());
        }
        
        init();
        
//       System.out.println(move(0,0,horses[0],2));
//       System.out.println(Arrays.toString(horses));
//       
//       System.out.println(move(0,1,horses[1],1));
//       System.out.println(Arrays.toString(horses));
        
        solve(0,0);
        System.out.println(ans);
    }
}