package jooby;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import jooby.fn.ExSupplier;

import com.google.common.annotations.Beta;

/**
 * Give you access to the actual HTTP response. You can read/write headers and write HTTP body.
 *
 * @author edgar
 * @since 0.1.0
 */
@Beta
public interface Response {

  public class Forwarding implements Response {

    private Response response;

    public Forwarding(final Response response) {
      this.response = requireNonNull(response, "A response is required.");
    }

    @Override
    public void download(final String filename, final InputStream stream) throws Exception {
      response.download(filename, stream);
    }

    @Override
    public void download(final String filename, final Reader reader) throws Exception {
      response.download(filename, reader);
    }

    @Override
    public void download(final File file) throws Exception {
      response.download(file);
    }

    @Override
    public void download(final String filename) throws Exception {
      response.download(filename);
    }

    @Override
    public Response cookie(final String name, final String value) {
      return response.cookie(name, value);
    }

    @Override
    public Response cookie(final SetCookie cookie) {
      return response.cookie(cookie);
    }

    @Override
    public Response clearCookie(final String name) {
      return response.clearCookie(name);
    }

    @Override
    public Variant header(final String name) {
      return response.header(name);
    }

    @Override
    public Response header(final String name, final byte value) {
      return response.header(name, value);
    }

    @Override
    public Response header(final String name, final char value) {
      return response.header(name, value);
    }

    @Override
    public Response header(final String name, final Date value) {
      return response.header(name, value);
    }

    @Override
    public Response header(final String name, final double value) {
      return response.header(name, value);
    }

    @Override
    public Response header(final String name, final float value) {
      return response.header(name, value);
    }

    @Override
    public Response header(final String name, final int value) {
      return response.header(name, value);
    }

    @Override
    public Response header(final String name, final long value) {
      return response.header(name, value);
    }

    @Override
    public Response header(final String name, final short value) {
      return response.header(name, value);
    }

    @Override
    public Response header(final String name, final String value) {
      return response.header(name, value);
    }

    @Override
    public Charset charset() {
      return response.charset();
    }

    @Override
    public Response charset(final Charset charset) {
      return response.charset(charset);
    }

    @Override
    public Response length(final int length) {
      return response.length(length);
    }

    @Override
    public <T> T local(final String name) {
      return response.local(name);
    }

    @Override
    public Response local(final String name, final Object value) {
      return response.local(name, value);
    }

    @Override
    public Map<String, Object> locals() {
      return response.locals();
    }

    @Override
    public Optional<MediaType> type() {
      return response.type();
    }

    @Override
    public Response type(final MediaType type) {
      return response.type(type);
    }

    @Override
    public Response type(final String type) {
      return response.type(type);
    }

    @Override
    public void send(final Object body) throws Exception {
      response.send(body);
    }

    @Override
    public void send(final Object body, final BodyConverter converter) throws Exception {
      response.send(body, converter);
    }

    @Override
    public Formatter format() {
      return response.format();
    }

    @Override
    public void redirect(final String location) throws Exception {
      response.redirect(location);
    }

    @Override
    public void redirect(final HttpStatus status, final String location) throws Exception {
      response.redirect(status, location);
    }

    @Override
    public HttpStatus status() {
      return response.status();
    }

    @Override
    public Response status(final HttpStatus status) {
      return response.status(status);
    }

    @Override
    public Response status(final int status) {
      return response.status(status);
    }

    @Override
    public boolean committed() {
      return response.committed();
    }

    @Override
    public String toString() {
      return response.toString();
    }

    public Response delegate() {
      return response;
    }
  }

  /**
   * Handling content negotiation for inline-routes. For example:
   *
   * <pre>
   *  {{
   *      get("/", (req, resp) -> {
   *        Object model = ...;
   *        resp.when("text/html", () -> Viewable.of("view", model))
   *            .when("application/json", () -> model)
   *            .send();
   *      });
   *  }}
   * </pre>
   *
   * The example above will render a view when accept header is "text/html" or just send a text
   * version of model when the accept header is "application/json".
   *
   * @author edgar
   * @since 0.1.0
   */
  interface Formatter {

