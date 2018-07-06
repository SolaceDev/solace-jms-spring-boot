/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.solace.spring.boot.autoconfigure;

<<<<<<< HEAD
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
=======
import com.solace.services.core.model.SolaceServiceCredentials;
import com.solace.spring.cloud.core.SolaceMessagingInfo;
import com.solacesystems.jms.SolConnectionFactory;
import com.solacesystems.jms.SpringSolJmsConnectionFactoryCloudFactory;
import org.json.JSONObject;
import org.junit.Test;
>>>>>>> upstream-master
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;

<<<<<<< HEAD
import com.solace.spring.cloud.core.SolaceMessagingInfo;
import com.solacesystems.jms.SolConnectionFactory;
import com.solacesystems.jms.SpringSolJmsConnectionFactoryCloudFactory;

@RunWith(Parameterized.class)
public class SolaceJmsAutoCloudConfigurationTest {

	protected static final Log logger = LogFactory.getLog(SolaceJmsAutoCloudConfigurationTest.class);

	@Parameterized.Parameters(name = "{index}: {0}")
	public static Object[] data() {
		return new Object[] {
				"solace-messaging",
				"solace-pubsub"
		};
	}

	private String serviceName;

	@Rule
	public final EnvironmentVariables environmentVariables = new EnvironmentVariables();
=======
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;
>>>>>>> upstream-master

@RunWith(Parameterized.class)
public class SolaceJmsAutoCloudConfigurationTest<T> extends SolaceJmsAutoConfigurationTestBase {

	// Just enough to satisfy the Cloud Condition we need
	private static String CF_CLOUD_APP_ENV = "VCAP_APPLICATION={}";

	// Some other Service
	private static String CF_VCAP_SERVICES_OTHER = "VCAP_SERVICES={ otherService: [ { id: '1' } , { id: '2' } ]}";

    @Parameter(0) public String beanClassName;
    @Parameter(1) public Class<T> beanClass;
	CloudCondition cloudCondition = new CloudCondition();

	public SolaceJmsAutoCloudConfigurationTest(String serviceName) {
		this.serviceName = serviceName;
	}

	@Before
	public void info() {
		logger.info(String.format("Testing with service %s", serviceName));
	}

	@After
	public void tearDown() {
		if (this.context != null) {
			this.context.close();
		}
	}

	public SolaceJmsAutoCloudConfigurationTest() {
		super(SolaceJmsAutoCloudConfiguration.class);
	}

