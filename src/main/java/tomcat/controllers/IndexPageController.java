package tomcat.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
public class IndexPageController {
  @RequestMapping("/")
  public String getIndexPage(){
    return "index.html";
  }
}