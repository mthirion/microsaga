package org.jboss.integration.fis2demo.gateway;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import org.json.JSONObject;
import net.sf.json.JSONObject;



@RestController
@RequestMapping("/api")
@CrossOrigin
public class MyRestController {

	@Autowired
	private MySpringBootConfig config;
	
	private static Processor processor;

    @RequestMapping(

            method = RequestMethod.POST,

            //produces = MediaType.APPLICATION_JSON_VALUE,

            //value={"/{entity}", "/{entity}/{id}/relationships/{entity2}", "/{entity}/{id}/{child}", "/{entity}/{id}"})
            value={"/open_account"}
    )
    public String doPost(@RequestParam final Map<String, String> allRequestParams, @RequestBody String requestBody) {
    	
    	JSONObject data = JSONObject.fromString(requestBody);
    	
    	System.out.println("DEBUG : Data received at /api/open_account : ");
    	System.out.println(data.toString());
    	System.out.println();
    	
    	if (config == null) {
    		System.out.println("Config is null");
    		System.out.flush();
    		System.exit(1);
    	}

    	if (processor == null) processor = new Processor();
    	processor.openAccount(data, config);
    	
    	return requestBody;
    }    
    
    
    
    
    @RequestMapping(

            method = RequestMethod.GET,
            value={"/user"}
    )
    public String doGet(@RequestParam final Map<String, String> allRequestParams) {
    	
    	return "Done";
    }    
    
    @RequestMapping(

            method = RequestMethod.GET,
            value={"/user/{id}"}
    )    
    public String doGet(@PathVariable("id") long id) {

    	return "User : " + Long.toString(id);
    }    
    
    
}
