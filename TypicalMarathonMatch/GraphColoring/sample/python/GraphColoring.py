import sys

N,M = map(int,raw_input().split())
a = [];
b = [];

for i in range(M):
	p = map(int,raw_input().split())
	a.append(p[0])
	b.append(p[1])

for i in range(N):
    print i

sys.stdout.flush()
