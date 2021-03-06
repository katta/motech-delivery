package org.motechproject.deliverytools.outboundendpoint;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static ch.lambdaj.Lambda.joinFrom;

@RequestMapping("/motech-delivery-tools/outbound")
@Controller
public class HttpEndpoint {
    private static Request lastRequest;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private RequestArchive requestArchive = new RequestArchive();

    @RequestMapping(value = "receive", method = RequestMethod.GET)
    @ResponseBody
    public String receive(HttpServletRequest servletRequest) throws UnsupportedEncodingException {
        String queryString = URLDecoder.decode(servletRequest.getQueryString(), "UTf-8");
        logger.info(String.format("Received request: %s", queryString));
        lastRequest = new Request(queryString);
        requestArchive.archive(lastRequest);
        return "Received";
    }

    @RequestMapping(value = "lastreceived", method = RequestMethod.GET)
    @ResponseBody
    public String lastRequest(GetLastReceivedRequest getLastReceivedRequest, HttpServletResponse response) throws InterruptedException {
        if (getLastReceivedRequest.waitForSeconds() > 0) {
            for (int i = 0; i < getLastReceivedRequest.waitForSeconds(); i++) {
                if (lastRequest != null) return lastRequestQuery();
                Thread.sleep(1000);
            }
        }
        return lastRequestQuery();
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ResponseBody
    public String getAll(HttpServletResponse response) throws InterruptedException {
        return joinFrom(requestArchive.getAll(), "<br>").queryString();
    }

    private String lastRequestQuery() {
        if (lastRequest == null) {
            return "";
        }
        return lastRequest.queryString();
    }

    @RequestMapping(value = "clear", method = RequestMethod.GET)
    @ResponseBody
    public String clear() {
        lastRequest = null;
        requestArchive.purgeAll();
        
        return "All Good";
    }
}
