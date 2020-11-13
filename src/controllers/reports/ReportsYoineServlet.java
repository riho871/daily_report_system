package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsYoineServlet
 */
@WebServlet("/reports/yoine")
public class ReportsYoineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsYoineServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 日報データを取得
        EntityManager em = DBUtil.createEntityManager();
        Report r = em.find(Report.class , Integer.parseInt(request.getParameter("id")));
        
        // いいね数に1を加算する
        int yoine = r.getYoine() + 1;
        r.setYoine(yoine);
        
        //日報テーブルを更新
        em.getTransaction().begin();
        em.getTransaction().commit();
        em.close();
        
        // 日報一覧ページへ遷移
        request.getSession().setAttribute("flush", "いいねしました。");
        response.sendRedirect(request.getContextPath() + "/reports/index");
    }

}