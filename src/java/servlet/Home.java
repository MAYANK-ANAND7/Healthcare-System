/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import db.DBManager;
import db.Group;
import db.Post;
import db.User;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 *
 * @author paolo
 */
public class Home extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        DBManager manager = (DBManager) getServletContext().getAttribute("dbmanager");
        User logged = (User) request.getAttribute("user");
        String notificationsTitle = "You are not logged in";
        boolean isLogged = false;
        if (logged != null) {
            isLogged = true;
            Date lastTime = manager.getLastQickDisplayTime(logged.getId());
            if (lastTime == null) {
                lastTime = new Date();
            }
            LinkedList<Post> postFromLastTime = manager.getPostsFromDate(lastTime, logged);
            manager.updateQuickDisplayTime(logged.getId());
            LinkedList<Group> invitingGroups = manager.getInvites(logged);
            request.setAttribute("postFromLastTime", postFromLastTime);
            request.setAttribute("invitingGroups", invitingGroups);
            request.setAttribute("dbmanager", manager);
            notificationsTitle = "You have new invites";
        }
        try {
            request.setAttribute("isLogged", isLogged);
            request.setAttribute("notificationsTitle", notificationsTitle);
            request.getRequestDispatcher("/home.jsp").forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }
}
