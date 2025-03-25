import sys
from collections import deque

# 상수 및 전역 변수
INF = 10**9  # 충분히 큰 값
N = 0
M = 0
sy = sx = ey = ex = 0
board = []
wBoard = []
dist = []
medusa = None
warriors = []
NO_WARRIOR = None  # Warrior(-1, -1, -1) 로 초기화 후 할당

dy = [-1, 1, 0, 0]
dx = [0, 0, -1, 1]

dy2 = [-1, -1, 0, 1, 1, 1, 0, -1]
dx2 = [0, 1, 1, 1, 0, -1, -1, -1]

class Pair:
    def __init__(self, y, x):
        self.y = y
        self.x = x
    def __str__(self):
        return f"({self.y}, {self.x})"

NO_PAIR = Pair(-1, -1)

class TupleX:
    # Java의 Tuple과 동일한 역할: y, x, 그리고 방향(dir)
    def __init__(self, y, x, d):
        self.y = y
        self.x = x
        self.dir = d

class Medusa:
    def __init__(self, y, x):
        self.y = y
        self.x = x

    def is_same(self, y, x):
        return self.y == y and self.x == x

    def end(self):
        global ey, ex
        return self.is_same(ey, ex)

    def move(self):
        nxt = self.find_nxt_pos()
        self.y = nxt.y
        self.x = nxt.x
        global warriors, wBoard, M, NO_WARRIOR
        for i in range(M):
            if warriors[i] == NO_WARRIOR:
                continue
            wy = warriors[i].y
            wx = warriors[i].x
            # 메두사와 전사의 좌표가 같으면 전사는 사라짐
            if self.is_same(wy, wx):
                wBoard[wy][wx] -= 1
                warriors[i] = NO_WARRIOR

    def find_nxt_pos(self):
        global dist, board, N
        curDist = dist[self.y][self.x]
        ret = NO_PAIR
        # 상하좌우 순서대로 다음 위치 후보를 찾음
        for dir in range(4):
            ny = self.y + dy[dir]
            nx = self.x + dx[dir]
            if OOB(ny, nx) or board[ny][nx] == 1:
                continue
            nDist = dist[ny][nx]
            if curDist > nDist:
                curDist = nDist
                ret = Pair(ny, nx)
        return ret

    def find_best_sight(self):
        best = NO_SIGHT
        for sDir in range(4):
            sight = Sight(self.y, self.x, sDir)
            if sight.is_higher(best):
                best = sight
        return best

