package com.rdfanalyst.rest;

import com.rdfanalyst.accounting.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.rdfanalyst.rest.AddQueryResponse.*;

@Controller
public class QueryInfoController {

    private static final Logger logger = LoggerFactory.getLogger(QueryInfoController.class);

    @Autowired
    private QueryAccountingService queryAccountingService;

    @Autowired
    private ResultService resultService;

    @RequestMapping(value = "/add-query", method = RequestMethod.POST)
    public
    @ResponseBody
    AddQueryResponse addQuery(@RequestBody AddQueryRequest addQueryRequest) {
        try {
            queryAccountingService.registerQuery(new Query(addQueryRequest.getQuery(), addQueryRequest.getStream()));
            return ok();
        } catch (DuplicateQueryNameException e) {
            return duplicateNameError();
        } catch (Exception e) {
            logger.error("There was an exception while adding a new query. ", e);
            return customError(e);
        }
    }

    @RequestMapping(value = "/all-queries", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Query> allQueries() {
        return queryAccountingService.getAllQueries();
    }

    @RequestMapping(value = "/queries/{queryName}", method = RequestMethod.GET)
    public
    @ResponseBody
    Query queryDetails(@PathVariable String queryName) {
        return queryAccountingService.findQueryByTopic(queryName);
    }

    @RequestMapping(value = "/responses/{queryName}", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<RDFTriple> responsesSince(@PathVariable String queryName) {
        return resultService.findAllResultsForTopic(queryName);
    }
}
