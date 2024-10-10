import java.util.*;
import java.io.*;
public class Main {
   public static void main(String[] args) throws IOException{
	   BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
       st = new StringTokenizer(br.readLine());
       n = Integer.parseInt(st.nextToken());
       m = Integer.parseInt(st.nextToken());
       t = Integer.parseInt(st.nextToken());
       
       board = new int[n+1][m+1];
       nxtBoard = new int[n+1][m+1];
       for(int y=1; y<=n; y++) {
    	   st = new StringTokenizer(br.readLine());
    	   for(int x=1; x<=m; x++) {
    		   board[y][x] = Integer.parseInt(st.nextToken());
    	   }
       }
       
       for(int turn = 1; turn<=t; turn++) {
//    	   System.out.println("----");
//    	   System.out.println("turn: "+turn);
    	   
    	   diffuseAll();
//    	   System.out.println("after diffuse");
//    	   printBoard(board);
    	   cleanUp();
//    	   System.out.println("after cleanup");
//    	   printBoard(board);
       }
       
       int ans = 0;
       for(int y=1; y<=n; y++) {
    	   for(int x=1; x<=m; x++) {
    		   if(board[y][x] != -1)
    			   ans += board[y][x];
    	   }
       }
       System.out.println(ans);
   }
   
   
   
   static void cleanUp() {
	   // 돌풍 위치 찾기 
	   int Y = findY();
	   
	   int num1 = ((Y - 1) + (m - 1)) * 2;
	   int num2 = ((n - (Y + 1)) + (m-1)) * 2;
	   
	   // 윗 부분 청소 
	   clean(Y, 0, num1, true);
	   
	   // 아랫부분 청소
	   clean(Y + 1, 2, num2, false);
	   
//	   printBoard(board);
   }
   
   static void clean(int sy, int dir, int num, boolean up) {
	   int y = sy;
	   int x = 1;
	   
	   for(int cnt = 0; cnt < num; cnt++) {
		   int ny = y + dy[dir];
		   int nx = x + dx[dir];
		   if(OOB(ny,nx, up ? 0 : sy - 1, up ? sy : n )) {
			   dir = up ? (dir + 1) % 4 : (dir + 3) % 4;
			   ny = y + dy[dir];
			   nx = x + dx[dir];
		   }
		   board[y][x] = board[ny][nx];
		   y = ny;
		   x = nx;
	   }
	   
	   board[sy][1] = -1;
	   board[sy + dy[1]][1 + dx[1]] = 0;
   }
   
   static int findY() {
	   for(int y = 1; y<=n; y++) {
		   if(board[y][1] == -1)
			   return y;
	   }
	   return -1;
   }
   
   static void diffuseAll() {
	   copy(board,nxtBoard);
	   
	   for(int y=1; y<=n; y++) {
		   for(int x=1; x<=m; x++) {
			   if(board[y][x] != -1)
				   diffuse(y,x);
		   }
	   }
	   
	   copy(nxtBoard,board);
   }
   
   static void diffuse(int y,int x) {
	   for(int dir = 0; dir < 4; dir++) {
		   int ny = y + dy[dir];
		   int nx = x + dx[dir];
		   if(OOB(ny,nx, 0, n) || board[ny][nx] == -1)
			   continue;
		   nxtBoard[ny][nx] += board[y][x] / 5;
		   nxtBoard[y][x] -= board[y][x] / 5;
	   }
   }
   
   static void copy(int src[][], int dst[][]) {
	   for(int y=1; y<=n; y++) {
		   System.arraycopy(src[y], 1, dst[y], 1, m);
	   }
   }
   
   static void printBoard(int board[][]) {
	   for(int y=1; y<=n; y++) {
		   for(int x=1; x<=m; x++) {
			   System.out.printf("%3d", board[y][x]);
		   }
		   System.out.println();
	   }
   }
   
   static int n,m,t;
   static StringTokenizer st;
   static int board[][], nxtBoard[][];
   static int dy[] = {-1,0,1,0};
   static int dx[] = {0,1,0,-1};
   static boolean OOB(int y,int x, int yStart, int yEnd) {
	   return y<=yStart || y>yEnd || x<=0 || x>m;
   }
}