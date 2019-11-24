package cn.utokato.feign.interfaces;

import cn.utokato.feign.bean.MethodInfo;
import cn.utokato.feign.bean.ServerInfo;

/**
 * rest 请求调用 handler
 *
 * @author lma
 * @date 2019/11/24
 */
public interface RestHandler {
    /**
     * 初始化服务信息
     */
    void init(ServerInfo serverInfo);

    /**
     * 调用rest请求
     */
    Object invokeRest(MethodInfo methodInfo);
}
