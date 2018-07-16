if [ "$#" -ne 4 ]
then
  echo "1st param: path to the working directory of the defects4j bug"
  echo "2nd param: path to the file (will be created) where you want to write out a list of the new classes created"
  echo "3rd param: path to the file (will be created) where you want to write out a list of the modified classes (the classes with failing tests taken out of them)"
  echo "4rd param: path to wherever javaparser/javaparser-core/target/classes is stored"
  exit 0
fi

  BASEDIR=$PWD
  D4JWKDIR="$1"
  NEWCLASSESLIST="$2"
  MODIFIEDCLASSESLIST="$3"
  JAVAPARSER="$4"
  PATHTOTESTS="$BASEDIR/moveNegativeTests_pathToTests"
  NEGTESTSLIST="$BASEDIR/moveNegativeTests_negTestsList"

  cd $D4JWKDIR
  defects4j export -p 'dir.src.tests' -o $PATHTOTESTS
  defects4j export -p 'tests.trigger' -o $NEGTESTSLIST
  cd $BASEDIR
  javac -cp "$JAVAPARSER:$BASEDIR" MoveNegativeTests.java
  java -cp "$JAVAPARSER:$BASEDIR" MoveNegativeTests $PATHTOTESTS $NEGTESTSLIST $D4JWKDIR $NEWCLASSESLIST $MODIFIEDCLASSESLIST

  rm $PATHTOTESTS
  rm $NEGTESTSLIST

  exit 0
