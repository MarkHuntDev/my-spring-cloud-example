package com.example.config;

public class SpringProfile {

    /**
     * A microservice interacts with stubs (without Service Discovery, Config Server and so on).
     */
    public static final String PROFILE_STAND_ALONE = "stand-alone";

    /**
     * A microservice locates within microservices environment (with Service Discovery and other services).
     */
    public static final String PROFILE_DEPENDENT = "dependent";

    /**
     * A secured microservice.
     */
    public static final String PROFILE_SECURED = "secured";

    /**
     * A microservice just ignores security.
     */
    public static final String PROFILE_NO_SECURITY = "no-security";

    /**
     * Uses by tests.
     */
    public static final String PROFILE_TEST = "test";

    private SpringProfile() {
    }
}
