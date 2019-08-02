package learn.sharding.jdbc.example.futureTask;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallBackController {

    @Autowired
    private CallBackService callBackService;

    @RequestMapping("/callback")
    public String callBack(){
        return callBackService.callBack();
    }
}
