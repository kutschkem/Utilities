package kutschke.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is intended to faciliate Rewriting of URLs, that means when a lot
 * of Parameters have to wander over multiple pages, it should take away a lot
 * of the work that comes from testing all cases of Parameter-Combinations that
 * could appear.<br/>
 * Call the toString() method to build an URL from the internal Parameter cache
 * and the base URL.<br/>
 * This class is not thread-safe.
 * 
 * @author Michael
 * 
 */
public class URLRewriter implements Cloneable {

	/**
	 * the internal parameter cache used by buildURLfromCache
	 */
	protected Map<String, String[]> params = new HashMap<String, String[]>();

	private String baseURL = "";
	
	private String fileName = "";

	private String encoding = "UTF-8";

	private HttpServletResponse sessionEncoder = null;

	public URLRewriter() {

	}

	public URLRewriter(HttpServletRequest request) {
		this.initialize(request);
	}
	
	/**
	 * initializes base URL, file name, parameters and session encoder
	 * @param request
	 * @param response
	 */
	public URLRewriter(HttpServletRequest request, HttpServletResponse response){
		this.initialize(request).setSessionEncoder(response);
	}

	public Map<String, String[]> getParams() {
		return params;
	}

	/**
	 * actually clones the given parameter map to avoid side-effects
	 * 
	 * @param params
	 * @return
	 */
	public URLRewriter setParams(Map<String, String[]> params) {
		this.params = new HashMap<String, String[]>(params);
		return this;
	}

	/**
	 * set a baseURL that toString() can use.
	 * 
	 * @param baseURL
	 * @return this
	 */
	public URLRewriter setBaseURL(String baseURL) {
		this.baseURL = baseURL;
		return this;
	}

	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * sets the encoding that is to be used in the URLs. <br/>
	 * Note that the default is "UTF-8", and that all methods who use this
	 * encoding will switch back to the default if the character encoding is not
	 * supported.
	 * 
	 * @param encoding
	 *            the name of the character set that will be used for encoding
	 * @return this
	 */
	public URLRewriter setEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	/**
	 * 
	 * @return the name of the character set used for encoding URLs and their
	 *         parameters
	 * @see #setEncoding(String)
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * The session encoder will encode the session id into the url, if set
	 * 
	 * @param sessionEncoder
	 *            the sessionEncoder to set
	 */
	public URLRewriter setSessionEncoder(HttpServletResponse sessionEncoder) {
		this.sessionEncoder = sessionEncoder;
		return this;
	}

	/**
	 * encodes the url to reflect the session id. Will only work if a session
	 * encoder is set
	 * 
	 * @param url
	 *            the original url
	 * @return the encoded url (with session id)
	 * @see #setSessionEncoder(HttpServletResponse)
	 */
	protected String sessionEncode(String url) {
		if (sessionEncoder == null)
			return url;
		else
			return sessionEncoder.encodeURL(url);
	}

	/**
	 * Initializes the Parameter Map with the Parameters from the given Request.<br/>
	 * Also initializes filename and baseURL. Supports reverse proxies
	 * 
	 * @param request
	 * @return this
	 */
	public URLRewriter initialize(HttpServletRequest request) {

		params = new HashMap<String, String[]>();
		for (Object str : request.getParameterMap().keySet()) {
			params.put((String) str, request.getParameterValues((String) str));
		}
		
		/* compute BaseURL for URLManager */
		if(request.getHeader("X-Forwarded-Server") == null){
		Matcher match = Pattern.compile(".*/").matcher(request.getRequestURL());
		match.find();
		String baseURL = match.group();
		setBaseURL(baseURL);
		}
		else{
			Matcher match = Pattern.compile("[^,]*").matcher(request.getHeader("X-Forwarded-Server"));
			match.find();
			setBaseURL("http://"+match.group()+"/");
		}

		/* give requested file to url manager */
		String fileName = request.getRequestURI().replaceAll("[^/]+/","").replace("/","");
		setFileName(fileName);
		
		return this;
	}

	/**
	 * set/change a parameter in the internal parameter cache
	 * 
	 * @param key
	 * @param value
	 * @return this
	 */
	public URLRewriter setParameter(String key, String... value) {
		params.put(key, value);
		return this;
	}

