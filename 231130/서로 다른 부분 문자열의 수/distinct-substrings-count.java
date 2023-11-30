import java.io.*;
import java.util.*;

public class Main{
	static String text;
	static int n;
	static int toInt(char ch) {
		return ch - 'a' + 1;
	}
	
	static long pPow[][];
	static int p[] = {31,37};
	static int mod[] = {(int)1e9+7, (int)1e9+9};
	
	static void initialize(long h[], int len) {
		for(int k = 0; k <2 ;k++) {
			for(int i = 0; i< len;i++) {
				h[k] = (h[k] + toInt(text.charAt(i)) * pPow[k][len - i -1]) % mod[k];
			}
		}
	}
	
	static long genUniqueKey(long h1, long h2) {
		return h1 + Math.max(mod[0], mod[1]) * h2;
	}
	
	static HashSet<Long> set = new HashSet<>();
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		text = br.readLine();
		n = text.length();
		
		pPow = new long[2][n+1];
		for(int k=0;k < 2; k++) {
			pPow[k][0] = 1;
			for(int i = 1; i<=n; i++) {
				pPow[k][i] = (pPow[k][i-1] * p[k]) % mod[k];
			}
		}
		
		for(int len = 1; len <= n; len++) {
			long h[] = new long[2];
			
			initialize(h, len);
			
			long key = genUniqueKey(h[0],h[1]);
			set.add(key);
			
			for(int i = 1; i<=n-len;i++) {
				for(int k = 0; k < 2; k++) {
					h[k] = (h[k] * p[k] - toInt(text.charAt(i-1)) * pPow[k][len] + toInt(text.charAt(i + len - 1))) % mod[k];
					
					if(h[k] < 0)
						h[k] += mod[k];
				}
				
				key = genUniqueKey(h[0], h[1]);
				set.add(key);
			}
		}
		
		System.out.println(set.size());
	}
}