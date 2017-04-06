# Classpath and command line

## Compiling from command line

This practice session will start with closing your IDE. 
Today we will learn how to compile and run Java from command line. 

A Java application consists of many classes and external dependencies. 
External dependencies are jar files containing useful compiled code that your classes can reference. 
How does Java find all these jars when you start your application so it can load the code? 
How does it find the classes that you created yourself? 
 
The JVM (Java Virtual Machine) finds classes and other resources from the **classpath**.
The classpath is a **list of jar files and directories** (relative or absolute paths).
When starting the JVM, the user can specify the classpath elements using a command line argument.
The elements in the command line argument are separated by colon ':' in linux and semicolon ';' in windows. 
When the classpath is not specified, then the current working directory will be used as the only classpath element. 
Note that system classes (*java.lang* etc.) are loaded from a separate bootclasspath. 

When the classes are organized in packages, then the classpath must contain the directories that contain the packages.
For example, then you have classes *app.FileResult* and *app.StorageApp*, then the class files must be in the directory *app* and *app* must be in the directory contained in the classpath. 

```
some_classpath_dir
   └── app
        ├── FileResult.class
        └── StorageApp.class
```

When the JVM needs to load a class, it will search the locations in the classpath in the order they are specified and use the first match.
The files in both jars and directories must follow the usual java package structure, i.e. the class file must be located within a directory matching its package name. 

The java compiler (*javac*) uses the same classpath mechanism for finding dependencies during compile time. 
The syntax for specifying the classpath is the same as for the java runtime. 

### Compiling the application

For the first part we'll be using a small sample application called StorageApp. 
StorageApp uses an external library called *google gson* which can convert objects into strings and back. 
The StorageApp creates an object, converts it into a string and back and prints out the results. 
We will try to compile and run it on the command line. 

1. download *storage.zip* from the root of this repository.
2. unzip *storage.zip*.
3. open the command line and navigate to the unpacked *storage* directory. 
   you don't need to leave that directory for the rest of this chapter.  
4. check that the source files are in *src* and the gson jar is in *lib*. 
   read the source code.  
5. create a directory named *build*. 
   this is where we want the compiler to put the compiled class files.
   by default the compiler will put the class files next to the source, but packaging the application is easier when the class files are in a separate directory.
6. run `javac -help` on the command line.
7. use *javac* to compile the code in *src*. 
   
   set the necessary options to generate all debugging info, specify *utf-8* for source file encoding and place generated class files in *build*. 
   note that you should pass all source files to *javac* in one go, not compile them one-by-one.   
   
   the source code references classes from package *com.google.gson*. 
   try to compile the classes without setting the classpath. 
   what error do you get? 
   try again, but this time add gson-2.7.jar to classpath. 

8. make sure the class files were created in *build*. 

### Running the application 

1. make sure you're still in the *storage* directory. 
2. run `java -help` on the command line.
3. the main method is in *app.StorageApp*. 
   try to start the application with *java app.StorageApp*.
   note that you need to pass the full class name including the package name (*fully qualified class name*) and **not the file path**.  
   the command should fail. why can the JVM not find the main class?
   remember what the error looks like. you will need to understand it some day. 
4. try to start the application again, but this time add the *build* directory to classpath (and nothing else).
   the command should fail. why can the JVM not find the Gson class?
   remember what the error looks like. you will need to understand it some day. 
5. try to start the application again, but this time add both the *build* directory and the gson jar to classpath.
   you should specify the *-cp* option only once and separate the elements using either ':' or ';'.
   the application should start up and generate some output.

### Packaging the application

As mentioned earlier in the maven practice session, jar files are regular zip archives that contain class files and resources. 
The JDK includes a command line tool to generate jar files. 

1. run `jar -help` on the command line.
2. package the contents of the *build* directory into a jar *build.jar*. 
   run `jar cvf build.jar -C build .` (dot is part of the command).
3. open the jar in your favourite archive tool and see what's inside.
4. start the application. 
   this time ignore the *build* directory and add *build.jar* and the gson jar to classpath. 
5. delete the *build* and *src* directories, but keep the jar file.

### Using jar files in your project

1. open the ide and and start a new project.
2. add the following class to your project: 
   https://gist.github.com/mbakhoff/0ddfeeb56acacc3000c4ffb72f8c088b 
3. the class depends on the FileResult class from the *storage* code and won't compile.  
   let's include the storage code in our project.
   open the *File* menu in the ide, open the project structure. 
   open the libraries section. 
   on the left side there's a list of libraries. 
   click the green plus sign, select *Java* and find your *build.jar*.
   the project should compile now. 
4. close the ide again.
5. move to the new project's directory on the command line.
6. create a *build* directory and compile the FileCalculator using *javac*.
7. start the FileCalculator using *java*. 

## Classpath resources

An application consists of its classes and dependencies. 
In addition, it can contain different non-code resources: images, configuration files, translations etc.
All these resources are included in the jar when packaging and distributing your application.

Packaging all the resources in the jar is convenient because the files don't get lost, but more importantly, they are always in the same place as the code.
When the resources are in the jar, then there is no need to know any absolute or relative paths within the machine where your application is running at.
This may seem useless when you run the app in on your own machine, but becomes more useful when the jar is shipped to other developers. 
But how can the application access the resources when they are packaged into the jar? 

The JVM classloading mechanism can already find class files from any jar and directory in the classpath. 
It turns out that there's an easy way to use that same mechanism to find and read any file from the classpath in your own code. 
To load classes or find resources from the classpath you will need to use a ClassLoader object.
In this practice session we will use the `getResourceAsStream` method from the ClassLoader class. 

Each class in a running application is loaded by some classloader. 
To get a reference to the classloader of an object: 

```
Class<?> c = someObject.getClass();
ClassLoader cl = c.getClassLoader();
```

Alternatively you can directly reference a class: 

```
Class<?> c = SomeClass.class;
ClassLoader cl = c.getClassLoader();
```

Note that a Class object also has the `getResourceAsStream` method, but that works a little different from the one in ClassLoader. 
See https://stackoverflow.com/q/6608795 for details. 

### Loading classpath resources

As an exercise we will look at a small application that simulates a regular student trying to solve a programming test. 
The student uses a fixed class template to generate different random classes until something passes the automatic tests.
You will have to fix the application so it will correctly load the class template from the classpath.

The application uses the standard maven directory layout. 
The code is in *src/main/java* and resources are in *src/main/resources*.
When the application is packaged by maven, the resources are automatically included in the resulting jar file.
When running a maven application from the ide, both the compiled classes and the resources folder are included in the classpath.  

1. clone this github repository and open it in the ide.  
2. implement ClassGenerator#getStream so that the template.txt is loaded from the classpath using ClassLoader#getResourceAsStream.  
3. open the command line and navigate to the project.
4. run *mvn clean package*. 
   this will compile the code and package the jar into the *target* directory.
5. run the *generator.ClassGenerator* from command line. 
   the classpath should contain the class-generator jar from *target* and commons-io from *target/dependency*.
6. change ClassGenerator#getStream so that Class#getResourceAsStream is used. 
   don't forget to rebuild the application after changing the code. 
