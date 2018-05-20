/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import db.DBManager;
import db.Group;
import db.Post;
import db.User;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Pier DAgostino
 */
public class PostPage extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DBManager manager = (DBManager) getServletContext().getAttribute("dbmanager");
        User logged = (User) request.getAttribute("user");
        try {
            String groupIdParam = request.getParameter("id");
            int groupId = groupIdParam != null ? Integer.parseInt(groupIdParam) : 0;
            Group viewing = manager.getGroup(groupId);
            LinkedList<Post> groupPosts = manager.getGroupPosts(viewing);
            request.setAttribute("groupId", groupIdParam);
            request.setAttribute("posts", groupPosts);
            request.getRequestDispatcher("post.jsp").forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(GroupsPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DBManager dbmanager = (DBManager) getServletContext().getAttribute("dbmanager");
        User user = (User) request.getAttribute("user");
        String groupIdParam = request.getParameter("id");
        int groupId = groupIdParam != null ? Integer.parseInt(groupIdParam) : 0;
        Group group = dbmanager.getGroup(groupId);
        if (group != null) {
            MultipartRequest multipart = new MultipartRequest(request, group.getFilesRealPath(getServletContext()), 1024 * 1024 * 1024, "UTF-8", new PostFileRenamePolicy());
            String text = multipart.getParameter("text");
            int postId = dbmanager.addPost(groupId, user.getId(), text, multipart);
        }
        response.sendRedirect("/group-posts?id=" + groupId);
    }

}

class PostFileRenamePolicy extends DefaultFileRenamePolicy {
    @Override
    public File rename(File f) {
        String fileName = f.getName();
        if(fileName.length() > 45) {
            int n = fileName.length() - 45;
            File newFile = new File(f.getParent() + File.separator + fileName.substring(n));
            f.renameTo(newFile);
            f = newFile;
        }
        return super.rename(f);
    }
}
