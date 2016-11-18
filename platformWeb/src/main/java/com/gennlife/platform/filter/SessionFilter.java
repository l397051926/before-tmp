package com.gennlife.platform.filter;


import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.model.User;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.util.SpringContextUtil;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chen-song on 16/9/21.
 */
public class SessionFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(SessionFilter.class);
    private static Gson gson = GsonUtil.getGson();
    private static JsonParser jsonParser = new JsonParser();
    private static View view = new View();
    private static Set<String> okSet = new HashSet();
    private static Set<String> adminSet = new HashSet();
    private static JedisCluster jedisCluster;

    public SessionFilter() {
        if(jedisCluster == null) {
            jedisCluster = (JedisCluster) SpringContextUtil.getBean("jedisClusterFactory");
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String uri = request.getRequestURI();
        if(okSet.contains(uri)) {
            filterChain.doFilter(request, response);
        } else {
            HttpSession session = request.getSession();
            String sessionID = session.getId();
            if(!jedisCluster.exists(sessionID).booleanValue()) {
                view.viewString(ParamUtils.errorSessionLosParam(), response);
            } else {
                String uid = jedisCluster.get(sessionID);
                String userStr = null;
                User user = null;
                if(jedisCluster.exists(uid + "_info").booleanValue()) {
                    userStr = jedisCluster.get(uid + "_info");
                    JsonReader jsonReader = new JsonReader(new StringReader(userStr));
                    jsonReader.setLenient(true);
                    user = gson.fromJson(jsonReader, User.class);
                }
                servletRequest.setAttribute("currentUser", user);
                if(adminSet.contains(uri) && !AuthorityUtil.isAdmin(user)) {
                    view.viewString(ParamUtils.errorAuthorityParam(), response);
                }

                filterChain.doFilter(request, response);
            }
        }

    }

    public void destroy() {
    }

    static {
        okSet.add("/user/Login");
        okSet.add("/base/Login");
        okSet.add("/user/SendEmailForChangePWD");
        okSet.add("/user/ExistEmail");
        adminSet.add("/bsma/OrgMapData");
        adminSet.add("/bsma/DeleteOrg");
        adminSet.add("/bsma/UpdateOrg");
        adminSet.add("/bsma/GetStaffInfo");
        adminSet.add("/bsma/AddStaff");
        adminSet.add("/bsma/DeleteStaff");
        adminSet.add("/bsma/GetProfessionList");
        adminSet.add("/bsma/GetRoleInfo");
        adminSet.add("/bsma/GetStaffTree");
        adminSet.add("/bsma/DeleteRoles");
        adminSet.add("/bsma/AddRole");
        adminSet.add("/bsma/EditRole");
        adminSet.add("/bsma/GetRoleStaff");
        adminSet.add("/bsma/GetRoleResource");
        adminSet.add("/bsma/GetResourceTree");
        adminSet.add("/common/UploadFileForImportLab");
        adminSet.add("/common/UploadFileForImportStaff");
    }
}
