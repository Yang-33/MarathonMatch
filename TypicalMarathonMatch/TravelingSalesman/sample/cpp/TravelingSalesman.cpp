#include <bits/stdc++.h>
using namespace std;

using VS = vector<string>;    using LL = long long;
using VI = vector<int>;       using VVI = vector<VI>;
using PII = pair<int, int>;   using PLL = pair<LL, LL>;
using VL = vector<LL>;        using VVL = vector<VL>;

#define ALL(a)  begin((a)),end((a))
#define RALL(a) (a).rbegin(), (a).rend()
#define SZ(a) int((a).size())
#define SORT(c) sort(ALL((c)))
#define RSORT(c) sort(RALL((c)))
#define UNIQ(c) (c).erase(unique(ALL((c))), end((c)))
#define FOR(i, s, e) for (int(i) = (s); (i) < (e); (i)++)
#define FORR(i, s, e) for (int(i) = (s); (i) > (e); (i)--)
//#pragma GCC optimize ("-O3") 
#ifdef YANG33
#include "mydebug.hpp"
#else
#define DD(x) 
#endif
const int INF = 1e9;                          const LL LINF = 1e16;
const LL MOD = 1000000007;                    const double PI = acos(-1.0);
int DX[8] = { 0, 0, 1, -1, 1, 1, -1, -1 };    int DY[8] = { 1, -1, 0, 0, 1, -1, 1, -1 };

/* ------問題------



-----問題ここまで----- */
/* -----解説等-----

なるべく関数を切り出してアをしないようにする

----解説ここまで---- */


class RandGen {
	inline void InitRand() { srand((unsigned int)time(NULL)); }
	long long myrand() {
		return abs(rand() * rand() + 2311 * rand() + 1992);
	}
public:
	RandGen() { InitRand(); }
	long long getrand() { return myrand(); }
	double getrandDouble() { // [0,1)
		return (myrand()) * (1.0 / LLONG_MAX);
	}
};

class TimeChecker {
	clock_t TimeStart;
	clock_t TimeEnd;
public:
	double T;
	TimeChecker() {}
	TimeChecker(double T) :TimeStart(clock()), TimeEnd(clock() + CLOCKS_PER_SEC * T), T(T) {
		fprintf(stderr, "you can do sth for %.3f sec\n", double(TimeEnd - TimeStart) / CLOCKS_PER_SEC);
	}
	bool ScheduleOK() {
		return clock() < TimeEnd;
	}
	clock_t begin() { return TimeStart; }
	clock_t end() { return TimeEnd; }
	clock_t now() { return clock(); }
};



// 始点から1周するオーソドックスなTSP
// 焼きなまし編

class TSP_Solver {
	using dataType = double;
	using indexType = int;
	// 元データ
	vector<dataType> posX, posY;
	indexType DataN;

	// 返却値
	vector<indexType> Path;

	// 大域変数
	dataType Score;
	RandGen Randdevice;
	TimeChecker Timechecker;
	const double Dt;
	// 関数
	indexType indexAdjust(indexType a) { return (a) % DataN; }
	dataType pow2(dataType x) { return x * x; }

	// アクセスは調整されるので適当にして良い
	dataType dist(indexType a, indexType b) {
		a = Path[indexAdjust(a)], b = Path[indexAdjust(b)];
		return sqrt(
			pow2(posX[a] - posX[b])
			+ pow2(posY[a] - posY[b]));
	}

	dataType initial_solution() {
		dataType score = 0;
		std::random_device rdev{}; //乱数生成器を生成
		std::mt19937 mt(rdev()); //メルセンヌ・ツイスタ生成器を使用

		FOR(i, 0, DataN) {
			Path.push_back(i);
		}
		shuffle(ALL(Path), mt);
		FOR(i, 0, DataN) { // 1周するため
			score += dist(i, i + 1);
		}
		return score;
	};
	pair<indexType, indexType>changePoints;

	// 辺をswapしたときのスコアdiff
	dataType swap2EdgeScoreDiff(bool rnd = 1) {
		indexType A, B;
		if (rnd) {
			A = indexAdjust(Randdevice.getrand() % DataN);
			B = indexAdjust(Randdevice.getrand() % DataN);
		}
		else {
			A = changePoints.first;
			B = changePoints.second;
		}
		if (A == B)return 0;
		if (A > B)swap(A, B);
		indexType Anx = indexAdjust(A + 1);
		indexType Bnx = indexAdjust(B + 1);

		dataType preAedge = dist(A, Anx);
		dataType preBedge = dist(B, Bnx);

		dataType aftAedge = dist(A, B);
		dataType aftBedge = dist(Anx, Bnx);
		changePoints = { A,B };

		dataType res = -(preAedge + preBedge) + (aftAedge + aftBedge);

		return res;
	}
	// 変化点が与えられているのでスコアの変更とパスの変更をする

	void swapExecute() {
		Score += swap2EdgeScoreDiff(0);
		// reverse(TODO: bst)
		// [A+1,B]をrev
		indexType Anx = indexAdjust(changePoints.first + 1);
		indexType B = indexAdjust(changePoints.second);
		for (indexType i = Anx, j = B; i < j; i++, j--) {
			swap(Path[i], Path[j]);
		}
	}

	// 焼きなまし
	// 
	bool simulatedAnnealing(double diff) {
		if (diff <= 0)return 1;
		const long long t = Timechecker.now();
		const long long T = Timechecker.begin() + CLOCKS_PER_SEC * Dt;
		if (t > T)return 0;
		double startTemp = 100;
		double endTemp = 1;
		double temp = startTemp + (endTemp - startTemp) * t / T;
		const long long R = 10000;
		double probability = exp((-diff) / temp);
		double randd = ((double)(Randdevice.getrand() % R) / R);
		bool FORCE_NEXT = probability > randd;
		return FORCE_NEXT;
	}

public:
	TSP_Solver(const vector<dataType>& posx, const vector<dataType>& posy, const double T, const double ANT)
		: posX(posx), posY(posy), DataN(SZ(posX)), Randdevice(RandGen()), Timechecker(T), Dt(ANT) {}

	vector<indexType> solve() {
		Score = initial_solution();
		DD(de("initial", Score));
		// 時間内までやる
		// path の 2本を交換することからはじめる
		// 交換して良くなるなら遷移
		TimeChecker	smchecker(Dt);
		while (smchecker.ScheduleOK()) {
			FOR(_, 0, 1000) {
				dataType scoreDiff = swap2EdgeScoreDiff();
				bool forcenext = simulatedAnnealing(scoreDiff);
				bool accept = (scoreDiff < 0 || forcenext);
				if (accept) { // 適宜かえる
					swapExecute();
				}
			}
		}
		Timechecker = TimeChecker(Timechecker.T);
		while (Timechecker.ScheduleOK()) {
			FOR(_, 0, 1000) {
				dataType scoreDiff = swap2EdgeScoreDiff();
				bool accept = (scoreDiff < 0);
				if (accept) { // 適宜かえる
					swapExecute();
				}
			}
		}
		DD(de("res", Score));
		return Path;
	}
	dataType getFinalScore() {
		solve();
		return Score;
	}

};



int main() {

	int N;
	cin >> N;

	vector < double > posX(N), posY(N);
	for (int i = 0; i < N; i++) {
		cin >> posX[i] >> posY[i];
	}
	TSP_Solver sol(posX, posY, 100, 10);
	vector<int> order = sol.solve();

	for (int i : order) {
		cout << i << endl;
	}

	return 0;

}