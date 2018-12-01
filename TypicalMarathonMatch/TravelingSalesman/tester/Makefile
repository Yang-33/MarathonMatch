###############################################################################
##### Makefile                                                             ####
###############################################################################

JAVAC  = javac
JAR    = jar
TARGET = Tester

.PHONY: all clean

###############################################################################

all: $(TARGET).jar

$(TARGET).jar: tester.mf *.class
	$(JAR) cvfm $(TARGET).jar $^

*.class: $(TARGET).java
	$(JAVAC) $^
	
clean:
	rm -f *.class *.jar
