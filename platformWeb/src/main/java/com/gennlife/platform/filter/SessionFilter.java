package com.gennlife.platform.filter;


import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.bean.HitsConfigBean;
import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.UserProcessor;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.LogUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.util.RedisUtil;
import com.gennlife.platform.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chen-song on 16/9/21.
 */
@WebFilter(urlPatterns = "/*")
public class SessionFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(SessionFilter.class);
    private static View view = new View();
    private static Set<String> okSet = new HashSet();
    private static Set<String> adminSet = new HashSet();

    public SessionFilter() {

    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        boolean  permissionFlag =true;
        if (okSet.contains(uri)) {
            filterChain.doFilter(request, response);
        } else {
            HttpSession session = request.getSession(false);
            if (session == null) {
                String cookie = ((HttpServletRequest) servletRequest).getHeader("Cookie");
                LogUtils.BussnissLogError("session 空: " + uri + " cookie " + cookie);
                logger.info("session 空: " + uri + " cookie " + cookie);
                view.viewString(ParamUtils.errorSessionLosParam(), response);
                return;
            }
            session.setMaxInactiveInterval(3600 * 5);
            String sessionID = session.getId();
            String uid = RedisUtil.getValue(sessionID);
            if(HitsConfigBean.HIES_SESSION_ID.equals(uid)){
                logger.info("hist入口接入了 session:"+sessionID);
                logger.info("hist入口接入了 request url is: " + uri);
                servletRequest.setAttribute(HitsConfigBean.HIES_SESSION_ID,HitsConfigBean.HIES_SESSION_ID);
                filterChain.doFilter(request, response);
                return;
            }
            if (uid == null) {
                logger.info("RedisUtil.getValue()-> uid is null");
                logger.info("request url is: " + uri);
                String cookie = ((HttpServletRequest) servletRequest).getHeader("Cookie");
                logger.info("RedisUtil.getValue取不到数据 sessionID:" + sessionID + " cookie:" + cookie + " uri=" + uri);
                view.viewString(ParamUtils.errorSessionLosParam(), response);
                return;
            }
            User user = UserProcessor.getUserByUidFromRedis(uid);

            if (user == null) {
                //LogUtils.BussnissLogError("RedisUtil.getUser取不到数据:" + uid);
                user = UserProcessor.getUserByUids(uid);
                if (user == null) {
                    LogUtils.BussnissLogError("错误user id");
                    view.viewString(ParamUtils.errorSessionLosParam(), response);
                    return;
                }
                RedisUtil.setUser(user);
            }

            SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String failTime = AllDao.getInstance().getSyUserDao().getFailureTimeByUid(uid);
            String effecTime = AllDao.getInstance().getSyUserDao().getEffectiveTimeByUid(uid);
            Date date=new Date();
            if(!("长期有效".equals(user.getStatus()))){
                if("禁用".equals(user.getStatus())){
//                    RedisUtil.userLogout(session.getId());
                    view.viewString(ParamUtils.errorPermission(), response);
//                    response.sendRedirect("/uranus/login.html");
                    return;

                }
                try {
                    if(date.after(time.parse(failTime)) ||date.before(time.parse(effecTime))){
//                        RedisUtil.userLogout(session.getId());
                        view.viewString(ParamUtils.errorPermission(), response);
//                        response.sendRedirect("/uranus/login.html");
                        return;
                    }
                } catch (ParseException e) {
                    logger.error("", e);
                    view.viewString(ParamUtils.errorPermission(), response);
                    return;
                }
            }

            servletRequest.setAttribute("currentUser", user);
            if (adminSet.contains(uri) && !AuthorityUtil.isAdmin(user)) {
                view.viewString(ParamUtils.errorAuthorityParam(), response);
                return;
            }
            filterChain.doFilter(request, response);
        }

    }

    public void destroy() {
    }

    static {
        okSet.add("/part3/addUser");
        okSet.add("/user/Info");
        okSet.add("/user/ssoLogin");
        okSet.add("/detail/get_patien_sn");
        okSet.add("/user/Login");
        okSet.add("/base/Login");
        okSet.add("/user/SendEmailForChangePWD");
        okSet.add("/user/ExistEmail");
        okSet.add("/user/SetRedis");
        okSet.add("/user/UpdatePWD");
        adminSet.add("/bsma/OrgMapData");
        adminSet.add("/bsma/DeleteOrg");
        adminSet.add("/bsma/UpdateOrg");
//        adminSet.add("/bsma/GetStaffInfo");
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
        adminSet.add("/bsma/DeleteAllOrg");
        adminSet.add("/common/UploadFileForImportLab");
        adminSet.add("/common/UploadFileForImportStaff");
        okSet.add("/etl/getEtlDatacount");
        okSet.add("/etl/getEtlStatisticsTable");
        okSet.add("/case/SearchItemSet");
        okSet.add("/case/SearchItemSetT");
        okSet.add("/rws/PreAggregation");
        okSet.add("/rws/PreFindForProjectData");
        okSet.add("/rws/dependenceChange");
        okSet.add("/rws/FindByProjectId");
        okSet.add("/rws/GetAllActiveOrIndex");
        okSet.add("/rws/GetSavedActivityData");
        okSet.add("/rws/SaveOrSearchActive");
        okSet.add("/rws/ClacResultSearch");
        okSet.add("/rws/ClacIndexResultSearch");
        okSet.add("/rws/getCalcTotalByActiveId");
        okSet.add("/rws/FindTotalForImport");
        okSet.add("/rws/DeleteByActiveId");
        okSet.add("/rws/checkActiveIsOnlyOne");
        okSet.add("/rws/activeIsChange");
        okSet.add("/rws/getRwsEventConfig");
        okSet.add("/rws/getLoadSearchDefinedEventListConfig");
        okSet.add("/rws/editActiveName");
        okSet.add("/case/SearchItemSetForRws");
        okSet.add("/crf/buildIndexForAll");
        okSet.add("/detail/getSchemaData");
        okSet.add("/detail/get_patien_sn");
        okSet.add("/bsma/sendSystemMessage");
//        okSet.add("/detail/getHomePageConfig");

    }
}
