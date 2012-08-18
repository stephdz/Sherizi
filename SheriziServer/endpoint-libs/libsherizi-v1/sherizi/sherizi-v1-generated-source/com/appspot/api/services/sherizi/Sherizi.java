/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This file was generated.
 *  with google-apis-code-generator 1.1.1 (build: 2012-08-07 15:34:44 UTC)
 *  on 2012-08-18 at 11:29:53 UTC 
 */

package com.appspot.api.services.sherizi;


import com.google.api.client.googleapis.services.GoogleClient;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethod;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.common.base.Preconditions;

import java.io.IOException;


/**
 * Service definition for Sherizi (v1).
 *
 * <p>
 * 
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link JsonHttpRequestInitializer} to initialize global parameters via its
 * {@link Builder}. Sample usage:
 * </p>
 *
 * <pre>
  public class SheriziRequestInitializer implements JsonHttpRequestInitializer {
      public void initialize(JsonHttpRequest request) {
        SheriziRequest sheriziRequest = (SheriziRequest)request;
        sheriziRequest.setPrettyPrint(true);
        sheriziRequest.setKey(ClientCredentials.KEY);
    }
  }
 * </pre>
 *
 * @since 1.3.0
 * @author Google, Inc.
 */
public class Sherizi extends GoogleClient {

