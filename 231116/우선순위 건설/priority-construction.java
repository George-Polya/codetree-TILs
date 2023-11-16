import java.io.*;
import java.util.*;

public class Main {
    static int n;
    static int inDegree[], dp[], time[];
    static StringTokenizer st;
    static List<Integer> adj[];
    public static void main(String[] args) throws IOException{
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(br.readLine());
        inDegree = new int[n+1];
        dp = new int[n+1];
        time = new int[n+1];
        adj = new List[n+1];
        for(int i =1 ; i<=n; i++){
            adj[i] = new ArrayList<>();
        }

        for(int i = 1; i<=n; i++){
            st = new StringTokenizer(br.readLine());
            int cnt = 0;
            while(st.hasMoreTokens()){
            	int value = Integer.parseInt(st.nextToken());
            	if(value == -1)
            		break;
            	if(cnt == 0) {
            		dp[i] = time[i] = value;
            	}else {
            		adj[value].add(i);
            		inDegree[i]++;
            	}
            	cnt++;
            }
        }
        
        Queue<Integer> q = new LinkedList<>();
        for(int i = 1; i<=n; i++) {
        	if(inDegree[i] == 0)
        		q.add(i);
        }
        
        while(!q.isEmpty()) {
        	int cur = q.poll();
        	
        	for(int nxt : adj[cur]) {
        		inDegree[nxt]--;
        		
        		dp[nxt] = Math.max(dp[nxt], dp[cur] + time[nxt]);
        		if(inDegree[nxt] == 0)
        			q.add(nxt);
        	}
        }
        
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i<=n; i++) {
        	sb.append(dp[i]).append('\n');
        }
        System.out.println(sb);
        
        
        
    }
}