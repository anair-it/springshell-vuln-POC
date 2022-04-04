package org.anair;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class VulnerableController {

    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> ping(){
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }

    @RequestMapping(value = "/zeroday", method = RequestMethod.POST)
    public ResponseEntity<String> zerodayVuln(Zeroday zeroday) {
        System.out.println("zerodayvuln endpoint");
        return new ResponseEntity<> ("exploited", HttpStatus.OK);
    }

}