  /**
   * The default encoded base path of the service. This is determined when the library is generated
   * and normally should not be changed.
   * @deprecated (scheduled to be removed in 1.8) Use "/" + {@link #DEFAULT_SERVICE_PATH}.
   */
  @Deprecated
  public static final String DEFAULT_BASE_PATH = "/_ah/api/sherizi/v1/";

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://dz-sherizi.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "sherizi/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Construct a Sherizi instance to connect to the Sherizi service.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport The transport to use for requests
   * @param jsonFactory A factory for creating JSON parsers and serializers
   * @deprecated (scheduled to be removed in 1.8) Use
   *             {@link #Sherizi(HttpTransport, JsonFactory, HttpRequestInitializer)}.
   */
  @Deprecated
  public Sherizi(HttpTransport transport, JsonFactory jsonFactory) {
    super(transport, jsonFactory, DEFAULT_BASE_URL);
  }

  /**
   * Construct a Sherizi instance to connect to the Sherizi service.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport The transport to use for requests
   * @param jsonFactory A factory for creating JSON parsers and serializers
   * @param httpRequestInitializer The HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Sherizi(HttpTransport transport, JsonFactory jsonFactory,
      HttpRequestInitializer httpRequestInitializer) {
    super(transport, jsonFactory, DEFAULT_ROOT_URL, DEFAULT_SERVICE_PATH, httpRequestInitializer);
  }

  /**
   * Construct a Sherizi instance to connect to the Sherizi service.
   *
   * @param transport The transport to use for requests
   * @param jsonHttpRequestInitializer The initializer to use when creating an JSON HTTP request
   * @param httpRequestInitializer The initializer to use when creating an {@link HttpRequest}
   * @param jsonFactory A factory for creating JSON parsers and serializers
   * @param jsonObjectParser JSON parser to use or {@code null} if unused
   * @param baseUrl The base URL of the service on the server
   * @param applicationName The application name to be sent in the User-Agent header of requests
   */
  @Deprecated
  Sherizi(
      HttpTransport transport,
      JsonHttpRequestInitializer jsonHttpRequestInitializer,
      HttpRequestInitializer httpRequestInitializer,
      JsonFactory jsonFactory,
      JsonObjectParser jsonObjectParser,
      String baseUrl,
      String applicationName) {
      super(transport,
          jsonHttpRequestInitializer,
          httpRequestInitializer,
          jsonFactory,
          jsonObjectParser,
          baseUrl,
          applicationName);
  }

  /**
   * Construct a Sherizi instance to connect to the Sherizi service.
   *
   * @param transport The transport to use for requests
   * @param jsonHttpRequestInitializer The initializer to use when creating an JSON HTTP request
   * @param httpRequestInitializer The initializer to use when creating an {@link HttpRequest}
   * @param jsonFactory A factory for creating JSON parsers and serializers
   * @param jsonObjectParser JSON parser to use or {@code null} if unused
   * @param rootUrl The root URL of the service on the server
   * @param servicePath The service path of the service on the server
   * @param applicationName The application name to be sent in the User-Agent header of requests
   */
  Sherizi(
      HttpTransport transport,
      JsonHttpRequestInitializer jsonHttpRequestInitializer,
      HttpRequestInitializer httpRequestInitializer,
      JsonFactory jsonFactory,
      JsonObjectParser jsonObjectParser,
      String rootUrl,
      String servicePath,
      String applicationName) {
      super(transport,
          jsonHttpRequestInitializer,
          httpRequestInitializer,
          jsonFactory,
          jsonObjectParser,
          rootUrl,
          servicePath,
          applicationName);
  }

  @Override
  protected void initialize(JsonHttpRequest jsonHttpRequest) throws IOException {
    super.initialize(jsonHttpRequest);
  }


  /**
   * Returns an instance of a new builder.
   *
   * @param transport The transport to use for requests
   * @param jsonFactory A factory for creating JSON parsers and serializers
   * @deprecated (scheduled to removed in 1.8) Use
   *             {@link Builder#Builder(HttpTransport, JsonFactory, HttpRequestInitializer)}.
   */
   @Deprecated
   public static Builder builder(HttpTransport transport, JsonFactory jsonFactory) {
     return new Builder(transport, jsonFactory, new GenericUrl(DEFAULT_BASE_URL));
   }


  /**
   * Create a request for the method "initiateTransfer".
   *
   * This request holds the parameters needed by the the sherizi server.  After setting any optional
   * parameters, call the {@link InitiateTransfer#execute()} method to invoke the remote operation.
   *
   * @param emailFrom
   * @param deviceFrom
   * @param emailTo
   * @param deviceTo
   * @param transferMode
   * @return the request
   * @throws IOException if the initialization of the request fails
   */
  public InitiateTransfer initiateTransfer(String emailFrom, String deviceFrom, String emailTo, String deviceTo, String transferMode) throws IOException {
    InitiateTransfer result = new InitiateTransfer(emailFrom, deviceFrom, emailTo, deviceTo, transferMode);
    initialize(result);
    return result;
  }


  public class InitiateTransfer extends SheriziRequest {

    private static final String REST_PATH = "initiateTransfer/{emailFrom}/{deviceFrom}/{emailTo}/{deviceTo}/{transferMode}";

    /**
     * Internal constructor.  Use the convenience method instead.
     */
    InitiateTransfer(String emailFrom, String deviceFrom, String emailTo, String deviceTo, String transferMode) {
      super(Sherizi.this, HttpMethod.POST, REST_PATH, null);
      this.emailFrom = Preconditions.checkNotNull(emailFrom, "Required parameter emailFrom must be specified.");
      this.deviceFrom = Preconditions.checkNotNull(deviceFrom, "Required parameter deviceFrom must be specified.");
      this.emailTo = Preconditions.checkNotNull(emailTo, "Required parameter emailTo must be specified.");
      this.deviceTo = Preconditions.checkNotNull(deviceTo, "Required parameter deviceTo must be specified.");
      this.transferMode = Preconditions.checkNotNull(transferMode, "Required parameter transferMode must be specified.");
    }



    /**
     * Sends the "initiateTransfer" request to the Sherizi server.
     *
     * @throws IOException if the request fails
     */
    public void execute() throws IOException {
      HttpResponse response = executeUnparsed();
      response.ignore();
    }

    /**
     * Queues the "initiateTransfer" request to the Sherizi server into the given batch request.
     *
     * <p>
     * Example usage:
     * </p>
     *
     * <pre>
       request.queue(batchRequest, new JsonBatchCallback&lt;Void&gt;() {

         public void onSuccess(Void content, GoogleHeaders responseHeaders) {
           log("Success");
         }

         public void onFailure(GoogleJsonError e, GoogleHeaders responseHeaders) {
           log(e.getMessage());
         }
       });
     * </pre>
     *
     * @param batch a single batch of requests
     * @param callback batch callback
     * @since 1.6
     */
    public void queue(com.google.api.client.googleapis.batch.BatchRequest batch,
        com.google.api.client.googleapis.batch.json.JsonBatchCallback<Void> callback)
        throws IOException {
      batch.queue(buildHttpRequest(), Void.class,
          com.google.api.client.googleapis.json.GoogleJsonErrorContainer.class, callback);
    }

    /**
     * @since 1.7
     */
    @Override
    public InitiateTransfer setFields(String fields) {
      super.setFields(fields);
      return this;
    }


    @com.google.api.client.util.Key
    private String emailFrom;

    /**

     */
    public String getEmailFrom() {
      return emailFrom;
    }


    public InitiateTransfer setEmailFrom(String emailFrom) {
      this.emailFrom = emailFrom;
      return this;
    }


    @com.google.api.client.util.Key
    private String deviceFrom;

    /**

     */
    public String getDeviceFrom() {
      return deviceFrom;
    }


    public InitiateTransfer setDeviceFrom(String deviceFrom) {
      this.deviceFrom = deviceFrom;
      return this;
    }


    @com.google.api.client.util.Key
    private String emailTo;

    /**

     */
    public String getEmailTo() {
      return emailTo;
    }


    public InitiateTransfer setEmailTo(String emailTo) {
      this.emailTo = emailTo;
      return this;
    }


    @com.google.api.client.util.Key
    private String deviceTo;

    /**

     */
    public String getDeviceTo() {
      return deviceTo;
    }


    public InitiateTransfer setDeviceTo(String deviceTo) {
      this.deviceTo = deviceTo;
      return this;
    }


    @com.google.api.client.util.Key
    private String transferMode;

    /**

     */
    public String getTransferMode() {
      return transferMode;
    }


    public InitiateTransfer setTransferMode(String transferMode) {
      this.transferMode = transferMode;
      return this;
    }



  }

  /**
   * Create a request for the method "deleteUser".
   *
   * This request holds the parameters needed by the the sherizi server.  After setting any optional
   * parameters, call the {@link DeleteUser#execute()} method to invoke the remote operation.
   *
   * @param email
   * @param deviceName
   * @return the request
   * @throws IOException if the initialization of the request fails
   */
  public DeleteUser deleteUser(String email, String deviceName) throws IOException {
    DeleteUser result = new DeleteUser(email, deviceName);
    initialize(result);
    return result;
  }


  public class DeleteUser extends SheriziRequest {

    private static final String REST_PATH = "user/{email}/{deviceName}";

    /**
     * Internal constructor.  Use the convenience method instead.
     */
    DeleteUser(String email, String deviceName) {
      super(Sherizi.this, HttpMethod.DELETE, REST_PATH, null);
      this.email = Preconditions.checkNotNull(email, "Required parameter email must be specified.");
      this.deviceName = Preconditions.checkNotNull(deviceName, "Required parameter deviceName must be specified.");
    }



    /**
     * Sends the "deleteUser" request to the Sherizi server.
     *
     * @return the {@link com.appspot.api.services.sherizi.model.User} response
     * @throws IOException if the request fails
     */
    public com.appspot.api.services.sherizi.model.User execute() throws IOException {
      HttpResponse response = executeUnparsed();
      com.appspot.api.services.sherizi.model.User result = response.parseAs(
          com.appspot.api.services.sherizi.model.User.class);
      result.setResponseHeaders(response.getHeaders());
      return result;
    }

    /**
     * Queues the "deleteUser" request to the Sherizi server into the given batch request.
     *
     * <p>
     * Example usage:
     * </p>
     *
     * <pre>
       request.queue(batchRequest, new JsonBatchCallback&lt;User&gt;() {

         public void onSuccess(User content, GoogleHeaders responseHeaders) {
           log("Success");
         }

         public void onFailure(GoogleJsonError e, GoogleHeaders responseHeaders) {
           log(e.getMessage());
         }
       });
     * </pre>
     *
     * @param batch a single batch of requests
     * @param callback batch callback
     * @since 1.6
     */
    public void queue(com.google.api.client.googleapis.batch.BatchRequest batch,
        com.google.api.client.googleapis.batch.json.JsonBatchCallback<com.appspot.api.services.sherizi.model.User> callback)
        throws IOException {
      batch.queue(buildHttpRequest(), com.appspot.api.services.sherizi.model.User.class,
          com.google.api.client.googleapis.json.GoogleJsonErrorContainer.class, callback);
    }

    /**
     * @since 1.7
     */
    @Override
    public DeleteUser setFields(String fields) {
      super.setFields(fields);
      return this;
    }


    @com.google.api.client.util.Key
    private String email;

    /**

     */
    public String getEmail() {
      return email;
    }


    public DeleteUser setEmail(String email) {
      this.email = email;
      return this;
    }


    @com.google.api.client.util.Key
    private String deviceName;

    /**

     */
    public String getDeviceName() {
      return deviceName;
    }


    public DeleteUser setDeviceName(String deviceName) {
      this.deviceName = deviceName;
      return this;
    }



  }

  /**
   * Create a request for the method "searchFriends".
   *
   * This request holds the parameters needed by the the sherizi server.  After setting any optional
   * parameters, call the {@link SearchFriends#execute()} method to invoke the remote operation.
   *
   * @param emails
   * @return the request
   * @throws IOException if the initialization of the request fails
   */
  public SearchFriends searchFriends(String emails) throws IOException {
    SearchFriends result = new SearchFriends(emails);
    initialize(result);
    return result;
  }


  public class SearchFriends extends SheriziRequest {

    private static final String REST_PATH = "searchFriends/{emails}";

    /**
     * Internal constructor.  Use the convenience method instead.
     */
    SearchFriends(String emails) {
      super(Sherizi.this, HttpMethod.POST, REST_PATH, null);
      this.emails = Preconditions.checkNotNull(emails, "Required parameter emails must be specified.");
    }



    /**
     * Sends the "searchFriends" request to the Sherizi server.
     *
     * @return the {@link com.appspot.api.services.sherizi.model.Users} response
     * @throws IOException if the request fails
     */
    public com.appspot.api.services.sherizi.model.Users execute() throws IOException {
      HttpResponse response = executeUnparsed();
      com.appspot.api.services.sherizi.model.Users result = response.parseAs(
          com.appspot.api.services.sherizi.model.Users.class);
      result.setResponseHeaders(response.getHeaders());
      return result;
    }

    /**
     * Queues the "searchFriends" request to the Sherizi server into the given batch request.
     *
     * <p>
     * Example usage:
     * </p>
     *
     * <pre>
       request.queue(batchRequest, new JsonBatchCallback&lt;Users&gt;() {

         public void onSuccess(Users content, GoogleHeaders responseHeaders) {
           log("Success");
         }

         public void onFailure(GoogleJsonError e, GoogleHeaders responseHeaders) {
           log(e.getMessage());
         }
       });
     * </pre>
     *
     * @param batch a single batch of requests
     * @param callback batch callback
     * @since 1.6
     */
    public void queue(com.google.api.client.googleapis.batch.BatchRequest batch,
        com.google.api.client.googleapis.batch.json.JsonBatchCallback<com.appspot.api.services.sherizi.model.Users> callback)
        throws IOException {
      batch.queue(buildHttpRequest(), com.appspot.api.services.sherizi.model.Users.class,
          com.google.api.client.googleapis.json.GoogleJsonErrorContainer.class, callback);
    }

    /**
     * @since 1.7
     */
    @Override
    public SearchFriends setFields(String fields) {
      super.setFields(fields);
      return this;
    }


    @com.google.api.client.util.Key
    private String emails;

    /**

     */
    public String getEmails() {
      return emails;
    }


    public SearchFriends setEmails(String emails) {
      this.emails = emails;
      return this;
    }



  }

  /**
   * Create a request for the method "saveOrUpdateUser".
   *
   * This request holds the parameters needed by the the sherizi server.  After setting any optional
   * parameters, call the {@link SaveOrUpdateUser#execute()} method to invoke the remote operation.
   *
   * @param content the {@link com.appspot.api.services.sherizi.model.User}
   * @return the request
   * @throws IOException if the initialization of the request fails
   */
  public SaveOrUpdateUser saveOrUpdateUser(com.appspot.api.services.sherizi.model.User content) throws IOException {
    SaveOrUpdateUser result = new SaveOrUpdateUser(content);
    initialize(result);
    return result;
  }


  public class SaveOrUpdateUser extends SheriziRequest {

    private static final String REST_PATH = "saveOrUpdateUser";

    /**
     * Internal constructor.  Use the convenience method instead.
     */
    SaveOrUpdateUser(com.appspot.api.services.sherizi.model.User content) {
      super(Sherizi.this, HttpMethod.POST, REST_PATH, content);
      Preconditions.checkNotNull(content);
    }



    /**
     * Sends the "saveOrUpdateUser" request to the Sherizi server.
     *
     * @return the {@link com.appspot.api.services.sherizi.model.User} response
     * @throws IOException if the request fails
     */
    public com.appspot.api.services.sherizi.model.User execute() throws IOException {
      HttpResponse response = executeUnparsed();
      com.appspot.api.services.sherizi.model.User result = response.parseAs(
          com.appspot.api.services.sherizi.model.User.class);
      result.setResponseHeaders(response.getHeaders());
      return result;
    }

    /**
     * Queues the "saveOrUpdateUser" request to the Sherizi server into the given batch request.
     *
     * <p>
     * Example usage:
     * </p>
     *
     * <pre>
       request.queue(batchRequest, new JsonBatchCallback&lt;User&gt;() {

         public void onSuccess(User content, GoogleHeaders responseHeaders) {
           log("Success");
         }

         public void onFailure(GoogleJsonError e, GoogleHeaders responseHeaders) {
           log(e.getMessage());
         }
       });
     * </pre>
     *
     * @param batch a single batch of requests
     * @param callback batch callback
     * @since 1.6
     */
    public void queue(com.google.api.client.googleapis.batch.BatchRequest batch,
        com.google.api.client.googleapis.batch.json.JsonBatchCallback<com.appspot.api.services.sherizi.model.User> callback)
        throws IOException {
      batch.queue(buildHttpRequest(), com.appspot.api.services.sherizi.model.User.class,
          com.google.api.client.googleapis.json.GoogleJsonErrorContainer.class, callback);
    }

    /**
     * @since 1.7
     */
    @Override
    public SaveOrUpdateUser setFields(String fields) {
      super.setFields(fields);
      return this;
    }



  }


  /**
   * Builder for {@link Sherizi}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends GoogleClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport The transport to use for requests
     * @param jsonFactory A factory for creating JSON parsers and serializers
     * @param baseUrl The base URL of the service. Must end with a "/"
     */
    @Deprecated
    Builder(HttpTransport transport, JsonFactory jsonFactory, GenericUrl baseUrl) {
      super(transport, jsonFactory, baseUrl);
    }

    /**
     * Returns an instance of a new builder.
     *
     * @param transport The transport to use for requests
     * @param jsonFactory A factory for creating JSON parsers and serializers
     * @param httpRequestInitializer The HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(HttpTransport transport, JsonFactory jsonFactory,
        HttpRequestInitializer httpRequestInitializer) {
      super(transport, jsonFactory, DEFAULT_ROOT_URL, DEFAULT_SERVICE_PATH, httpRequestInitializer);
    }

    /** Builds a new instance of {@link Sherizi}. */
    @SuppressWarnings("deprecation")
    @Override
    public Sherizi build() {
      if (isBaseUrlUsed()) {
        return new Sherizi(
            getTransport(),
            getJsonHttpRequestInitializer(),
            getHttpRequestInitializer(),
            getJsonFactory(),
            getObjectParser(),
            getBaseUrl().build(),
            getApplicationName());
      }
      return new Sherizi(
          getTransport(),
          getJsonHttpRequestInitializer(),
          getHttpRequestInitializer(),
          getJsonFactory(),
          getObjectParser(),
          getRootUrl(),
          getServicePath(),
          getApplicationName());
    }

    @Override
    @Deprecated
    public Builder setBaseUrl(GenericUrl baseUrl) {
      super.setBaseUrl(baseUrl);
      return this;
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      super.setRootUrl(rootUrl);
      return this;
    }

    @Override
    public Builder setServicePath(String servicePath) {
      super.setServicePath(servicePath);
      return this;
    }

    @Override
    public Builder setJsonHttpRequestInitializer(
        JsonHttpRequestInitializer jsonHttpRequestInitializer) {
      super.setJsonHttpRequestInitializer(jsonHttpRequestInitializer);
      return this;
    }

    @Override
    public Builder setHttpRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
      super.setHttpRequestInitializer(httpRequestInitializer);
      return this;
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      super.setApplicationName(applicationName);
      return this;
    }

    @Override
    public Builder setObjectParser(JsonObjectParser parser) {
      super.setObjectParser(parser);
      return this;
    }
  }
}
