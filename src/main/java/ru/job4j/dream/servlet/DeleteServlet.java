package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Photo;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Candidate candidate = PsqlStore.instOf().findByIdCandidate(Integer.valueOf(id));
        PsqlStore.instOf().deleteCandidate(candidate.getId());

        if (candidate.getPhotoId() != 0) {
            String path = "images" + File.separator + candidate.getPhotoId();
            for (File f : new File(path).listFiles()) {
                f.delete();
            }
            Files.deleteIfExists(Paths.get(path));
            PsqlStore.instOf().deletePhoto(candidate.getPhotoId());
        }

        req.setAttribute("candidates", PsqlStore.instOf().findAllCandidates());
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }
}