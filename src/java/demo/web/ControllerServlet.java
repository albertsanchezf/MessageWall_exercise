package demo.web;

import demo.impl.MessageWall_and_RemoteLogin_Impl;
import demo.spec.MessageWall;
import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ControllerServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        process(request, response);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        
        String view = perform_action(request);
        forwardRequest(request, response, view);
    }

    protected String perform_action(HttpServletRequest request)
        throws IOException, ServletException 
    {
        
        String view;
        String serv_path = request.getServletPath();
        HttpSession session = request.getSession();
        RemoteLogin rl = getRemoteLogin();
        MessageWall mw = getMessageWall();
            
        if (serv_path.equals("/login.do")) 
        {
            String usr = (String) request.getParameterValues("user")[0];
            String pwd = (String) request.getParameterValues("password")[0];
            
            UserAccess ua = rl.connect(usr, pwd);
            if (ua != null)
            {
                session.setAttribute("userAccess", ua);
                view = "/view/wallview.jsp";
            }
            else
                view = "/error-invalid-login.html";

            return view;
        } 
        
        else if (serv_path.equals("/put.do")) 
        {
            String msg = (String) request.getParameterValues("msg")[0];
            UserAccess ua = (UserAccess) session.getAttribute("userAccess");
            
            if (ua != null)
            {
                mw.put(ua.getUser(), msg);
                view = "/view/wallview.jsp";
            }
            else
                view = "/error-not-loggedin.html";

            return view;
        } 
        
        else if (serv_path.equals("/refresh.do")) 
        {
            UserAccess ua = (UserAccess) session.getAttribute("userAccess");
            
            if (ua != null)
            {
                view = "/view/wallview.jsp";
            }
            else
                view = "/error-not-loggedin.html";

            return view;
        } 
        
        else if (serv_path.equals("/delete.do")) 
        {
            UserAccess ua = (UserAccess) session.getAttribute("userAccess");
            int msg_id = Integer.parseInt(request.getParameterValues("index")[0]);
            
            if (ua.delete(msg_id))
            {
                view = "/view/wallview.jsp";
            }
            else
                view = "/error-bad-action.html";

            return view;
        } 
        
        else if (serv_path.equals("/logout.do")) 
        {
            //¿¿¿¿????
            session.invalidate();
            return "/goodbye.html";
        } 
        
        else 
        {
            
            return "/error-bad-action.html";
        }
    }

    public RemoteLogin getRemoteLogin() {
        return (RemoteLogin) getServletContext().getAttribute("remoteLogin");
    }
    
    // Això està afegit per mi. Realment es fa així?
    public MessageWall getMessageWall() {
        return (MessageWall) getServletContext().getAttribute("remoteLogin");
    }
    
    public void forwardRequest(HttpServletRequest request, HttpServletResponse response, String view) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(view);
        if (dispatcher == null) {
            throw new ServletException("No dispatcher for view path '"+view+"'");
        }
        dispatcher.forward(request,response);
    }
}


