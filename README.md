# Java---Examples

# 1 . CompletableFuture  Examples

We know that java 5 had Future interface for representing the result of asynchronous executions in a multithreaded environment. Java 8 introduces CompletableFuture which is very advanced and has overcome some issues that were there in Future - Java 5.

## 1. It didn't have exception handling capability - ##
lets say there was exception thrown by the thread executing asynchronously - then that exception is lost, it will not be propagated to main thread.

## 2. Call back function capability was not there ##
If you want a function to be called when the async thread completes and  Future result was available, it was not possible. Only way was to call get() method and block for it's completion.
## 3. Chaining multiple callbacks or Futures was not possible ##
If you have several asynchronous calculations to be chained together it was not possible. Again only was to use get() method and blocking on it.

## Completable Future ##
 
Lets see how to create a Completable Future

```
CompletableFuture<String>  future=new CompletableFuture<String>();
```

This is the far most simple way of creating a completable Future. Now when main thread calls 
get() method on it, it will be blocked till this future is completed.
```
String result = future.get();
```
We can complete the future by calling complete() method by providing result manually.
```
future.complete("This is result string");
```
Lets see how we actually use it in applications.


## 1. Running some async tasks without anything to return ##
```
CompletableFuture future= CompletableFuture.runAsync(new Runnable() {

    @Override
    public void run() {
   // Do some long running task here
   // some calculations     
    }
});
```
the same thing with a lambda expression

```
CompletableFuture.runAsync(() -> {

    // Do some long running task here
    // some calculations
    
}); 

```
Here if you are wondering which thread this will run, it uses common thread pool.
```
ForkJoinPool.commonPool()
```
You can also create your own thread pool and pass as second argument like below
```
ExecutorService executorService = Executors.newFixedThreadPool(10);
CompletableFuture future=CompletableFuture.runAsync(() -> {

    // Do some long running task here
    // some calculations

}, executorService);
```
So when the main thread or any other thread calls future.get() it will be blocked till the thread completes.
## 2. Running some async tasks with result to return ##
For returning the result we can use Supplier<T> interface and supply method of CompletableFuture.
```
CompletableFuture.supplyAsync(()-> {
   
        //Do your calculations and return the result here
        return "This is result";
   
});
 

String s = future.get();
```

 ## 3. Doing some operation on result of one future asynchronously on completion of this future ##
when we want some operation to be done on completion of this future we can do it by calling
thenApply() method on it.
```
CompletableFuture<String> future= CompletableFuture.supplyAsync(()-> {

        //Do your calculations and return the result here
        return "This is result";

});
CompletableFuture<String> thenApply = future.thenApply(s -> {

    return s.concat(" concatinated in then apply");

});
String s = future.get();// This returns string  -->   "This is result concatinated in then apply"
```
Similary we can chain many futures like  
```
 future1.thenApply(()->{}).thenApply(()->{});
 ```
 ## 4. Doing something on results ##
above method thenApply()  is for cases where you transform the result of one future and return is as result.
But there may be cases where we just want to consume the returned result of CompletableFuture, in that case we can use
thenRun() or thenAccept().
```
future.thenAccept(s -> {
    // do something with the result or consume the result
});
future.thenRun(() -> {
    // do something with the result or consume the result
});
 ```
## 5. Combining results of multiple CompletableFuture ##
Lets say you have two services which asynchronously return results using CompletableFuture
 ```
CompletableFuture<Employee> getEmployee(String employeeId) {
    return CompletableFuture.supplyAsync(() -> {
      return   employeeService.getEmployee(employeeId);
    });
}

CompletableFuture<Project> getEmployee(String employeeId) {
    return CompletableFuture.supplyAsync(() -> {
        return   projectService.getProjectByEmployee(employeeId);
    });
 ```
So when you want another service method to return the Complete employee details you can use thenCompose method

 ```
CompletableFuture<EmployeeWithProject> getEmployeeComplete(String employeeId) {
    return getEmployee(employeeId).thenCompose(employee -> getProject(employee));
}
 ```
here you cannot use thenApply() because if you use thenApply() it will return 
```
CompletableFuture<CompletableFuture<EmployeeWithProject>> 
```

thenApply() will always wrap the result in CompletableFuture. That's the reason thenCompose is used.
thenCompose() is like flatMap().

We can use thenCombine() for combining results of two futures like below.

```
CompletableFuture<Employee> getEmployee(String employeeId) {
    return CompletableFuture.supplyAsync(() -> {
      return   employeeService.getEmployee(employeeId);
    });
}

CompletableFuture<Project> getProject(String employeeId) {
    return CompletableFuture.supplyAsync(() -> {
        return   projectService.getProjectByEmployee(employeeId);
    });
}

CompletableFuture<EmployeeWithProject> getEmployeeComplete(String employeeId) {
    CompletableFuture<Employee> employeeCompletableFuture = getEmployee(employeeId);
    CompletableFuture<Project> projectFuture = getProject(employeeId);
    return employeeCompletableFuture.thenCombine(projectFuture,(project,employee)->{
        return new EmployeeWithProject(employee,project);
    });
} 
```


## 6. Combining many futures ##
We have allOf()  and anyOf() methods that can be used to wait or combine many async futures or when some are complete.
```
CompletableFuture<String> future1= CompletableFuture.supplyAsync(()-> {

        //Do your calculations and return the result here
        return "This is result1";

});

CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()-> {

    //Do your calculations and return the result here
    return "This is result2";

});

CompletableFuture<String> future3 = CompletableFuture.supplyAsync(()-> {

    //Do your calculations and return the result here
    return "This is result3";

});

CompletableFuture<String> finalRes = CompletableFuture.allOf(future1, future2, future3).thenApply(unused -> {
    String res1 = future1.get();
    String res2 = future2.get();
    String res3 = future3.get();
    return res3 + res2 + res3;//combine result and return your final result here;

});
String finalResult = finalRes.get();
```

So allOf() - will be called when all the futures passed are completed successfully and then you can combine the result.

anyOf() - is called when any one of the futures passed are completed successfully, it is useful when partial result is available and you want to do something. 
