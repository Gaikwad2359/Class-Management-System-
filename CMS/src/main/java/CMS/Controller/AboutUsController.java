package CMS.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutUsController {

    @GetMapping("/about")
    public String aboutUs() {
        return "About/aboutUs";
    }
}
