package org.apache.nifi.web.security.user;

import java.util.List;

/**
 *
 */
public class NewAccountRequest {

    private final List<String> chain;
    private final String justification;

    public NewAccountRequest(final List<String> chain, final String justification) {
        this.chain = chain;
        this.justification = justification;
    }

    public List<String> getChain() {
        return chain;
    }

    public String getJustification() {
        return justification;
    }
    
    public String getUsername() {
        // the end user is the first item in the chain
        return chain.get(0);
    }
    
}
