import java.io.*;
import java.util.*;

public class Main {
    static int n,m;
    static StringTokenizer st;
    static ArrayList<Integer> adj[];
    static int inDegree[], dp[];
    static boolean visited[];
    static Queue<Integer> q = new LinkedList<>();
    static final int MOD = (int)1e9 + 7;
    public static void main(String[] args) throws IOException{
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        inDegree = new int[n+1];
        dp = new int[n+1];
        adj = new ArrayList[n+1];
        for(int i = 1; i<=n; i++)
            adj[i] = new ArrayList<>();

        for(int i = 0 ; i < m;i++){
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            adj[a].add(b);
            inDegree[b]++;
        }


        visited = new boolean[n+1];
        dp[1] = 1;
        q.add(1);

        while(!q.isEmpty()){
            int cur = q.poll();
            if(visited[cur])
                continue;
            visited[cur] = true;

            for(int nxt : adj[cur]){
                inDegree[nxt]--;
                dp[nxt] += (dp[cur] % MOD);
                if(inDegree[nxt] == 0)
                    q.add(nxt);
            }
        }

        System.out.println(dp[n]);

    }
}