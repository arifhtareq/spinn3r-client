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

/**
 * Used to startup the API and specify defaults for limits, where to start
 * indexing, tiers, language, etc.
 */
public abstract class Config {

    /**
     * When we've ran out of results (because the client is up to date) then we
     * should spin for a few seconds.  If the sleep interval is -1 then we sleep
     * for a random amount of time between 0 and 30 seconds.
     */
    public static final long DEFAULT_SLEEP_INTERVAL = 30L * 1000L;

    /**
     * Default number of results to fetch.
     *
     */
    public static int      DEFAULT_LIMIT       = 10;

    /**
     * When fetching the API this specifies the default version to return.
     */
    public static String   DEFAULT_VERSION     = "2.1.1";
    
    private int            limit               = DEFAULT_LIMIT;
    private String         lang                = null;
    private String         version             = DEFAULT_VERSION;
    private String         vendor              = null;
    private int            tier_start          = -1;
    private int            tier_end            = -1;
    private Date           after               = new Date(); /* use epoch as default */
    private String         firstRequestURL     = null;

    /**
     * How long we should sleep if an API call doesn't return enough values.
     */
    private long sleepInterval = DEFAULT_SLEEP_INTERVAL;

    /**
     * 
     * Set the value of <code>firstRequestURL</code>.
     *
     */
    public void setFirstRequestURL( String firstRequestURL ) { 
        this.firstRequestURL = firstRequestURL;
    }

    /**
     * 
     * Get the value of <code>firstRequestURL</code>.
     *
     */
    public String getFirstRequestURL() { 
        return this.firstRequestURL;
    }

    /**
     * 
     * Get the value of <code>after</code>.
     *
     */
    public Date getAfter() { 
        return this.after;
    }

    /**
     * 
     * Set the value of <code>after</code>.
     *
     */
    public void setAfter( Date after ) { 
        this.after = after;
    }
    
    /**
     * 
     * Specify the vendor for this call.  This MUST be specified or the client
     * will not work.
     *
     */
    public String getVendor() { 
        return this.vendor;
    }

    /**
     * 
     * Set the value of <code>vendor</code>.
     *
     */
    public void setVendor( String vendor ) { 
        this.vendor = vendor;
    }

    /**
     * 
     * Get the value of <code>version</code>.
     *
     */
    public String getVersion() { 
        return this.version;
    }

    /**
     * 
     * Set the value of <code>version</code>.
     *
     */
    public void setVersion( String version ) { 
        this.version = version;
    }

    /**
     * 
     * Get the value of <code>lang</code>.
     *
     */
    public String getLang() { 
        return this.lang;
    }

    /**
     * 
     * Set the value of <code>lang</code>.
     *
     */
    public void setLang( String lang ) { 
        this.lang = lang;
    }

    /**
     * 
     * Set the value of <code>limit</code>.
     *
     */
    public void setLimit( int limit ) { 
        this.limit = limit;
    }

    /**
     * 
     * Get the value of <code>limit</code>.
     *
     */
    public int getLimit() { 
        return this.limit;
    }

    /**
     * Specify the tier to fetch.
     */
    public void setTier( int start, int end ) {
        tier_start = start;
        tier_end   = end;
    }

    public int getTierStart() {
        return tier_start;
    }

    public int getTierEnd() {
        return tier_end;
    }

    public long getSleepInterval() {

        long result = sleepInterval;
        
        if ( result == -1 ) {
            //use a random number generator to compute the
            float f = new Random().nextFloat();
            
            result = (long)(f * 30L);

        }
        
        return result;
    }

}