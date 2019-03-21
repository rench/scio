package com.scio.cloud.oauth2.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
/**
 * ScioOauth2CodeService
 *
 * @author Wang.ch
 * @date 2019-03-21 17:29:53
 */
public class ScioOauth2CodeServices implements AuthorizationCodeServices {
  // generator 4 char
  private RandomValueStringGenerator generator = new RandomValueStringGenerator(4);
  protected final ConcurrentHashMap<String, OAuth2Authentication> authorizationCodeStore =
      new ConcurrentHashMap<String, OAuth2Authentication>();

  @Override
  public String createAuthorizationCode(OAuth2Authentication authentication) {
    String code = generator.generate();
    authorizationCodeStore.put(code, authentication);

    return code;
  }

  @Override
  public OAuth2Authentication consumeAuthorizationCode(String code) throws InvalidGrantException {
    OAuth2Authentication auth = authorizationCodeStore.remove(code);
    if (auth == null) {
      throw new InvalidGrantException("Invalid authorization code: " + code);
    }
    return auth;
  }
}
