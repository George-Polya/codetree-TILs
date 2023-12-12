import java.io.*;
import java.util.*;

public class Main {
    static int n;
    static StringTokenizer st;
    static int arr[];
    static int dp[][];
    static int INT_MAX = (int)2e9;
    static int dist(int i, int j){
        if( i == 0)
            return 0;
        
        return Math.abs(arr[i] - arr[j]);
    }
    public static void main(String[] args) throws IOException{
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        arr = new int[n+1];
        st = new StringTokenizer(br.readLine());
        for(int i = 1; i<=n; i++){
            arr[i] = Integer.parseInt(st.nextToken());
        }

        dp = new int[n+1][n+1];
        for(int i = 0; i <=n; i++){
            Arrays.fill(dp[i], INT_MAX);
        }

        dp[0][0] = 0;
        for(int i = 0;i <=n; i++){
            for(int j = 0; j<=n; j++){
                int next = Math.max(i,j)+1;
                if(next == n+1)
                    continue;

                dp[next][j] = Math.min(dp[next][j], dp[i][j] + dist(i,next));
                dp[i][next] = Math.min(dp[i][next], dp[i][j] + dist(j,next));
            }
        }

        int ans = INT_MAX;
        for(int i = 0; i <=n ; i++){
            ans = Math.min(ans, dp[i][n]);
        }
        System.out.println(ans);
    }
}