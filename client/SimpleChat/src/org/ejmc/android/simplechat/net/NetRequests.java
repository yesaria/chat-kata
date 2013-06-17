package org.ejmc.android.simplechat.net;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.ejmc.android.simplechat.Magic;
import org.ejmc.android.simplechat.model.ChatList;
import org.ejmc.android.simplechat.model.Message;

/**
 * Proxy to remote API.
 * 
 * @author startic
 * 
 */
public class NetRequests {

	private NetConfig netConfig;

	public NetRequests(NetConfig netConfig) {
		super();
		this.netConfig = netConfig;
	}

	/**
	 * Gets chat messages from sequence number.
	 * 
	 * @param seq
	 * @param handler
	 */
	public void chatGET(int seq, NetResponseHandler<ChatList> handler) {

		HttpParams params = new BasicHttpParams();
		if (seq > 0) {
			params.setIntParameter("seq", seq);
		}
		HttpGet get = new HttpGet("/api/chat");

		new HttpJsonAsyncTask<ChatList>(netConfig, handler, ChatList.class)
				.execute(get);
	}

	/**
	 * POST message to chat.
	 * 
	 * @param message
	 * @param handler
	 */
	public void chatPOST(Message message, NetResponseHandler<Message> handler) {

		HttpPost post = new HttpPost("/api/chat");
		HttpEntity entity = createJSONEntity(message);
		post.setEntity(entity);

		new HttpJsonAsyncTask<Message>(netConfig, handler, Message.class)
				.execute(post);
	}

	/**
	 * Creates htp entity from object.
	 * 
	 * @param o
	 * @return
	 */
	private StringEntity createJSONEntity(Object o) {
		try {
			String json = netConfig.getGson().toJson(o);

			StringEntity entity = new StringEntity(json, Magic.DEFAULT_ENCODING);

			entity.setContentType(Magic.JSON_CONTENT_TYPE);

			return entity;
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException("UTF-8 not supported");

		}
	}

}