class Sight:
    def __init__(self, my=None, mx=None, sDir=None):
        global N, wBoard, warriors, medusa
        # 기본 생성자: cnt=-1, sDir=5 (NO_SIGHT 용)
        if my is None or mx is None or sDir is None:
            self.cnt = -1
            self.sDir = 5
            self.sighted = None
            self.notFreezed = []
            return

        self.sDir = sDir
        # sighted 배열 초기화 (0으로 채움)
        self.sighted = [[0] * N for _ in range(N)]
        q = deque()
        # 시선 방향 조정: sDir 값에 따라 기준 방향 설정
        dir_val = sDir
        if sDir == 1:
            dir_val = 4
        elif sDir == 2:
            dir_val = 6
        elif sDir == 3:
            dir_val = 2

        freezeds = []
        for nDir in self.get_dirs_single(dir_val):
            ny = my + dy2[nDir]
            nx = mx + dx2[nDir]
            if OOB(ny, nx):
                continue
            self.sighted[ny][nx] = 1
            q.append(TupleX(ny, nx, nDir))
            if wBoard[ny][nx] != 0:
                freezeds.append(TupleX(ny, nx, nDir))
        while q:
            cur = q.popleft()
            for nDir in self.get_dirs(cur.dir, sDir):
                ny = cur.y + dy2[nDir]
                nx = cur.x + dx2[nDir]
                if OOB(ny, nx) or self.sighted[ny][nx] == 1:
                    continue
                if wBoard[ny][nx] != 0:
                    freezeds.append(TupleX(ny, nx, nDir))
                q.append(TupleX(ny, nx, nDir))
                self.sighted[ny][nx] = 1
        for freezed in freezeds:
            if self.sighted[freezed.y][freezed.x] == 2:
                continue
            self.shadow(freezed.y, freezed.x, dir_val)
        self.cnt = 0
        self.notFreezed = []
        for w in warriors:
            if w == NO_WARRIOR:
                continue
            wy = w.y
            wx = w.x
            if self.sighted[wy][wx] == 1:
                self.cnt += 1
            else:
                self.notFreezed.append(w)

    def shadow(self, y, x, dir_val):
        global N, medusa
        # medusa의 y 또는 x와 같으면 한 방향으로 그림자 투사
        if y == medusa.y or x == medusa.x:
            for dist_val in range(1, N + 1):
                ny = y + dy2[dir_val] * dist_val
                nx = x + dx2[dir_val] * dist_val
                if OOB(ny, nx):
                    continue
                self.sighted[ny][nx] = 2
        else:
            q = deque()
            q.append(Pair(y, x))
            self.sighted[y][x] = 2
            while q:
                cur = q.popleft()
                for nDir in self.get_dirs_xy(cur.y, cur.x, dir_val):
                    ny = cur.y + dy2[nDir]
                    nx = cur.x + dx2[nDir]
                    if OOB(ny, nx) or self.sighted[ny][nx] == 2:
                        continue
                    if self.sighted[ny][nx] == 1:
                        q.append(Pair(ny, nx))
                        self.sighted[ny][nx] = 2
            self.sighted[y][x] = 1

    def is_higher(self, s):
        # 우선 cnt가 높은 쪽, 같으면 sDir 값이 작은 쪽이 우선
        if self.cnt != s.cnt:
            return self.cnt > s.cnt
        return self.sDir < s.sDir

    def get_dirs_single(self, dir_val):
        if dir_val % 2 == 0:
            return [ (dir_val + 7) % 8, dir_val, (dir_val + 1) % 8 ]
        return [dir_val]

    def get_dirs_xy(self, wy, wx, dir_val):
        global medusa
        if dir_val == 0:
            if wy < medusa.y and wx < medusa.x:
                return [7, 0]
            if wy < medusa.y and wx > medusa.x:
                return [0, 1]
        elif dir_val == 2:
            if wy < medusa.y and wx > medusa.x:
                return [1, 2]
            if wy > medusa.y and wx > medusa.x:
                return [2, 3]
        elif dir_val == 4:
            if wy > medusa.y and wx > medusa.x:
                return [3, 4]
            if wy > medusa.y and wx < medusa.x:
                return [4, 5]
        elif dir_val == 6:
            if wy > medusa.y and wx < medusa.x:
                return [5, 6]
            if wy < medusa.y and wx < medusa.x:
                return [6, 7]
        return [dir_val]

    def get_dirs(self, cDir, sDir):
        if sDir == 0:
            if cDir == 7:
                return [7, 0]
            elif cDir == 0:
                return [0]
            else:
                return [0, 1]
        elif sDir == 1:
            if cDir == 3:
                return [3, 4]
            elif cDir == 4:
                return [4]
            else:
                return [4, 5]
        elif sDir == 2:
            if cDir == 5:
                return [5, 6]
            elif cDir == 6:
                return [6]
            else:
                return [6, 7]
        else:  # sDir == 3
            if cDir == 1:
                return [1, 2]
            elif cDir == 2:
                return [2]
            else:
                return [2, 3]

    def __str__(self):
        return f"cnt: {self.cnt}, sDir: {self.sDir}"

