package pl.us.pawel.cwieka.travellingsalesmanproblem.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import pl.us.pawel.cwieka.travellingsalesmanproblem.aco.AntColonyOptimization;
import pl.us.pawel.cwieka.travellingsalesmanproblem.resource.AcoInput;

@RestController
@RequestMapping("/api")
public class RestApiController {

    private static final Logger logger = LogManager.getLogger(RestApiController.class);

    @PostMapping(value = "/calculatePath", consumes="application/json")
    @ResponseBody
    public String calculatePath(@RequestBody AcoInput acoInput){

        logger.info("POST request: /api/calculatePath started.");

        AntColonyOptimization antColonyOptimization = new AntColonyOptimization(acoInput);

        return antColonyOptimization.startAntOptimization();
    }


}
