package org.thoughtworks.app.timesheetTracker.controller;

/**
 * Created by shishirv on 27/01/2017.
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {

    /**
     * Controller for Index Page.
     * @return Index.html
     */
    @RequestMapping(method = RequestMethod.GET)
    public String getIndexPage() {
        System.out.println("Here ");
        return "index";
    }

}
