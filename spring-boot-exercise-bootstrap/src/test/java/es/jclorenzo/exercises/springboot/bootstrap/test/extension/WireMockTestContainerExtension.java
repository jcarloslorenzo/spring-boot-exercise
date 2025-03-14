package es.jclorenzo.exercises.springboot.bootstrap.test.extension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.utility.MountableFile;
import org.wiremock.integrations.testcontainers.WireMockContainer;

/**
 * The Class WireMockTestContainerExtension.
 */
public class WireMockTestContainerExtension implements BeforeAllCallback, AfterAllCallback {

	/** The wiremock container. */
	private WireMockContainer wiremockContainer;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("resource")
	public void beforeAll(final ExtensionContext context) throws Exception {

		if (this.wiremockContainer == null) {
			this.wiremockContainer =
					new WireMockContainer("wiremock/wiremock:3.12.1")
							.withCopyFileToContainer(
									MountableFile.forHostPath("src/test/resources/wiremock/mappings"),
									"/home/wiremock/mappings");
		}

		this.wiremockContainer.start();
		System.setProperty("WIREMOCK_BASE_URL",
				"http://localhost:".concat(this.wiremockContainer.getMappedPort(8080).toString()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterAll(final ExtensionContext context) throws Exception {
		if (this.wiremockContainer != null && this.wiremockContainer.isRunning()) {
			this.wiremockContainer.close();
		}
	}

}
