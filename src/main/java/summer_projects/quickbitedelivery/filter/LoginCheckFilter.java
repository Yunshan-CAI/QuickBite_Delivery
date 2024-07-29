package summer_projects.quickbitedelivery.filter;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import summer_projects.quickbitedelivery.common.R;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * to check if the user has logged in
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher path_mather = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //get the request's uri
        String requestURI = request.getRequestURI();
        log.info("url received: " + requestURI);

        //define the urls that don't need to be processed
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/favicon.ico"
        };

        //check if this uri needs to be processed
        boolean check = check(urls, requestURI);

        //if not, let it go
        if (check == true) {
            log.info("let it go");
            filterChain.doFilter(request, response);
            return;
        }

        //check if login, if yes, let it go
        if (request.getSession().getAttribute("employee") != null) {
            log.info("already logged, let it go");
            filterChain.doFilter(request, response);
            return;
        }

        //check if login, if no, give the data to the frontend
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        log.info("not logged in, blocked");

    }

    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = path_mather.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
