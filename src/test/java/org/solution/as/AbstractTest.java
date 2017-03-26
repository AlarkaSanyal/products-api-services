package org.solution.as;

import org.junit.runner.RunWith;
import org.solution.Application;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This is the parent class for Unit and Integration tests.
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		classes=Application.class)
public abstract class AbstractTest {
	
}
