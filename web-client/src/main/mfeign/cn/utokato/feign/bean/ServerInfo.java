package cn.utokato.feign.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务器信息类
 *
 * @author lma
 * @date 2019/11/24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerInfo {

    /**
     * 服务器的 url
     */
    private String url;

}
