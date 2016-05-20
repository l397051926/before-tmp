/**
 * Created by chen-song on 16/4/8.
 */
var userBaseColumnList = " p_user.id ,p_user.uid,uname,uemail ,uposition ,p_user.orgID ,orgName,lab ,telphone ,age ,sex ,date_format(uptime, \'%Y-%m-%d %H:%m:%S\') as lastModifyTime ";
var FinishedProjectSelect = "p.projectID,projectName,creater,startTime,endTime";
module.exports = {
    user:{
        login:'select ' + userBaseColumnList + 'from p_user JOIN p_org o on o.orgID = p_user.orgID ' + ' where uid=? and pwd = ?',
        testOrg:'select * from p_org'
    },
    org:{
        getOrgList:' select orgID,orgName from p_org order by orgID',
        getOneOrgList:'select orgName,u.uid AS UID,u.uname,o.orgID AS ORGID from p_org o join p_user u on o.orgID = u.orgID where o.orgID =?'
    },
    project:{
        getFinishedProjects:'select ' + FinishedProjectSelect + " from p_project p JOIN pro_user u on u.projectID = p.projectID where u.uid = ? and p.pstatus = 5 order by createTime desc limit ?,? ",
        getFinishedProjectCounter:" select count(*) from p_project p JOIN pro_user u on u.projectID = p.projectID where u.uid=? and p.pstatus = 5 "
    }

}