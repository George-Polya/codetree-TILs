import java.io.*;
import java.util.*;

public class Main {
	static int n;
	static int arr[];
	static StringTokenizer st;
	static int ans;
	static void solve(int cur, int cnt, int sum) {
		if(cnt == 3) {
			ans = Math.max(ans, sum);
			return;
		}
		
		if(cur == n)
			return;
		
		int num = arr[cur];
		
		if(check(sum, num))
			solve(cur + 1, cnt + 1, sum + num);
		
		solve(cur + 1, cnt, sum);
		
	}
	
	static boolean check(int sum, int num) {
		return (sum & num) == 0;
	}
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		arr = new int[n];
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i < n ;i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		solve(0,0,0);
		System.out.println(ans);
	}
}