package learn.sharding.jdbc.example.futureTask;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadExcutorTests {

    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args){

        List<Future> list = new ArrayList<>();

        Future<String> future1 = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000);
                return "123";
            }
        });
        list.add(future1);

        Future<String> future2 = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {

                return "456";
            }
        });
        list.add(future2);

        Future<String> future3 = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {

                return "789";
            }
        });
        list.add(future3);

        executorService.shutdown();

        while(future1.isDone() || future2.isDone() || future3.isDone()){
            try {
                System.out.println(future1.get());// 阻塞
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println(11111);

//        for (Future future : list){
//            try {
//                System.out.println(future.get().toString());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
    }

}
