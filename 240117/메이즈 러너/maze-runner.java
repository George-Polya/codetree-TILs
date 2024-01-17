import java.io.*;
import java.util.*;

public class Main {
    static int n, m, k;
    static StringTokenizer st;
    static int board[][],nxtBoard[][];
    static List<Integer> occupied[][];
    static int ans;
    static class Pair {
        int y, x;

        public Pair(int y, int x) {
            this.y = y;
            this.x = x;
        }
        
        public boolean isSame(Pair p) {
            return y == p.y && x == p.x;
        }
        
        public String toString() {
            return y+" "+x+" ";
        }
    }

    static Pair players[];
    static Pair exit;
    static Pair EXITED = new Pair(-1, -1);

    static boolean end() {
        for (int i = 1; i <= m; i++)
            if (players[i] != EXITED)
                return false;
        return true;
    }

    static class Square {
        int y, x, size;

        public Square(int y, int x, int size) {
            this.y = y;
            this.x = x;
            this.size = size;
        }

        public boolean ishigher(Square s) {
            if (size != s.size)
                return size < s.size;
            if (y != s.y)
                return y < s.y;
            return x < s.x;
        }

        @Override
        public String toString() {
            return "Square [y=" + y + ", x=" + x + ", size=" + size + "]";
        }
    }

    static Square getSquare(Pair exit, Pair player) {
        int y1 = player.y;
        int x1 = player.x;
        
        int y2 = exit.y;
        int x2 = exit.x;
        int size = Math.max(Math.abs(y1-y2), Math.abs(x1-x2)) + 1;
        
        int y = Math.max(y1, y2) - (size - 1);
        int x = Math.max(x1, x2) - (size - 1);
        if(y <= 0)
            y = 1;
        if(x<= 0 )
            x = 1;
        return new Square(y,x,size);
    }

    static Square NO_SQUARE = new Square(11, 11, 20);

    static Square findBestSquare() {
        Square bestSquare = NO_SQUARE;
        for (int i = 1; i <= m; i++) {
            if (players[i] == EXITED)
                continue;
            Square cur = getSquare(exit, players[i]);
            if (cur.ishigher(bestSquare))
                bestSquare = cur;
        }
        return bestSquare;
    }
    
    
    static Pair getTarget(int y,int x, int size) {
        int ny = y;
        int nx = x;
        
        if(y > size)
            ny -= size - 1;
        
        if(x > size)
            nx -= size - 1;
        
        int targetY = nx;
        int targetX = size + 1 - ny;
        
        if(y > size)
            targetY += size - 1;
        if(x > size)
            targetX += size - 1;
        return new Pair(targetY, targetX);
    }
    
    static Pair getTarget(int y,int x, int sy,int sx, int size) {
//    	System.out.println((y-sy + 1)+" "+(x-sx+1));
    	int ny = y - sy + 1;
    	int nx = x - sx + 1;
    	int targetY = nx;
    	int targetX = size + 1 - ny;
    	targetY += sy - 1;
    	targetX += sx - 1;
//    	System.out.println(targetY+" "+targetX);
    	return new Pair(targetY, targetX);
    }
    
    static void rotate(Square square) {
        int sy = square.y;
        int sx = square.x;
        int size = square.size;
        
        boolean flag = false;
        
        for(int y=sy ; y < sy + size; y++) {
            for(int x= sx; x<sx + size; x++) {
            	Pair target = getTarget(y,x,sy,sx,size);
                // 회전한 벽의 내구도 감소 
                nxtBoard[target.y][target.x] = board[y][x] <= 0 ? board[y][x] : board[y][x]- 1 ;
                
                
                // 출구도 회전됨
                if(!flag && exit.isSame(new Pair(y,x))) {
//                    System.out.println("target: "+target);
                    exit = target;
                    flag = true;
                }
                
                
//                 플레이어의 위치도 변함
                if(!occupied[y][x].isEmpty()) {
//                    players[occupied[y][x]] = new Pair(target.y,target.x);
                	for(int idx : occupied[y][x]) {
                		players[idx] = new Pair(target.y, target.x);
                	}
                }
                
//                for(int i = 1; i<=m; i++) {
//                	if(players[i] == EXITED)
//                		continue;
//                	if(players[i].isSame(new Pair(y,x)))
//                		players[i] = new Pair(target.y, target.x);
//                }
            }
        }
        
//        System.out.println("exit: "+exit);
//        System.out.println("players: "+Arrays.toString(players));
        
        
        
    }

    
    
