import sys

N,M = map(int,raw_input().split())
depotX,depotY = map(int,raw_input().split())

posX = [];
posY = [];
for i in range(N):
	p = map(int,raw_input().split())
	posX.append(p[0])
	posY.append(p[1])

cap = []
speed = []
for i in range(M):
	p = map(int,raw_input().split())
	cap.append(p[0])
	speed.append(p[1])

ans = [[0 for i in range(0)] for j in range(M)]

for i in range(N):
	ans[i % M].append(i)

for i in range(M):
	print len(ans[i])
	for j in ans[i]:
		print j

sys.stdout.flush()
