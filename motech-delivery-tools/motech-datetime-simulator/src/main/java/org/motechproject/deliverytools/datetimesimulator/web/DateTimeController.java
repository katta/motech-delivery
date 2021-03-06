package org.motechproject.deliverytools.datetimesimulator.web;

import org.apache.log4j.Logger;
import org.motechproject.deliverytools.datetimesimulator.domain.TimeMachine;
import org.motechproject.util.DateTimeSourceUtil;
import org.motechproject.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/motech-delivery-tools/datetime")
@Controller
public class DateTimeController {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @RequestMapping(value = "update", method = RequestMethod.GET)
    @ResponseBody
    public String update(@RequestParam String date, @RequestParam String hour, @RequestParam String minute, HttpServletResponse response) {
        try {
            TimeMachine sourceInstance;
            if (!(DateTimeSourceUtil.SourceInstance instanceof TimeMachine)) {
                DateTimeSourceUtil.SourceInstance = new TimeMachine(DateTimeSourceUtil.SourceInstance);
            }
            sourceInstance = (TimeMachine) DateTimeSourceUtil.SourceInstance;
            sourceInstance.update(date, hour, minute);
            return String.format("Successfully set datetime to: %s", DateUtil.now());
        } catch (Exception e) {
            response.setStatus(500);
            logger.error(String.format("Could not set the datetime from Date=%s, Hour=%s, Minute=%s. Did you use something like: 2011-10-17", date, hour, minute), e);
            return e.toString();
        }
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public String get() {
        return DateUtil.today().toString();
    }
}
