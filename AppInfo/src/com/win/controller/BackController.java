package com.win.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
/**
 * 
 * @author 华大大
 *   后台控制器
 *   2017-11-16
 */
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.win.pojo.AppInfo;
import com.win.pojo.BackendUser;
import com.win.pojo.DevUser;
import com.win.service.BackService;

@Controller
public class BackController {
	@Autowired
	private BackService bs;

	public BackService getBs() {
		return bs;
	}

	public void setBs(BackService bs) {
		this.bs = bs;
	}

	int userid;

	// 登陆
	@RequestMapping("/loginUser")
	public String loginBack(String userCode, String userPassword, HttpServletRequest request) {
		BackendUser backendUser = bs.loginBack(userCode, userPassword);
		System.out.println(backendUser);
		if (backendUser == null) {
			request.setAttribute("error", "账号或密码错误");
			return "backendlogin.jsp";
		} else {
			request.getSession().setAttribute("userSession", backendUser);
			userid = backendUser.getId();
			return "backend/main.jsp";
		}
	}

	@RequestMapping("/list")
	public String list(AppInfo ai, @Param("pageIndex") @RequestParam(defaultValue = "1") Integer pageIndex, Model ml,
			HttpServletRequest request) {
		// System.out.println("进入" + ai);
		HttpSession session = request.getSession();
		if (ai == null) {
			Object obj = session.getAttribute("ai");
			if (obj != null) {
				// System.out.println("获取上一次的值");
				ai = (AppInfo) obj;
			}
		}
		// System.out.println("判断后" + ai);
		// 得到总数
		int pageCount = bs.selectAppCount(ai);
		// 设置页面大小
		int pageSize = 5;
		// 得到总页数
		int pageNo = pageCount % pageSize == 0 ? pageCount / pageSize : pageCount / pageSize + 1;

		List<AppInfo> list = bs.list(ai, (pageIndex - 1) * pageSize, pageSize);
		ml.addAttribute("appInfoList", list);

		ml.addAttribute("pageIndex", pageIndex);
		ml.addAttribute("pageNo", pageNo);
		ml.addAttribute("pageCount", pageCount);
		ml.addAttribute("flatFormList", bs.dataDictionary("APP_FLATFORM"));
		ml.addAttribute("statusList", bs.dataDictionary("APP_STATUS"));
		ml.addAttribute("categoryLevel1List", bs.categoryLevelList(null));
		ml.addAttribute("querySoftwareName", ai.getSoftwareName());
		ml.addAttribute("queryStatus", ai.getStatus());
		ml.addAttribute("queryFlatformId", ai.getFlatformId());
		ml.addAttribute("queryCategoryLevel1", ai.getCategoryLevel1());
		ml.addAttribute("queryCategoryLevel2", ai.getCategoryLevel2());
		ml.addAttribute("queryCategoryLevel3", ai.getCategoryLevel3());
		if (ai.getCategoryLevel1() != null) {
			ml.addAttribute("categoryLevel2List", bs.categoryLevelList(ai.getCategoryLevel1()));
		}
		if (ai.getCategoryLevel2() != null) {
			ml.addAttribute("categoryLevel3List", bs.categoryLevelList(ai.getCategoryLevel2()));
		}
		// System.out.println("上次記錄" + ai);
		session.setAttribute("ai", ai);
		return "backend/applist.jsp";
	}

	// 进入审核
	@RequestMapping("/check")
	public String check(AppInfo ai, Model ml) {
		// System.out.println("_____-" + ai.getId() + "___" +
		// ai.getVersionId());
		ml.addAttribute("appInfo", bs.list(ai, 0, 1).get(0));
		ml.addAttribute("appVersion", bs.selectVersion(ai.getVersionId()));
		return "backend/appcheck.jsp";
	}

	@RequestMapping("/checksave")
	public void checksave(AppInfo ai, HttpServletResponse response) throws IOException {
		System.out.println("___" + ai);
		bs.checksave(ai);
		response.sendRedirect("list?createdBy="+userid);
	}

}
