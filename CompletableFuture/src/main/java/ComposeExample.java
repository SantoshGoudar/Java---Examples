import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ComposeExample {
    public static void main(String[] args) throws Exception {
        // first create two some async operation
        CompletableFuture<String> future1 = doSomeTimeConsumingOperation1();

        //Use of compose for composing resutls for future1 and future2
        CompletableFuture<String> final_result = future1.thenCompose(s -> {

            try {
                return doSomeTimeConsumingOperation2(s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return null;
        });
        System.out.println(" Main thread Waiting for async future to complete");
        String result = final_result.join();

        System.out.println("Main Completed with result " + result);


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

    public static CompletableFuture<String> doSomeTimeConsumingOperation2(String s) throws Exception {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);//just to make it look like time consuming operation

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return s + " My op2 result";
        });
    }
}


