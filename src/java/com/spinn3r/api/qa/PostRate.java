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

package com.spinn3r.api.qa;

import com.spinn3r.api.*;

import java.util.*;
import java.io.*;

/**
 * Command line debug client for testing the API.  Also shows example usage.
 * 
 * This class will fetch the current API results as of INTERVAL minutes and then
 * keep fetching until it's up to date.
 * 
 */
public class PostRate {

    /**
     * How far behind should be start by default?
     */
    public static long INTERVAL = 24L * 60L * 60L * 1000L;

    /**
     * The date of the last item we found.
     */
    private Date last = null;

    private long fetch_before = -1;
    private long fetch_after = -1;

    /**
     * Keeps track of the client that the user wants to use from the command
     * line.
     */
    private Client client = null;

    /**
     * Results from the last call.
     */
    private List<BaseItem> results = null;

    private static long start = -1;

    private HashMap<Long,Integer> countRegistry = new HashMap();
    
    public PostRate( Client client ) {
        this.client = client;
    }
    
    /**
     * Process results, handling them as necessary.
     */
    void process( List<BaseItem> results ) throws Exception {

        int total = 0;
        for( BaseItem item : results ) {
            ++total;

            //update the state internally so we have a copy of the last item
            //found.
            last = item.getPubDate();
            
        }

        //update the number of values for this item interval
        long interval = 60L * 60L * 1000L;

        long hour = (last.getTime() / interval) * interval;

        if ( ! countRegistry.containsKey( hour ) )
            countRegistry.put( hour, 0 );
        
        int current_total = countRegistry.get( hour );
        
        current_total += total;
        
        countRegistry.put( hour, current_total );

        long diff = (start + INTERVAL) - last.getTime();
        
        System.out.println( "---" );
        System.out.println( "Seconds behind:   " + ( diff / 1000 ) );
        System.out.println( "Minutes behind:   " + ( diff / (60*1000) ) );
        System.out.println( "Current total :   " + current_total );

        if ( current_total % 1000 == 0 && current_total != 0 ) {
            printStats();
        }
        
    }

    private void printStats() {

        System.out.println( "------" );
        System.out.println( "Current stats: " );
        
        for( long hour : countRegistry.keySet() ) {

            System.out.println( new Date( hour ) + ": " + countRegistry.get( hour ) );
            
        }
        
    }
    
    public void exec() throws Exception {

        while( true ) {

            //fetch the most recent results.  This will block if necessary.

            long fetch_before = System.currentTimeMillis();

            client.fetch();

            long fetch_after  = System.currentTimeMillis();

            //get the results found from the last fetch.
            results = client.getResults();

            System.out.println( "Found N results: " + results.size() );

            process( results );

            if ( last.getTime() > start + INTERVAL ) {
                printStats();
                break;
            }

        }
        
    }

    public static void main( String[] args ) throws Exception {

        //parse out propeties.
        
        String vendor  = "spinn3r-debug";

        Config config = null;
        Client client = null;
        
        config = new FeedConfig();
        client = new FeedClient();
        
        // set your vendor.  This is required.  Don't use the default.
        
        config.setVendor( vendor );

        // just english for right now
        //config.setLang( "en" );

        // Fetch for the last 5 minutes and then try to get up to date.  In
        // production you'd want to call setFirstRequestURL from the
        // getLastRequestURL returned from fetch() below
        
        long now = System.currentTimeMillis();
        start = now - INTERVAL;
        config.setAfter( new Date( start ) );
        
        client.setConfig( config );

        new PostRate( client ).exec();
        
    }
    
}