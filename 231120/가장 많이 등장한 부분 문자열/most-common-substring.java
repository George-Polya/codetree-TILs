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
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		m = Integer.parseInt(st.nextToken());
		text = st.nextToken();
		n = text.length();
		
		pPow = new long[2][n+1];
		
		Map<Long, Integer> maps[] = new HashMap[2];
		for(int k = 0; k < 2; k++) {
			pPow[k][0] = 1;
			maps[k] = new HashMap<>();
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
		
		
		maps[0].put(tH[0], 1);
		
		maps[1].put(tH[1], 1);
		
		
		for(int i = 1; i <= n - m; i++) {
			for(int k = 0; k < 2; k++) {
				tH[k] = (tH[k] * p[k] - toInt(text.charAt(i-1)) * pPow[k][m]) % mod[k];
				
				maps[k].put(tH[k], maps[k].getOrDefault(tH[k], 1) + 1);
				
				
				if(tH[k] < 0)
					tH[k] += mod[k];
			}
		}
		
		
		int ans = 0;
		for(long key : maps[0].keySet()) {
			ans = Math.max(ans, maps[0].get(key));
		}
		
		System.out.println(ans);
	}
	
}