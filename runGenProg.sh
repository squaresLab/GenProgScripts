export D4J_HOME="/Users/zhendeveloper/Desktop/LabBox/defects4j"
export GP4J_HOME="/Users/zhendeveloper/git/genprog4java"
export DAIKONDIR="/Users/zhendeveloper/Desktop/LabBox/daikonparent/daikon-5.6.5"

P1=$1
P2=$2
P3=allHuman
P4=20
P5=BuggyGP4J
P6=0
P7=4
P8=false
P9=/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home
P10=/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home
P11="false"
P12=\"\"
P13="false"
P14=\"\"
P15=target/test-classes/
P16=300000
#5 minutes, needs to be long for Daikon

cd GenProgScripts
sh runGenProgForBugExperimental.sh $P1 $P2 $P3 $P4 $P5 $P6 $P7 $P8 $P9 $P10 $P11 $P12 $P13 $P14 $P15 $P16
