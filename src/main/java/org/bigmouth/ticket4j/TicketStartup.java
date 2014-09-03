package org.bigmouth.ticket4j;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.bigmouth.framework.core.SpringContextHolder;
import org.bigmouth.ticket4j.utils.JVMUtils;


public class TicketStartup {
    
    public static void main(String[] args) {
        System.out.println("《东皇钟》使用合约");
        System.out.println("您必须同意以下协议才可以使用本软件");
        System.out.println("1. 本软件所有购票接口基于12306官网，从下载、安装、升级完全免费；");
        System.out.println("2. 本软件只是一个辅助工具，其购票原理是减少人机交互，循环操作，并不能保证百分之百成功；");
        System.out.println("3. 不得对本软件进行恶意篡改、破解、反编译及倒卖软件等不法行为，否则我们有权无条件终止您的使用；");
        System.out.println("4. 因不可抗力因素导致本软件不能使用，本软件不承担任何责任；");
        System.out.println("5. 本软件未授权任何单位及个人，也未在任何第三方平台销售；");
        System.out.println("6. 使用本软件需遵守相关法律法规，不得利用本软件有非法倒票、卖票等行为；");
        System.out.println("7. 本软件可能会保存并上传用户的使用记录，并承诺将严格保障用户隐私权，对用户的个人信息保密，未经用户的同意不得向他人泄露，但法律另有规定的除外。只有当相关部门依照法定程序要求我们披露用户的个人资料时，我们才会依法或为维护公共安全之目的向执法单位提供用户的个人资料，且不承担任何法律责任。");
        System.out.println("-------------------http://www.big-mouth.cn/ticket4j-------------------");
        System.out.print("如果您同意以上协议，继续使用请输入“Y”确认：");
        String input = new Scanner(System.in).next();
        if (!StringUtils.equalsIgnoreCase(input, "Y")) {
            System.out.println("再见!");
            System.exit(0);
        }
        
        JVMUtils.bootUsingSpring(new String[] {
                "config/applicationContext.xml"
        }, args);
        TicketProcess process = SpringContextHolder.getBean("ticketProcess");
        process.start();
        System.out.println("再见!");
    }
}
