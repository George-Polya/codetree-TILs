import java.io.*;
import java.util.*;

public class Main {
	static int n,q;
	static String str;
	static StringTokenizer st;
	static char inputStr[];
	static int A[];
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());
		str = br.readLine();
		int len = n * 2 + 1;
		inputStr = new char[len];
		A = new int[len];
		
		for(int i = 0; i< n;i++) {
			inputStr[i * 2] = '#';
			inputStr[i*2+1] = str.charAt(i);
		}
		
		inputStr[len - 1] = '#';
		int r = -1;
		int p = -1;
		
		for(int i = 0; i < len;i++) {
			if(r < i)
				A[i] = 0;
			else {
				int ii = 2 * p - i;
				A[i] = Math.min(r-i, A[ii]);
			}
			
			
			while(i - A[i] - 1 >=0 && i + A[i] + 1 < len &&
					inputStr[i - A[i] - 1] == inputStr[i+A[i]+1])
				A[i]++;
			
			if(i + A[i] > r) {
				r = i + A[i];
				p = i;
			}
					
		}
//		System.out.println(Arrays.toString(A));
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < q; i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken()) - 1;
			int b = Integer.parseInt(st.nextToken()) - 1;
			int radius = (b-a);
			a = 2 * a + 1;
			b = 2 * b + 1;
			int center = (a+b)/2;
			
			if(A[center] >= radius)
				sb.append("Yes\n");
			else
				sb.append("No\n");
			
		}
		
		System.out.println(sb);
		
	}
}