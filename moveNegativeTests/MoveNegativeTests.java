import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
//import com.github.javaparser.ast.body.type.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.util.List;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Collection;
import java.util.HashSet;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.EnumSet;


//requires java 1.8
public class MoveNegativeTests
{
  static String pathToSrcOfTests = null;
  static Collection<String> newClasses = new LinkedList<>();
  static String pathToListOfNewClasses = null;
  static Collection<String> modifiedClasses = new LinkedList<>();
  static String pathToListOfModifiedClasses = null;
  static int timeoutLength = 1000;

  public static void main(String[] args)
  {
    if (args.length != 5)
    {
      System.err.println("0th param: path to the file containing the path to the source directory of tests");
      System.err.println("1st param: path to the list of failing test cases");
      System.err.println("2nd param: path to the working directory of the defects4j bug");
      System.err.println("3rd param: path to the file to write the list of new test classes to");
      System.err.println("4th param: path to the file to write the list of modified test classes to");
      return;
    }

    pathToListOfNewClasses = args[3];
    pathToListOfModifiedClasses = args[4];
    String wkdirD4j = args[2].endsWith(File.separator) ? args[2] : args[2] + File.separator; //add the separator to the end of the path
    try
    {
      pathToSrcOfTests = wkdirD4j + Files.readAllLines(Paths.get(args[0]), Charset.defaultCharset()).get(0);
      if (!pathToSrcOfTests.endsWith(File.separator))
        pathToSrcOfTests += File.separator;
    } catch (IOException e) {
      throw new IllegalArgumentException("Path to file containing the path to the source directory of tests is most likely bad", e);
    }
    List<String> failingTestCases;
    try
    {
      failingTestCases = Files.readAllLines(Paths.get(args[1]), Charset.defaultCharset());
    } catch (IOException e) {
      throw new IllegalArgumentException("Path to the file containing a list of failing test cases is most likely bad", e);
    }
    //HashMap uses .equals, so using Strings as keys is safe
    Map<String, Collection<String>> classToMethodsMapping = new HashMap<>();
    for (String failingTest : failingTestCases)
    {
      String[] classAndMethod = failingTest.split("::");
      if(classAndMethod.length != 2)
        throw new RuntimeException("Format of the failing test classes file is unexpected.");
      String clazz = classAndMethod[0];
      String method = classAndMethod[1];
      if(classToMethodsMapping.containsKey(clazz))
      {
        classToMethodsMapping.get(clazz).add(method);
      }
      else
      {
        Collection<String> methodsOfClazz = new HashSet<String>();
        methodsOfClazz.add(method);
        classToMethodsMapping.put(clazz, methodsOfClazz);
      }
    }

    //now process all of the classes
    for(String clazz : classToMethodsMapping.keySet())
    {
      processClass(clazz, classToMethodsMapping.get(clazz));
    }

    //write out the file w/ a list of all newly created java classes
    try
    {
      FileWriter fw = new FileWriter(pathToListOfNewClasses);
      for (String newClass : newClasses)
      {
        fw.write(newClass, 0, newClass.length());
        fw.write('\n');
      }
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    //write out the file w/ a list of all modified java classes
    try
    {
      FileWriter fw = new FileWriter(pathToListOfModifiedClasses);
      for (String modifiedClass : modifiedClasses)
      {
        fw.write(modifiedClass, 0, modifiedClass.length());
        fw.write('\n');
      }
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void processClass(String clazz, Collection<String> methodsOfClazz)
  {
    String clazzDotsReplaced = clazz.replace('.', File.separatorChar);
    String pathToClass = pathToSrcOfTests + clazzDotsReplaced + ".java";
    CompilationUnit origCU;
    try
    {
      origCU = JavaParser.parse(new File(pathToClass));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Something is probably wrong with the pathToClass", e);
    }

    //set up the new java class for failing tests
    CompilationUnit newCU = new CompilationUnit();
    Optional<PackageDeclaration> packages = origCU.getPackageDeclaration();
    if(packages.isPresent())
      newCU.setPackageDeclaration(packages.get());
    NodeList<ImportDeclaration> imports = origCU.getImports();
    if(imports != null)
      newCU.setImports(imports); //copy all of the imports from the original test class
    //declare a new class
    String origShortClassName = getShortClassName(clazzDotsReplaced); //working with dots is a pain, due to regex
    Optional<ClassOrInterfaceDeclaration> classDecOptional = origCU.getClassByName(origShortClassName);
    if(!classDecOptional.isPresent())
      throw new RuntimeException("The class with name " + origShortClassName + " was not found");
    ClassOrInterfaceDeclaration classDecOrig = classDecOptional.get();
    String newShortClassName = origShortClassName + "_FailingTest";
    ClassOrInterfaceDeclaration classDecNew = newCU.addClass(newShortClassName);
    classDecNew.setExtendedTypes(classDecOrig.getExtendedTypes());
    classDecNew.setImplementedTypes(classDecOrig.getImplementedTypes());

    //search for methods to move
    NodeList<BodyDeclaration<?>> membersOfOrigClass = classDecOrig.getMembers();
    Collection<MethodDeclaration> methodsToRemove = new LinkedList<>();
    for (BodyDeclaration<?> member : membersOfOrigClass)
    {
      if(member instanceof MethodDeclaration)
      {
        MethodDeclaration method = (MethodDeclaration) member;
        if(methodsOfClazz.contains(method.getName().asString()))
        {
          //this method needs to move
          //add a new copy of the method into the new file
          MethodDeclaration methodCopy = method.clone();
          classDecNew.addMember(methodCopy);
          //schedule to remove the original copy of the method from the old file
          //cannot remove method immediately, as it will cause a ConcurrentModificationException
          methodsToRemove.add(method);
          //method.remove(); //method inherited from its parent Node->BodyDeclaration->CallableDeclaration
        }
      }
    }

    //remove the failing methods from the original file
    for (MethodDeclaration method : methodsToRemove)
      method.remove(); //method inherited from its parent Node->BodyDeclaration->CallableDeclaration

    //add timeout checks to both original and new files
    addTimeoutChecks(origCU, classDecOrig);
    addTimeoutChecks(newCU, classDecNew);

    //write out the new file
    try
    {
      String pathOfNewFile = pathToSrcOfTests + clazzDotsReplaced + "_FailingTest.java";
      FileWriter fw = new FileWriter(pathOfNewFile, false);
      String stuffToWrite = newCU.toString();
      fw.write(stuffToWrite, 0, stuffToWrite.length());
      fw.close();
      newClasses.add(clazz + "_FailingTest");
      modifiedClasses.add(clazz);
    } catch (IOException e) {
      e.printStackTrace();
    }

    //change the old file
    try
    {
      FileWriter fw = new FileWriter(pathToSrcOfTests + clazzDotsReplaced + ".java", false);
      String stuffToWrite = origCU.toString();
      fw.write(stuffToWrite, 0, stuffToWrite.length());
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void addTimeoutChecks(CompilationUnit cu, ClassOrInterfaceDeclaration classDec)
  {
    //repeated imports is OK, no need to check whether import is already existing
    cu.addImport("org.junit.Rule");
    cu.addImport("org.junit.Test");
    cu.addImport("org.junit.rules.Timeout");
    cu.addImport("java.util.concurrent.CountDownLatch");

    VariableDeclarator latchVD = new VariableDeclarator(JavaParser.parseClassOrInterfaceType("CountDownLatch"), "latch")
        .setInitializer("new CountDownLatch(1)");
    FieldDeclaration latchFD = new FieldDeclaration(EnumSet.<Modifier>of(Modifier.PRIVATE, Modifier.FINAL), latchVD);
    classDec.addMember(latchFD);

    VariableDeclarator timeoutVD = new VariableDeclarator(JavaParser.parseClassOrInterfaceType("Timeout"), "globalTimeout")
        .setInitializer("Timeout.seconds(" + timeoutLength + ")");
    FieldDeclaration timeoutFD = new FieldDeclaration(EnumSet.<Modifier>of(Modifier.PUBLIC), timeoutVD);
    classDec.addMember(timeoutFD);
  }

  private static String getShortClassName(String fullClassName)
  {
    String[] segments = fullClassName.split("/");
    return segments[segments.length - 1];
  }
}
