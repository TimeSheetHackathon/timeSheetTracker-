package org.thoughtworks.app.timeSheetTracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/")
public class IndexController {

    /**
     * Controller for Index Page.
     * @return Index.html
     */

    @RequestMapping("/{country}/{city}")
    public String getIndexPage(@PathVariable("country") String country, @PathVariable("city") String city, HttpServletResponse response) throws UnsupportedEncodingException {
        response.addCookie(new Cookie("city", URLEncoder.encode(city, "UTF-8")));
        response.addCookie(new Cookie("country", URLEncoder.encode(country, "UTF-8")));
        return "index";
    }

    @RequestMapping("/{city}/project")
    public String getProjectPageForCity(@PathVariable("city") String city, HttpServletResponse response) throws UnsupportedEncodingException {
        response.addCookie(new Cookie("city", URLEncoder.encode(city, "UTF-8")));
        return "percentage";
    }

    @RequestMapping("/{city}/project/{project}")
    public String getProjectPage(@PathVariable("city") String city, @PathVariable("project") String project, HttpServletResponse response) throws UnsupportedEncodingException {
        response.addCookie(new Cookie("city", URLEncoder.encode(city, "UTF-8")));
        response.addCookie(new Cookie("project", URLEncoder.encode(project, "UTF-8")));
        return "project";
    }

    @RequestMapping("/{country}")
    public String getIndexPage(@PathVariable("country") String country, HttpServletResponse response) throws UnsupportedEncodingException {
        response.addCookie(new Cookie("country", URLEncoder.encode(country, "UTF-8")));
        return "index";
    }

    @RequestMapping("/")
    public String getCountryPage() {
        return "country";
    }

}
