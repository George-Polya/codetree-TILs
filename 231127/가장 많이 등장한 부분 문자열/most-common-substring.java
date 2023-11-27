import java.util.*;
import java.io.*;
public class Main {
	static int p[] = {31,39};
	static int mod[] = {(int)1e9 + 7, (int)1e9+9};
	static long pPow[][];
	
	static int n,l;
	static int toInt(char ch) {
		return ch - 'a' + 1;
	}
	static String text;
	static StringTokenizer st;
	
	static long genUniqueKey(long h1, long h2) {
		return h1 * Math.max(mod[0], mod[1]) + h2;
	}
	
	static HashMap<Long, Integer> map = new HashMap<>();
	
    public static void main(String[] args) throws IOException{
//        String S = "abcd"; // 주어진 문자열
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	st = new StringTokenizer(br.readLine());
    	l = Integer.parseInt(st.nextToken());
    	text = st.nextToken();
    	n = text.length();
    	
    	pPow = new long[2][n + 1];
    	for(int k = 0; k <2 ; k++) {
    		pPow[k][0] = 1;
    		for(int i = 1; i<=n; i++) {
    			pPow[k][i] = (pPow[k][i-1] * p[k]) % mod[k];
    		}
    	}
    	
    	long tH[] = new long[2];
    	
    	for(int k = 0; k <2 ;k++) {
    		for(int i = 0; i < l; i++) {
    			tH[k] = (tH[k]  + toInt(text.charAt(i)) * pPow[k][l - i -1]) % mod[k];
    		}
    	}
    	
    	long key = genUniqueKey(tH[0], tH[1]);
    	map.put(key, map.getOrDefault(key, 0) + 1);
    	
    	for(int i = 1; i <= n - l; i++) {
    		for(int k = 0; k < 2; k++) {
    			tH[k] = (tH[k] * p[k] - toInt(text.charAt(i-1)) * pPow[k][l] + toInt(text.charAt(i + l - 1))) % mod[k];
    			
    			if(tH[k] <= 0)
    				tH[k] += mod[k];
    		}
    		
    		key = genUniqueKey(tH[0], tH[1]);
    		map.put(key, map.getOrDefault(key, 0) + 1);
    	}
    	
    	
    	int ans = 0;
    	for(long k : map.keySet()) {
    		ans = Math.max(ans, map.get(k));
    	}
    	
    	System.out.println(ans);
        	
    }

  
}