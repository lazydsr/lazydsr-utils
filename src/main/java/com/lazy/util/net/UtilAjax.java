package com.lazy.util.net;

import javax.servlet.http.HttpServletRequest;

/**
 * UtilAjax
 * PROJECT_NAME: lazy
 * PACKAGE_NAME: com.lazy.com.lazydsr.util.net
 * Created by Lazy on 2017/10/11 14:27
 * Version: 0.1
 * Info: Ajax常用工具类
 */
public class UtilAjax {
    /**
     * 判断一个请求是不是ajax请求
     *
     * @param request HttpServletRequest
     * @return true or false
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {

        return request.getHeader("X-Requested-With") != null
                && "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString());
    }
}
