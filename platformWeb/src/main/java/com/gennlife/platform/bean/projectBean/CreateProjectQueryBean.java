package com.gennlife.platform.bean.projectBean;

import java.util.List;

/**
 * Created by chensong on 2015/12/11.
 */
public class CreateProjectQueryBean {
    private CreateProject createProject;
    private List<ProUser> proUserList;
    private ProLog prolog;
    public CreateProject getCreateProject() {
        return createProject;
    }

    public void setCreateProject(CreateProject createProject) {
        this.createProject = createProject;
    }

    public List<ProUser> getProUserList() {
        return proUserList;
    }

    public void setProUserList(List<ProUser> proUserList) {
        this.proUserList = proUserList;
    }

    public ProLog getProlog() {
        return prolog;
    }

    public void setProlog(ProLog prolog) {
        this.prolog = prolog;
    }
}
