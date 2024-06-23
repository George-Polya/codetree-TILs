//  package samsung;
import java.util.*;
import java.io.*;


public class Main{
	static StringTokenizer st;
	static int n,m;
	static int cur_y, cur_x, k;
	static int board[][];
	static int up, down, left, right, front, back;
	
	static int dy[] = {0,0,-1,1};
	static int dx[] = {1,-1,0,0};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>m;
	}
	
	static void roll(int dir) {
		int temp = down;
		if(dir == 0) {
			down = right;
			right = up;
			up = left;
			left = temp;
		}else if(dir == 1) {
			down = left;
			left = up;
			up = right;
			right = temp;
		}else if(dir == 2) {
			down = back;
			back = up;
			up = front;
			front = temp;
		}else if(dir == 3) {
			down = front;
			front = up;
			up = back;
			back = temp;
		}
	}
	
	static void simulate(int ny,int nx, int dir) {
		
		
		roll(dir);
		cur_y = ny;
		cur_x = nx;
		
		if(board[cur_y][cur_x] == 0) {
			board[cur_y][cur_x] = down;
		}else {
			down = board[cur_y][cur_x];
			board[cur_y][cur_x] = 0;
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		cur_y = Integer.parseInt(st.nextToken());
		cur_x = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		cur_y += 1;
		cur_x += 1;
		board = new int[n+1][m+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=m; x++)
				board[y][x] = Integer.parseInt(st.nextToken());
		}
		
		st = new StringTokenizer(br.readLine());
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i<k;i++) {
			int dir = Integer.parseInt(st.nextToken());
			dir -= 1;
			int ny = cur_y + dy[dir];
			int nx = cur_x + dx[dir];
			if(OOB(ny,nx))
				continue;
			simulate(ny,nx, dir);
			sb.append(up).append('\n');
		}
		System.out.println(sb);
	}
}