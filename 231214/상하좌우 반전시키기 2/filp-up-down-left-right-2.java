import java.io.*;
import java.util.*;

public class Main{
	static int n,m;
	static int original[][], board[][];
	static StringTokenizer st;
	static int INT_MAX = (int)1e9;
	static int ans = INT_MAX;
	static int dy[] = {0,-1,1,0,0};
	static int dx[] = {0,0,0,-1,1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>m;
	}
	
	static void copy(int dst[][], int src[][]) {
		for(int y=1; y<=n; y++) {
			System.arraycopy(src[y], 1, dst[y], 1, m);
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		original = new int[n+1][m+1];
		board = new int[n+1][m+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=m; x++) {
				original[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		for(int state = 0; state < (1 << m); state++) {
			copy(board, original);
			
			for(int x=1; x<=m; x++) {
				if((state & (1 << (x-1))) != 0 ) {
					int y = 1; 
					
					for(int dir = 0; dir< 5; dir++) {
						int ny = y + dy[dir];
						int nx = x + dx[dir];
						if(OOB(ny,nx))
							continue;
						board[ny][nx] = 1-board[ny][nx];
					}
				}
			}
			
			int num = 0;
			for(int x=1; x<=m; x++) {
				if((state & (1 << (x-1))) != 0 ) {
					num++;
				}
			}
			
			
			for(int y=2; y<=n;y++) {
				for(int x=1; x<=m; x++) {
					if(board[y-1][x] == 0) {
						num++;
						for(int dir = 0; dir< 5; dir++) {
							int ny = y + dy[dir];
							int nx = x + dx[dir];
							if(OOB(ny,nx))
								continue;
							board[ny][nx] = 1-board[ny][nx];
						}
					}
				}
			}
			
			boolean allFilled = true;
			for(int x=1;x<=m;x++) {
				if(board[n][x] == 0)
					allFilled = false;
			}
			
			if(allFilled)
				ans = Math.min(ans, num);
		}
		
		System.out.println(ans == INT_MAX ? -1 : ans);
	}
}