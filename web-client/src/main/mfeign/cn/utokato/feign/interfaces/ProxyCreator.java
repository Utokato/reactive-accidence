package cn.utokato.feign.interfaces;

/**
 * 创建代理类的接口
 *
 * @author lma
 * @date 2019/11/24
 */
public interface ProxyCreator {
    /**
     * 创建代理类
     *
     * @param type class
     * @return object
     */
    Object createProxy(Class<?> type);
}
