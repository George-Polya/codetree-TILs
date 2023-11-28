import java.io.*;
import java.util.*;

public class Main{
	static String text;
	static int n;
	static StringTokenizer st;
	
	static int mod[] = {(int)1e9 + 7, (int)1e9+9};
	static int p[] = {31, 37};
	static long pPow[][];
	static int toInt(char ch) {
		return ch - 'a' + 1;
	}
	
	static long genUniqueKey(long h1, long h2) {
		return h1 + Math.max(mod[0], mod[1]) * h2;
	}
	
	static boolean check(int len) {
		HashSet<Long> set = new HashSet<>();
		long h[] = new long[2];
		
		for(int k = 0; k < 2; k++) {
			for(int i = 0; i < len; i++) {
				h[k] = (h[k] + toInt(text.charAt(i)) * pPow[k][len - i - 1]) % mod[k];
			}
		}
		
		set.add(genUniqueKey(h[0],h[1]));
		
		for(int i = 1; i <= n - len; i++) {
			for(int k = 0; k < 2; k++) {
				h[k] = (h[k] * p[k] - toInt(text.charAt(i-1)) * pPow[k][len] + toInt(text.charAt(i + len - 1))) % mod[k];
				
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
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		text = br.readLine();
		n = text.length();
		pPow = new long[2][n + 1];
		
		for(int k = 0; k < 2; k++) {
			pPow[k][0] = 1;
			for(int i = 1; i<=n; i++) {
				pPow[k][i] = (pPow[k][i-1] * p[k]) % mod[k];
			}
		}
		
		
		int left = 1;
		int right = n + 1;
		
		int ans = 0;
		while(left <= right) {
			int mid = (left + right) / 2;
			
			if(check(mid)) {
				left =mid + 1;
				ans = mid;
			}else {
				right = mid - 1;
			}
		}
		
		System.out.println(ans);
	}
}