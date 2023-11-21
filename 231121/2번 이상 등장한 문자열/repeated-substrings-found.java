import java.io.*;
import java.util.*;
public class Main {
    static String text;
    static int n;
    static int mod[] = new int[]{(int)1e9 + 7, (int)1e9+9};
    static int p[] = new int[]{31, 37};
    static long pPow[][];
    static int toInt(char ch){
        return ch - 'a' + 1;
    }

    static long genUniqueKey(long h1, long h2){
        return h1 * Math.max(mod[0], mod[1]) + h2;
    }

    static boolean exist(int target){
        HashSet<Long> set = new HashSet<>();

        long h[] = new long[2];
        for(int k = 0; k < 2; k++){
            for(int i = 0; i < target; i++){
                h[k] = (h[k] + toInt(text.charAt(i)) * pPow[k][target - 1 - i]) % mod[k];
            }
        }

        set.add(genUniqueKey(h[0],h[1]));

        for(int i = 1; i<=n-target; i++){
            for(int k = 0; k < 2; k++){
                h[k] = (h[k] * p[k] - toInt(text.charAt(i-1)) * pPow[k][target]+ toInt(text.charAt(i + target - 1))) % mod[k] ;

                if(h[k] < 0)
                    h[k] += mod[k];
            }

            long key = genUniqueKey(h[0], h[1]);
            if(set.contains(key))
                return true;

            set.add(key);
        }
        return false;

    }
    public static void main(String[] args) throws IOException{
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        text = br.readLine();
        n = text.length();

        int l = 1, r = n, ans = 0;

        pPow = new long[2][n+1];
        for(int k = 0; k <2; k++){
            pPow[k][0] = 1;
            for(int i = 1; i<=n; i++)
                pPow[k][i] = (pPow[k][i-1] * p[k]) % mod[k];
        }

        while(l<=r){
            int mid = (l + r) / 2;

            if(exist(mid)){
                ans = mid;
                l = mid + 1;
            }else{
                r = mid - 1;
            }
        }

        System.out.println(ans);
    }
}