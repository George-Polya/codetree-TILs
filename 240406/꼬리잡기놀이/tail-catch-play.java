import java.io.*;
import java.util.*;

public class Main{
	static int n,m,k;
	static StringTokenizer st;
	static int board[][];
	static boolean visited[][];
	static class Pair{
		int y,x;
		public Pair(int y,int x) {
			this.y = y;
			this.x = x;
		}
		public String toString() {
			return y+" "+x;
		}
	}
	
	static class Node{
		int team;
		int value;
		int order;
		int y,x;
		Node prev, next;
		
		public Node(int team, int value, int order, int y, int x) {
			this.team = team;
			this.value = value;
			this.order = order;
			this.y = y;
			this.x = x;
		}
		
		
	}
	static Node NO_NODE = new Node(-1,-1,-1,-1,-1);
	
	static Node heads[], tails[];
	
	static int dy[] = {1,0,-1,0};
	static int dx[] = {0,1,0,-1};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1;x<=n; x++) {
				System.out.print(board[y][x]+" ");
			}
			System.out.println();
		}
	}
	
	static void printTeam(int idx) {
		for(Node cur = heads[idx]; cur != null; cur = cur.prev) {
			System.out.printf("value: %d, order: %d, y: %d, x:%d \n", cur.value, cur.order, cur.y,cur.x);
		}
		
//		for(Node cur = tails[idx]; cur != null; cur = cur.next) {
//			System.out.printf("value: %d, order: %d, y: %d, x:%d \n", cur.value, cur.order, cur.y,cur.x);
//		}
	}
	
	static void dfs(Node cur, Node parent) {
		int value = board[cur.y][cur.x];
//		System.out.println("value: "+value);
//		System.out.println("cur: "+ new Pair(cur.y,cur.x));
//		System.out.println("parent: "+ new Pair(parent.y, parent.x));
//		System.out.println("--------");

		int team = cur.team;
		int order = cur.order + 1;
		
		for(int dir = 0; dir < 4; dir++) {
			int ny = cur.y + dy[dir];
			int nx = cur.x + dx[dir];
			if(OOB(ny,nx) || board[ny][nx] == 0 || visited[ny][nx])
				continue;
			
			int nxtValue = board[ny][nx];
			
			if(value == 1 && nxtValue == 2) {
				visited[ny][nx] = true;
				Node nxt = new Node(team, nxtValue, order, ny,nx);
				dfs(nxt, cur);
			}else if(value == 2 && nxtValue != 1) {
				visited[ny][nx] = true;
				Node nxt = new Node(team, nxtValue, order, ny,nx);
				dfs(nxt, cur);
			}else if(value ==3) {
				visited[ny][nx] = true;
				Node nxt = new Node(team, nxtValue, order, ny,nx);
				dfs(nxt, cur);
			}
		}
		
		if(value == 4) {
			return;
		}else if(value == 3) {
			cur.next = parent;
			parent.prev = cur;
			cur.prev = null;
			tails[team] = cur;
		}else if(value == 2) {
			cur.next = parent;
			parent.prev = cur;
		}
		
	}
	
	/*
	 * 이동 역시 dfs로 수행
	 * 가장 마지막부터 이동하고, 연쇄적으로 이동 
	 * 
	 * 이동은 현재위치가 내 다음노드의 위치로 변경됨 
	 */
	
	static void move(Node cur) {
		if(cur ==  null) {
			return;
		}
//		System.out.printf("cur: %d %d\n", cur.y, cur.x);
		Node prev = cur.prev;
//		if(prev != null)
//			System.out.printf("prev: %d %d\n", prev.y, prev.x);
		Node next = cur.next;
//		if(next != null)
//			System.out.printf("next: %d %d\n", next.y, next.x);
		
		move(prev);
		
		if(cur.value == 1) {
			for(int dir = 0; dir < 4; dir++) {
				int ny = cur.y + dy[dir];
				int nx = cur.x + dx[dir];
				if(OOB(ny,nx) || board[ny][nx] == 0)
					continue;
				if(board[ny][nx] == 4) {
					cur.y = ny;
					cur.x = nx;
					board[cur.y][cur.x] = cur.value;
					break;
				}
			}
		}else if(cur.value == 2) {
			cur.y = next.y;
			cur.x = next.x;
			board[cur.y][cur.x] = cur.value;
		}else if(cur.value == 3){
			board[cur.y][cur.x] = 4;
			cur.y = next.y;
			cur.x = next.x;
			board[cur.y][cur.x] = cur.value;
		}
		
	}
	
	
	static void moveAllTeams() {
		for(int i = 0; i< m;i++) {
			Node head = heads[i];
			move(head);
//			System.out.println("%%%");
		}
	}
	

	static class Tuple{
		int y,x,dir;
		public Tuple(int y,int x,int dir) {
			this.y= y;
			this.x= x;
			this.dir =dir;
		}
		
		public String toString() {
			return y+" "+x+" "+dir;
		}
	}
	static int score;
	
	static Tuple getBallPos(int round) {
		int mod = round % n;
		int pos = mod == 0 ? n : mod;
		int start = mod == 0 ? (round / n - 1) % 4 : (round/n) % 4;
		
		int dist = pos - 1;
		int y = -1;
		int x = -1;
		int moveDir = -1;
		if(start == 0) {
			y = 1;
			x = 1;
			int dir = 0;
			y = y + dy[dir] * dist;
			x = x + dx[dir] * dist;
			moveDir = 1;
		}else if(start == 1) {
			y = n;
			x = 1;
			int dir = 1;
			y = y + dy[dir] * dist;
			x = x + dx[dir] * dist;
			moveDir = 2;
		}else if(start == 2) {
			y = n;
			x = n;
			int dir = 2;
			y = y + dy[dir] * dist;
			x = x + dx[dir] * dist;
			moveDir = 3; 
		}else if(start == 3) {
			y = 1;
			x = n;
			int dir = 3;
			y = y + dy[dir] * dist;
			x = x + dx[dir] * dist;
			moveDir = 0;
		}
		
		return new Tuple(y,x,moveDir);
	}
	
	static Node findNode(int y, int x) {
		for(int i = 0; i < m; i++) {
			for(Node cur=heads[i]; cur != null; cur = cur.prev) {
				if(cur.y == y && cur.x == x) {
					return cur;
				}
			}
		}
		return NO_NODE;
	}
	
	/*
	 * 현재 노드가 속한 팀의 방향을 전부 변경 
	 * 
	 */
	static void reverse(Node node) {
		int team = node.team;
		
		Stack<Node> stk = new Stack<>();
		for(Node cur = heads[team]; cur != null; cur=cur.prev)
			stk.add(cur);
		
		Node cur = stk.pop();
		heads[team] = cur;
		cur.next = null;
		cur.value = 1;
		board[cur.y][cur.x] = cur.value;
		int order = 1;
		cur.order = 1;
		
		while(!stk.isEmpty()) {
			Node prev = stk.pop();
			
			cur.prev = prev;
			prev.next = cur;
			prev.order = ++order;
			cur = prev;
		}
		cur.prev = null;
		cur.value = 3;
		board[cur.y][cur.x] = cur.value;
		tails[team] = cur;
		
	}
	
	static void getScore(Tuple ball) {
		int y = ball.y;
		int x = ball.x;
		int dir = ball.dir;
		
		for(int dist = 0; dist < n; dist++) {
			int ny = y + dy[dir] * dist;
			int nx = x + dx[dir] * dist;
			
			if(board[ny][nx] > 0 && board[ny][nx] < 4) {
//				System.out.println(board[ny][nx]);
				Node node = findNode(ny,nx);
				if(node != NO_NODE) {
					score += node.order * node.order;
					reverse(node);
					break;
				}
			}
		}
	}
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		visited = new boolean[n+1][n+1];
		board = new int[n+1][n+1];
		
		heads = new Node[m];
		tails = new Node[m];
		
		int cnt = 0;
		for(int y = 1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1;x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
				if(board[y][x] == 1) {
					heads[cnt] = new Node(cnt, board[y][x], 1, y,x);
					cnt++;
				}
			}
		}
		
		/*
		 * 초기화
		 * 각 사람들이 어느팀에 속하는지, 앞에 누가 있는지, 뒤에 누가 있는지, 자신이 팀에서 몇번째인지 초기화 
		 */
		for(int i= 0; i < m;i++) {
			Node cur = heads[i];
			visited[cur.y][cur.x] = true;
			dfs(heads[i], NO_NODE);
		}
		
		
//		// 팀의 이동 
//		int round = 1;
//		moveAllTeams();
//		printBoard(board);
//		printTeam(0);
//		System.out.println("-----");
//		// 공 던지기 
//		Tuple ball = getBallPos(round);
//		// 점수 획득
//		getScore(ball);
//		printBoard(board);
//		printTeam(0);
		
		for(int round =1; round<=k; round++) {
//			System.out.println("round: "+round);
			moveAllTeams();
//			printBoard(board);
//			for(int i = 0; i < m; i++) {
//				printTeam(i);
//				System.out.println("=====");
//			}
//			System.out.println("###");
			Tuple ball = getBallPos(round);
			getScore(ball);
//			printBoard(board);
//			for(int i = 0; i < m; i++) {
//				printTeam(i);
//				System.out.println("=====");
//			}
//			System.out.println("--------");
		}
		System.out.println(score);
		
	}
}