package cn.utoakto;

import cn.utokato.feign.interfaces.ProxyCreator;
import cn.utokato.feign.proxy.JdkProxyCreator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author lma
 * @date 2019/11/24
 */
@SpringBootApplication
public class ApplicationStarter {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }


    @Bean
    ProxyCreator jdkProxyCreator() {
        return new JdkProxyCreator();
    }

    @Bean
    FactoryBean<IUserApi> userApi(ProxyCreator proxyCreator) {
        return new FactoryBean<IUserApi>() {
            /**
             * 返回代理对象
             */
            @Override
            public IUserApi getObject() throws Exception {
                return (IUserApi) proxyCreator.createProxy(getObjectType());
            }

            @Override
            public Class<?> getObjectType() {
                return IUserApi.class;
            }
        };
    }
}