class Warrior:
    def __init__(self, id, y, x):
        self.id = id
        self.y = y
        self.x = x

    def __str__(self):
        return f"id: {self.id} | ({self.y}, {self.x})"

    def move(self, sighted):
        global medusa, wBoard, warriors, NO_WARRIOR
        distance = get_distance(self.y, self.x, medusa.y, medusa.x)
        direction = -1
        # 우선 상하좌우 중 이동
        for dir in range(4):
            ny = self.y + dy[dir]
            nx = self.x + dx[dir]
            if OOB(ny, nx) or sighted[ny][nx] == 1:
                continue
            D = get_distance(ny, nx, medusa.y, medusa.x)
            if distance > D:
                distance = D
                direction = dir
        if direction == -1:
            return Move(0, False)
        # 현재 위치에서 warrior 이동 및 wBoard 업데이트
        wBoard[self.y][self.x] -= 1
        self.y += dy[direction]
        self.x += dx[direction]
        if medusa.is_same(self.y, self.x):
            warriors[self.id] = NO_WARRIOR
            return Move(1, True)
        wBoard[self.y][self.x] += 1

        direction = -1
        # 추가 이동: 범위 2~5 (나머지 방향)
        for dir in range(2, 6):
            nDir = dir % 4
            ny = self.y + dy[nDir]
            nx = self.x + dx[nDir]
            if OOB(ny, nx) or sighted[ny][nx] == 1:
                continue
            D = get_distance(ny, nx, medusa.y, medusa.x)
            if distance > D:
                distance = D
                direction = nDir
        if direction == -1:
            return Move(1, False)
        wBoard[self.y][self.x] -= 1
        self.y += dy[direction]
        self.x += dx[direction]
        if medusa.is_same(self.y, self.x):
            warriors[self.id] = NO_WARRIOR
            return Move(2, True)
        wBoard[self.y][self.x] += 1
        return Move(2, False)

class Move:
    def __init__(self, dist, attack):
        self.dist = dist
        self.attack = attack

def get_distance(y1, x1, y2, x2):
    return abs(y1 - y2) + abs(x1 - x2)

def OOB(y, x):
    global N
    return y < 0 or y >= N or x < 0 or x >= N

def print_board(bd):
    global N, INF
    for y in range(N):
        for x in range(N):
            val = -1 if bd[y][x] == INF else bd[y][x]
            print(f"{val:3d}", end="")
        print()

def init():
    global ey, ex, dist, board, medusa, N
    q = deque()
    q.append(Pair(ey, ex))
    dist[ey][ex] = 0
    while q:
        cur = q.popleft()
        if medusa.is_same(cur.y, cur.x):
            break
        for dir in range(4):
            ny = cur.y + dy[dir]
            nx = cur.x + dx[dir]
            if OOB(ny, nx) or dist[ny][nx] != INF or board[ny][nx] == 1:
                continue
            q.append(Pair(ny, nx))
            dist[ny][nx] = dist[cur.y][cur.x] + 1

def move_all(best_sight):
    distSum = 0
    attackerCnt = 0
    for w in best_sight.notFreezed:
        move_result = w.move(best_sight.sighted)
        if move_result.attack:
            attackerCnt += 1
        distSum += move_result.dist
    return distSum, attackerCnt

def main():
    global N, M, sy, sx, ey, ex, board, wBoard, dist, medusa, warriors, INF, NO_WARRIOR
    input_data = sys.stdin.read().strip().split()
    if not input_data:
        return
    it = iter(input_data)
    N = int(next(it))
    M = int(next(it))
    sy = int(next(it))
    sx = int(next(it))
    ey = int(next(it))
    ex = int(next(it))
    # 메두사 초기화
    medusa_obj = Medusa(sy, sx)
    medusa = medusa_obj
    warriors = [None] * M
    wBoard = [[0] * N for _ in range(N)]
    for i in range(M):
        y = int(next(it))
        x = int(next(it))
        warriors[i] = Warrior(i, y, x)
        wBoard[y][x] += 1
    board = []
    dist = [[INF] * N for _ in range(N)]
    for y in range(N):
        row = [int(next(it)) for _ in range(N)]
        board.append(row)
    init()
    if dist[sy][sx] == INF:
        print(-1)
        return

    output_lines = []
    while True:
        medusa.move()
        if medusa.end():
            output_lines.append("0")
            break
        best_sight = medusa.find_best_sight()
        dsum, attacker_count = move_all(best_sight)
        output_lines.append(f"{dsum} {best_sight.cnt} {attacker_count}")
    print("\n".join(output_lines))

# NO_SIGHT 객체 (default Sight)
NO_SIGHT = Sight()
# NO_WARRIOR 전역 변수 초기화
NO_WARRIOR = Warrior(-1, -1, -1)

if __name__ == '__main__':
    main()
