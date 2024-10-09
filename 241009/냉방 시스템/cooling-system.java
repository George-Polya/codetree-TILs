import java.util.*;
import java.io.*;
public class Main {
   public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        
        board = new int[n+1][n+1];
        for(int y = 1;y <=n; y++) {
        	st = new StringTokenizer(br.readLine());
        	for(int x = 1; x<=n; x++) {
        		int value = Integer.parseInt(st.nextToken());
        		if(value <= 1) {
        			board[y][x] = value; 
        		}else {
        			aircons.add(new int[] {y,x,value-2});
        		}
        	}
        		
        }
        
        wall = new boolean[n+1][n+1][4];
        scores = new int[n+1][n+1];
        nxtScores = new int[n+1][n+1];
        
        
        for(int i = 0; i <m;i++) {
        	st = new StringTokenizer(br.readLine());
        	int y = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken());
        	int s = Integer.parseInt(st.nextToken());
        	if(s == 0) {
        		wall[y][x][1] = true;
        		wall[y + dy[1]][x + dx[1]][3] = true;
        	}else {
        		wall[y][x][0] = true;
        		wall[y + dy[0]][x + dx[0]][2] = true;
        	}
        }
        
        
        
        int turn = 0;
        while(!end()) {
        	turn++;
        	simulate();
        	if(turn >= 100)
        		break;
        }
        System.out.println(turn >= 100 ? -1 : turn);
   }
   
   static void simulate() {
	// 에어컨 가동 
     windAll();
//     System.out.println("after wind");
//     printBoard(scores);

     
     mixAll();
//     System.out.println("after mix");
//     printBoard(scores);

     
     decreaseAll();
//     System.out.println("after decrease");
//     printBoard(scores);
   }
   
   static boolean end() {
	   for(int y=1; y<=n; y++) {
		   for(int x=1; x<=n; x++) {
			   if(board[y][x] == 1 && scores[y][x] < k)
				   return false;
		   }
	   }
	   return true;
   }
   
   static void decreaseAll() {
	   for(int y=1; y<=n; y++) {
		   for(int x=1; x<=n; x++) {
			   if(y == 1 || y == n || x == 1 || x==n)
				   decrease(y,x);
		   }
	   }
   }
   
   static void decrease(int y,int x) {
	   if(scores[y][x] == 0)
		   return;
	   scores[y][x]--;
   }
   
   static void mixAll() {
	   copy(scores, nxtScores);
	   
	   for(int y=1; y<=n; y++) {
		   for(int x=1; x<=n; x++) {
			   mix(y,x);
		   }
	   }
	   
	   copy(nxtScores, scores);
   }
   
   static void mix(int y,int x) {
	   int score = scores[y][x];
	   for(int dir = 0; dir < 4; dir++) {
		   if(isWall(y,x,dir))
			   continue;
		   int ny = y + dy[dir];
		   int nx = x + dx[dir];
		   
		   int nScore = scores[ny][nx];
		   
		   // 현재 위치가 더 높을 때만 전파 >> 중복 전파 방지! 
		   if(score > nScore) {
			   int diff = (score - nScore) / 4;
			   nxtScores[y][x] -= diff;
			   nxtScores[ny][nx] += diff;
		   }
		   
		   
	   }
   }
   
   static void copy(int src[][], int dst[][]) {
	   for(int y=1;y<=n; y++) {
		   System.arraycopy(src[y], 1, dst[y], 1, n);
	   }
   }
   
   static void windAll() {
	   for(int[] aircon : aircons) {
		   wind(aircon[0], aircon[1], aircon[2]);
	   }
   }
   
   static void printBoard(int board[][]) {
	   for(int y=1; y<=n; y++) {
		   for(int x=1; x<=n; x++) {
			   System.out.printf("%3d", board[y][x]);
		   }
		   System.out.println();
	   }
   }
   static void wind(int y, int x, int dir) {
	   visited = new boolean[n+1][n+1];
	   
	   int ny = y + dy[dir];
	   int nx = x + dx[dir];
	   
	   dfs(ny,nx,dir, 5);
   }
   
   static void dfs(int y,int x,int dir, int value) {
	   if(visited[y][x] || value == 0)
		   return;
	   scores[y][x] += value;
	   visited[y][x] = true;
	   
	   if(!isWall(y,x,(dir+3) % 4)) {
		   int ny = y + dy[(dir + 3) % 4];
		   int nx = x + dx[(dir + 3) % 4];
		   
		   if(!isWall(ny,nx,dir)) {
			   int nny = ny + dy[dir];
			   int nnx = nx + dx[dir];
			   dfs(nny,nnx, dir, value - 1);
		   }
	   }
	   
	   if(!isWall(y,x,dir)) {
		   int ny = y + dy[dir];
		   int nx = x + dx[dir];
		   dfs(ny,nx,dir, value - 1);
	   }
	   
	   if(!isWall(y,x,(dir + 1) % 4)) {
		   int ny = y + dy[(dir + 1) % 4];
		   int nx = x + dx[(dir + 1) % 4];
		   
		   if(!isWall(ny,nx,dir)) {
			   int nny = ny + dy[dir];
			   int nnx = nx + dx[dir];
			   dfs(nny,nnx, dir, value - 1);
		   }
	   }
   }
   
   /*
    * (y,x)위치에서 dir 방향으로 벽이 있나요? 
    */
   static boolean isWall(int y,int x, int dir) {
	   int ny = y + dy[dir];
	   int nx = x + dx[dir];
	   
	   return OOB(ny, nx) || wall[y][x][dir];
   }
   
   static int dy[] = {0,-1,0,1};
   static int dx[] = {-1,0,1,0};
   static boolean OOB(int y,int x) {
	   return y<=0 || y>n || x<=0 || x>n;
   }
   
   static int n,m,k;
   static StringTokenizer st;
   static int board[][],scores[][], nxtScores[][];
   static boolean visited[][], wall[][][];
   
   static ArrayList<int[]> aircons = new ArrayList<>();
   
}