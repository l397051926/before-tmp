package com.gennlife.platform.processor;

import ca.uhn.hl7v2.model.v26.datatype.CWE;
import ca.uhn.hl7v2.model.v26.message.MFN_M02;
import ca.uhn.hl7v2.model.v26.message.MFN_M05;
import ca.uhn.hl7v2.model.v26.segment.*;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.dao.OrgMapper;
import com.gennlife.platform.dao.SyUserMapper;
import com.gennlife.platform.model.Lab;
import com.gennlife.platform.model.User;
import org.springframework.stereotype.Service;

@Service
public class YongyouProcessor {

    public void ds001(MFN_M02 m) {
        final MSH msh = m.getMSH();
        final MFI mfi = m.getMFI();
        final MFE mfe = m.getMF_STAFF().getMFE();
        final STF stf = m.getMF_STAFF().getSTF();
        final PRA pra = m.getMF_STAFF().getPRA();
        final ORG org = m.getMF_STAFF().getORG();
        final EDU edu = m.getMF_STAFF().getEDU();
        final CWE cwe = (CWE)mfe.getMfe4_PrimaryKeyValueMFE(0).getData();
        // 操作类型 <- MFE-1
        final String operation = mfe.getMfe1_RecordLevelEventCode().getValue();
        // 工号 <- MFE-4-1
        final String id = cwe.getCwe1_Identifier().getValue();
        // 姓名 <- MFE-4-2
        final String name = cwe.getCwe2_Text().getValue();
        // 邮箱 <- STF-15
        final String mail = stf.getStf15_EMailAddress(0).getValue();
        // 手机号 <- STF-10-1
        final String phone = stf.getStf10_Phone(0).getXtn1_TelephoneNumber().getValue();
        // 职称名称 <- STF-19-2
        final String title = stf.getStf19_JobCodeClass().getJcc2_JobClass().getValue();
        // 科室编码 <- PRA-9-1
        final String departmentId = pra.getPra9_Institution().getCwe1_Identifier().getValue();
        // 科室名称 <- PRA-9-2
        final String department = pra.getPra9_Institution().getCwe2_Text().getValue();
        // 状态
        final String status;
        // 生效时间
        final String activationTime;
        // 失效时间
        final String inactivationTime;
        {
            // STF-7 Active/Inactive Flag (Y/N)
            final String statusCode = stf.getStf7_ActiveInactiveFlag().getValue();
            // STF-12-1 Institution Activation Date yyyy-MM-dd
            final String activationDate = stf.getStf12_InstitutionActivationDate(0).getDin1_Date().getValue();
            // STF-13-1 Institution Inactivation Date yyyy-MM-dd
            final String inactivationDate = stf.getStf13_InstitutionInactivationDate(0).getDin1_Date().getValue();
            if ("N".equals(statusCode)) {
                status = "禁用";
                activationTime= null;
                inactivationTime = null;
            } else if (activationDate != null && inactivationDate != null) {
                status = "定期有效";
                activationTime = activationDate + " 00:00:00";
                inactivationTime = inactivationDate + " 23:59:59";
            } else {
                status = "长期有效";
                activationTime = null;
                inactivationTime = null;
            }
        }
        if ("D".equals(operation)){
            _userDao().deleteUserByUids(new String[]{id});
        } else {
            final User user;
            switch (operation) {
                case "A":
                    user = new User();
                    break;
                case "U":
                    user = _userDao().getUserByUid(id);
                    break;
                default:
                    throw new RuntimeException("未知的操作：" + operation);
            }
            user.setUid(id);
            user.setUname(name);
            user.setUemail(mail);
            user.setTelphone(phone);
            user.setUprofession(title);
            user.setLabID(departmentId);
            user.setLab_name(department);
            user.setStatus(status);
            user.setEffective_time(activationTime);
            user.setFailure_time(inactivationTime);
            switch (operation) {
                case "A":
                    _userDao().insertOneUser(user);
                    break;
                case "U":
                    _userDao().updateByUid(user);
                    break;
                default:
                    throw new RuntimeException("未知的操作：" + operation);
            }
        }
    }

    public void ds002(MFN_M05 m) {
        final MSH msh = m.getMSH();
        final MFI mfi = m.getMFI();
        final MFE mfe = m.getMF_LOCATION().getMFE();
        final LOC loc = m.getMF_LOCATION().getLOC();
        final LRL lrl = m.getMF_LOCATION().getLRL();
        final CWE cwe = (CWE)mfe.getMfe4_PrimaryKeyValueMFE(0).getData();
        // 操作类型 <- MFE-1
        final String operation = mfe.getMfe1_RecordLevelEventCode().getValue();
        // 科室编号 <- MFE-4-1
        final String id = cwe.getCwe1_Identifier().getValue();
        // 科室名称 <- MFE-4-2
        final String name = cwe.getCwe2_Text().getValue();
        // 科室类型 <- LOC-2
        final String type = loc.getLoc2_LocationDescription().getValue();
        // 科室负责人编码 <- MFE-7-1 ?
        final String leaderId = mfe.getMfe7_EnteredBy().getXcn1_IDNumber().getValue();
        // 科室负责人 <- MFE-7-2 ?
        final String leaderName = mfe.getMfe7_EnteredBy().getXcn2_FamilyName().getFn1_Surname().getValue();
        // 上级科室编号 <- LRL-1-1
        final String parentId = lrl.getLrl1_PrimaryKeyValueLRL().getPl1_PointOfCare().getValue();
        // 上级科室名称
        final String parentName = _orgDao().getLabBylabID(parentId).getLab_name();
        if ("D".equals(operation)){
            _orgDao().deleteLabs(new String[]{id});
        } else {
            final Lab lab;
            switch (operation) {
                case "A":
                    lab = new Lab();
                    break;
                case "U":
                    lab = _orgDao().getLabBylabID(id);
                    break;
                default:
                    throw new RuntimeException("未知的操作：" + operation);
            }
            lab.setLabID(id);
            lab.setLab_name(name);
            lab.setLab_leader(leaderId);
            lab.setLab_leaderName(leaderName);
            lab.setLab_parent(parentId);
            lab.setLab_parentName(parentName);
            switch (operation) {
                case "A":
                    _orgDao().insertOneLab(lab);
                    break;
                case "U":
                    _orgDao().updateLabInfoByNameWithLab(lab);
                    break;
                default:
                    throw new RuntimeException("未知的操作：" + operation);
            }
        }
    }

    private static SyUserMapper _userDao() {
        return AllDao.getInstance().getSyUserDao();
    }

    private static OrgMapper _orgDao() {
        return AllDao.getInstance().getOrgDao();
    }

}
