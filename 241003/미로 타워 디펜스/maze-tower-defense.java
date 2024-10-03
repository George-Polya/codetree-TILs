import java.util.*;
import java.io.*;
public class Main {
   public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        board = new int[n+1][n+1];
        for(int y=1; y<=n; y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x=1; x<=n; x++) {
        		board[y][x] = Integer.parseInt(st.nextToken());
        	}
        }
        
        init();
//        printBoard(idxBoard);
        
        
        for(int turn = 1; turn<=m; turn++) {
//        	System.out.println("------");
//        	System.out.println("turn: "+turn);
        	
        	st = new StringTokenizer(br.readLine());
        	int d = Integer.parseInt(st.nextToken());
        	int p = Integer.parseInt(st.nextToken());
        	
        	simulate(d,p);
        }
        System.out.println(ans);
   }
   static int ans;
   
   static void simulate(int d,int p) {
	   // 공격 
	   int score = attack(d,p);
	   ans += score;
//	   System.out.println("after attack");
//	   System.out.println("score: "+score);
//	   System.out.println(Arrays.toString(arr));
	   
	   fall();
//	   System.out.println("after fall");
//	   System.out.println(Arrays.toString(arr));
	   
	   score = chainExplode();
//	   System.out.println("after explode");
//	   System.out.println("score: "+score);
	   ans += score;
//	   System.out.println(Arrays.toString(arr));
	   
	   
	   pairing();
//	   System.out.println("after pairing");
//	   System.out.println(Arrays.toString(arr));
   }
   
   static int attack(int d, int p) {
	   int ret = 0;
	   for(int dist = 1; dist<=p; dist++) {
		   int y = cy + dy[d] * dist;
		   int x = cx + dx[d] * dist;
		   
		   if(OOB(y,x))
			   break;
		   
		   int idx = idxBoard[y][x];
		   ret += arr[idx];
		   arr[idx] = 0;
	   }
	   return ret;
   }
   
   static void fall() {
	   int temp[] = new int[n*n];
	   
	   int idx = 1;
	   
	   for(int i = 1; i < n*n;i++) {
		   if(arr[i] == 0)
			   continue;
		   temp[idx++] = arr[i];
	   }
	   arr = temp;
   }
   
   static int findEnd(int start) {
	   int value = arr[start];
	   int end = start + 1;
	   
	   while(end < n * n) {
		   if(arr[end] == value)
			   end++;
		   else
			   break;
	   }
	   
	   return end-1;
   }
   
   static void explode(int start, int end) {
	   for(int i = start; i <= end;i++)
		   arr[i] = 0;
   }
   
   static int chainExplode() {
	   boolean explode;
	   int ret = 0;
	   do {
		   explode = false;
		   
		   
		   int start = 1;
		   while(start < n*n) {
			   
			   if(arr[start] == 0) {
				   start++;
				   continue;
			   }
			   
			   int end = findEnd(start);
			   int cnt = end - start + 1;
			   if(cnt >= 4) {
//				   System.out.printf("start: %d, end: %d\n", start,end);
				   ret += arr[start] * cnt;
				   explode(start,end);
				   explode = true;
				   start = end;
			   }
			   start++;
		   }
		   fall();
	   }while(explode);
	   
	   return ret;
   }
   
   static void pairing() {
	   int idx = 1;
	   int temp[] = new int[n*n];
	   for(int start = 1; start < n*n; start++) {
		   if(arr[start] == 0 || idx == n*n)
			   break;
		   int end = findEnd(start);
		   int cnt = end - start + 1;
		   temp[idx++] = cnt;
		   if(idx == n*n)
			   break;
		   temp[idx++] = arr[start];
		   
		   start = end;
	   }
	   arr = temp;
   }
   
   
   
   static void printBoard(int board[][]) {
	   for(int y=1; y<=n; y++) {
		   for(int x=1; x<=n; x++) {
			   System.out.printf("%3d", board[y][x]);
		   }
		   System.out.println();
	   }
   }
   static void init() {
	   int moveDir = 2;
	   int moveCnt = 1;
	   
	   arr = new int[n*n];
	   cy = (n+1)/2;
	   cx = cy;
	   
	   int y = cy;
	   int x = cx;
	   idxBoard = new int[n+1][n+1];
	   
	   int idx = 1;
	   while(idx < n * n) {
		   for(int i = 0; i < moveCnt; i++) {
			   y = y + dy[moveDir];
			   x = x + dx[moveDir];
			   if(idx == n * n)
				   break;
			   
			   idxBoard[y][x] = idx;
			   arr[idx] = board[y][x];
			   idx++;
		   }
		   
		   moveDir = (moveDir + 3) % 4;
		   if(moveDir == 0 || moveDir == 2)
			   moveCnt++;
	   }
	   
   }
   
   static int cy, cx;
   static int dy[] = {0,1,0,-1};
   static int dx[] = {1,0,-1,0};
   static boolean OOB(int y,int x) {
	   return y<=0 || y>n || x<=0 || x>n;
   }
   static int n,m;
   static int board[][];
   static int arr[];
   static int idxBoard[][];
   static StringTokenizer st;
}