import java.util.*;
import java.io.*;
public class Main {
   public static void main(String[] args) throws IOException{
	   BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	   st = new StringTokenizer(br.readLine());
	   
	   n = Integer.parseInt(st.nextToken());
	   l = Integer.parseInt(st.nextToken());
	   
	   board = new int[n][n];
	   for(int y = 0; y<n; y++) {
		   st = new StringTokenizer(br.readLine());
		   for(int x=0; x<n; x++) {
			   board[y][x] = Integer.parseInt(st.nextToken());
		   }
	   }
	   
	   int ans = 0;
	   for(int y = 0; y<n; y++) {
//		   System.out.println("-----");
		   if(possible(board[y])) {
			   ans++;
		   }
	   }
	   
	   for(int x=0; x<n; x++) {
//		   System.out.println("-----");
		   int temp[] = new int[n];
		   for(int y=0; y<n; y++) {
			   temp[y] = board[y][x];
		   }
		   
		   if(possible(temp)) {
			   ans++;
		   }
	   }
	   System.out.println(ans);
   }
   
   static boolean check(int value, int start, int end, int arr[]) {
	   if(end >= n || end < 0 || start >= n || start < 0)
		   return false;
	   for(int i = start; i <= end; i++) {
		   if(arr[i] != value)
			   return false;
	   }
	   return true;
   }
   static boolean possible(int arr[]) {
	   
	   // 모든 높이가 동일하면 true
	   int value = arr[0];
	   if(check(arr[0], 0, n-1, arr))
		   return true;
	   
	   
	   // 인접한 두 칸의 높이 차이가 1보다 크면 false
	   for(int i = 0; i < n - 1; i++) {
		   if(Math.abs(arr[i] - arr[i+1]) > 1)
			   return false;
	   }
	   
	   int count[] = new int[n];
	   
	   
	   // 인접한 두칸의 높이차이가 1이고, [i+1,...,i+L]의 높이가 같지 않거나 범위를 벗어나면 false
	   for(int i = 0; i < n - 1; i++) {
//		   System.out.println("i: "+i);
		   
		   if(arr[i] - arr[i+1] == 1) {
			   if(!check(arr[i+1], i + 1, i + l, arr))
				   return false;
			   for(int idx = i + 1; idx <= i + l; idx++)
				   count[idx]++;
		   }
	   }
	   
	   for(int i = n-1; i>=1; i--) {
//		   System.out.println("i: "+i);
		   if(arr[i] - arr[i-1] == 1 ) {
			   if(!check(arr[i-1], i - l, i - 1, arr))
				   return false;
			   for(int idx = i - l; idx <= i-1;idx++)
				   count[idx]++;
		   }
	   }
	   
//	   System.out.println("arr: "+Arrays.toString(arr));
//	   System.out.println("count: " + Arrays.toString(count));
	   
	   for(int i = 0; i < n; i++) {
		   if(count[i] >= 2)
			   return false;
	   }
	   return true;
	   
	   
   }
   
   static int n,l;
   static StringTokenizer st;
   static int board[][];
}