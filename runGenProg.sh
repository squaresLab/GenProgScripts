export D4J_HOME="/home/ylyu1/genprog/defects4j"
export GP4J_HOME="/home/ylyu1/genprog/genprog4java"
export DAIKONDIR="/home/ylyu1/genprog/daikon-5.6.6"

P1=$1
P2=$2
P3=allHuman
P4=20
P5=BuggyGP4J$3
P6=0
P7=4
P8=false
P9=/usr/lib/jvm/java-8-openjdk-amd64
P10=/usr/lib/jvm/java-8-openjdk-amd64
P11="false"
P12=\"\"
P13="false"
P14=\"\"
P15=target/test-classes/
P16=300000
#5 minutes, needs to be long for Daikon
P17=$3

#cd GenProgScripts
sh runGenProgForBugExperimental.sh $P1 $P2 $P3 $P4 $P5 $P6 $P7 $P8 $P9 $P10 $P11 $P12 $P13 $P14 $P15 $P16 $P17
