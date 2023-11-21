import java.io.*;
import java.util.*;
public class Main {
    static int n;
    static String pattern;
    static StringTokenizer st;
    static int p[] = new int[]{31, 37};
    static int mod[] = new int[]{(int)1e9+7, (int)1e9+9};
    static long pPow[][];
    static long pH[];
    static int L;
    static int toInt(char ch){
        return ch - 'a' + 1;
    }
    static class Pair{
        int id;
        char value;
        public Pair(int id, char value){
            this.id = id;
            this.value = value;
        }
    }
    static ArrayList<Pair> adj[];
    // static long tH = new long[2];
    static int ans;
    public static long genUniqueKey(long h1, long h2) {
        return h1 * Math.max(mod[0], mod[1]) + h2;
    }

    static HashSet<Long> set = new HashSet<>();
    static void dfs(int cur, String str){
        int m = str.length();
        

        for(Pair nxt : adj[cur]){
            dfs(nxt.id, str + nxt.value);
        }

        if(m >= L && adj[cur].size() == 0){
            long tH[] = new long[2];

            for(int k = 0; k < 2; k++){
                for(int i = 0; i < L ; i++){
                    tH[k] = (tH[k] + toInt(str.charAt(i)) * pPow[k][L - i - 1]) % mod[k];
                }
            }

            
            if(tH[0] == pH[0] && tH[1] == pH[1])
                ans++;
            

            for(int i = 1; i<= m - L; i++){
                for(int k = 0; k < 2; k++){
                    tH[k] = ((tH[k] * p[k] - toInt(str.charAt(i-1)) * pPow[k][L]) + toInt(str.charAt(i + L - 1))) % mod[k];
                }

                if(tH[0] == pH[0] && tH[1] == pH[1])
                    ans++;
                
                
            }
        }
    }
    public static void main(String[] args) throws IOException{
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        pattern = st.nextToken();
        L = pattern.length();
        pPow = new long[2][n+1];
        for(int k =0 ; k< 2; k++){
            pPow[k][0] = 1;
            for(int i = 1; i<=n; i++){
                pPow[k][i] = (pPow[k][i - 1] * p[k]) % mod[k];
            }
        }

        pH = new long[2];
        for(int k = 0; k < 2; k++){
            for(int i = 0; i < L ; i++){
                pH[k] = (pH[k] + toInt(pattern.charAt(i)) * pPow[k][L - i - 1]) % mod[k];
            }
        }

        long key = genUniqueKey(pH[0], pH[1]);

        adj = new ArrayList[n+1];
        for(int i = 1; i<=n; i++)
            adj[i] = new ArrayList<>();

        for(int i = 0; i < n - 1; i++){
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            char value = st.nextToken().charAt(0);
            adj[a].add(new Pair(b, value));
        }

        dfs(1, "");
        
        System.out.println(ans);
    }
}