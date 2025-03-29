from collections import namedtuple, deque
from typing import Tuple, List

def printBoard(board):
    n = len(board)
    m = len(board[0])

    for y in range(n):
        for x in range(m):
            print(f"{board[y][x]:3d}",end='')
        print()

def rotateLeft(y,x):
    return M - 1 - x, y

def rotateRight(y,x):
    return x, M - 1 - y

def init(plane, target, source):
    global machine, q
    if plane == 4:
        for y in range(M):
            for x in range(M):
                ty, tx = y + M, x + M
                target[ty][tx] = source[y][x]
                if target[ty][tx] == 2:
                    machine = Machine(ty,tx, getPlane(ty,tx))

    elif plane == 0:
        for y in range(M):
            for x in range(M):
                y1,x1 = rotateLeft(y,x)
                ty, tx = y1 + M, x1 + 2 * M
                target[ty][tx] = source[y][x]
    elif plane == 1:
        for y in range(M):
            for x in range(M):
                y1,x1 = rotateRight(y,x)
                ty, tx = y1 + M, x1
                target[ty][tx] = source[y][x]
    elif plane == 2:
        for y in range(M):
            for x in range(M):
                ty, tx = y + 2 * M, x + M
                target[ty][tx] = source[y][x]

    elif plane == 3:
        for y in range(M):
            for x in range(M):
                y1,x1 = rotateRight(y,x)
                y1,x1 = rotateRight(y1,x1)
                ty,tx = y1, x1 + M
                target[ty][tx] = source[y][x]



class Anormaly:
    def __init__(self, y,x,dir,v):
        self.y = y
        self.x = x
        self.dir = dir
        self.v = v

    def __repr__(self):
        return f"({self.y}, {self.x}) | dir: {self.dir} | v: {self.v}"

    def diffuse(self, turn):
        if turn % self.v == 0:
            dist = turn // self.v
            ny = self.y + dy[self.dir] * dist
            nx = self.x + dx[self.dir] * dist

            if OOB(ny,nx, 0, N, 0,N) or board[ny][nx] == 1 or board[ny][nx] == 4:
                return
            if board[ny][nx] == 0:
                board[ny][nx] = ANORMAL


dy = [0,0,1,-1]
dx = [1,-1,0,0]
INF = -1
def OOB(y, x, min_y, max_y, min_x, max_x) :
    return y<min_y or y>=max_y or x < min_x or x >= max_x

def findExitFrom(y,x) -> Tuple:
    global dist1
    q = deque([Pair(y,x)])
    dist1[y][x] = 0
    ret = Pair(-1,-1)
    while q:
        cur = q.popleft()


        if board[cur.y][cur.x] == 0:
            ret = Pair(cur.y, cur.x)
            break

        for dir in range(4):
            ny = cur.y + dy[dir]
            nx = cur.x + dx[dir]
            if OOB(ny,nx,0, N, 0, N) or board[ny][nx] == 1 or dist1[ny][nx] != INF:
                continue

            dist1[ny][nx] = dist1[cur.y][cur.x] + 1
            q.append(Pair(ny,nx))
    return ret

def findExit():
    for y in range(N):
        for x in range(N):
            if board[y][x] == 3:
                return findExitFrom(y,x), Pair(y,x)

    return NO_PAIR

def diffuseAll(turn):
    for anormal in anormalys:
        anormal.diffuse(turn)


ANORMAL = 2

def getPlane(y,x):
    # 동
    if not OOB(y,x, M, 2*M, 2*M, 3*M):
        return 0
    # 서
    if not OOB(y,x,M, 2*M, 0, M):
        return 1
    # 남
    if not OOB(y,x,2*M, 3*M, M , 2*M):
        return 2
    # 북
    if not OOB(y,x, 0, M, M, 2*M):
        return 3
    # 윗면
    if not OOB(y,x, M, 2*M, M, 2*M):
        return 4

    # 시간의 벽 바깥. 미지의 공간
    if OOB(y,x, 0, 3*M, 0, 3*M):
        return 5

    return -1

def rotate(y,x, plane, dir, c_start, n_start):
    if plane == 0:
        nDir = 1
        y -= c_start.y
        x -= c_start.x

        if dir == 2:
            y1,x1 = rotateRight(y,x)
        elif dir == 3:
            y1,x1 = rotateLeft(y,x)

        y2 = y1 + n_start.y
        x2 = x1 + n_start.x

        ny = y2 + dy[nDir]
        nx = x2 + dx[nDir]
        return ny,nx, getPlane(ny,nx)


    elif plane == 1:
        nDir = 0
        y -= c_start.y
        x -= c_start.x

        if dir == 2:
            y1,x1 = rotateLeft(y,x)
        elif dir == 3:
            y1,x1 = rotateRight(y,x)
        y2 = y1 + n_start.y
        x2 = x1 + n_start.x

        ny = y2 + dy[nDir]
        nx = x2 + dx[nDir]
        return ny,nx, getPlane(ny,nx)

    elif plane == 2:
        nDir = 3
        y -= c_start.y
        x -= c_start.x

        if dir == 0:
            y1,x1 = rotateLeft(y,x)
        elif dir == 1:
            y1,x1 = rotateRight(y,x)
        y2 = y1 + n_start.y
        x2 = x1 + n_start.x

        ny = y2 + dy[nDir]
        nx = x2 + dx[nDir]
        return ny,nx, getPlane(ny,nx)

    elif plane == 3:
        nDir = 2
        y -= c_start.y
        x -= c_start.x

        if dir == 0:
            y1,x1 = rotateRight(y,x)
        elif dir == 1:
            y1,x1 = rotateLeft(y,x)
        y2 = y1 + n_start.y
        x2 = x1 + n_start.x

        ny = y2 + dy[nDir]
        nx = x2 + dx[nDir]
        return ny,nx, getPlane(ny,nx)


