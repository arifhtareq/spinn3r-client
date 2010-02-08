package com.spinn3r.api;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URLConnection;



public class PartailBaseClientResult<ResultType> {

 
    private Config<ResultType> config = null;

    private String lastRequestURL = null;
    private String nextRequestURL = null;

    private boolean hasMoreResults        = true;
    private boolean hasMoreResultsHeadder = false;

    private int requestLimit = -1;

    private URLConnection connection = null;

    public PartailBaseClientResult ( Config<ResultType> config_value ) {
        config = config_value;
    }


    /**
     * Return the config used for this request.
     */
    public Config<ResultType> getConfig () {
        return config;
    }


    /**
     * 
     * Get the last requested URL for debug and logging purposes.
     *
     */
    public String getLastRequestURL() { 
        return this.lastRequestURL;
    }

    /**
     * 
     * Set the value of <code>lastRequestURL</code>.
     *
     */
    void setLastRequestURL( String lastRequestURL ) { 
        this.lastRequestURL = lastRequestURL;
    }

    /**
     * 
     * Get the value of <code>nextRequestURL</code>.
     *
     */
    public String getNextRequestURL() { 
        return this.nextRequestURL;
    }

    /**
     * 
     * Set the value of <code>nextRequestURL</code>.
     *
     */
    void setNextRequestURL( String next ) { 

        Config<ResultType> config = getConfig();

        //TODO: apply the correct hostname to the next request.

        if ( config.getHost() != null ) {
            String path = next.substring( next.indexOf( "/", "http://".length() ), next.length() );
            next = String.format( "http://%s%s", config.getHost(), path );
        }

        this.nextRequestURL = next;
    }


    /**
     * Return true if more results are available.
     *
     */
    public boolean getHasMoreResults() {
        return hasMoreResults;
    }


    /**
     * Set if more results are available.
     *
     */
    void setHasMoreResults( boolean value ) {
        hasMoreResults = value;
    }


    /**
     * Return true if more results are available.
     *
     */
    public boolean getHasMoreResultsHeadder() {
        return hasMoreResultsHeadder;
    }


    /**
     * Set if more results are available.
     *
     */
    void setHasMoreResultsHeadder( boolean value ) {
        hasMoreResultsHeadder = value;
    }

 
    /**
     * Get the limit used to gennreat this result.
     */
    public int getRequestLimit () {
        return requestLimit;
    }


    void setRequestLimit ( int value ) {
        requestLimit = value;
    }

    
    URLConnection getConnection () {
        return connection;
    }

    void setConnection ( URLConnection value ) {
        connection = value;
    }

}