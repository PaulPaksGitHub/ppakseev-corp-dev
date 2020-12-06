package tomcat.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class IndexPageController {
  @RequestMapping("/")
  public String getIndexPage(){
    return "index.html";
  }
}