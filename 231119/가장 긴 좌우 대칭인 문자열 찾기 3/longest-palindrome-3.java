import java.io.*;
import java.util.*;

public class Main {
	static int n;
	static char ch;
	static String str;
	static StringTokenizer st;
	static char inputStr[];
	static int A[];
	
	static boolean check(int center, int radius) {
		if(inputStr[center] == '#') {
			for(int i = center + 1; i <= center + radius; i+=2)
				if(inputStr[i] == ch)
					return false;
		}else {
			for(int i = center; i<= center + radius; i+=2)
				if(inputStr[i] == ch)
					return false;
		}
		return true;
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		ch = st.nextToken().charAt(0);
		str = br.readLine();
		int len = 2 * n + 1;
		
		inputStr = new char[len];
		for(int i = 0; i < n ;i++) {
			inputStr[i * 2] = '#';
			inputStr[i * 2 + 1] = str.charAt(i);
		}
		
		inputStr[len - 1] = '#';
		
		int p = -1;
		int r = -1;
		
		A = new int[len];
		
		for(int i = 0; i < len;i++) {
			if(r < i)
				A[i] = 0;
			else {
				int ii = 2 * p - i;
				A[i] = Math.min(r - i, A[ii]);
			}
			
			while(i - A[i] - 1 >=0 && i + A[i] + 1 < len && 
					inputStr[i-A[i]-1] == inputStr[i+A[i]+1])
				A[i]++;
			
			if(i + A[i] > r) {
				r = i + A[i];
				p = i;
			}
			
		}
		
//		System.out.println(Arrays.toString(A));
		
		int ans = 0;
		for(int i = 0; i < len; i++) {
			int radius = A[i];
			if(check(i, radius))
				ans = Math.max(ans, radius);
		}
		System.out.println(ans);
	}
}