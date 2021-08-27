import java.util.concurrent.CompletableFuture;

public class BasicCreationofFuture {

    public static void main(String[] args) throws Exception{
        //Creating a CompletableFuture
        CompletableFuture<String> future=new CompletableFuture<String>();
        //String result = future.get();  this block the main thread for future to complete.
        // but since our future is not doing anything it will block for ever.

        //lets complete that future
        future.complete("this is the result string");

        //now from main thread get the result
        System.out.println(future.get());



    }
}
