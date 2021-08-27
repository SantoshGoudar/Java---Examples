import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CombineExample {
    public static void main(String[] args) throws Exception {
        // first create two some async operation
        CompletableFuture<String> future1 = doSomeTimeConsumingOperation1();
        CompletableFuture<String> future2 = doSomeTimeConsumingOperation2();
        //Use of combine for composing resutls for future1 and future2
        CompletableFuture<String> final_result = future1.thenCombine(future2, (s, s2) -> {
            return s + s2;
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
}



