#1st param: project name (Math, Lang, ...)
#2nd param: bug number

P1=$1
P2=$2
P3=allHuman
P4=20
P5=BuggyGP4J
P6=0
P7=4
P8=false
P9=$JAVA7_HOME
P10=$JAVA_HOME
P11="false"
P12=\"\"
P13="false"
P14=\"\"
P15=target/test-classes/
P16=300000
#5 minutes, needs to be long for Daikon
P17=0 #deprecated, not needed for mutation testing

#cd GenProgScripts
sh runGenProgForBugExperimental.sh $P1 $P2 $P3 $P4 $P5 $P6 $P7 $P8 $P9 $P10 $P11 $P12 $P13 $P14 $P15 $P16 $P17
