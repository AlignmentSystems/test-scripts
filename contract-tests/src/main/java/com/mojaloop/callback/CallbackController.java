package com.mojaloop.callback;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

@RestController
public class CallbackController {

    private Logger logger = Logger.getLogger(CallbackController.class.getName());

    private HashMap<String,String> correlationMap = new HashMap<String,String>();

    @RequestMapping(method = { RequestMethod.GET }, value = { "/version" })
    public String getVersion() {
        return "1.0";
    }

    @RequestMapping(value = "/participants/{Type}/{Id}",method = RequestMethod.PUT)
    public void putParticipants(@PathVariable("Type") String type, @PathVariable("Id") String id, @RequestHeader("X-Forwarded-For") String correlationId, @RequestBody String payload) throws IOException {
        if(correlationId.indexOf(",") != -1) {
            logger.info("Header: " + correlationId.substring(0, correlationId.indexOf(",")) + " Body: " + payload);
            correlationMap.put(correlationId.substring(0, correlationId.indexOf(",")), payload);
        }
    }

    @RequestMapping(value = "/participants/{Type}/{Id}/error",method = RequestMethod.PUT)
    public void putParticipantsError(@PathVariable("Type") String type, @PathVariable("Id") String id, @RequestHeader("X-Forwarded-For") String correlationId, @RequestBody String payload) throws IOException {
        if(correlationId.indexOf(",") != -1) {
            logger.info("Header: " + correlationId.substring(0, correlationId.indexOf(",")) + " Body: " + payload);
            correlationMap.put(correlationId.substring(0, correlationId.indexOf(",")), payload);
        }
    }

    @RequestMapping(value = "/correlationid", method = RequestMethod.POST)
    public void addCorrelationId(@RequestBody String payload){
        JsonParser jsonParser = new JacksonJsonParser();
        correlationMap.put((String)jsonParser.parseMap(payload).get("correlationId"),(String)jsonParser.parseMap(payload).get("correlationId"));
    }

    @RequestMapping(value = "/correlationid/{correlationId}", method = RequestMethod.GET)
    public String getCorrelationId(@PathVariable String correlationId){
        logger.info("correlationId in getCorrelationId: "+correlationId);
        return correlationMap.get(correlationId);
    }


}
