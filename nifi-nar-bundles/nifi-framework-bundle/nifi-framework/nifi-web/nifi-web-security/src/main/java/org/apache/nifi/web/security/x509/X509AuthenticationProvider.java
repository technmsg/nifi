package org.apache.nifi.web.security.x509;

import org.apache.nifi.web.security.token.NewAccountAuthenticationRequestToken;
import org.apache.nifi.web.security.token.NewAccountAuthenticationToken;
import org.apache.nifi.web.security.token.NiFiAuthenticationRequestToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 *
 */
public class X509AuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (NewAccountAuthenticationRequestToken.class.isAssignableFrom(authentication.getClass())) {
            return new NewAccountAuthenticationToken(((NewAccountAuthenticationRequestToken) authentication).getNewAccountRequest());
        } else if (NiFiAuthenticationRequestToken.class.isAssignableFrom(authentication.getClass())) {
            return authentication;
        } else {
            return null;
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return NiFiAuthenticationRequestToken.class.isAssignableFrom(authentication);
    }
    
}
