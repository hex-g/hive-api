package hive.gateway.security;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import hive.common.security.HiveHeaders;
import org.springframework.core.Ordered;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserHeaderZuulFilter extends ZuulFilter {
  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return Ordered.LOWEST_PRECEDENCE - 100;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() {
    RequestContext.getCurrentContext().addZuulRequestHeader(
        HiveHeaders.AUTHENTICATED_USER_NAME_HEADER,
        SecurityContextHolder.getContext().getAuthentication().getName()
    );
    return null;
  }
}
