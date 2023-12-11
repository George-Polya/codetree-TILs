import java.io.*;
import java.util.*;

public class Main {
	static int n;
	static StringTokenizer st;
	static int bits[];
	static int ans;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		bits = new int[n];
		
		for(int i = 0; i<n;i++) {
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			for(int j = 0; j < num;j++) {
				bits[i] |= (1 << Integer.parseInt(st.nextToken()));
			}
		}
		
//		System.out.println(Arrays.toString(bits));
		
		for(int i = 0; i < n ;i++) {
			for(int j = i + 1; j<n;j++) {
				if( (bits[i] & bits[j]) == 0)
					ans++;
			}
		}
		
		System.out.println(ans);
		
	}
}