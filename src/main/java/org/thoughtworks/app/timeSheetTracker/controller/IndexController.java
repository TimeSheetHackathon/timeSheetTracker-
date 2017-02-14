package org.thoughtworks.app.timeSheetTracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class IndexController {

    /**
     * Controller for Index Page.
     * @return Index.html
     */
    @RequestMapping("/{city}")
    public String getIndexPage(@PathVariable("city") String city, HttpServletResponse response) {
        response.addCookie(new Cookie("city", city));
        return "index";
    }

    @RequestMapping("/")
    public String getCountryPage() {
        return "country";
    }

}
