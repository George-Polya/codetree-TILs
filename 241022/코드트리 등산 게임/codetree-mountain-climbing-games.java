import java.io.*;
import java.util.*;
public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        for(int i = 0; i <n;i++) {
        	st = new StringTokenizer(br.readLine());
        	int cmd = Integer.parseInt(st.nextToken());
        	if(cmd == 100) {
        		init();
        	}else if(cmd == 200) {
        		int h = Integer.parseInt(st.nextToken());
        		int idx = search(h);
        		
        		index.add(idx);
        		
        		if(idx == mount.size())
        			mount.add(new ArrayList<>());
        		mount.get(idx).add(h);
        		
        	}else if(cmd == 300) {
                int idx = index.remove(index.size() - 1);
                mount.get(idx).remove(mount.get(idx).size() - 1);
                if (mount.get(mount.size() - 1).size() == 0)
                    mount.remove(mount.size() - 1);
        		
        	}else if(cmd == 400) {
                int m_idx = Integer.parseInt(st.nextToken()) - 1;
                System.out.println((index.get(m_idx) + mount.size()) * 1000000 + mount.get(mount.size() - 1).get(0));
        	}
        }
    }
    
    static void init() {
    	n = Integer.parseInt(st.nextToken());
    	for(int i = 0; i <n;i++) {
    		int h = Integer.parseInt(st.nextToken());
    		int idx = search(h);
    		index.add(idx);
    		
    		if(idx == mount.size())
    			mount.add(new ArrayList<>());
    		mount.get(idx).add(h);
    	}
    }
    
    static int search(int h) {
    	if(mount.size() == 0)
    		return 0;
    	int l = 0;
    	int r = mount.size() - 1;
    	int ret = mount.size();
    	while(l <= r) {
    		int mid = (l + r) / 2;
    		if(h <= mount.get(mid).get(mount.get(mid).size() - 1)) {
    			ret = mid;
    			r = mid - 1;
    		}{
    			l = mid + 1;
    		}
    	}
    	return ret;
    }
    static ArrayList<ArrayList<Integer>> mount = new ArrayList<>(); 
    static ArrayList<Integer> index = new ArrayList<>();
    static int n;
    static StringTokenizer st;
}