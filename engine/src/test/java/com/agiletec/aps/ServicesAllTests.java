package com.agiletec.aps;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.entando.entando.aps.system.services.group.GroupServiceIntegrationTest;
import org.entando.entando.web.group.GroupControllerTest;


public class ServicesAllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(ServicesAllTests.class.getName());

        //
        suite.addTestSuite(GroupServiceIntegrationTest.class);
        suite.addTest(new JUnit4TestAdapter(GroupControllerTest.class));

        return suite;
    }

}
