import java.io.*;
import java.util.*;

public class Main{
	static int n,m;
	static int uf[];
	
	static int find(int x) {
		if(x == uf[x])
			return x;
		
		return uf[x] = find(uf[x]);
	}
	
	static void union(int x, int y) {
		if(x > y) {
			int temp = x;
			x = y;
			y = temp;
		}
		
		
		x = find(x);
		y = find(y);
		
		if( x == y)
			return;
		uf[y] = x;
	}
	
	static StringTokenizer st;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		uf = new int[n+1];
		for(int i = 1; i <= n; i++) {
			uf[i] = i;
		}
		int ans = n;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			
			while(true) {
				a = find(a);
				
				if(a >= b)
					break;
				
				uf[a] = a + 1;
				a = a + 1;
				ans--;
				
			}
			sb.append(ans).append('\n');
			
		}
		
		System.out.println(sb);
		
		
	}
}