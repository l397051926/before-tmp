package com.gennlife.platform.filter;


import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.controller.UserController;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.UserProcessor;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.MemCachedUtil;
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
    private static Set<String> adminSet = new HashSet<>();

    static{
        okSet.add("/user/Login");
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
        adminSet.add("/case/SearchCase");
        adminSet.add("/common/UploadFileForImportLab");
        adminSet.add("/common/UploadFileForImportStaff");
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
        }else {
            HttpSession session = request.getSession();
            String sessionID = session.getId();
            String uid = MemCachedUtil.get(sessionID);
            String exSessionID = MemCachedUtil.get(uid);
            if(!sessionID.equals(exSessionID)){//一个用户两次登陆
                MemCachedUtil.delete(exSessionID);
            }
            try{
                User user = MemCachedUtil.getUser(uid);
                if(user == null){
                    user = UserProcessor.getUserByUid(uid);
                    if(user == null){
                        view.viewString(ParamUtils.errorParam("用户不存在"),response);
                    }
                    MemCachedUtil.setUserWithTime(uid,user, UserController.sessionTimeOut);
                }
                if(adminSet.contains(uri)){
                    if(!AuthorityUtil.isAdmin(user)){//没有管理权限
                        view.viewString(ParamUtils.errorAuthorityParam(),response);
                    }else{//放行
                        filterChain.doFilter(request,response);
                    }
                }else {
                    filterChain.doFilter(request,response);
                }
            }catch (Exception e){
                logger.error("",e);
                view.viewString(ParamUtils.errorParam("session异常"),response);
            }



        }
    }

    @Override
    public void destroy() {

    }
}
