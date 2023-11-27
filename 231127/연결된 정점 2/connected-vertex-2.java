import java.io.*;
import java.util.*;

public class Main{
	static int MAX_N = 100000;
	static int n;
	static int uf[] = new int[MAX_N + 1];
	static int count[] = new int[MAX_N + 1];
	
	static int find(int x) {
		if(x == uf[x])
			return x;
		
		return uf[x] = find(uf[x]);
	}
	
	static void union(int x, int y) {
		x = find(x);
		y = find(y);
		
		if( x == y)
			return;
		uf[y] = x;
		count[x] += count[y];
	}
	
	static StringTokenizer st;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		n = Integer.parseInt(br.readLine());
		for(int i = 1; i <= MAX_N; i++) {
			uf[i] = i;
			count[i] = 1;
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i<=n; i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			if(a < b)
				union(a,b);
			else
				union(b,a);
			
			sb.append(count[Math.min(find(a), find(b))]).append('\n');
			
		}
		
		System.out.println(sb);
		
//		for(int i = 1; i<=4; i++) {
//			System.out.print(count[i]+" ");
//		}
	}
}