    @Parameters(name = "{0}")
    public static Collection<Object[]> parameterData() {
        Set<ResolvableType> classes = new HashSet<>();
        classes.add(ResolvableType.forClass(SpringSolJmsConnectionFactoryCloudFactory.class));
        classes.add(ResolvableType.forClass(SolConnectionFactory.class));
        classes.add(ResolvableType.forClass(SolaceServiceCredentials.class));
        classes.add(ResolvableType.forClass(SolaceMessagingInfo.class));
        return getTestParameters(classes);
    }

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void noBeanNotCloud() throws NoSuchBeanDefinitionException {
		try {
			load("");
			this.context.getBean(beanClass);
		} catch (NoSuchBeanDefinitionException e) {
			assertTrue(e.getBeanType().isAssignableFrom(beanClass));
			throw e;
		}
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void noBeanIsCloudNoService() throws NoSuchBeanDefinitionException {
		load(CF_CLOUD_APP_ENV);

		Environment env = context.getEnvironment();
		String VCAP_APPLICATION = env.getProperty("VCAP_APPLICATION");
		assertNotNull(VCAP_APPLICATION);
		assertEquals("{}", VCAP_APPLICATION);

		String VCAP_SERVICES = env.getProperty("VCAP_SERVICES");
		assertNull(VCAP_SERVICES);

		try {
<<<<<<< HEAD
			this.context.getBean(SolConnectionFactory.class);
		} catch (NoSuchBeanDefinitionException e) {
			assertTrue(e.getBeanType().isAssignableFrom(SolConnectionFactory.class));
			throw e;
		}
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void isCloudNoServiceNoSpringJCSMPFactoryCloudFactory() throws NoSuchBeanDefinitionException {
		load(EmptyCloudConfiguration.class, CF_CLOUD_APP_ENV);

		Environment env = context.getEnvironment();
		String VCAP_APPLICATION = env.getProperty("VCAP_APPLICATION");
		assertNotNull(VCAP_APPLICATION);
		assertEquals("{}", VCAP_APPLICATION);

		String VCAP_SERVICES = env.getProperty("VCAP_SERVICES");
		assertNull(VCAP_SERVICES);

		try {
			this.context.getBean(SpringSolJmsConnectionFactoryCloudFactory.class);
		} catch (NoSuchBeanDefinitionException e) {
			assertTrue(e.getBeanType().isAssignableFrom(SpringSolJmsConnectionFactoryCloudFactory.class));
			throw e;
		}
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void isCloudWrongServiceNoSolConnectionFactory() throws NoSuchBeanDefinitionException {
		load(EmptyCloudConfiguration.class, CF_CLOUD_APP_ENV, CF_VCAP_SERVICES_OTHER);

		Environment env = context.getEnvironment();
		String VCAP_APPLICATION = env.getProperty("VCAP_APPLICATION");
		assertNotNull(VCAP_APPLICATION);
		assertEquals("{}", VCAP_APPLICATION);

		String VCAP_SERVICES = env.getProperty("VCAP_SERVICES");
		assertNotNull(VCAP_SERVICES);
		assertFalse(VCAP_SERVICES.contains(serviceName));

		try {
			this.context.getBean(SolConnectionFactory.class);
=======
			this.context.getBean(beanClass);
>>>>>>> upstream-master
		} catch (NoSuchBeanDefinitionException e) {
			assertTrue(e.getBeanType().isAssignableFrom(beanClass));
			throw e;
		}
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void noBeanIsCloudWrongService() throws NoSuchBeanDefinitionException {
		load(CF_CLOUD_APP_ENV, CF_VCAP_SERVICES_OTHER);

		Environment env = context.getEnvironment();
		String VCAP_APPLICATION = env.getProperty("VCAP_APPLICATION");
		assertNotNull(VCAP_APPLICATION);
		assertEquals("{}", VCAP_APPLICATION);

		String VCAP_SERVICES = env.getProperty("VCAP_SERVICES");
		assertNotNull(VCAP_SERVICES);
		assertFalse(VCAP_SERVICES.contains(serviceName));

		try {
			this.context.getBean(beanClass);
		} catch (NoSuchBeanDefinitionException e) {
			assertTrue(e.getBeanType().isAssignableFrom(beanClass));
			throw e;
		}
	}

	private void makeCloudEnv() {
		// To force the detection of spring cloud connector which uses
		// EnvironmentAccessor
		environmentVariables.set("VCAP_APPLICATION", "{}");
		assertEquals("{}", System.getenv("VCAP_APPLICATION"));
	}

<<<<<<< HEAD
	private String addOneSolaceService(String serviceLabel) {
		// Make a service visible to the Cloud Connector, will end up using the
		// SolaceMessagingInfoCreator

		Map<String, Object> services = createOneService(serviceLabel);
		JSONObject jsonMapObject = new JSONObject(services);
		String JSONString = jsonMapObject.toString();
		environmentVariables.set("VCAP_SERVICES", String.format("{ \"%s\": [" + JSONString + "] }", serviceLabel));
		return JSONString;
	}

=======
>>>>>>> upstream-master
	@Test
	public void hasBeanIsCloudHasService() throws NoSuchBeanDefinitionException {

		makeCloudEnv();

<<<<<<< HEAD
		String JSONString = addOneSolaceService(serviceName);
		String CF_VCAP_SERVICES = String.format("VCAP_SERVICES={ \"%s\": [%s] }", serviceName, JSONString);
=======
		String JSONString = addOneSolaceService("VCAP_SERVICES");
		String CF_VCAP_SERVICES = "VCAP_SERVICES={ \"solace-messaging\": [" + JSONString + "] }";
>>>>>>> upstream-master

		load(CF_CLOUD_APP_ENV, CF_VCAP_SERVICES);

		Environment env = context.getEnvironment();

		String VCAP_APPLICATION = env.getProperty("VCAP_APPLICATION");
		assertNotNull(VCAP_APPLICATION);
		assertEquals("{}", VCAP_APPLICATION);

		String VCAP_SERVICES = env.getProperty("VCAP_SERVICES");
		assertNotNull(VCAP_SERVICES);
		assertTrue(VCAP_SERVICES.contains(serviceName));

		validateBackwardsCompatibility();

<<<<<<< HEAD
		makeCloudEnv();

		String JSONString = addOneSolaceService(serviceName);
		String CF_VCAP_SERVICES = String.format("VCAP_SERVICES={ \"%s\": [" + JSONString + "] }", serviceName);

		load(EmptyCloudConfiguration.class, CF_CLOUD_APP_ENV, CF_VCAP_SERVICES);

		Environment env = context.getEnvironment();

		String VCAP_APPLICATION = env.getProperty("VCAP_APPLICATION");
		assertNotNull(VCAP_APPLICATION);
		assertEquals("{}", VCAP_APPLICATION);

		String VCAP_SERVICES = env.getProperty("VCAP_SERVICES");
		assertNotNull(VCAP_SERVICES);
		assertTrue(VCAP_SERVICES.contains(serviceName));

		SpringSolJmsConnectionFactoryCloudFactory springSolConnectionFactoryCloudFactory = this.context
				.getBean(SpringSolJmsConnectionFactoryCloudFactory.class);
		assertNotNull(springSolConnectionFactoryCloudFactory);

		assertNotNull(springSolConnectionFactoryCloudFactory.getSolConnectionFactory());

		List<SolaceMessagingInfo> availableServices = springSolConnectionFactoryCloudFactory.getSolaceMessagingInfos();

		assertNotNull(availableServices);

		assertEquals(1, availableServices.size());
=======
		T bean = this.context.getBean(beanClass);
		assertNotNull(bean);
>>>>>>> upstream-master

		if (beanClass.equals(SpringSolJmsConnectionFactoryCloudFactory.class)) {
            SpringSolJmsConnectionFactoryCloudFactory springSolConnectionFactoryCloudFactory =
                    (SpringSolJmsConnectionFactoryCloudFactory) bean;
            assertNotNull(springSolConnectionFactoryCloudFactory.getSolConnectionFactory());
            List<SolaceServiceCredentials> availableServices = springSolConnectionFactoryCloudFactory
                    .getSolaceServiceCredentials();
            assertNotNull(availableServices);
            assertEquals(1,availableServices.size());
        }
	}

	@Test
	public void isCloudConfiguredBySolaceMessagingInfoAndDefaultsForOtherProperties() {

		makeCloudEnv();

<<<<<<< HEAD
		String JSONString = addOneSolaceService(serviceName);
		String CF_VCAP_SERVICES = String.format("VCAP_SERVICES={ \"%s\": [" + JSONString + "] }", serviceName);
=======
		String JSONString = addOneSolaceService("VCAP_SERVICES");
		String CF_VCAP_SERVICES = "VCAP_SERVICES={ \"solace-messaging\": [" + JSONString + "] }";
>>>>>>> upstream-master

		load(CF_CLOUD_APP_ENV, CF_VCAP_SERVICES);

		SpringSolJmsConnectionFactoryCloudFactory springSolConnectionFactoryCloudFactory = this.context
				.getBean(SpringSolJmsConnectionFactoryCloudFactory.class);
		assertNotNull(springSolConnectionFactoryCloudFactory);

		SolConnectionFactory solConnectionFactory = this.context.getBean(SolConnectionFactory.class);
		assertNotNull(solConnectionFactory);

		JmsTemplate jmsTemplate = this.context.getBean(JmsTemplate.class);

		assertEquals(jmsTemplate.getConnectionFactory(), solConnectionFactory);
		assertEquals("tcp://192.168.1.50:7000", solConnectionFactory.getHost());
		assertEquals("sample-msg-vpn", solConnectionFactory.getVPN());
		assertEquals("sample-client-username", solConnectionFactory.getUsername());
		assertEquals("sample-client-password", solConnectionFactory.getPassword());
		assertFalse(solConnectionFactory.getDirectTransport());

	}

	@Test
	public void isCloudConfiguredBySolaceMessagingInfoAndOtherProperties() {
		makeCloudEnv();

<<<<<<< HEAD
		String JSONString = addOneSolaceService(serviceName);
		String CF_VCAP_SERVICES = String.format("VCAP_SERVICES={ \"%s\": [" + JSONString + "] }", serviceName);
=======
		String JSONString = addOneSolaceService("VCAP_SERVICES");
		String CF_VCAP_SERVICES = "VCAP_SERVICES={ \"solace-messaging\": [" + JSONString + "] }";
>>>>>>> upstream-master

		load(CF_CLOUD_APP_ENV, CF_VCAP_SERVICES, "solace.jms.host=192.168.1.80:55500",
				"solace.jms.clientUsername=bob", "solace.jms.clientPassword=password", "solace.jms.msgVpn=newVpn");

		SpringSolJmsConnectionFactoryCloudFactory springSolConnectionFactoryCloudFactory = this.context
				.getBean(SpringSolJmsConnectionFactoryCloudFactory.class);
		assertNotNull(springSolConnectionFactoryCloudFactory);

		SolConnectionFactory solConnectionFactory = this.context.getBean(SolConnectionFactory.class);
		assertNotNull(solConnectionFactory);

		JmsTemplate jmsTemplate = this.context.getBean(JmsTemplate.class);

		// Notice that the other properties where ineffective
		assertEquals(jmsTemplate.getConnectionFactory(), solConnectionFactory);
		assertEquals("tcp://192.168.1.50:7000", solConnectionFactory.getHost());
		assertEquals("sample-msg-vpn", solConnectionFactory.getVPN());
		assertEquals("sample-client-username", solConnectionFactory.getUsername());
		assertEquals("sample-client-password", solConnectionFactory.getPassword());
		assertFalse(solConnectionFactory.getDirectTransport());

	}

	@Test
	public void isCloudConfiguredBySolaceMessagingInfoAndOtherPropertiesWhenMissingCredentials() {

		makeCloudEnv();

		Map<String, Object> services = createOneService(serviceName);
		@SuppressWarnings("unchecked")
		Map<String, Object> credentials = (Map<String, Object>) services.get("credentials");
		credentials.remove("clientUsername");
		credentials.remove("clientPassword");

		JSONObject jsonMapObject = new JSONObject(services);
		String JSONString = jsonMapObject.toString();
		environmentVariables.set("VCAP_SERVICES", String.format("{ \"%s\": [" + JSONString + "] }", serviceName));

		String CF_VCAP_SERVICES = String.format("VCAP_SERVICES={ \"%s\": [" + JSONString + "] }", serviceName);

		load(CF_CLOUD_APP_ENV, CF_VCAP_SERVICES, "solace.jms.host=192.168.1.80:55500",
				"solace.jms.clientUsername=bob", "solace.jms.clientPassword=password", "solace.jms.msgVpn=newVpn");

		SpringSolJmsConnectionFactoryCloudFactory springSolConnectionFactoryCloudFactory = this.context
				.getBean(SpringSolJmsConnectionFactoryCloudFactory.class);
		assertNotNull(springSolConnectionFactoryCloudFactory);

		SolConnectionFactory solConnectionFactory = this.context.getBean(SolConnectionFactory.class);
		assertNotNull(solConnectionFactory);

		JmsTemplate jmsTemplate = this.context.getBean(JmsTemplate.class);

		// Notice that the other properties where ineffective, only the username
		// and password where effective in this case.
		assertEquals(jmsTemplate.getConnectionFactory(), solConnectionFactory);
		assertEquals("tcp://192.168.1.50:7000", solConnectionFactory.getHost());
		assertEquals("sample-msg-vpn", solConnectionFactory.getVPN());
		assertEquals("bob", solConnectionFactory.getUsername());
		assertEquals("password", solConnectionFactory.getPassword());
		assertFalse(solConnectionFactory.getDirectTransport());
	}

	private void validateBackwardsCompatibility() {
		//Expects SolaceMessagingInfo bean to be annotated with @Primary

		assertEquals(2, this.context.getBeanNamesForType(SolaceServiceCredentials.class).length);
		assertEquals(2, this.context.getBeanNamesForType(SolaceMessagingInfo.class).length);
		SolaceServiceCredentials ssc = this.context.getBean(SolaceServiceCredentials.class);
		SolaceMessagingInfo smi = this.context.getBean(SolaceMessagingInfo.class);

<<<<<<< HEAD
	private Map<String, Object> createOneService(String serviceLabel) {
		Map<String, Object> exVcapServices = new HashMap<String, Object>();

		Map<String, Object> exCred = new HashMap<String, Object>();

		exCred.put("clientUsername", "sample-client-username");
		exCred.put("clientPassword", "sample-client-password");
		exCred.put("msgVpnName", "sample-msg-vpn");
		exCred.put("smfHosts", Arrays.asList("tcp://192.168.1.50:7000"));
		exCred.put("smfTlsHosts", Arrays.asList("tcps://192.168.1.50:7003", "tcps://192.168.1.51:7003"));
		exCred.put("smfZipHosts", Arrays.asList("tcp://192.168.1.50:7001"));
		exCred.put("webMessagingUris", Arrays.asList("http://192.168.1.50:80"));
		exCred.put("webMessagingTlsUris", Arrays.asList("https://192.168.1.50:80"));
		exCred.put("jmsJndiUris", Arrays.asList("smf://192.168.1.50:7000"));
		exCred.put("jmsJndiTlsUris", Arrays.asList("smfs://192.168.1.50:7003", "smfs://192.168.1.51:7003"));
		exCred.put("mqttUris", Arrays.asList("tcp://192.168.1.50:7020"));
		exCred.put("mqttTlsUris", Arrays.asList("ssl://192.168.1.50:7021", "ssl://192.168.1.51:7021"));
		exCred.put("mqttWsUris", Arrays.asList("ws://192.168.1.50:7022"));
		exCred.put("mqttWssUris", Arrays.asList("wss://192.168.1.50:7023", "wss://192.168.1.51:7023"));
		exCred.put("restUris", Arrays.asList("http://192.168.1.50:7018"));
		exCred.put("restTlsUris", Arrays.asList("https://192.168.1.50:7019"));
		exCred.put("amqpUris", Arrays.asList("amqp://192.168.1.50:7016"));
		exCred.put("amqpTlsUris", Arrays.asList("amqps://192.168.1.50:7017"));
		exCred.put("managementHostnames", Arrays.asList("vmr-Medium-VMR-0"));
		exCred.put("managementUsername", "sample-mgmt-username");
		exCred.put("managementPassword", "sample-mgmt-password");
		exCred.put("activeManagementHostname", "vmr-medium-web");

		exVcapServices.put("credentials", exCred);
		exVcapServices.put("label", serviceLabel);
		exVcapServices.put("name", "test-service-instance-name");
		exVcapServices.put("plan", "vmr-shared");
		exVcapServices.put("provider", "Solace Systems");
		// no need to check for tags in terms of validation. It's more for
		exVcapServices.put("tags",
				Arrays.asList("solace", serviceLabel, "rest", "mqtt", "mq", "queue", "jms", "messaging", "amqp"));

		return exVcapServices;
	}
=======
		//Primary child class always supersedes any parent
		assertTrue(ssc.getClass().isAssignableFrom(SolaceMessagingInfo.class));
		assertTrue(!ssc.getClass().isAssignableFrom(SolaceServiceCredentials.class));
>>>>>>> upstream-master

		assertTrue(smi.getClass().isAssignableFrom(SolaceMessagingInfo.class));
		assertTrue(!smi.getClass().isAssignableFrom(SolaceServiceCredentials.class));
	}
}