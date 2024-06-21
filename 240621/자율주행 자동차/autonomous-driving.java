import java.util.*;
import java.io.*;
public class Main {
	static int n,m;
	static int board[][];
	static StringTokenizer st;
	static boolean visited[][];
	
	static class Tuple{
		int y,x,dir;
		public Tuple(int y,int x,int dir) {
			this.y = y;
			this.x = x;
			this.dir = dir;
		}
		
		public String toString() {
			return y+" "+x+" "+dir;
		}
	}
	static Tuple car = new Tuple(-1,-1,-1);
	static Tuple NO_POS = new Tuple(-1,-1,-1);
	
	static int dy[] = {-1,0,1,0};
	static int dx[] = {0,1,0,-1};
	
	/*
	 * 자동차의 다음 위치와 방향을 리턴하는 함수 
	 * 1. 현재 방향(dir)을 기준으로 왼쪽 방향에 한번도 간 적이 없다면 좌회전해서 해당 방향으로 한칸 전진 
	 * 2. 왼쪽이 인도거나 이미 방문했다면 좌회전하고 다시 1번 수행 
	 * 3. 4방향 모두 체크했는데도 전진 못함 
	 */
	static Tuple getNxtPos() {
		int y = car.y;
		int x = car.x;
		int dir = car.dir;
		
		
		for(int i = 0; i < 4; i++) {
			/*
			 * 방향정하기 
			 * 만약 첫번째에(i=0)에 현재 방향(dir)에 갈 수 있으면 그대로 리턴 => moveDir = (dir + 3) % 4  
			 * 아니라면 좌회전을 구현해줘야 하는데 이것은 (dir + 3 - i) % 4로 처리 
			 */
			int moveDir = (dir + 3 - i) % 4; 
			int ny = y + dy[moveDir];
			int nx = x + dx[moveDir];
			
			if(visited[ny][nx] || board[ny][nx] == 1) // 왼쪽이 인도거나 이미 방문한 경우 
				continue;
			
			return new Tuple(ny,nx,moveDir);
		}
		
		// 4 방향 모두 체크했는데도 전진하지 못한 경우
		// 한칸 후진했을때의 위치(ny,nx)
		int ny = y + dy[(dir+2)%4];
		int nx = x + dx[(dir+2)%4];
		
		if(board[ny][nx] == 1) // 후진하려한 위치가 인도인 경우 
			return NO_POS;
		return new Tuple(ny,nx,dir); // 후진할 수 있으면 방향은 그대로 유지한 채로 후진 
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		board = new int[n][m];
		visited = new boolean[n][m];
		st = new StringTokenizer(br.readLine());
		car.y = Integer.parseInt(st.nextToken());
		car.x = Integer.parseInt(st.nextToken());
		car.dir = Integer.parseInt(st.nextToken());
		
		for(int y=0; y<n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=0; x<m; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		
		visited[car.y][car.x] = true;
		
		int ans = 1;
		
		while(true) {
//			System.out.println(car);
			Tuple nxt = getNxtPos();
			if(nxt == NO_POS)
				break;
			car = nxt;
			if(!visited[nxt.y][nxt.x])
				ans++;
			visited[nxt.y][nxt.x] = true;
		}
		System.out.println(ans);
	}
}