import java.util.*;
import java.io.*;

public class Main {
	static String text, pattern;
	static int n, m;
	static long pPow[][];
	
	static int toInt(char ch) {
		return ch - 'a' + 1;
	}
	
	static int p[] = new int[] {31,37};
	static int mod[] = new int[] {(int)1e9+7, (int)1e9+9};
	
	static StringTokenizer st;
	
	static long getUniqueKey(long h1, long h2) {
		return h1 * Math.max(mod[0], mod[1]) + h2;
	}
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		m = Integer.parseInt(st.nextToken());
		text = st.nextToken();
		n = text.length();
		
		pPow = new long[2][n+1];
		
		for(int k = 0; k < 2; k++) {
			pPow[k][0] = 1;
			for(int i = 1; i<=n;i++) {
				pPow[k][i] = (pPow[k][i-1] * p[k]) % mod[k];
			}
		}
		
		
		long tH[] = new long[2];
		for(int k = 0; k < 2; k++) {
			for(int i = 0; i < m; i++) {
				tH[k] = (tH[k] + toInt(text.charAt(i)) * pPow[k][m - i - 1]) % mod[k];
			}
		}
		
		int ans = 0;
		HashMap<Long, Integer> map = new HashMap<>();
		long key = getUniqueKey(tH[0], tH[1]);
		map.put(key, map.getOrDefault(key, 0) + 1);
		ans = Math.max(ans, map.get(key));
		
		
		for(int i = 1; i <= n - m; i++) {
			for(int k = 0; k < 2; k++) {
				tH[k] = (tH[k] * p[k] - toInt(text.charAt(i-1)) * pPow[k][m] + toInt(text.charAt(i + m - 1))) % mod[k];
				
				
				if(tH[k] < 0)
					tH[k] += mod[k];
			}
			
			key = getUniqueKey(tH[0], tH[1]);
			map.put(key, map.getOrDefault(key, 0) + 1);
			ans = Math.max(ans, map.get(key));
		}
		
		System.out.println(ans);
		
	}
	
}