package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;




































@ThreadSafe
public class AutoRetryHttpClient
  implements HttpClient
{
  private final HttpClient backend;
  private final ServiceUnavailableRetryStrategy retryStrategy;
  private final Log log = LogFactory.getLog(getClass());
  

  public AutoRetryHttpClient(HttpClient client, ServiceUnavailableRetryStrategy retryStrategy)
  {
    if (client == null) {
      throw new IllegalArgumentException("HttpClient may not be null");
    }
    if (retryStrategy == null) {
      throw new IllegalArgumentException("ServiceUnavailableRetryStrategy may not be null");
    }
    
    this.backend = client;
    this.retryStrategy = retryStrategy;
  }
  




  public AutoRetryHttpClient()
  {
    this(new DefaultHttpClient(), new DefaultServiceUnavailableRetryStrategy());
  }
  







  public AutoRetryHttpClient(ServiceUnavailableRetryStrategy config)
  {
    this(new DefaultHttpClient(), config);
  }
  







  public AutoRetryHttpClient(HttpClient client)
  {
    this(client, new DefaultServiceUnavailableRetryStrategy());
  }
  
  public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException
  {
    HttpContext defaultContext = null;
    return execute(target, request, defaultContext);
  }
  
  public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException
  {
    return execute(target, request, responseHandler, null);
  }
  
  public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context)
    throws IOException
  {
    HttpResponse resp = execute(target, request, context);
    return responseHandler.handleResponse(resp);
  }
  
  public HttpResponse execute(HttpUriRequest request) throws IOException {
    HttpContext context = null;
    return execute(request, context);
  }
  
  public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException
  {
    URI uri = request.getURI();
    HttpHost httpHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
    
    return execute(httpHost, request, context);
  }
  
  public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException
  {
    return execute(request, responseHandler, null);
  }
  
  public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context)
    throws IOException
  {
    HttpResponse resp = execute(request, context);
    return responseHandler.handleResponse(resp);
  }
  
  public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException
  {
    for (int c = 1;; c++) {
      HttpResponse response = this.backend.execute(target, request, context);
      if (this.retryStrategy.retryRequest(response, c, context)) {
        long nextInterval = this.retryStrategy.getRetryInterval();
        try {
          this.log.trace("Wait for " + nextInterval);
          Thread.sleep(nextInterval);
        } catch (InterruptedException e) {
          throw new InterruptedIOException(e.getMessage());
        }
      } else {
        return response;
      }
    }
  }
  
  public ClientConnectionManager getConnectionManager() {
    return this.backend.getConnectionManager();
  }
  
  public HttpParams getParams() {
    return this.backend.getParams();
  }
}