class Machine:
    def __init__(self, y,x,plane):
        self.y = y
        self.x = x
        self.plane = plane
    def __repr__(self):
        return f"({self.y}, {self.x}) | plane: {self.plane}"



def getStartPoint(y,x):
    return Pair((y // M) * M, (x // M) * M)

def getNxtPos(y,x,plane, dir):
    if plane < 5:
        ny, nx = y + dy[dir], x + dx[dir]
        nPlane = getPlane(ny,nx)

        if nPlane != -1: # 윗면, 동서남북, 또는 시간의 벽 바깥이다.
            return (ny,nx, nPlane)
        else:
            c_start = getStartPoint(y,x)
            n_start = getStartPoint(ny,nx)

            return rotate(y,x,plane, dir, c_start, n_start)
    else:
        ny = y + dy[dir]
        nx = x + dx[dir]
        return ny,nx,plane

def mapping(y, x):
    ny = y % M + wall_start.y
    nx = x % M + wall_start.x

    if y == 3 * M:
        ny = wall_start.y + M
    if y == -1:
        ny = wall_start.y - 1

    if x == 3 * M:
        nx = wall_start.x + M
    if x == -1:
        nx = wall_start.x - 1

    return ny, nx


def move():
    global q, dist1, machine, dist2
    size = len(q)
    canMove = False
    for _ in range(size):
        cur = q.popleft() # 처음 시작은 시간의 벽이다.
        machine = cur
        # print(f"machine: {cur}")
        for dir in range(4):
            if cur.plane != 5: # 지금은 시간의 벽 안쪽
                ny, nx, nPlane = getNxtPos(cur.y, cur.x, cur.plane, dir)
                if nPlane == 5: # 시간의 벽 바깥. 즉 미지의 공간
                    # 시간의 벽의 좌표를 미지의 공간 좌표로 매핑
                    nny, nnx = mapping(ny, nx)

                    # if OOB(nny,nnx, 0, N, 0,N) or dist1[nny][nnx] != INF or board[nny][nnx] != 0:
                    if OOB(nny,nnx, 0,N, 0,N) or dist1[nny][nnx] != INF or board[nny][nnx] == 1 or board[nny][nnx] == 2:
                        continue


                    # 앞으로는 미지의 공간에서 작업하겠다
                    q.append(Machine(nny,nnx, 5))
                    dist1[nny][nnx] = dist2[cur.y][cur.x] + 1
                    canMove = True
                else: # 시간의 벽 안쪽
                    if tBoard[ny][nx] == 1 or dist2[ny][nx] != INF:
                        continue

                    q.append(Machine(ny,nx, nPlane))
                    dist2[ny][nx] = dist2[cur.y][cur.x] + 1
                    canMove = True
            else: # 지금은 미지의 공간
                ny,nx, _ = getNxtPos(cur.y, cur.x, cur.plane, dir)

                if OOB(ny,nx, 0, N, 0,N) or dist1[ny][nx] != INF or board[ny][nx] == 1 or board[ny][nx] == 2:
                    continue

                q.append(Machine(ny,nx, 5))
                dist1[ny][nx] = dist1[cur.y][cur.x]+1
                canMove = True
    return canMove


def finish():

    return dist1[end.y][end.x] != INF


if __name__ == '__main__':
    N, M, F = map(int,input().split())
    board = []
    Pair  = namedtuple('Pair', ['y', 'x'])
    NO_PAIR = Pair(-1,-1)
    dist1 = [[INF] * N for _ in range(N)]
    dist2 = [[INF] * (3 * M) for _ in range(3*M)]

    for y in range(N):
        board.append([])

        tmp = list(map(int,input().split()))

        for x in range(N):
            board[y].append(tmp[x])
            if board[y][x] == 4:
                end = Pair(y,x)

    # exit : 시간의 벽을 빠져나오는 곳
    # wall_start : 시간의 벽의 좌상단
    EXIT, wall_start = findExit()
    dist1 = [[INF] * N for _ in range(N)]



    tBoard=[[7] * (3*M) for _ in range(3*M)]

    machine = None

    # 시간의 벽 초기화
    for plane in range(5):
        tmp = []
        for y in range(M):
            tmp.append(list(map(int,input().split())))

        init(plane,tBoard, tmp)

    anormalys = []
    for _ in range(F):
        y,x,dir,v = map(int,input().split())
        anormalys.append(Anormaly(y,x,dir,v))
        board[y][x] = ANORMAL


    # print("exit: ", EXIT)
    q = deque()
    q.append(machine)
    dist2[machine.y][machine.x] = 0
    turn = 0
    while not finish():
        turn += 1
        # print("-----")
        # print("turn: ",turn)

        # 1. 이상현상 확산
        diffuseAll(turn)

        # 2. exit가 이상현상에 의해 막히면 즉시 -1

        # 3. 타임 머신의 이동
        canMove = move()
        if not canMove:
            print(-1)
            exit()
        # print("after diffuse")
        # printBoard(board)
        # print("after move")
        # print("machine: ", machine)
        # printBoard(dist1)






    print(turn)







