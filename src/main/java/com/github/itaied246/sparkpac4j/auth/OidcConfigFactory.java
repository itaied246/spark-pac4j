package com.github.itaied246.sparkpac4j.auth;

import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.oidc.client.OidcClient;
import org.pac4j.oidc.config.OidcConfiguration;
import org.pac4j.oidc.profile.OidcProfile;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;

public class OidcConfigFactory implements ConfigFactory {

	private String clientId;
	private String secret;
	private String discoveryUri;
	private String callbackUrl;

	public OidcConfigFactory(String clientId, String secret, String discoveryUri,
			String callbackUrl) {
		this.clientId = clientId;
		this.secret = secret;
		this.discoveryUri = discoveryUri;
		this.callbackUrl = callbackUrl;
	}

	@Override
	public Config build() {
		OidcClient<OidcProfile> oidcClient = createOidcClient();

		Clients clients = new Clients(callbackUrl, oidcClient);

		Config config = createConfig(clients);

		return config;
	}

	private OidcClient<OidcProfile> createOidcClient() {
		OidcConfiguration config = new OidcConfiguration();
		config.setClientId(clientId);
		config.setSecret(secret);
		config.setDiscoveryURI(discoveryUri);
		config.setUseNonce(true);

		return new OidcClient<OidcProfile>(config);
	}

	private Config createConfig(Clients clients) {
		Config config = new Config(clients);
		config.setHttpActionAdapter(new DefaultHttpActionAdapter());
		return config;
	}

}
