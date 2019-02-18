if [ "$#" -ne 7 ]
then
  echo "1st param: path to the working directory of the defects4j bug"
  echo "2nd param: path to the file (will be created) where you want to write out a list of the new classes created"
  echo "3rd param: path to the file (will be created) where you want to write out a list of the modified classes (the classes with failing tests taken out of them)"
  echo "4rd param: path to wherever javaparser/javaparser-core/target/classes is stored"
  echo "5th param: timeout length for unit tests (in milliseconds)"
  echo "6th param: path to the directory to place the modified test classes w/o timeouts to (this is for Daikon)"
	echo "7th param: the folder where the java 8 instalation is located"
  exit 0
fi

  BASEDIR=$PWD
  D4JWKDIR="$1"
  NEWCLASSESLIST="$2"
  MODIFIEDCLASSESLIST="$3"
  JAVAPARSER="$4"
  TIMEOUT="$5"
  DAIKONTESTS="$6"
  DIROFJAVA8="$7"
  PATHTOTESTS="$D4JWKDIR/moveNegativeTests_pathToTests"
  NEGTESTSLIST="$D4JWKDIR/moveNegativeTests_negTestsList"

  cd $D4JWKDIR
  defects4j export -p 'dir.src.tests' -o $PATHTOTESTS
  defects4j export -p 'tests.trigger' -o $NEGTESTSLIST
  cd $BASEDIR
  $DIROFJAVA8/bin/javac -cp "$JAVAPARSER:$BASEDIR" MoveNegativeTests.java
  $DIROFJAVA8/bin/java -cp "$JAVAPARSER:$BASEDIR" MoveNegativeTests $PATHTOTESTS $NEGTESTSLIST $D4JWKDIR $NEWCLASSESLIST $MODIFIEDCLASSESLIST $TIMEOUT $DAIKONTESTS

  rm $PATHTOTESTS
  rm $NEGTESTSLIST

  exit 0
