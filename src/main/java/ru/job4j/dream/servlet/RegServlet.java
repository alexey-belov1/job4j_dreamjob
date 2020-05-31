package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        if (PsqlStore.instOf().findByEmailUser(email) != null) {
            req.setAttribute("error", "Данный email уже занят");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        } else {
            PsqlStore.instOf().save(
                    new User(
                            0,
                            req.getParameter("name"),
                            email,
                            req.getParameter("password")
                    )
            );
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}