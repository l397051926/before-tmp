package com.gennlife.platform.filter;


import com.gennlife.platform.model.User;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chen-song on 16/9/21.
 */
public class SessionFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(SessionFilter.class);
    private static Gson gson = GsonUtil.getGson();
    private static View view = new View();
    private static Set<String> okSet= new HashSet<String>();
    static{
        okSet.add("/user/Login");
        okSet.add("/user/SendEmailForChangePWD");
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        if(okSet.contains(uri)){//放行
            filterChain.doFilter(request,response);
        }else{
            HttpSession session = request.getSession();
            User user = gson.fromJson((String)session.getAttribute("user"),User.class);
            if(user == null || user.getUid() == null){
                view.viewString(ParamUtils.errorSessionLosParam(),response);
            }else{
                filterChain.doFilter(request,response);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
