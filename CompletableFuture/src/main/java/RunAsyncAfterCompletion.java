import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RunAsyncAfterCompletion {

    public static void main(String[] args) throws Exception {
        // first create some async operation
        CompletableFuture<String> future = doSomeTimeConsumingOperation();
        //chain it so that when first is complete do another async operation;
        CompletableFuture<Void> final_result = future.thenRun(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);//just to make it look like time consuming operation
                System.out.println(future.get());
                System.out.println("Running this once my future is complete");
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });
        System.out.println(" Main thread Waiting for async future to complete");
        //wait for final operation to complete, here it doesn't return anything
        final_result.join();
        System.out.println("Main Completed");
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
