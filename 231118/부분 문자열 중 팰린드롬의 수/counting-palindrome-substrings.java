import java.io.*;
import java.util.*;

public class Main {
	static String str;
	static char inputStr[];
	static int n;
	static int A[];
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		str = br.readLine();
		int len = str.length();
		
		n = 2 * len + 1;
		inputStr = new char[n];
		A = new int[n];
		for(int i = 0; i < len; i++) {
			inputStr[i * 2] = '#';
			inputStr[i * 2 + 1] = str.charAt(i);
		}
		
		inputStr[n-1] = '#';
//		System.out.println(Arrays.toString(inputStr));
		
		int r = -1;
		int p = -1;
		for(int i = 0; i < n ;i++) {
			if(r < i)
				A[i] = 0;
			else {
				int ii = 2 * p - i;
				A[i] = Math.min(r - i, A[ii]);
			}
			
			
			while(i - A[i] - 1 >=0 && i + A[i] + 1 < n &&
					(inputStr[i - A[i] - 1] == inputStr[i+A[i]+1]))
				A[i]++;
			
			
			if(i + A[i] > r) {
				r = i + A[i];
				p = i;
			}
			
		}
		
//		System.out.println(Arrays.toString(A));
		int ans = 0;
		for(int i = 0; i <= n/2;i++)
			ans += A[i];
		System.out.println(ans);
		
		
	}
}