package com.win.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
/**
 * 
 * @author �����
 * 2017-11-16
 * �����߿���
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.win.pojo.AppInfo;
import com.win.pojo.AppVersion;
import com.win.pojo.DevUser;
import com.win.service.DevService;

import net.sf.json.JSONArray;

@Controller
public class DevController {
	@Autowired
	private DevService ds;

	public DevService getDs() {
		return ds;
	}

	public void setDs(DevService ds) {
		this.ds = ds;
	}

	// ������id
	int devId;
	// �ļ�����·��
	final String EPATH = "E:/Workspace/java/AppInfo/WebRoot/statics/uploadfiles/";
	// �ļ����ݿ�·��
	final String APATH = "/AppInfo/statics/uploadfiles/";

	// ��½
	@RequestMapping("/loginDev")
	public String loginDev(String devCode, String devPassword, HttpServletRequest request, Model ml) {
		DevUser devUser = ds.loginDev(devCode, devPassword);

		if (devUser == null) {
			ml.addAttribute("error", "�˺Ż��������");
			return "devlogin.jsp";
		} else {
			devId = devUser.getId();
			request.getSession().setAttribute("devUserSession", devUser);
			return "developer/main.jsp";
		}
	}

	// ��ʾ����б�
	@RequestMapping("/selectAppInfoList")
	public String selectAppInfoList(AppInfo ai, @Param("pageIndex") @RequestParam(defaultValue = "1") Integer pageIndex,
			Model ml, HttpServletRequest request) {
		// System.out.println("����" + ai);
		HttpSession session = request.getSession();
		if (ai == null) {
			Object obj = session.getAttribute("ai");
			if (obj != null) {
				ai = (AppInfo) obj;
			}
		}
		// System.out.println("�жϺ�" + ai);
		// �õ�����
		int pageCount = ds.selectAppCount(ai);
		// ����ҳ���С
		int pageSize = 5;
		// �õ���ҳ��
		int pageNo = pageCount % pageSize == 0 ? pageCount / pageSize : pageCount / pageSize + 1;
		List<AppInfo> appInfoList = ds.selectAppInfoList(ai, (pageIndex - 1) * pageSize, pageSize);
		ml.addAttribute("appInfoList", appInfoList);
		ml.addAttribute("pageIndex", pageIndex);
		ml.addAttribute("pageNo", pageNo);
		ml.addAttribute("pageCount", pageCount);
		ml.addAttribute("flatFormList", ds.dataDictionary("APP_FLATFORM"));
		ml.addAttribute("statusList", ds.dataDictionary("APP_STATUS"));
		ml.addAttribute("categoryLevel1List", ds.categoryLevelList(null));
		ml.addAttribute("querySoftwareName", ai.getSoftwareName());
		ml.addAttribute("queryStatus", ai.getStatus());
		ml.addAttribute("queryFlatformId", ai.getFlatformId());
		ml.addAttribute("queryCategoryLevel1", ai.getCategoryLevel1());
		ml.addAttribute("queryCategoryLevel2", ai.getCategoryLevel2());
		ml.addAttribute("queryCategoryLevel3", ai.getCategoryLevel3());
		if (ai.getCategoryLevel1() != null) {
			ml.addAttribute("categoryLevel2List", ds.categoryLevelList(ai.getCategoryLevel1()));
		}
		if (ai.getCategoryLevel2() != null) {
			ml.addAttribute("categoryLevel3List", ds.categoryLevelList(ai.getCategoryLevel2()));
		}
		session.setAttribute("ai", ai);
		return "developer/appinfolist.jsp";
	}

	// ��ѯ�ּ�
	@RequestMapping("/categoryLevelList")
	public void categoryLevelList(@Param("id") Integer id, String tcode, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		System.out.println(tcode + "-----");
		if (id != null && tcode == null) {
			System.out.println("����");
			response.getWriter().print(JSONArray.fromObject(ds.categoryLevelList(id)));
		} else if (id == null && tcode == null) {
			System.out.println("����");
			response.getWriter().print(JSONArray.fromObject(ds.categoryLevelList(id)));
		} else {
			System.out.println("ƽ̨");
			response.getWriter().print(JSONArray.fromObject(ds.dataDictionary(tcode)));
		}
	}

	// ����������
	@RequestMapping("/appinfoadd")
	public void appinfoadd(@Param("typeCode") String typeCode, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(JSONArray.fromObject(ds.dataDictionary(typeCode)));
	}

	// �ж�������Ƿ����
	@ResponseBody
	@RequestMapping("/existAppName")
	public String existAppName(@Param("APKName") String APKName) {
		System.out.println(APKName + "//");
		if (APKName == null || APKName.trim() == "") {
			return "empty";
		} else {
			Integer num = ds.existAppName(APKName);
			if (num == 0) {
				return "ok";
			} else {
				return "no";
			}
		}
	}

	// ������
	@RequestMapping("/appinfoaddsave")
	public void appinfoaddsave(AppInfo ai, MultipartFile a_logoPicPath, HttpServletRequest request,
			HttpServletResponse response) throws IllegalStateException, IOException {
		String hz = a_logoPicPath.getOriginalFilename().substring(a_logoPicPath.getOriginalFilename().lastIndexOf("."));
		String path = request.getSession().getServletContext().getRealPath("statics/uploadfiles") + "\\"
				+ ai.getAPKName() + hz;
		ai.setLogoPicPath(APATH + ai.getAPKName() + hz);
		ai.setLogoLocPath(path);
		request.getSession().removeAttribute("ai");
		if (ds.appinfoaddsave(ai) > 0) {
			a_logoPicPath.transferTo(new File(EPATH + ai.getAPKName() + hz));
		}
		response.sendRedirect("selectAppInfoList?devId=" + devId);
	}

	// �������
	@RequestMapping("/appview")
	public String appview(AppInfo ai, Model ml) {
		ml.addAttribute("appInfo", ds.selectAppInfoList(ai, 0, 1).get(0));
		ml.addAttribute("appVersionList", ds.appVersionList(ai.getId()));
		return "developer/appinfoview.jsp";
	}

	// ��������汾����
	@RequestMapping("appversionadd")
	public String appversionadd(@Param("appId") Integer appId, Model ml) {
		System.out.println(appId);
		ml.addAttribute("appVersionList", ds.appVersionList(appId));
		ml.addAttribute("appId", appId);

		return "developer/appversionadd.jsp";
	}

	// ����汾����
	@RequestMapping("/addversionsave")
	public String addversionsave(AppVersion av, MultipartFile a_downloadLink, HttpServletRequest request)
			throws IllegalStateException, IOException {
		String path = request.getSession().getServletContext().getRealPath("statics/uploadfiles") + "\\"
				+ av.getVersionNo() + ".apk";
		av.setApkFileName(av.getVersionNo() + ".apk");
		av.setDownloadLink(APATH + av.getVersionNo() + ".apk");
		av.setApkLocPath(path);
		if (ds.addversionsave(av) > 0) {
			a_downloadLink.transferTo(new File(EPATH + av.getVersionNo() + ".apk"));
		}
		// System.out.println("�������I" + av.getId());
		ds.updAppInfoVersion(av.getAppId(), av.getId());
		return "selectAppInfoList?devId=" + devId;
	}

	// ��������汾�޸�
	@RequestMapping("/appversionmodify")
	public String appversionmodify(@Param("vid") Integer vid, @Param("aid") Integer aid, Model ml) {
		List<AppVersion> list = ds.appVersionList(aid);
		ml.addAttribute("appVersionList", list);
		ml.addAttribute("appVersion", list.get(0));
		return "developer/appversionmodify.jsp";
	}

	// ����汾�޸�
	@RequestMapping("appversionmodifysave")
	public void appversionmodifysave(AppVersion av, HttpServletRequest request, MultipartFile attach,
			HttpServletResponse response) throws IllegalStateException, IOException {
		String path = request.getSession().getServletContext().getRealPath("statics/uploadfiles") + "\\"
				+ av.getVersionNo() + ".apk";
		av.setApkFileName(av.getVersionNo() + ".apk");
		av.setDownloadLink(APATH + av.getVersionNo() + ".apk");
		av.setApkLocPath(path);
		Integer num = ds.appversionmodifysave(av);
		if (num > 0) {
			attach.transferTo(new File(EPATH + av.getVersionNo() + ".apk"));
		}
		response.sendRedirect("selectAppInfoList?devId=" + devId);

	}

	// ɾ���ļ�
	@ResponseBody
	@RequestMapping("/delfile")
	public String delfile(Integer id, String flag) {
		if (ds.delfile(id, flag) > 0) {
			return "success";
		} else {
			return "failed";
		}
	}

	// ��������޸�
	@RequestMapping("/appinfomodify")
	public String appinfomodify(AppInfo ai, Model ml) {
		ai.setDevId(devId);
		ml.addAttribute("appInfo", ds.selectAppInfoList(ai, 0, 1).get(0));
		return "developer/appinfomodify.jsp";
	}

	// ��Ϣ�޸�
	@RequestMapping("/appinfomodifysave")
	public void appinfomodifysave(AppInfo ai, MultipartFile attach, HttpServletResponse response)
			throws IllegalStateException, IOException {
		if (!attach.isEmpty()) {
			String hz = attach.getOriginalFilename().substring(attach.getOriginalFilename().lastIndexOf("."));
			ai.setLogoPicPath(APATH + ai.getAPKName() + hz);
		}
		ai.setModifyBy(devId);
		System.out.println(ai);
		if (ds.appinfomodifysave(ai) > 0) {
			if (!attach.isEmpty()) {
				String hz = attach.getOriginalFilename().substring(attach.getOriginalFilename().lastIndexOf("."));
				attach.transferTo(new File(EPATH + ai.getAPKName() + hz));
			}
		}
		response.sendRedirect("selectAppInfoList?devId=" + devId);
	}

	// ɾ�����
	@ResponseBody
	@RequestMapping("/delapp")
	public String delapp(Integer id) {
		if (ds.delappInfo(id) > 0) {
			ds.delVersion(id);
			return "true";
		} else {
			return "false";
		}

	}

	// �ϼ��¼�
	@ResponseBody
	@RequestMapping("/sale")
	public String sale(Integer id, String type, HttpServletResponse response) {
		System.out.println("__" + id + "___" + type);
		if (type.equals("open")) {
			if (ds.sale(id, type) > 0) {
				return "opentrue";
			} else {
				return "error";
			}

		} else if (type.equals("close")) {
			if (ds.sale(id, type) > 0) {
				return "closetrue";
			} else {
				return "error";
			}
		} else {
			return "error";
		}
	}
}
