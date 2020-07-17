package thread.future;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * @author msl on 2020/7/17.
 */
public class CompletableFutureDemo {
    public static void main(String[] args) {

    }

    @Test
    public void test() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 2);

        future.thenAccept(System.out::println);
    }
}
