#include <iostream>
#include <vector>
using namespace std;

int main(int argc, char *argv[]) {

    int N; 
    cin >> N;

    vector < int > h(N), w(N);
    for (int i = 0; i < N; i++) {
        cin >> w[i] >> h[i];
    }

    for (int i = 0; i < N; i++) {
        cout << 50 * (i % 20) << " " << 50 * (i / 20) << endl;
    }

    return 0;
    
}
