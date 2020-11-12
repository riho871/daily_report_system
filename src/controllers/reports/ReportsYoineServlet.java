package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import models.Yoine;
import models.validators.ReportValidator;
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
        // 初回起動を判定するための処理
        // アプリケーションスコープから値を取得
        ServletContext sc = this.getServletContext();
        Report r = (Report) sc.getAttribute("report");

        // 初回起動判定の続き
        // アプリケーションスコープに値がなければnewする
        if(r == null) {
            r = new Report();
            sc.setAttribute("report", r);
        }

        // リクエストパラメーターの取得
            request.setCharacterEncoding("UTF-8");
            String report = request.getParameter("action");


        // いいねボタン押されたら
        if (report != null) {

            // Yoineでいいねを加算
            Yoine y = new Yoine();
            y.yoineCount(r);

            // いいねの数をアプリケーションスコープに保存
            sc.setAttribute("report", r);
        }

        // フォワード
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
        rd.forward(request, response);

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Report r = em.find(Report.class, (Integer)(request.getSession().getAttribute("report_id")));

            r.setYoine(Integer.valueOf(request.getParameter("yoine")));

            List<String> errors = ReportValidator.validate(r);
            if(errors.size() > 0) {
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("report", r);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
                rd.forward(request, response);
            } else {
                em.getTransaction().begin();
                //em.persist(r);
                em.getTransaction().commit();
                request.getSession().setAttribute("flush", "いいねしました。");
                em.close();

                response.sendRedirect(request.getContextPath() + "/reports/index");
            }
        }
    }

}