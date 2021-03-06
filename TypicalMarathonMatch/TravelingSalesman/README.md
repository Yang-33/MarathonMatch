# Traveling Salesman

## 問題文
巡回セールスマン問題です．座標が重複しない0 ~ N - 1までのN個の頂点の座標xi, yiが与えられるので，訪れる順に頂点番号を出力してください．

## 制約
50 <= N <= 1000  
0 <= xi, yi <= 100  
i ≠ j の時, (xi, yi) ≠ (xj, yj)


### 入力
```
N
x0 y0
x1 x1
.
.
xN-1 yN-1
```

### 出力
```
v0
v1
.
.
vN-1
```

## スコア
v0 → v1 → ... → vN-1 → V0 と頂点を訪れた時の移動距離をスコアとする．

## テスタ
TopCoder の Marathon Match と同じです．```"<command>"```にプログラムの実行コマンド，```<seed>```に乱数のシードを入れてください．
```sh
$ java -jar tester.jar -exec "<command>" -seed <seed>
```
その他オプション
```
-vis  : ビジュアライズ
-save : 画像の保存
-num  : 頂点番号の描画
```


## 目安？
適当に焼き鈍すとこれぐらいになった．  
1)  1378.8662340957915
2)  1418.0769043547557
3)  1812.6556612111692
4)  958.281235582922
5)  1097.787930313046
6)  2253.558897084062
7)  1747.8310279198308
8)  1010.3108701430247
9)  1164.9143856031271
10) 1532.9861355671067  
 
## Yang33の結果10秒焼きなまし,100秒Random 2opt(全探索はしない)
case:1
Score = 1531.5139437078114
case:2
Score = 1567.0568518521745
case:3
Score = 1994.175503018277
case:4
Score = 1049.0746873034768
case:5
Score = 1183.526173793413
case:6
Score = 2540.5091372998654
case:7
Score = 1943.0824058814333
case:8
Score = 1136.7671924728015
case:9
Score = 1262.6947142207378
case:10
Score = 1700.665122225083

ちょっと多い...

##  参考文献

[1] [焼き鈍しのコツ Ver. 1.2 - ジジイのプログラミング](http://shindannin.hatenadiary.com/entry/20121224/1356364040)  
