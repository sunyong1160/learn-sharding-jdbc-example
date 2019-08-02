package learn.sharding.jdbc.example.futureTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class CallBackServiceImpl implements CallBackService {

    @Autowired
    private NotifyThread notifyThread;

    @Override
    public String callBack() {

        String result = "";
        Future<String> future1 = notifyThread.call();
        Future<String> future2 = notifyThread.getInt();
        while (true) {
            if(future1.isDone()){
                try {
                    result = future1.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            if(future2.isDone()){
                try {
                    result = future2.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            break;
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
}
