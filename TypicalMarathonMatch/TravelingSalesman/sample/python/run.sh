TESTER=../../tester/tester.jar

for i in `seq 1 10`; do 
    echo "case:$i"
    java -jar $TESTER -exec "python TravelingSalesman.py" -seed $i -vis -save -num
done
