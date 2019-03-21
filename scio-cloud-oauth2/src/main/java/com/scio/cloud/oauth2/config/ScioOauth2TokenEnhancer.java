package com.scio.cloud.oauth2.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
/**
 * token enhancer to response more info like user name etc.
 *
 * @author Wang.ch
 * @date 2019-03-20 14:07:36
 */
public class ScioOauth2TokenEnhancer implements TokenEnhancer {

  @Override
  public OAuth2AccessToken enhance(
      OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    if (accessToken instanceof DefaultOAuth2AccessToken) {
      DefaultOAuth2AccessToken enhancerToken = ((DefaultOAuth2AccessToken) accessToken);
      enhancerToken.setValue(getNewScioToken());
      OAuth2RefreshToken refreshToken = enhancerToken.getRefreshToken();
      if (refreshToken instanceof DefaultOAuth2RefreshToken) {
        enhancerToken.setRefreshToken(new DefaultOAuth2RefreshToken(getNewScioToken()));
      }
      Map<String, Object> additionalInformation = new HashMap<String, Object>();
      Map<String, Object> ext = new HashMap<>();
      Object principal = authentication.getPrincipal();
      if (principal instanceof User) {
        User user = (User) principal;
        ext.put("username", user.getUsername());
      } else {
        ext.put("username", principal);
      }
      ext.put("client_id", authentication.getOAuth2Request().getClientId());
      additionalInformation.put("ext", ext);
      enhancerToken.setAdditionalInformation(additionalInformation);
    }
    return accessToken;
  }

  private String getNewScioToken() {
    return "scio@" + UUID.randomUUID().toString().replace("-", "");
  }
}
