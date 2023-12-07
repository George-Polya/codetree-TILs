import java.util.*;
import java.io.*;


public class Main {
	static int n, arr[], merged[][], dp[][];
	static StringTokenizer st;
	static int INT_MAX = (int)2e9;
	static int findMin(int i, int j) {
		if(dp[i][j] != INT_MAX)
			return dp[i][j];
		
		if(i == j)
			return dp[i][j] = 0;
		
		int best = INT_MAX;
		for(int k = i; k<j;k++) {
			int cost = findMin(i,k) + findMin(k+1,j) + query(1,1,n,i,k) + query(1,1,n,k+1,j);
			best = Math.min(best, cost);
		}
		
		return dp[i][j] = best;
	}
	
	static int sumTree[];
	
	static int query(int cur, int left, int right, int qLeft, int qRight) {
		if(qRight < left || right < qLeft)
			return 0;
		if(qLeft <= left && right <= qRight)
			return sumTree[cur];
		
		int mid = (left+right) / 2;
		
		int leftSum = query(cur * 2, left ,mid, qLeft, qRight);
		int rightSum = query(cur *2 + 1, mid +1 ,right, qLeft, qRight);
		
		return leftSum + rightSum;
	}
	
	static void init(int cur, int left, int right) {
		if(left == right) {
			sumTree[cur] = arr[left];
			return;
		}
		
		int mid = (left + right) / 2;
		
		init(cur *2 ,left, mid);
		init(cur *2 + 1, mid + 1, right);
		sumTree[cur] = sumTree[cur *2 ]+sumTree[cur*2+1];
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		arr = new int[n+1];
		st = new StringTokenizer(br.readLine());
		for(int i = 1; i<=n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		sumTree = new int[4 * (n+1)];
		
		init(1,1,n);
		
//		merged = new int[n+1][n+1];
//		for(int i = 1; i<=n;i++) {
//			merged[i][i] = arr[i];
//			for(int j = i + 1; j<=n;j++) {
//				merged[i][j] = merged[i][j-1] + arr[j];
//			}
//		}
		
		dp = new int[n+1][n+1];
		for(int i = 1; i<=n; i++) {
			for(int j = 1; j<=n; j++) {
				dp[i][j] = INT_MAX;
			}
		}
		
		System.out.println(findMin(1,n));
		
	}
}