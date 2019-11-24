package cn.utokato.feign.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 方法调用信息类
 *
 * @author lma
 * @date 2019/11/24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodInfo {
    /**
     * 请求 url
     */
    private String url;
    /**
     * 请求方法 get/put/delete/post ...
     */
    private HttpMethod method;
    /**
     * 请求参数
     */
    private Map<String, Object> params;
    /**
     * 请求体
     */
    private Mono body;
    /**
     * 请求body的类型
     */
    private Class<?> bodyElementType;

    /**
     * 返回是flux还是mono
     */
    private boolean isFlux;

    /**
     * 返回值类型
     */
    private Class<?> returnElementType;
}
