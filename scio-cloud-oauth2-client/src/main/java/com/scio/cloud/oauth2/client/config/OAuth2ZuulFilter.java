package com.scio.cloud.oauth2.client.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * zuul proxy auto get access token.
 *
 * @doc
 *     https://stackoverflow.com/questions/43285205/spring-cloud-zuul-apply-filter-only-to-specific-route
 * @author Wang.ch
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "security.oauth2")
public class OAuth2ZuulFilter extends ZuulFilter {
  private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
  private static final String TOKEN_TYPE = "TOKEN_TYPE";

  private List<String> zuulRoutes = new ArrayList<>();

  private OAuth2RestOperations restTemplate;
  @Autowired RouteLocator locator;

  @Autowired
  public void setRestTemplate(OAuth2RestOperations restTemplate) {
    // List<Route> list = locator.getRoutes();
    this.restTemplate = restTemplate;
  }

  @Override
  public int filterOrder() {
    return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
  }

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public boolean shouldFilter() {
    RequestContext ctx = RequestContext.getCurrentContext();
    if (ctx.containsKey("proxy")) {
      String id = (String) ctx.get("proxy");
      if (!zuulRoutes.contains(id)) {
        return false;
      } else {
        ctx.set(TOKEN_TYPE, "Bearer");
        return true;
      }
    }
    return false;
  }

  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    ctx.addZuulRequestHeader("authorization", ctx.get(TOKEN_TYPE) + " " + getAccessToken(ctx));
    return null;
  }

  private String getAccessToken(RequestContext ctx) {
    String value = (String) ctx.get(ACCESS_TOKEN);
    if (restTemplate != null) {
      try {
        value = restTemplate.getAccessToken().getValue();
      } catch (Exception e) {
        throw new BadCredentialsException("Cannot obtain valid access token");
      }
    }
    return value;
  }

  /** @param zuulRoutes the zuulRoutes to set */
  public void setZuulRoutes(List<String> zuulRoutes) {
    this.zuulRoutes = zuulRoutes;
  }
}
