package org.anair;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VulnerableController {

    @RequestMapping(value = "/ping")
    public String ping(){
        return "pong";
    }

    @RequestMapping(value = "/zeroday")
    public String zerodayVuln(Zeroday zeroday) {
        return "zeroday";
    }

}
