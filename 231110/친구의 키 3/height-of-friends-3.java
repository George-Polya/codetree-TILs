import java.io.*;
import java.util.*;
public class Main {
    static int n,m;
    static StringTokenizer st;
    static int inDegree[];
    static boolean visited[];
    static ArrayList<Integer> adj[];

    public static void main(String[] args) throws IOException{
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        adj = new ArrayList[n+1];
        inDegree = new int[n+1];
        visited = new boolean[n+1];
        for(int i = 1; i<=n; i++){
            adj[i] = new ArrayList<>();
        }

        for(int i = 0; i <m ;i++){
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            adj[a].add(b);
            inDegree[b]++;
        }

        // Queue<Integer> q = new LinkedList<>();
        PriorityQueue<Integer> q = new PriorityQueue<>();
        for(int i = 1; i<=n; i++){
            Collections.sort(adj[i]);
            if(inDegree[i] == 0)
                q.add(i);
        }

        StringBuilder sb = new StringBuilder();
        while(!q.isEmpty()){
            
            int cur = q.poll();
            visited[cur] = true;
            sb.append(cur).append(' ');
            for(int nxt: adj[cur]){
                inDegree[nxt]--;    
                if(inDegree[nxt] == 0)
                    q.add(nxt);
            }
        }

        boolean cycle = false;
        for(int i = 1; i<=n; i++){
            if(!visited[i])
                cycle = true;
        }

        System.out.println(cycle ? -1 : sb);

    }
}