package apollo.datastore.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LangFilter implements Filter {

    FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {

        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // exclude certain urls from init param
        if(request.getServletPath().startsWith("/tasks/")) {
            chain.doFilter(request, response);
            return;
        }

        boolean languageSet = false;
        String language = Locale.DEFAULT_LANGUAGE;

        if(request.getMethod().equalsIgnoreCase("get")) {
            language = request.getParameter(Cookies.LANG.getName());
            if(language != null) {
                language = language.toLowerCase();
                if(!Locale.SUPPORTED_LANGUAGES.contains(language))
                    language = Locale.DEFAULT_LANGUAGE;
                Cookie languageCookie = new Cookie(Cookies.LANG.getName(), language);
                languageCookie.setMaxAge(Cookies.MAX_AGE);
                languageCookie.setPath(Cookies.LANG_PATH);
                response.addCookie(languageCookie);
                languageSet = true;
            }
        }

        if(!languageSet) {
            Cookie cookies[] = request.getCookies();
            if(cookies != null) for(Cookie cookie : cookies)
                if(cookie.getName().equalsIgnoreCase(Cookies.LANG.getName())) {
                    language = cookie.getValue().toLowerCase();
                    break;
                }
            if(!Locale.SUPPORTED_LANGUAGES.contains(language))
                language = Locale.DEFAULT_LANGUAGE;
        }

        request.setAttribute(Cookies.LANG.getName(), language);

        // pass the request along the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() { }
}
