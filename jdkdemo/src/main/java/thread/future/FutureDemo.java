package thread.future;

import common.CommonTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Future接口在Java 5中被引入，设计初衷是对将来某个时刻会发生的结果进行建模。
 * 它建模 了一种异步计算，返回一个执行运算结果的引用，当运算结束后，这个引用被返回给调用方。
 * 在 Future中触发那些潜在耗时的操作把调用线程解放出来，让它能继续执行其他有价值的工作，
 * 不再需要呆呆等待耗时的操作完成。
 *      <p1>
 *          打个比方，你可以把它想象成这样的场景：
 *          你拿了一袋子衣 服到你中意的干洗店去洗。
 *          干洗店的员工会给你张发票，
 *          告诉你什么时候你的衣服会洗好（这就 是一个Future事件）。
 *          衣服干洗的同时，你可以去做其他的事情。
 *          Future的另一个优点是它比 更底层的{@link Thread}更易用。
 *          要使用Future，通常你只需要将耗时的操作封装在一个{@link Callable}对 象中，
 *          再将它提交给ExecutorService，就万事大吉了。
 *          下面这段代码展示了Java 8之前使用 Future的一个例子。
 *      <p1/>
 * @author msl on 2020/7/17.
 */
public class FutureDemo extends CommonTest {



    @Test
    public void 串行执行操作() {
        timeExecute(()-> {
            int a = 一个耗时三秒的计算操作(5);
            int b = 一个耗时五秒的计算操作(2);
            int result = 一个耗时一秒的求和操作(a, b);
            //耗时为 5 + 2 + 1 = 8秒
            System.out.println("结算结果:" + result);
        });
    }


    @Test
    public void 使用Future顺序执行() {
        timeExecute(()-> {
            ExecutorService executor = Executors.newCachedThreadPool();
            Future<Integer> futureA = executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return 一个耗时三秒的计算操作(5);
                }
            });
            Future<Integer> futureB = executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return 一个耗时五秒的计算操作(2);
                }
            });


            Integer a = null;
            Integer b = null;
            try {
                a = futureA.get();
                b = futureB.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            //1.三秒操作和五秒操作  同时执行， 执行完毕时间取耗时最长的操作 为5秒
            //2.执行一秒的求和操作
            //3.结果为  5 + 1 = 6秒

            int result = 一个耗时一秒的求和操作(a, b);
            System.out.println("结算结果:" + result);

        });
    }

    @Test
    public void 使用Thread执行() throws InterruptedException {
        timeExecute(() -> {
            List<Integer> list = new ArrayList<>();
            new Thread(()-> list.add(一个耗时三秒的计算操作(5))).start();
            new Thread(()-> list.add(一个耗时五秒的计算操作(2))).start();

            do {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(list.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (list.size() != 2);
            int result = 一个耗时一秒的求和操作(list.get(0), list.get(1));

            //三秒和五秒的同步计算  共耗时5秒,   do while 大概浪费1秒时间，  求和操作1秒
            //预计共耗时 5 + 1 + 1 = 7秒
            System.out.println("结算结果:" + result);
        });
    }








    private int 一个耗时三秒的计算操作(int num) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return num * 100;
    }

    private int 一个耗时五秒的计算操作(int num) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return num * 100;
    }

    private int 一个耗时一秒的求和操作(int a, int b) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return a + b;
    }

}
