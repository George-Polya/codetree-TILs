import java.io.*;
import java.util.*;

public class Main {
	static StringTokenizer st;
	static int n, A[], B[], dp[][];
	
	static int solve(int a, int b) {
		if(a == n || b == n) // 끝까지 도달하면 더 이상 점수를 얻을 수 없음
			return 0;
		
		if(dp[a][b] != -1) // 이미 계산한 적이 있으면 저장된 값 반환
			return dp[a][b];
		
		dp[a][b] = 0; // 초기화
		
		// p1의 카드가 더 작은 경우, p1 카드만 버림
		if(A[a] < B[b]) 
			dp[a][b] = Math.max(dp[a][b], solve(a+1, b));
		
		// p1의 카드가 더 큰 경우, p2의 카드 버리고 p1 점수 얻음
		if(A[a] > B[b])
			dp[a][b] = Math.max(dp[a][b], solve(a, b+1) + B[b]);
		
		// 둘 다 카드를 버림
		dp[a][b] = Math.max(dp[a][b], solve(a+1, b+1));
		
		return dp[a][b];
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		A = new int[n];
		B = new int[n];
		
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i < n; i++)
			A[i] = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i < n; i++)
			B[i] = Integer.parseInt(st.nextToken());
		
		dp = new int[n][n]; 
		for(int y = 0; y < n; y++)
			Arrays.fill(dp[y], -1);
		
		int ans = solve(0, 0);
		System.out.println(ans);
	}
}