	/**
	 * Adds a parameter value to the Parameter-Array <i>key</i> <br/>
	 * The difference to setParameter is that this will make an Array parameter,
	 * not a scalar value. Also this will NOT overwrite the existing Parameter
	 * value, but ADD to it.
	 * 
	 * @param key
	 * @param value
	 * @return this
	 */
	public URLRewriter addParameter(String key, String... value) {
		List<String> lst;
		if (params.get(key) != null)
			lst = new ArrayList<String>(Arrays.asList(params.get(key)));
		else
			lst = new ArrayList<String>();
		lst.addAll(Arrays.asList(value));
		params.put(key, lst.toArray(new String[0]));
		return this;
	}

	/**
	 * remove a parameter in the internal parameter cache
	 * 
	 * @param key
	 * @return this
	 */
	public URLRewriter removeParameter(String key) {
		params.remove(key);
		return this;
	}
	
	public URLRewriter empty(){
		return setParams(new HashMap<String,String[]>());
	}

	/**
	 * intersect the cached parameters with the given parameter names, leaving
	 * only those behind who also are in the <i>parameters</i> set.
	 * 
	 * @param parameters
	 * @return this
	 */
	public URLRewriter intersectParameters(String... parameters) {
		if (parameters == null)
			return this;
		return intersectParameters(new HashSet<String>(Arrays
				.asList(parameters)));

	}

