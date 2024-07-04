// package samsung;
import java.util.*;
import java.io.*;

public class Main {
   static int n,m;
   static StringTokenizer st;
   static int board[][];
   
   static class Horse{
	   int y,x, id;
	   public Horse(int y,int x, int id) {
		   this.y = y;
		   this.x = x;
		   this.id = id;
	   }
   }
   
   static ArrayList<Horse> arr = new ArrayList<>();
   static ArrayList<Horse> enemys =new ArrayList<>();
   static int moveDirs[];
   
   static void initialize() {
	   for(int y=1; y<=n; y++) {
		   for(int x=1; x<=m; x++)
			   board[y][x] = 0;
	   }
	   
	   for(Horse enemy : enemys) {
		   board[enemy.y][enemy.x] = 3;
	   }
   }
   
   
   static int ans = Integer.MAX_VALUE;
   
   static int getArea() {
	   int area = 0;
	   for(int y=1; y<=n; y++) {
		   for(int x=1; x<=m; x++) {
			   if(board[y][x] > 0)
				   area++;
		   }
			   
	   }
	   
	   
	   return n * m - area;
   }
   
   static void solve(int cnt) {
	   if(cnt == arr.size()) {
//		   System.out.println(Arrays.toString(moveDirs));
		   initialize();
		   for(int i = 0; i<cnt;i++)
			   move(arr.get(i), moveDirs[i]);
		   
		   ans = Math.min(ans, getArea());
		   return;
	   }
	   
	   Horse horse = arr.get(cnt);
	   if(horse.id == 1) {
		   for(int dir = 0; dir < 4; dir++) {
			   moveDirs[cnt] = dir;
			   solve(cnt + 1);
		   }
	   }else if(horse.id == 2) {
		   for(int dir : new int[]{1,3 }) {
			   moveDirs[cnt] = dir;
			   solve(cnt + 1);
		   }
	   }else if(horse.id == 3) {
		   for(int dir = 0; dir<4;dir++) {
			   moveDirs[cnt] = dir;
			   solve(cnt + 1);
		   }
	   }else if(horse.id == 4) {
		   for(int dir = 0; dir<4;dir++) {
			   moveDirs[cnt] = dir;
			   solve(cnt + 1);
		   }
	   }else if(horse.id == 5) {
		   moveDirs[cnt] = 0;
		   solve(cnt + 1);
	   }
   }
   
   static int dy[] = {-1,1,0,0};
   static int dx[] = {0,0,-1,1};
   static boolean OOB(int y, int x) {
	   return y<=0 || y>n || x<= 0 || x>m;
   }
   
   static void go(Horse horse, int moveDir) {
	   int y = horse.y;
	   int x = horse.x;
	   while(true) {
		   if(OOB(y,x) || board[y][x] == 3)
			   break;
		   board[y][x] = 1;
		   y = y + dy[moveDir];
		   x = x + dx[moveDir];
	   }
   }
   
   
   static void move(Horse horse, int dir) {
	   
	   
	   if(horse.id == 1) {
		   go(horse, dir);
	   }else if(horse.id == 2) {
		   if(dir == 1) {
			   for(int moveDir : new int[] {0, 1}) {
				   go(horse, moveDir);
			   }
		   }else if(dir  == 3) {
			   for(int moveDir : new int[] {2,3}) {
				   go(horse, moveDir);
			   }
		   }
		   
	   }else if(horse.id == 3) {
		   if(dir == 0) {
			   for(int moveDir : new int[] {0, 3}) {
				   go(horse, moveDir);
			   }
			   
		   }else if(dir == 1) {
			   for(int moveDir : new int[] {3, 1}){
				   go(horse, moveDir);
			   }
		   }else if(dir == 2) {
			   for(int moveDir: new int[] {1,2}) {
				   go(horse, moveDir);
			   }
			   
		   }else if(dir == 3) {
			   for(int moveDir : new int[] {2,0}) {
				   go(horse,moveDir);
			   }
		   }
	   }else if(horse.id == 4) {
		   if(dir == 0) {
			   for(int moveDir : new int[] {0,2,3}) {
				   go(horse, moveDir);
			   }
			   
		   }else if(dir == 1) {
			   for(int moveDir : new int[] {0,1,3}) {
				   go(horse, moveDir);
			   }
			   
		   }else if(dir == 2) {
			   for(int moveDir : new int[] {2,3,1}) {
				   go(horse, moveDir);
			   }
		   }else if(dir == 3) {
			   for(int moveDir : new int[] {0,1,2}) {
				   go(horse, moveDir);
			   }
		   }
		   
	   }else if(horse.id == 5) {
		   for(int moveDir = 0; moveDir<4;moveDir++) {
			   go(horse, moveDir);
		   }
	   }
   }
   
   static void printBoard(int board[][]) {
	   StringBuilder sb = new StringBuilder();
	   for(int y=1; y<=n; y++) {
		   for(int x=1; x<=m; x++)
			   sb.append(board[y][x]).append(' ');
		   sb.append('\n');
	   }
	   System.out.println(sb);
   }
   
   public static void main(String[] args) throws IOException{
	   BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	   st = new StringTokenizer(br.readLine());
	   n = Integer.parseInt(st.nextToken());
	   m = Integer.parseInt(st.nextToken());
	   board = new int[n+1][m+1];
	   for(int y=1; y<=n; y++) {
		   st = new StringTokenizer(br.readLine());
		   for(int x=1; x<=m; x++) {
			   int value = Integer.parseInt(st.nextToken());
			   if(value == 6)
				   enemys.add(new Horse(y,x,value));
			   else if(value != 0)
				   arr.add(new Horse(y,x,value));
		   }
	   }
	   
	   moveDirs = new int[arr.size()];
	   solve(0);
	   System.out.println(ans);
//	   moveDirs = new int[] {3,0,0};
//	   for(int i = 0; i<arr.size();i++)
//		   move(arr.get(i), moveDirs[i]);
//	   
//	   printBoard(board);
//	   System.out.println(getArea());
	   
   }
}