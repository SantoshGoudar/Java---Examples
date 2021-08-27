import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ThenApplyExample {
    public static void main(String[] args) throws Exception {
        // first create some async operation
        CompletableFuture<String> future = doSomeTimeConsumingOperation();
        //once that is complete do another async operation;
        CompletableFuture<String> final_result = future.thenApply(s -> {
            String result = null;
            try {
                result = future.get() + " Concatinated in then apply";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        });
        System.out.println(" Main thread Waiting for async future to complete");
        //wait for completion and get the result
        String result = final_result.join();

        System.out.println("Main Completed with result "+result);


    }


    public static CompletableFuture<String> doSomeTimeConsumingOperation() throws Exception {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);//just to make it look like time consuming operation

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "My first result";
        });
    }
}

