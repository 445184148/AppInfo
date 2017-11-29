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
 * @author 华大大
 * 2017-11-16
 * 开发者控制
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

	// 开发者id
	int devId;
	// 文件保存路徑
	final String EPATH = "E:/Workspace/java/AppInfo/WebRoot/statics/uploadfiles/";
	// 文件数据库路径
	final String APATH = "/AppInfo/statics/uploadfiles/";

	// 登陆
	@RequestMapping("/loginDev")
	public String loginDev(String devCode, String devPassword, HttpServletRequest request, Model ml) {
		DevUser devUser = ds.loginDev(devCode, devPassword);

		if (devUser == null) {
			ml.addAttribute("error", "账号或密码错误");
			return "devlogin.jsp";
		} else {
			devId = devUser.getId();
			request.getSession().setAttribute("devUserSession", devUser);
			return "developer/main.jsp";
		}
	}

	// 显示软件列表
	@RequestMapping("/selectAppInfoList")
	public String selectAppInfoList(AppInfo ai, @Param("pageIndex") @RequestParam(defaultValue = "1") Integer pageIndex,
			Model ml, HttpServletRequest request) {
		// System.out.println("进入" + ai);
		HttpSession session = request.getSession();
		if (ai == null) {
			Object obj = session.getAttribute("ai");
			if (obj != null) {
				ai = (AppInfo) obj;
			}
		}
		// System.out.println("判断后" + ai);
		// 得到总数
		int pageCount = ds.selectAppCount(ai);
		// 设置页面大小
		int pageSize = 5;
		// 得到总页数
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

	// 查询分级
	@RequestMapping("/categoryLevelList")
	public void categoryLevelList(@Param("id") Integer id, String tcode, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		System.out.println(tcode + "-----");
		if (id != null && tcode == null) {
			System.out.println("分类");
			response.getWriter().print(JSONArray.fromObject(ds.categoryLevelList(id)));
		} else if (id == null && tcode == null) {
			System.out.println("分类");
			response.getWriter().print(JSONArray.fromObject(ds.categoryLevelList(id)));
		} else {
			System.out.println("平台");
			response.getWriter().print(JSONArray.fromObject(ds.dataDictionary(tcode)));
		}
	}

	// 进入软件添加
	@RequestMapping("/appinfoadd")
	public void appinfoadd(@Param("typeCode") String typeCode, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(JSONArray.fromObject(ds.dataDictionary(typeCode)));
	}

	// 判断软件名是否存在
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

	// 软件添加
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

	// 软件详情
	@RequestMapping("/appview")
	public String appview(AppInfo ai, Model ml) {
		ml.addAttribute("appInfo", ds.selectAppInfoList(ai, 0, 1).get(0));
		ml.addAttribute("appVersionList", ds.appVersionList(ai.getId()));
		return "developer/appinfoview.jsp";
	}

	// 进入软件版本更新
	@RequestMapping("appversionadd")
	public String appversionadd(@Param("appId") Integer appId, Model ml) {
		System.out.println(appId);
		ml.addAttribute("appVersionList", ds.appVersionList(appId));
		ml.addAttribute("appId", appId);

		return "developer/appversionadd.jsp";
	}

	// 软件版本跟新
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
		// System.out.println("新增主鍵" + av.getId());
		ds.updAppInfoVersion(av.getAppId(), av.getId());
		return "selectAppInfoList?devId=" + devId;
	}

	// 进入软件版本修改
	@RequestMapping("/appversionmodify")
	public String appversionmodify(@Param("vid") Integer vid, @Param("aid") Integer aid, Model ml) {
		List<AppVersion> list = ds.appVersionList(aid);
		ml.addAttribute("appVersionList", list);
		ml.addAttribute("appVersion", list.get(0));
		return "developer/appversionmodify.jsp";
	}

	// 软件版本修改
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

	// 删除文件
	@ResponseBody
	@RequestMapping("/delfile")
	public String delfile(Integer id, String flag) {
		if (ds.delfile(id, flag) > 0) {
			return "success";
		} else {
			return "failed";
		}
	}

	// 进入软件修改
	@RequestMapping("/appinfomodify")
	public String appinfomodify(AppInfo ai, Model ml) {
		ai.setDevId(devId);
		ml.addAttribute("appInfo", ds.selectAppInfoList(ai, 0, 1).get(0));
		return "developer/appinfomodify.jsp";
	}

	// 信息修改
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

	// 删除软件
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

	// 上架下架
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
