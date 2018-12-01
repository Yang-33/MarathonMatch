#include <iostream>
#include <vector>
using namespace std;

int main(int argc, char *argv[]) {

    int N,M; 
    cin >> N >> M;

    int depotX, depotY;
    vector < int > posX(N), posY(N);
    vector < int > cap(M), speed(M);

    cin >> depotX >> depotY;
    for (int i = 0; i < N; i++) cin >> posX[i] >> posY[i];
    for (int i = 0; i < M; i++) cin >> cap[i] >> speed[i];

    vector < vector < int > > ans(M);
    for (int i = 0; i < N; i++) {
        ans[i % M].push_back(i);
    }

    for (int i = 0; i < M; i++) {
        cout << ans[i].size();
        for (int j = 0; j < ans[i].size(); j++) {
            cout << " " << ans[i][j];
        }
        cout << endl;
    }

    return 0;
    
}
