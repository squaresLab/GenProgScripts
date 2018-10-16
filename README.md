# GenProgScripts
The purpose of this repo is to allow user to run GenProg in the Defects4J bugs

Use runGenProgForBugExperimental.sh
# 1st param is the project in upper case (ex: Lang, Chart, Closure, Math, Time)
# 2nd param is the bug number (ex: 1,2,3,4,...)
# 3th param is the option of running the test suite (ex: allHuman, oneHuman, oneGenerated)
# 4th param is the test suite sample size (ex: 1, 100)
# 5th param is the folder where the bug files will be cloned to. Starting from $D4J_HOME (Ex: ExamplesCheckedOut)
# 6th param is the initial seed. It will then increase the seeds by adding 1 until it gets to the number in the next param.
# 7th param is the final seed.
# 8th param is on if the purpose is to test only fault loc and not really trying to find a patch. When it has reached the end of fault localization it will stop.
# 9th param is the folder where the java 7 instalation is located
# 10th param is the folder where the java 8 instalation is located
# 11th param is set to \"true\" if negative tests are to be specified using sampled tests else set this to \"false\""
# 12th param is the path to file containing sampled negative tests"
# 13th param is set to \"true\" if positive tests are to be specified using sampled tests else set this to \"false\""
# 14th param is the path to file containing sampled positive tests"
# 15th param is the path to the directory containing the class files of the tests relative to the path to the defects4j bug
# 16th param is the timeout length for unit tests (in milliseconds)
# 17th param is the mode of the invariant checker
