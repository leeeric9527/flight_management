package pers.gulo.fm.web;

import java.io.IOException;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.gulo.fm.dao.PassengerDao;
import pers.gulo.fm.dao.impl.PassengerDaoImpl;
import pers.gulo.fm.domain.Order;
import pers.gulo.fm.domain.User;
import pers.gulo.fm.exception.FMException;

public class CancelServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			int orderNo=Integer.parseInt(request.getParameter("orderNo"));
			PassengerDao pDao=new PassengerDaoImpl();
			Order order=new Order();
			order.setNo(orderNo);
			order=pDao.getOrder(order);
			try {
				pDao.bounce(order);
				User sessionUser = (User) request.getSession().getAttribute("user");
				sessionUser.setBalance(sessionUser.getBalance()+order.getFlight().getPrice());
				request.getSession().setAttribute("myOrderMsg", "取消订单成功！");
			} catch (FMException e) {
				request.getSession().setAttribute("myOrderMsg", "取消订单失败！");
				e.printStackTrace();
			}finally{
				response.sendRedirect(request.getContextPath()+"/myOrder.jsp");
			}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
