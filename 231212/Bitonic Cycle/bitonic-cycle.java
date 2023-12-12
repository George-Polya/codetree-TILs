import java.io.*;
import java.util.*;

public class Main {
    static int n;
    static long dp[][];
    static StringTokenizer st;
    static class Pair implements Comparable<Pair>{
        int x,y;
        public Pair(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int compareTo(Pair p){
            return x - p.x;
        }
    }

    static Pair points[];
    static long INT_MAX = (long)1e16;

    static long dist(int i, int j){
        int x1 = points[i].x;
        int y1 = points[i].y;

        int x2 = points[j].x;
        int y2 = points[j].y;

        return (x1-x2) * (x1-x2) + (y1-y2)*(y1-y2);
    }

    public static void main(String[] args) throws IOException{
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        points = new Pair[n];
        for(int i = 0; i < n; i++){
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            points[i] = new Pair(x,y);
        }

        Arrays.sort(points);

        dp = new long[n][n];

        for(int i = 0; i < n; i++){
            Arrays.fill(dp[i], INT_MAX);
        }

        dp[0][0] = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j<n;j++){
                int next = Math.max(i,j) + 1;

                if(next == n)
                    continue;

                dp[i][next] = Math.min(dp[i][next], dp[i][j] + dist(j, next));
                dp[next][j] = Math.min(dp[next][j], dp[i][j] + dist(i, next));

            }
        }

        long ans = INT_MAX;
        for(int i = 0; i < n- 1;i++){
            ans = Math.min(ans, dp[i][n-1] + dist(i,n-1));
        }
        System.out.println(ans);
    }
}