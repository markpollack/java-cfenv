/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.cfenv.jdbc;

import io.pivotal.cfenv.core.UriInfo;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Pollack
 */
public class OracleJdbcTests extends AbstractJdbcTests {


	private static final String INSTANCE_NAME = "database";
	private static final String SERVICE_NAME = "oracle-ups";
	public static final String ORACLE_SCHEME = "oracle";

	@Test
	public void oracleServiceCreation() {
		mockVcapServices(getServicesPayload(
				getUserProvidedServicePayload(SERVICE_NAME, hostname, port, username, password, INSTANCE_NAME, ORACLE_SCHEME + ":")
		));

		CfJdbcEnv cfJdbcEnv = new CfJdbcEnv();
		CfJdbcService cfJdbcService = cfJdbcEnv.findJdbcServiceByName(SERVICE_NAME);
		assertThat(cfJdbcService.getUsername()).isEqualTo(username);
		assertThat(cfJdbcService.getPassword()).isEqualTo(password);
		assertThat(cfJdbcService.getDriverClassName())
				.isEqualTo("oracle.jdbc.OracleDriver");

		String jdbcUrl = cfJdbcEnv.findJdbcServiceByName(SERVICE_NAME).getUrl();
		String expectedJdbcUrl = getExpectedJdbcUrl(ORACLE_SCHEME, INSTANCE_NAME);
		assertThat(expectedJdbcUrl).isEqualTo(jdbcUrl);
		UriInfo uriInfo = cfJdbcService.getCredentials().getUriInfo();

		assertThat(uriInfo.getScheme()).isEqualTo(ORACLE_SCHEME);
	}

	protected String getExpectedJdbcUrl(String scheme, String name) {
		return String.format("%s%s:thin:%s/%s@%s:%d/%s", AbstractJdbcUrlCreator.JDBC_PREFIX, scheme, username, password, hostname, port, name);
	}

	private String getExpectedJdbcUrl(String hostname, int port, String name, String user, String password) {
		return String.format("%s%s:thin:%s/%s@%s:%d/%s", AbstractJdbcUrlCreator.JDBC_PREFIX, "oracle", UriInfo.urlEncode(user), UriInfo.urlEncode(password), hostname, port, name);
	}
}
