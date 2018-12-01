#Makefile
ALLSOURCE = Tester.java
JAVAC = javac

.PHONY: all
all: comp
comp:
	${JAVAC} ${ALLSOURCE}
	jar cvfm tester.jar tester.mf *.class

.PHONY: clean
clean:
	rm -rf *.jar *.class
