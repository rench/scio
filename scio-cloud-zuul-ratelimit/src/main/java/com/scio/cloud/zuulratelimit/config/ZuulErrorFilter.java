package com.scio.cloud.zuulratelimit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * zuul error custom filter
 *
 * @author wang.ch
 * @date 2019-01-31 15:04:03
 */
@Component
public class ZuulErrorFilter extends ZuulFilter {
  private static final Logger LOG = LoggerFactory.getLogger(ZuulErrorFilter.class);

  @Override
  public boolean shouldFilter() {
    return RequestContext.getCurrentContext().containsKey("throwable");
  }

  @Override
  public Object run() throws ZuulException {
    try {
      RequestContext ctx = RequestContext.getCurrentContext();
      Object e = ctx.get("throwable");
      if (e != null && e instanceof ZuulException) {
        ZuulException zuulException = (ZuulException) e;
        // Remove error code to prevent further error handling in follow up filters
        ctx.remove("throwable");
        // custom
        ctx.setResponseBody(zuulException.getMessage());
        ctx.setResponseStatusCode(zuulException.nStatusCode);
      }
    } catch (Exception ex) {
      LOG.error("Exception filtering in custom error filter", ex);
      ReflectionUtils.rethrowRuntimeException(ex);
    }
    return null;
  }

  @Override
  public String filterType() {
    return "error";
  }

  @Override
  public int filterOrder() {
    // Needs to run before SendErrorFilter which has filterOrder == 0
    return -1;
  }
}
