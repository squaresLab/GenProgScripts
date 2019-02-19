# GenProgScripts
The purpose of this repo is to allow user to run GenProg in the Defects4J bugs

Tested with the following configuration:
* macOS Mojave 10.14.2
* Java SE 7u80 + 8u45
* Defects4J 1.4.0, commit fcf6b9b (Jan. 19, 2019)
* Daikon 5.7.2
* GenProg4Java+Invariants, commit 8182bc8 (Feb. 19, 2019). https://github.com/squaresLab/genprog4java/tree/Yiwei-Lyu-mini-proj
## Preconditions:

* The variable D4J_HOME should be directed to the folder where defects4j is installed.
* The variable GP4J_HOME should be directed to the folder where genprog4java is installed.
* The variable DAIKONDIR should be directed to the folder where daikon is installed.

# Instructions:

Use runGenProgForBugExperimental.sh

1st param is the project in upper case (ex: Lang, Chart, Closure (doesn't currently work), Math, Time)

2nd param is the bug number (ex: 1,2,3,4,...)

3th param is the option of running the test suite (ex: allHuman, oneHuman, oneGenerated)

4th param is the test suite sample size (ex: 1, 100)

5th param is the folder where the bug files will be cloned to. Starting from $D4J_HOME (Ex: ExamplesCheckedOut)

6th param is the initial seed. It will then increase the seeds by adding 1 until it gets to the number in the next param.

7th param is the final seed.

8th param is \"true\" if the purpose is to test only fault loc and not really trying to find a patch. When it has reached the end of fault localization it will stop. Otherwise, set to \"false\".

9th param is the folder where the java 7 instalation is located

10th param is the folder where the java 8 instalation is located

11th param is set to \"true\" if negative tests are to be specified using sampled tests else set this to \"false\""

12th param is the path to file containing sampled negative tests"

13th param is set to \"true\" if positive tests are to be specified using sampled tests else set this to \"false\""

14th param is the path to file containing sampled positive tests"

15th param is the path to the directory containing the class files of the tests relative to the path to the defects4j bug
- For example, the relative path to test class files of Lang projects is `target/tests/`, the relative path for Math projects is `target/test-classes/`

16th param is the timeout length for unit tests (in milliseconds)
- We're using `3000` milliseconds

17th param is the repair approach: 
- 0: don't incorporate invariant analysis into fitness.
- 1: deprecated
- 2: deprecated
- 3: optimize only for invariant diversity
- 4: use NSGA-II: optimize for test cases and invariant diversity.

# Test set:

We constructed our test set by stratifying the set of bugs into three categories: 
- bugs with only one repair action (as defined by Sobreira et. al's dissection of defects4j)
- bugs with more than one repair action and only one failing test case
- bugs with more than one repair action and more than one failing test case.

For all three categories, 
we sample three bugs from each of the six projects in defects4j, forming a test set of 54 total bugs.