    /**
     * Add a new when clause for a custom media-type.
     *
     * @param mediaType A media type to test for.
     * @param supplier An object provider.
     * @return The current {@link Formatter}.
     */
    @Nonnull
    default Formatter when(final String mediaType, final ExSupplier<Object> supplier) {
      return when(MediaType.valueOf(mediaType), supplier);
    }

    /**
     * Add a new when clause for a custom media-type.
     *
     * @param mediaType A media type to test for.
     * @param provider An object provider.
     * @return A {@link Formatter}.
     */
    @Nonnull
    Formatter when(MediaType mediaType, ExSupplier<Object> supplier);

    /**
     * Write the response and send it.
     *
     * @throws Exception If something fails.
     */
    void send() throws Exception;
  }

  void download(String filename, InputStream stream) throws Exception;

  void download(String filename, Reader reader) throws Exception;

  default void download(final String filename) throws Exception {
    download(filename, getClass().getResourceAsStream(filename));
  }

  default void download(final File file) throws Exception {
    download(file.getName(), new FileInputStream(file));
  }

  default Response cookie(final String name, final String value) {
    return cookie(new SetCookie(name, value));
  }

  Response cookie(SetCookie cookie);

  Response clearCookie(String name);

  /**
   * Get a header with the given name.
   *
   * @param name A name.
   * @return A HTTP header.
   */
  @Nonnull
  Variant header(@Nonnull String name);

  Response header(@Nonnull String name, char value);

  Response header(@Nonnull String name, byte value);

  Response header(@Nonnull String name, short value);

  Response header(@Nonnull String name, int value);

  Response header(@Nonnull String name, long value);

  Response header(@Nonnull String name, float value);

  Response header(@Nonnull String name, double value);

  Response header(@Nonnull String name, String value);

  Response header(@Nonnull String name, Date value);

  /**
   * @return Charset for text responses.
   */
  @Nonnull
  Charset charset();

  /**
   * Set the {@link Charset} to use.
   *
   * @param charset A charset.
   * @return This response.
   */
  @Nonnull
  Response charset(@Nonnull Charset charset);

  Response length(int length);

  /**
   * @return Get the response type.
   */
  Optional<MediaType> type();

  /**
   * Set the response media type.
   *
   * @param type A media type.
   * @return This response.
   */
  @Nonnull
  Response type(@Nonnull MediaType type);

  default Response type(@Nonnull final String type) {
    return type(MediaType.valueOf(type));
  }

  /**
   * Responsible of writing the given body into the HTTP response. The {@link BodyConverter} that
   * best matches the <code>Accept</code> header will be selected for writing the response.
   *
   * @param body The HTTP body.
   * @throws Exception If the response write fails.
   * @see BodyConverter
   */
  void send(@Nonnull Object body) throws Exception;

  /**
   * Responsible of writing the given body into the HTTP response.
   *
   * @param body The HTTP body.
   * @param converter The convert to use.
   * @throws Exception If the response write fails.
   * @see BodyConverter
   */
  void send(@Nonnull Object body, @Nonnull BodyConverter converter) throws Exception;

  @Nonnull Formatter format();

  default void redirect(final String location) throws Exception {
    redirect(HttpStatus.FOUND, location);
  }

  void redirect(HttpStatus status, String location) throws Exception;

  /**
   * @return A HTTP status.
   */
  @Nonnull HttpStatus status();

  /**
   * Set the HTTP status.
   *
   * @param status A HTTP status.
   * @return This response.
   */
  @Nonnull Response status(@Nonnull HttpStatus status);

  @Nonnull default Response status(final int status) {
    return status(HttpStatus.valueOf(status));
  }

  boolean committed();

  Map<String, Object> locals();

  <T> T local(String name);

  Response local(String name, Object value);

}
