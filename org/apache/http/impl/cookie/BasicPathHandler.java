package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;


































@Immutable
public class BasicPathHandler
  implements CookieAttributeHandler
{
  public void parse(SetCookie cookie, String value)
    throws MalformedCookieException
  {
    if (cookie == null) {
      throw new IllegalArgumentException("Cookie may not be null");
    }
    if ((value == null) || (value.trim().length() == 0)) {
      value = "/";
    }
    cookie.setPath(value);
  }
  
  public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException
  {
    if (!match(cookie, origin)) {
      throw new CookieRestrictionViolationException("Illegal path attribute \"" + cookie.getPath() + "\". Path of origin: \"" + origin.getPath() + "\"");
    }
  }
  

  public boolean match(Cookie cookie, CookieOrigin origin)
  {
    if (cookie == null) {
      throw new IllegalArgumentException("Cookie may not be null");
    }
    if (origin == null) {
      throw new IllegalArgumentException("Cookie origin may not be null");
    }
    String targetpath = origin.getPath();
    String topmostPath = cookie.getPath();
    if (topmostPath == null) {
      topmostPath = "/";
    }
    if ((topmostPath.length() > 1) && (topmostPath.endsWith("/"))) {
      topmostPath = topmostPath.substring(0, topmostPath.length() - 1);
    }
    boolean match = targetpath.startsWith(topmostPath);
    

    if ((match) && (targetpath.length() != topmostPath.length()) && 
      (!topmostPath.endsWith("/"))) {
      match = targetpath.charAt(topmostPath.length()) == '/';
    }
    
    return match;
  }
}
