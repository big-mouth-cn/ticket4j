/*
 * 文件名称: BuyTickets.java
 * 版权信息: Copyright 2013-2014 By Allen Hu. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2014-8-14
 * 修改内容: 
 */
package org.bigmouth.ticket4j;

import org.bigmouth.ticket4j.entity.Response;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;


public interface User {

    Response login(String passCode, Ticket4jHttpResponse ticket4jHttpResponse);
}
