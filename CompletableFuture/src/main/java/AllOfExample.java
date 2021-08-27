import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class AllOfExample {

    public static void main(String[] args) throws Exception {
        // first create two some async operation
        CompletableFuture<String> future1 = doSomeTimeConsumingOperation1();
        CompletableFuture<String> future2 = doSomeTimeConsumingOperation2();
        CompletableFuture<String> future3 = doSomeTimeConsumingOperation3();

        //use all of for waiting for completion of all futures and combining result
        CompletableFuture<String> final_result = CompletableFuture.allOf(future1, future2, future3).thenApply(unused -> {
            try {
                String s1 = future1.get();
                String s2 = future2.get();
                String s3 = future3.get();
                return s1 + s2 + s3;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        });
        System.out.println(" Main thread Waiting for async future to complete");
        String result = final_result.join();

        System.out.println("Main Completed with result " + result);

//AnyOF usage


        // first create two some async operation
        CompletableFuture<String> future4 = doSomeTimeConsumingOperation1();
        CompletableFuture<String> future5 = doSomeTimeConsumingOperation2();
        CompletableFuture<String> future6 = doSomeTimeConsumingOperation3();

        //use all of for waiting for completion of all futures and combining result
        CompletableFuture<Object> final_result2 = CompletableFuture.anyOf(future4, future5, future6);
        System.out.println(" Main thread Waiting for async anyof future to complete");
        Object result2 = final_result2.join();

        System.out.println("Main Completed with anyOf result " + result2);


    }


    public static CompletableFuture<String> doSomeTimeConsumingOperation1() throws Exception {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);//just to make it look like time consuming operation

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "My op1 result";
        });
    }

    public static CompletableFuture<String> doSomeTimeConsumingOperation2() throws Exception {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);//just to make it look like time consuming operation

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return " My op2 result";
        });
    }

    public static CompletableFuture<String> doSomeTimeConsumingOperation3() throws Exception {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);//just to make it look like time consuming operation

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return " My op3 result";
        });
    }
}

