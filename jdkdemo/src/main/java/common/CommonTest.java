package common;

import com.google.common.base.Stopwatch;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author msl on 2020/7/17.
 */
public class CommonTest {


    protected void timeExecute(Runnable runnable) {
        //开始计时
        Stopwatch stopwatch = Stopwatch.createStarted();
        //执行操作
        runnable.run();
        //结束计时
        stopwatch.stop();
        //打印计时
        System.out.println(MessageFormat.format("操作执行完成,共耗时[{0}]毫秒", (int) stopwatch.elapsed(TimeUnit.MILLISECONDS)));

    }
}
