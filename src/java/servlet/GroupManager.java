/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import db.DBManager;
import db.Group;
import db.User;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author halfblood
 */
public class GroupManager extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            //SHOW USERS FOLLOWING THE GROUP BUT INVISIBLE 
            DBManager manager = (DBManager) getServletContext().getAttribute("dbmanager");
            User user = (User) request.getAttribute("user");
            String groupParam = request.getParameter("id");
            Group groupToEdit = null;
            if (groupParam != null) {
                groupToEdit = manager.getGroup(Integer.parseInt(groupParam));

                if (groupToEdit == null) {
                    response.sendError(404);
                    return;
                }
            }
            int groupId = groupToEdit == null ? 0 : groupToEdit.getId();

            LinkedList<User> visibleFollowinUsers = manager.getUsersForGroupAndVisible(groupId, user.getId());
            LinkedList<User> notVisibleFollowinUsers = manager.getUsersForGroupAndNotVisible(groupId, user.getId());
            LinkedList<User> otherUsers = manager.getUsersNotInGroup(groupId, user.getId());

            //SETTING GROUP NAME TABLE
            String nameString = "";
            if (groupToEdit != null) {
                nameString = groupToEdit.getName();
            }

            request.setAttribute("visibleFollowinUsers", visibleFollowinUsers);
            request.setAttribute("notVisibleFollowinUsers", notVisibleFollowinUsers);
            request.setAttribute("otherUsers", otherUsers);
            request.setAttribute("nameString", nameString);
            request.setAttribute("groupId", groupId);
            request.setAttribute("group", groupToEdit);
            request.getRequestDispatcher("/groupManager.jsp").forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        DBManager manager = (DBManager) getServletContext().getAttribute("dbmanager");
        User logged = (User) request.getAttribute("user");
        Group groupToEdit = manager.getGroup(Integer.parseInt(request.getParameter("id")));
        int groupId;
        String newName = request.getParameter("change_group_name");
        if (groupToEdit == null) {
            groupId = manager.createGroup(logged.getId(), newName);
            Group.createFilesDirectory(getServletContext(), groupId);
        } else {
            groupId = groupToEdit.getId();
        }
        if (groupId > 0) {
            if("true".equals(request.getParameter("group-public"))) {
                manager.setPublicFlag(groupId, true);
            } else {
                manager.setPublicFlag(groupId, false);
            }
            Map<String, String[]> m = request.getParameterMap();
            try {
                if (groupToEdit != null) {
                    manager.changeGroupName(groupId, newName);
                }
                manager.updateGroupMembers(groupId, m);
            } catch (Exception e) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        try {
            response.sendRedirect("/group-manager?id=" + groupId);
        } catch (IOException ex) {
            Logger.getLogger(GroupManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
