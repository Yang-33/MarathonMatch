#include <iostream>
#include <vector>
using namespace std;

int main () {

    int N; 
    cin >> N;

    vector < int > posX(N), posY(N);
    for (int i = 0; i < N; i++) {
        cin >> posX[i] >> posY[i];
    }

    for (int i = 0; i < N; i++) {
        cout << i << endl;
    }

    return 0;
    
}
