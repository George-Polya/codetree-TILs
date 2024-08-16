import java.util.*;
import java.io.*;

public class Main {
    static class TourPakcage implements Comparable<TourPakcage>{
        int id;
        int sales;
        int dest;
        int profit;
        public TourPakcage(int id, int sales, int dest, int profit){
            this.id = id;
            this.sales = sales;
            this.dest = dest;
            this.profit = profit;
        }
        public int compareTo(TourPakcage tp){
            if(this.profit != tp.profit) return tp.profit - this.profit;
            return this.id - tp.id;
        }
    }
    static int Q,N,M;
    static int INF = 0xFFFFFFF;
    static int[][] map;
    static int[] dist;
    static boolean[] isTourPackageExist;
    static boolean[] isTourPakcageCancled;
    static int start;
    static PriorityQueue<TourPakcage> pq = new PriorityQueue<>();

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());

        Q = Integer.parseInt(st.nextToken());

        isTourPackageExist = new boolean[2000];
        isTourPakcageCancled = new boolean[2000];
        map = new int[2000][30001];

        for(int q = 0; q < Q; q++){
            st = new StringTokenizer(br.readLine());

            int cmd = Integer.parseInt(st.nextToken());

            if(cmd == 100){
                N = Integer.parseInt(st.nextToken());
                M = Integer.parseInt(st.nextToken());
                init();
                for(int i = 0; i < M; i++){
                    int u = Integer.parseInt(st.nextToken());
                    int v = Integer.parseInt(st.nextToken());
                    int w = Integer.parseInt(st.nextToken());
                    buildLandMark(u,v,w);
                }

            } else if(cmd == 200){
                int id = Integer.parseInt(st.nextToken());
                int sales = Integer.parseInt(st.nextToken());
                int dest = Integer.parseInt(st.nextToken());
                addTourPackage(id, sales, dest);
            } else if(cmd == 300){
                int id = Integer.parseInt(st.nextToken());
                cancelTourPakcage(id);
            } else if(cmd == 400){
                System.out.println(sellTourPakcage());
            } else if(cmd == 500){
                int startCity = Integer.parseInt(st.nextToken());
                changeCity(startCity);
            }

        }

    }

    private static void init(){
        for(int i = 0; i < N; i++){
            Arrays.fill(map[i], INF);
            map[i][i] = 0;
        }
    }


    // 그냥 다익스트라
    private static void dijkstra(){
        boolean[] visited = new boolean[N];
        Arrays.fill(dist, INF);
        dist[start] = 0;

        for(int i = 0; i < N; i++){
            int v = 0, minDist = INF;
            for(int j = 0; j < N; j++){
                if(!visited[j] && minDist > dist[j]){
                    v = j;
                    minDist = dist[j];
                }
            }
            visited[v] = true;
            for(int j = 0; j < N; j++){
                if(!visited[j] && dist[v] != INF && map[v][j] != INF && dist[j] > dist[v] + map[v][j]){
                    dist[j] = dist[v]+map[v][j];
                }
            }
        }

    }

    private static void buildLandMark(int u, int v, int w){
        map[u][v] = Math.min(map[u][v], w);
        map[v][u] = Math.min(map[v][u], w);
    }

    private static void addTourPackage(int id, int sales, int dest){
        isTourPackageExist[id] = true;
        int benefit = sales - dist[dest];
        pq.offer(new TourPakcage(id, sales, dest, benefit));
    }

    private static void cancelTourPakcage(int id){
        if(isTourPackageExist[id]) isTourPakcageCancled[id] = true;
    }

    private static int sellTourPakcage(){
        while (!pq.isEmpty()){
            if(pq.peek().profit < 0) break;

            TourPakcage tp = pq.poll();

            if(!isTourPakcageCancled[tp.id]) return tp.id;
        }
        return -1;
    }

    private static void changeCity(int startCity){
        start = startCity;
        dijkstra();
        List<TourPakcage> pakcages = new ArrayList<>();

        while (!pq.isEmpty()){
            pakcages.add(pq.poll());
        }

        for(TourPakcage tp: pakcages){
            addTourPackage(tp.id, tp.sales, tp.dest);
        }
    }
}