package org.acme;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * JAX-RS Application activation point.
 * All REST endpoints will be available under /api path.
 */
@ApplicationPath("")
public class JaxRsActivator extends Application {
}
