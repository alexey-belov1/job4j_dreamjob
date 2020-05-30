package ru.job4j.dream.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Photo;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class UploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("candidates", PsqlStore.instOf().findAllCandidates());
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Candidate candidate = PsqlStore.instOf().findByIdCandidate(Integer.valueOf(req.getParameter("id")));

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(req);
            File folder = new File("images");
            if (!folder.exists()) {
                folder.mkdir();
            }
            for (FileItem item : items) {
                candidate.setPhotoId(PsqlStore.instOf().save(new Photo(candidate.getPhotoId(), item.getName())));
                File folderId = new File("images" + File.separator + candidate.getPhotoId());
                if (folderId.exists()) {
                    for (File f : folderId.listFiles()) {
                        f.delete();
                    }
                } else {
                    folderId.mkdir();
                }

                try (FileOutputStream out = new FileOutputStream(new File(folderId + File.separator + item.getName()))) {
                    out.write(item.getInputStream().readAllBytes());
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

        PsqlStore.instOf().save(candidate);

        doGet(req, resp);
    }
}