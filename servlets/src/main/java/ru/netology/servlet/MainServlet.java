package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

    private PostController controller;

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_DELETE = "DELETE";

    private static final String PATH = "/api/posts";
    private static final String PATH_WITH_POST_ID = "/api/posts/\\d+";
    private static final String POST_ID_DELIMITER = "/";

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(METHOD_GET)) {
                if (path.equals(PATH)) {
                    controller.all(resp);
                    return;
                } else if (path.matches(PATH_WITH_POST_ID)) {
                    final var id = Long.parseLong(path.substring(path.lastIndexOf(POST_ID_DELIMITER) + 1));
                    controller.getById(id, resp);
                    return;
                }
            }
            if (method.equals(METHOD_POST) && path.equals(PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(METHOD_DELETE) && path.matches(PATH_WITH_POST_ID)) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf(POST_ID_DELIMITER) + 1));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

