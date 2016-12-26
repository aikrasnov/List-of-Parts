package ru.akrasnov.servlet;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.*;
import java.util.regex.Pattern;


/**
 * Created by akrasnov on 11.12.2016.
 */
@WebServlet("/filter")
public class FilterServlet extends HttpServlet {
    private List<Parts> getParts(Map<String, String> parametrs) {

        SessionFactory sessionBuilder = new Configuration().configure().buildSessionFactory();
        Session session = sessionBuilder.openSession();

        String pn = parametrs.get("PN");
        String partName = parametrs.get("PartName");
        String vendor = parametrs.get("Vendor");
        String qty = parametrs.get("Qty");
        String shippedAfter = parametrs.get("shippedAfter");
        String shippedBefore = parametrs.get("shippedBefore");
        String receivedAfter = parametrs.get("receivedAfter");
        String receivedBefore = parametrs.get("beforeReceived");

        List<String> listConditions = new ArrayList<String>();

        if (!(pn.equals(""))) listConditions.add("pn = " + "'" + pn + "'");
        if (!(partName.equals(""))) listConditions.add("partName = " + "'" + partName + "'");
        if (!(vendor.equals(""))) listConditions.add("vendor = " + "'" + vendor + "'");
        if (!(qty.equals(""))) listConditions.add("qty > " + qty);

        if (!(shippedAfter.equals(""))) listConditions.add("shipped > " + "'" + shippedAfter + "'");
        if (!(shippedBefore.equals(""))) listConditions.add("shipped < " + "'" + shippedBefore + "'");
        if (!(receivedAfter.equals(""))) listConditions.add("received > " + "'" + receivedAfter + "'");
        if (!(receivedBefore.equals(""))) listConditions.add("received < " + "'" + receivedBefore + "'");

        String where = "WHERE ";

        for (String item:listConditions) {
            where += item + " AND ";
        }

        where = where.substring(0, where.length() - " AND ".length());

        Query query;
        String HQL = "FROM Parts ";
        if (where.length() > "WHERE ".length()) {
            HQL += where;
            query = session.createQuery(HQL);
        }
        else {
            query = session.createQuery(HQL);
        }

        System.out.println("HQL: " + HQL);
        System.out.println(query.list());
        List<Parts> result = query.list();
        session.close();
        return result;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, String> parametrs = new HashMap<String, String>();
        parametrs.put("PN", req.getParameter("PN"));
        parametrs.put("PartName", req.getParameter("PartName"));
        parametrs.put("Vendor", req.getParameter("Vendor"));
        parametrs.put("Qty", req.getParameter("Qty"));
        parametrs.put("shippedAfter", req.getParameter("shippedAfter"));
        parametrs.put("shippedBefore", req.getParameter("shippedBefore"));
        parametrs.put("receivedAfter", req.getParameter("receivedAfter"));
        parametrs.put("beforeReceived", req.getParameter("beforeReceived"));

        // регулярка для проверки корректности формата
        String regexp = "^[a-zA-z]{3}+\\s{1}+\\d{2}+,+(\\s{1}|)+\\d{4}";

        String errorMessage = "";

        if (!(parametrs.get("shippedAfter").equals("")) && !Pattern.matches(regexp, parametrs.get("shippedAfter"))) errorMessage += "Ошибка: shipped after в неправильном формате!";
        if (!(parametrs.get("shippedBefore").equals("")) && !Pattern.matches(regexp, parametrs.get("shippedBefore"))) errorMessage += "Ошибка: shipped before в неправильном формате!";
        if (!(parametrs.get("receivedAfter").equals("")) && !Pattern.matches(regexp, parametrs.get("receivedAfter"))) errorMessage += "Ошибка: received after в неправильном формате!";
        if (!(parametrs.get("beforeReceived").equals("")) && !Pattern.matches(regexp, parametrs.get("beforeReceived"))) errorMessage += "Ошибка: received before в неправильном формате!";

        req.setAttribute("PN", req.getParameter("PN"));
        req.setAttribute("PartName", req.getParameter("PartName"));
        req.setAttribute("Vendor", req.getParameter("Vendor"));
        req.setAttribute("Qty", req.getParameter("Qty"));
        req.setAttribute("shippedAfter", req.getParameter("shippedAfter"));
        req.setAttribute("shippedBefore", req.getParameter("shippedBefore"));
        req.setAttribute("receivedAfter", req.getParameter("receivedAfter"));
        req.setAttribute("beforeReceived", req.getParameter("beforeReceived"));

        if (!(errorMessage.equals(""))) {
            req.setAttribute("parametrs", errorMessage);
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } else {

            System.out.println(parametrs);
            List<Parts> listParts = getParts(parametrs);

            String allRows = "";

            for (Parts part : listParts) {
                allRows += "<tr>";
                allRows += part.getPartInHtml();
                allRows += "</tr>";
            }

            req.setAttribute("parametrs", errorMessage);
            req.setAttribute("Rows", allRows);
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }

}