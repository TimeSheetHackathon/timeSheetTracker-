package org.thoughtworks.app.timesheetTracker.controller;

/**
 * Created by shishirv on 27/01/2017.
 */

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
     *
     * @return Index.html
     */
    @RequestMapping("/{city}")
    public String getIndexPage(@PathVariable("city") String city, HttpServletResponse response) {
        response.setHeader("Set-Cookie", "city=" + city + ";");
//        response.addCookie(new Cookie("city", city));
        return "index2";
    }

}
