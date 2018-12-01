import sys

N = int(raw_input())
w = []
h = []

for i in range(N):
	p = map(int,raw_input().split())
	w.append(p[0])
	h.append(p[1])

for i in range(N):
    print str(50 * (i % 20)) + " " + str(50 * (i / 20))

sys.stdout.flush()
