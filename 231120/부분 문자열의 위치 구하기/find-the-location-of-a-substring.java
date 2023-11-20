import java.util.*;
import java.io.*;

public class Main {
	static String A, B;
	static int n,m;
	
	static long pPow[][];
	
	static int toInt(char ch) {
		return ch - 'a' + 1;
	}
	
	static int p[] = new int[] {31,37};
	static int mod[] = new int[] {(int)1e9+7, (int)1e9+9};
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		A = br.readLine();
		B = br.readLine();
		n = A.length();
		m = B.length();
		pPow = new long[2][n+1];
		
		for(int k = 0; k < 2; k++) {
			pPow[k][0] = 1;
			for(int i = 1; i<=n; i++)
				pPow[k][i] = (pPow[k][i-1] * p[k]) % mod[k];
		}
		
		long ph[] = new long[2];
		for(int k = 0; k < 2; k++) {
			for(int i = 0; i < m; i++)
				ph[k] = (ph[k] + toInt(B.charAt(i)) * pPow[k][m - 1 - i]) % mod[k];
		}
		
		long th[] = new long[2];
		
		for(int k = 0; k < 2; k++) {
			for(int i = 0; i <m ;i++)
				th[k] = (th[k] + toInt(A.charAt(i)) * pPow[k][ m - 1 - i]) % mod[k];
		}
		
		if(ph[0] == th[0] && ph[1] == th[1]) {
			System.out.println(0);
			return;
		}
		
		boolean exist = false;
		int idx = -1;
		for(int i = 1; i<= n - m; i++) {
			for(int k = 0; k< 2; k++) {
				th[k] = (th[k] * p[k] - toInt(A.charAt(i-1)) * pPow[k][m] + toInt(A.charAt(i + m - 1))) % mod[k];
				
				if(th[k] < 0)
					th[k] += mod[k];
			}
			
			
			if(ph[0] == th[0] && ph[1] == th[1]) {
				idx = i;
				break;
			}
		}
		
		System.out.println(idx);
		
		
		
	}
	
}