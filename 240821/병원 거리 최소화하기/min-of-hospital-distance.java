import java.util.*;
import java.io.*;


public class Main {
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
//        board = new int[n+1][n+1];
        for(int y=1; y<=n; y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x=1; x<=n; x++) {
        		int value = Integer.parseInt(st.nextToken());
        		if(value == 1) {
        			people.add((y - 1) * n + x);
        		}else if(value == 2) {
        			hospitals.add((y - 1) * n + x);
        		}
        	}
        }
        
        selected = new int[m];
        solve(0,0);
        System.out.println(ans);
        
    }
    
    static int n,m;
    static StringTokenizer st;
    
    static List<Integer> people = new ArrayList<>();
    static List<Integer> hospitals = new ArrayList<>();
    static int selected[];
    static int ans = Integer.MAX_VALUE;
    
    static void solve(int cur,int cnt) {
    	if(cnt == m) {
    		
    		int sum = 0;
    		for(int person : people) {
    			int py = person % n == 0 ? person / n : person / n + 1;
        		int px = person % n == 0 ? person % n + n : person % n;
        		
        		sum += getMinDist(py,px);
    		}
    		
    		
    		ans = Math.min(ans, sum);
    		return;
    	}
    	
    	for(int i = cur; i < hospitals.size(); i++) {
    		selected[cnt] = hospitals.get(i);
    		solve(i + 1, cnt + 1);
    	}
    	
//    	if(cur == hospitals.size())
//    		return;
//    	
//    	selected[cnt] = hospitals.get(cur);
//		solve(cur + 1, cnt + 1);
//		selected[cnt] = 0;
		
		
//		solve(cur + 1, cnt);
    	
    }
    
    static int getMinDist(int py, int px) {
    	int ret = Integer.MAX_VALUE;
    	for(int h : selected) {
    		int hy = h % n == 0 ? h / n : h / n + 1;
    		int hx = h % n == 0 ? h % n + n : h % n;
    		ret = Math.min(ret,  getDistance(hy,hx,py,px));
    	}
    	return ret;
    }
    
    
    static int getDistance(int y1,int x1, int y2, int x2) {
    	return Math.abs(y1-y2) + Math.abs(x1-x2);
    }
 }