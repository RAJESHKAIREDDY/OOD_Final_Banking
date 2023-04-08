package notifications;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class MailgunEmailService {
	
	private static final String DOMAIN_NAME = "sandbox3e5d97fb9b6f43888ff79fd6aae2efcf.mailgun.org";
	private static final String API_KEY = "ca30881aa5fe03d7e5737e2ec9b11fd2-0f472795-af790700";
	
	public static JsonNode sendSimpleMessage() throws UnirestException {
		HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN_NAME + "/messages")
			.basicAuth("api", API_KEY)
			.queryString("from", "Excited User <peri@sandbox3e5d97fb9b6f43888ff79fd6aae2efcf.mailgun.org>")
			.queryString("to", "sriharshaperi@gmail.com")
			.queryString("subject", "hello")
			.queryString("text", "testing")
			.asJson();
		return request.getBody();
	}
}
