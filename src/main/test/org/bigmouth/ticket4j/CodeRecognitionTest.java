package org.bigmouth.ticket4j;

import java.io.File;

import org.bigmouth.framework.core.SpringContextHolder;
import org.bigmouth.ticket4j.entity.Response;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;
import org.bigmouth.ticket4j.utils.AntiUtils;
import org.bigmouth.ticket4j.utils.JVMUtils;


public class CodeRecognitionTest {

    public static void main(String[] args) throws InterruptedException {
        JVMUtils.bootUsingSpring(new String[] {
                "config/applicationContext.xml"
        }, args);
        
        AntiUtils antiUtils = SpringContextHolder.getBean("anti");
        Initialize initialize = SpringContextHolder.getBean("initialize");
        PassCode passCode = SpringContextHolder.getBean("passCode");
        
        int success = 0, fail = 0;
        Ticket4jHttpResponse response = initialize.init();
        for (int i = 0; i < 100; i++) {
            File loginPassCode = passCode.getLoginPassCode(response);
            byte[] code = antiUtils.recognition(4, loginPassCode.getPath());
            System.out.println("识别结果:" + new String(code));
            Response checkLogin = passCode.checkLogin(response, new String(code));
            if (checkLogin.isContinue()) {
                success ++;
            }
            else {
                fail ++;
            }
        }
        System.out.println("识别结果：正确 " + success + ", 错误 " + fail + ", 成功率 " + (float)(success/(success+fail)*100) + "%");
    }
}
