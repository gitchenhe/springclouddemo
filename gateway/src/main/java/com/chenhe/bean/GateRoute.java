package com.chenhe.bean;

import lombok.Data;

/**
 * @author chenhe
 * @date 2019-10-31 17:20
 * @desc
 */
@Data
public class GateRoute {
    //
    private Integer id;

    //映射路径
    private String path;

    //映射服务
    private String serviceId;

    //映射外连接
    private String url;

    //令牌桶流速
    private String limiterRate;

    //令牌桶容量
    private String limiterCapacity;

    //是否启用
    private String enabled;

    //是否忽略前缀
    private Integer stripPrefix;

    private String routeOrder;



}
