package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.CharArrayBuffer;



































@Immutable
public class BasicRequestLine
  implements RequestLine, Cloneable, Serializable
{
  private static final long serialVersionUID = 2810581718468737193L;
  private final ProtocolVersion protoversion;
  private final String method;
  private final String uri;
  
  public BasicRequestLine(String method, String uri, ProtocolVersion version)
  {
    if (method == null) {
      throw new IllegalArgumentException("Method must not be null.");
    }
    
    if (uri == null) {
      throw new IllegalArgumentException("URI must not be null.");
    }
    
    if (version == null) {
      throw new IllegalArgumentException("Protocol version must not be null.");
    }
    
    this.method = method;
    this.uri = uri;
    this.protoversion = version;
  }
  
  public String getMethod() {
    return this.method;
  }
  
  public ProtocolVersion getProtocolVersion() {
    return this.protoversion;
  }
  
  public String getUri() {
    return this.uri;
  }
  

  public String toString()
  {
    return BasicLineFormatter.DEFAULT.formatRequestLine(null, this).toString();
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
}
