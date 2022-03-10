package com.revature.loggingwith4j;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUtil {

        private static Logger logger = LoggerFactory.getLogger(com.revature.loggingwith4j.LoggingUtil.class);
//    final static Logger log = Logger.getLogger(Log4JDriver.class);

        public void logRequest(Context ctx, float ms) {
            logger.info(ctx.method() + " request made to: " + ctx.path() + " done in " + ms + " milliseconds");
        }


}