	/**
	 * intersect the cached parameters with the given parameter names, leaving
	 * only those behind who also are in the <i>parameters</i> set.
	 * 
	 * @param parameters
	 * @return this
	 */
	public URLRewriter intersectParameters(Set<String> parameters) {

		Iterator<String> it = params.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (!parameters.contains(key))
				it.remove();
		}
		return this;

	}

	/**
	 * build a URL with the GET-parameter from the internal cache.<br/>
	 * Note that this function MAY get a baseURL with a query string, but
	 * setting parameters manually and then using this function might in many
	 * cases be more efficient
	 * 
	 * @param baseURL
	 * @return
	 */
	private String buildURLfromInternalCache(String baseURL) {

		if (Pattern.compile("\\?").matcher(baseURL).find())
			return rewriteURLArray(baseURL, params);
		return createGETurlArray(baseURL, params);
	}

	@Override
	public URLRewriter clone() {
		try {
			URLRewriter newInstance = (URLRewriter) super.clone();
			return newInstance.setParams(new HashMap<String, String[]>(params));

		} catch (CloneNotSupportedException e) {
			// this should be unreachable
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Builds a URL composed of the internal base URL and the cached Parameter
	 * Map
	 */
	@Override
	public String toString() {
		return sessionEncode(
				buildURLfromInternalCache(this.getBaseURL() + this.getFileName()));
	}

	/**
	 * Use this if your baseURL does not have Parameters by itself. This
	 * function will create a url containing all the given GET-Parameters.
	 * 
	 * @param baseURL
	 *            a URL with no query string (and no '?' !!)
	 * @param Parameters
	 *            a map of parameter -> Value
	 * @return a well formed URL containing the given parameters
	 */
	public final String createGETurl(String baseURL,
			Map<String, String> Parameters) {
		if (Pattern.compile("\\?").matcher(baseURL).find())
			throw new IllegalArgumentException(
					"baseURL may in no case have a Query String!");
		StringBuffer buf = new StringBuffer();
		buf.append(baseURL);
		boolean firstSep = true;
		try {
			for (String key : Parameters.keySet()) {
				if (firstSep == true) {
					buf.append('?');
					firstSep = false;
				} else
					buf.append("&amp;");
				buf.append(URLEncoder.encode(key, getEncoding()));

				buf.append('=');
				buf.append(URLEncoder
						.encode(Parameters.get(key), getEncoding()));

			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.err
					.println("\n\n----Encoding not supported, switch back to UTF-8----");
			setEncoding("UTF-8");
			createGETurl(baseURL, Parameters);
		}
		return buf.toString();
	}

	/**
	 * 
	 * @see createGETurl
	 * @param baseURL
	 * @param params
	 * @return
	 */
	public final String createGETurlArray(String baseURL,
			Map<String, String[]> params) {
		if (Pattern.compile("\\?").matcher(baseURL).find())
			throw new IllegalArgumentException(
					"baseURL may in no case have a Query String!");
		StringBuffer buf = new StringBuffer();
		buf.append(baseURL);
		boolean firstSep = true;
		try {
			for (String key : params.keySet()) {

				for (String value : params.get(key)) {
					if (firstSep == true) {
						buf.append('?');
						firstSep = false;
					} else
						buf.append("&amp;");

					buf.append(URLEncoder.encode(key, getEncoding()));

					buf.append('=');
					buf.append(URLEncoder.encode(value, getEncoding()));
				}

			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.err
					.println("\n\n----Encoding not supported, switch back to UTF-8----");
			setEncoding("UTF-8");
			createGETurlArray(baseURL, params);
		}
		return buf.toString();
	}

	// the methods that come after here are merely helper methods,
	// that wouldn't really need an instance

	/**
	 * changes/adds a Parameter in a URL. Note that this does encoding, so
	 * putting any characters into newValue(except \0 of course) should be safe
	 * 
	 * @param originalURL
	 *            the original url, which will be rewritten
	 * @param parameter
	 *            the name of the parameter to be changed/added
	 * @param newValue
	 *            the new Value to be assigned to the parameter
	 * @return the url with it's GET-Parameter <i>parameter</i> changed to
	 *         <i>newValue</i>
	 */
	public final String rewriteURL(String originalURL, String parameter,
			String newValue) {
		String regex = parameter + "=[^&]*";
		String replacement = null;
		try {
			replacement = URLEncoder.encode(parameter, getEncoding()) + "="
					+ URLEncoder.encode(newValue, getEncoding());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.err
					.println("\n\n----Encoding not supported, switch back to UTF-8----");
			setEncoding("UTF-8");
			return rewriteURL(originalURL, parameter, newValue);
		}
		Matcher matcher = Pattern.compile(regex).matcher(originalURL);
		StringBuffer buf = new StringBuffer();
		if (matcher.find()) {
			matcher.appendReplacement(buf, replacement);
		}
		while (matcher.find()) {
			matcher.appendReplacement(buf, "");
		}
		matcher.appendTail(buf);
		matcher.reset();
		if (!matcher.find()) { // Parameter didn't exist? ADD IT!
			Matcher matcher2 = Pattern.compile("\\?").matcher(originalURL);
			if (!matcher2.find())
				buf.append('?');
			else
				buf.append("&amp;");
			try {
				buf.append(URLEncoder.encode(parameter, getEncoding()) + "="
						+ URLEncoder.encode(newValue, getEncoding()));
			} catch (UnsupportedEncodingException e) {
				// this should not be possible to reach
				e.printStackTrace();
			}
		}
		return buf.toString();
	}

	/**
	 * changes multiple Parameters in the original URL
	 * 
	 * @deprecated rather use methods that use Maps than this method
	 * @param originalURL
	 *            the original url, including any Query-String
	 * @param Parameter
	 *            the names of the parameters to be changed
	 * @param newValues
	 *            the new Values, in the same order as in the <i>Parameter</i>
	 *            array
	 * @return
	 * @see #rewriteURL(String, Map)
	 * @see #rewriteURLArray(String, Map)
	 */
	@Deprecated
	public final String rewriteURL(String originalURL, String[] Parameter,
			String[] newValues) {
		if (Parameter.length != newValues.length)
			throw new IllegalArgumentException("Array sizes do not match!");
		String result = originalURL;
		for (int i = 0; i < Parameter.length; i++)
			result = rewriteURL(result, Parameter[i], newValues[i]);
		return result;
	}

	/**
	 * changes multiple Parameters in the original URL. Use createGETurl instead
	 * if your baseURL does not contain any parameters
	 * 
	 * @param originalURL
	 * @param parameters
	 *            The map mapping the Parameters to new values
	 * @return the original URL with it's GET-Parameters adjusted
	 */
	public final String rewriteURL(String originalURL,
			Map<String, String> parameters) {
		String result = originalURL;
		for (String param : parameters.keySet()) {
			rewriteURL(result, param, parameters.get(param));
		}
		return result;
	}

	/**
	 * copies every Parameter of the old Request onto the new (original) URL
	 * 
	 * @param originalURL
	 * @param oldRequest
	 *            the request Object of the current location
	 * @return
	 */
	public final String rewriteURL(String originalURL,
			HttpServletRequest oldRequest) {
		Map<String, String> ParameterMap = new HashMap<String, String>();
		for (Object str : oldRequest.getParameterMap().keySet()) {
			ParameterMap.put((String) str, oldRequest
					.getParameter((String) str));
		}
		return rewriteURL(originalURL, ParameterMap);
	}

	/**
	 * copies every Parameter of the old Request that also appears in the
	 * parameters Set onto the new URL
	 * 
	 * @param originalURL
	 * @param oldRequest
	 * @param parameters
	 *            the Parameters to copy
	 * @return
	 * @deprecated use the instance methods instead (initializeMap and
	 *             intersectParameters
	 * @see #initializeMap
	 * @see #intersectParameters(Set)
	 * @see #intersectParameters(String[])
	 * 
	 */
	@Deprecated
	public final String rewriteURL(String originalURL,
			HttpServletRequest oldRequest, Set<String> parameters) {
		Map<String, String> ParameterMap = new HashMap<String, String>();
		for (Object str : oldRequest.getParameterMap().keySet()) {
			if (parameters.contains(str))
				ParameterMap.put((String) str, oldRequest
						.getParameter((String) str));
		}
		return rewriteURL(originalURL, ParameterMap);
	}

	/**
	 * This works like rewriteURL but instead sets an Array-value
	 * 
	 * @param result
	 * @param param
	 * @param newValues
	 * @return
	 */
	public final String rewriteURLArray(String result, String param,
			String[] newValues) {
		StringBuffer newValue = new StringBuffer();
		for (int i = 0; i < newValues.length - 1; i++) {
			try {
				newValue.append(URLEncoder.encode(newValues[i], getEncoding()));

				newValue.append("&amp;");
				newValue.append(URLEncoder.encode(param, getEncoding()));
				newValue.append('=');
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.err
						.println("\n\n----Encoding not supported, switch back to UTF-8----");
				setEncoding("UTF-8");
				rewriteURLArray(result, param, newValues);
			}
		}
		newValue.append(newValues[newValues.length - 1]);

		String regex = param + "=[^&]*";
		String replacement = null;
		try {
			replacement = URLEncoder.encode(param, getEncoding()) + "="
					+ newValue;
		} catch (UnsupportedEncodingException e) {
			// this should not be reached
			e.printStackTrace();
		}
		Matcher matcher = Pattern.compile(regex).matcher(result);
		StringBuffer buf = new StringBuffer();
		if (matcher.find()) {
			matcher.appendReplacement(buf, replacement);
		}
		while (matcher.find()) {
			matcher.appendReplacement(buf, "");
		}
		matcher.appendTail(buf);
		matcher.reset();
		if (!matcher.find()) { // Parameter didn't exist? ADD IT!
			Matcher matcher2 = Pattern.compile("\\?").matcher(result);
			if (!matcher2.find())
				buf.append('?');
			else
				buf.append("&amp;");
			try {
				buf.append(URLEncoder.encode(param, getEncoding()) + "="
						+ newValue);
			} catch (UnsupportedEncodingException e) {
				// this should not be possible to reach
				e.printStackTrace();
			}
		}
		return buf.toString();

	}

	/**
	 * works like rewriteURL(String,Map<String,String>), but instead creates
	 * Array-valued parameters
	 * 
	 * @param originalURL
	 * @param parameters
	 * @return
	 */
	public final String rewriteURLArray(String originalURL,
			Map<String, String[]> parameters) {
		String result = originalURL;
		for (String param : parameters.keySet()) {
			if (parameters.get(param).length > 1)
				rewriteURLArray(result, param, parameters.get(param));
			else
				rewriteURL(result, param, parameters.get(param)[0]);
		}
		return result;
	}
}