    static void rotateMaze() {
        // 1. 정사각형 잡기
        Square square = findBestSquare();
//        System.out.println("bestSquare : "+square);
        rotate(square);
    }
    
    static int dy[] = {-1,1,0,0};
    static int dx[] = {0,0,-1,1};
    static boolean OOB(int y,int x) {
        return y<=0 || y>n || x<=0 || x>n;
    }
    
    static int getDistance(int y1,int x1,int y2, int x2) {
        
        return Math.abs(y2-y1) + Math.abs(x2-x1);
    }
    
    static Pair getNxtPos(int y,int x) {
    	int minDist = getDistance(y,x, exit.y,exit.x);
    	int bestY = y;
    	int bestX = x;
    	
    	for(int dir = 0; dir<4;dir++) {
    		int ny = y + dy[dir];
    		int nx = x + dx[dir];
    		if(OOB(ny,nx))
    			continue;
    		int dist = getDistance(ny,nx, exit.y,exit.x);
    		if(minDist > dist) {
    			minDist = dist;
    			bestY = ny;
    			bestX = nx;
    		}else if(minDist == dist && board[ny][nx] == 0 && board[bestY][bestX] != 0) {
    			bestY = ny;
    			bestX = nx;
    		}
    	}
    	if(board[bestY][bestX] != 0)
    		return new Pair(y,x);
    	else
    		return new Pair(bestY,bestX);
    	
    }
    
    static void move(int idx) {
        Pair player = players[idx];
        int y = player.y;
        int x = player.x;
        
        Pair nxtPos = getNxtPos(y,x);
//        System.out.println(nxtPos);
        int cnt = 0;
//        System.out.println("nxtPos : "+nxtPos);
        if(nxtPos.isSame(player))
        	return;
        else {
            cnt += 1;
            if(nxtPos.isSame(exit))
                nxtPos = EXITED;
            players[idx] = nxtPos;
        }
//        System.out.println("cnt : "+cnt);
        ans += cnt;
    }
    
    static void playerMoveAll() {
        for(int i = 1; i<=m; i++) {
            if(players[i] == EXITED)
                continue;
            move(i);
        }
    }
    

    static void simulate() {
        // 1. 참가자의 이동
        playerMoveAll();
        
//        System.out.println("after move : "+Arrays.toString(players));
        if(end()) {
        	System.out.println(ans);
        	System.out.println(exit);
        	System.exit(0);
        }
        
        // 2. 미로회전
        initialize();
        copy(board, nxtBoard);
        rotateMaze();
        copy(nxtBoard, board);
//        printBoard(board);
    }
    
    static void initialize() {
        for(int y=1; y<=n; y++) {
        	for(int x=1; x<=n; x++)
        		occupied[y][x].clear();
        }
            
        
        for(int i = 1; i<=m; i++) {
            if(players[i] == EXITED)
                continue;
            int y = players[i].y;
            int x = players[i].x;
            occupied[y][x].add(i);
        }
        
    }
    
    static void copy(int src[][],int dst[][]) {
        for(int y=1; y<=n; y++)
            System.arraycopy(src[y], 1, dst[y], 1, n);
    }

    static void printBoard(int board[][]) {
        for(int y=1; y<=n; y++) {
            for(int x=1;x<=n; x++)
                System.out.printf("%-3d", board[y][x]);
            System.out.println();
        }
    }
    
    public static void main(String[] args) throws IOException {
//        System.setIn(new FileInputStream("./input.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        board = new int[n + 1][n + 1];
        nxtBoard = new int[n+1][n+1];
        players = new Pair[m + 1];
        occupied = new List[n+1][n+1];
        for(int y=1; y<=n; y++) {
        	for(int x=1; x<=n; x++)
        		occupied[y][x] = new ArrayList<>();
        }

        for (int y = 1; y <= n; y++) {
            st = new StringTokenizer(br.readLine());
            for (int x = 1; x <= n; x++) {
                board[y][x] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 1; i <= m; i++) {
            st = new StringTokenizer(br.readLine());
            int y = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            players[i] = new Pair(y, x);
        }

        st = new StringTokenizer(br.readLine());
        int y = Integer.parseInt(st.nextToken());
        int x = Integer.parseInt(st.nextToken());
        exit = new Pair(y, x);
//        System.out.println("exit: "+exit);
//        System.out.println("players: "+Arrays.toString(players));
        
        
        while (k-- > 0) {
//        	System.out.println("-----");
            simulate();
//            if (end())
//                break;
//            System.out.println(ans);
        }
        System.out.println(ans);
        System.out.println(exit);
    }
}