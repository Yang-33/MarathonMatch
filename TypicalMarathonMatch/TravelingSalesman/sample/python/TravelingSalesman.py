import sys

N = int(raw_input())
posX = [];
posY = [];

for i in range(N):
	p = map(int,raw_input().split())
	posX.append(p[0])
	posY.append(p[1])

for i in range(N):
    print i

sys.stdout.flush()
