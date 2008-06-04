/*
 * Copyright 2007 Tailrank, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.spinn3r.api;

import java.util.*;
import java.io.*;
import java.net.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

/**
 * 
 */
public class SourceListClient extends BaseClient implements Client {

    /**
     * When we've ran out of results (because the client is up to date) then we
     * should spin for a few seconds.  If the sleep interval is -1 then we sleep
     * for a random amount of time between 0 and 30 seconds.
     *
     * Default: Zero since it's usual for the permalinks status API to return <
     * 10 results.
     */
    public static final long DEFAULT_SLEEP_INTERVAL = 0L;

    public static int MAX_LIMIT = 250;

    public static int OPTIMAL_LIMIT = 250;

    public static int CONSERVATIVE_LIMIT = 10;

    /**
     * Base router request URL.
     */
    public static String ROUTER = "http://api.spinn3r.com/rss/source.list?";

    /**
     * How long we should sleep if an API call doesn't return enough values.
     */
    private long sleepInterval = DEFAULT_SLEEP_INTERVAL;

    public void fetch() throws IOException,
                               ParseException,
                               InterruptedException {
        
        super.fetch( config );
    }

    /**
     * Generate the first request URL based just on configuration directives.
     */
    protected String generateFirstRequestURL() {

        SourceListConfig config = (SourceListConfig)super.getConfig();
        
        StringBuffer params = new StringBuffer( 1024 ) ;

        addParam( params, "limit",   getOptimalLimit() );
        addParam( params, "vendor",  config.getVendor() );
        addParam( params, "version", config.getVersion() );

        //AFTER param needs to be added which should be ISO8601

        if ( config.getPublishedAfter() != null )
            addParam( params, "published_after",   toISO8601( config.getPublishedAfter() ) );

        if ( config.getFoundAfter() != null )
            addParam( params, "found_after",   toISO8601( config.getFoundAfter() ) );

        return getRouter() + params.toString();
        
    }

    protected BaseItem parseItem( Element current ) throws Exception {
        return new Source( current );
    }

    protected int getMaxLimit() {
        return MAX_LIMIT;
    }

    protected int getOptimalLimit() {
        return OPTIMAL_LIMIT;
    }

    protected int getConservativeLimit() {
        return CONSERVATIVE_LIMIT;
    }

    public String getRouter() {
        return "http://" + getHost() + "/rss/source.list?";
    }

    public long getSleepInterval() {
        return sleepInterval;
    }

    public static void main( String[] args ) throws Exception {

        SourceListConfig config = new SourceListConfig();
        SourceListClient client = new SourceListClient();

        config.setVendor( "XXXX" );
        config.setVersion( "2.2.1" );

        Date date = new Date( 1210661536159L );
        
        config.setFoundAfter( date );
        
        client.setConfig( config );

        int max = 2000;
        int total = 0;
        
        while( true ) {

            client.fetch();

            List<Source> results = client.getResults();

            total += results.size();
                
            for( Source item : results ) {
                System.out.printf( "resouce:    %s\n", item.getLink() );
                System.out.printf( "date_found: %s\n", item.getDateFound() );
            }

            //System.out.printf( "Found %d results:\n", results.size() );
            System.out.printf( "Last request URL: %s\n", client.getLastRequestURL() );
            System.out.printf( "Next request URL: %s\n", client.getNextRequestURL() );

            if ( total >= max )
                break;
            
        }

    }

}

